package org.edelweiss.logging.log.aspect;

import org.edelweiss.logging.log.aspect.part.PartIndexInfo;
import org.edelweiss.logging.log.aspect.part.PartInfo;
import org.edelweiss.logging.log.aspect.part.PartType;
import org.edelweiss.logging.log.aspect.part.StringPart;
import org.edelweiss.logging.log.el.ILogParseFunction;
import org.edelweiss.logging.log.el.LogEvaluationContext;
import org.edelweiss.logging.log.el.LogOperationExpressionEvaluator;
import org.edelweiss.logging.log.el.LogParseFunctionFactory;
import org.springframework.context.expression.AnnotatedElementKey;

import java.util.ArrayList;
import java.util.List;

public class LogOperationTemplateHandler {

    private final LogParseFunctionFactory logParseFunctionFactory;

    private final LogOperationExpressionEvaluator expressionEvaluator;

    private final String template;

    private final List<StringPart> partList;

    private final PartInfo partInfo;

    public LogOperationTemplateHandler(LogParseFunctionFactory logParseFunctionFactory, String template,
                                       LogOperationExpressionEvaluator expressionEvaluator) {
        this.logParseFunctionFactory = logParseFunctionFactory;
        this.template = template;
        this.partList = new ArrayList<>();
        this.partInfo = new PartInfo();
        this.expressionEvaluator = expressionEvaluator;
    }

    public void extractStringPart() {
        do {
            partInfo.setPartType(PartType.PLAIN_STRING);

            this.chooseNextStringPart();
            this.prePlainStringHandle();
            if (this.tailPlainStringHandle()) {
                break;
            }
            String part = this.getExtractedPartFromTemplate(template, partInfo);

            if (partInfo.getPartType().equals(PartType.SPEL_STRING)) {
                // 完整支持el表达式，不再加上前缀
                StringPart stringPart = new StringPart("#" + part, partInfo.getPartType(), partInfo.isBefore());
                partList.add(stringPart);
            } else {
                this.functionPartHandle(part);
            }
        } while (partInfo.getPartStart() >= 0);
    }

    public List<StringPart> methodValueInject(AnnotatedElementKey methodKey, LogEvaluationContext evaluationContext, boolean before) {
        for (StringPart stringPart : partList) {
            this.stringPartValueInject(methodKey, evaluationContext, stringPart, before);
        }
        return partList;
    }

    private void chooseNextStringPart() {
        PartIndexInfo spElPartIndex = this.getNextStringPart(template, partInfo.getPartEnd() + 2, PartType.SPEL_STRING);
        PartIndexInfo funcPartIndex = this.getNextStringPart(template, partInfo.getPartEnd() + 2, PartType.FUNCTION_STRING);

        if (spElPartIndex == null && funcPartIndex == null) {
            partInfo.setPartType(PartType.PLAIN_STRING);
        } else if (spElPartIndex != null && funcPartIndex != null) {
            int spElIndex = spElPartIndex.getIndex();
            int funcIndex = funcPartIndex.getIndex();
            if (spElIndex < funcIndex) {
                partInfo.buildStatus(PartType.SPEL_STRING, spElPartIndex.getIndex(), spElPartIndex.isBefore());
            } else if (spElIndex > funcIndex) {
                partInfo.buildStatus(PartType.FUNCTION_STRING, funcPartIndex.getIndex(), funcPartIndex.isBefore());
            } else {
                throw new RuntimeException("it's impossible");
            }
        } else if (spElPartIndex != null) {
            partInfo.buildStatus(PartType.SPEL_STRING, spElPartIndex.getIndex(), spElPartIndex.isBefore());
        } else {
            partInfo.buildStatus(PartType.FUNCTION_STRING, funcPartIndex.getIndex(), funcPartIndex.isBefore());
        }
    }

    private PartIndexInfo getNextStringPart(String template, int searchStart, PartType partType) {
        int i = template.indexOf(partType.beforePreIdentifier, searchStart);
        int j = template.indexOf(partType.afterPreIdentifier, searchStart);

        if (i > -1 && (i < j || j < 0)) {
            return new PartIndexInfo(i, true);
        }
        if (j > -1 && (j < i || i < 0)) {
            return new PartIndexInfo(j, false);
        }
        return null;
    }

    private void prePlainStringHandle() {
        if (partInfo.getPartStart() > partInfo.getPartLastEnd() + 2) {
            String plainPart = template.substring(partInfo.getPartLastEnd() + 2, partInfo.getPartStart());
            partList.add(new StringPart(plainPart, PartType.PLAIN_STRING, true));
        }
    }

    private boolean tailPlainStringHandle() {
        if (partInfo.getPartType().equals(PartType.PLAIN_STRING)) {
            if (partInfo.getPartLastEnd() + 2 <= template.length() - 1) {
                String plainPart = template.substring(partInfo.getPartLastEnd() + 2);
                StringPart stringPart = new StringPart(plainPart, PartType.PLAIN_STRING, true);
                partList.add(stringPart);
            }
            return true;
        }
        return false;
    }

    private String getExtractedPartFromTemplate(String template, PartInfo partInfo) {
        String subIdentifier = null;
        if (partInfo.isBefore()) {
            subIdentifier = partInfo.getPartType().beforeSubIdentifier;
        } else {
            subIdentifier = partInfo.getPartType().afterSubIdentifier;
        }
        partInfo.setPartEnd(template.indexOf(subIdentifier, partInfo.getPartStart() + 2));
        partInfo.setPartLastEnd(partInfo.getPartEnd());
        return template.substring(partInfo.getPartStart() + 2, partInfo.getPartEnd());
    }

    private void functionPartHandle(String part) {
        int left = part.indexOf("(");
        int right = part.lastIndexOf(")");
        String functionName = part.substring(0, left);

        StringPart functionPart = new StringPart(functionName, partInfo.getPartType(), partInfo.isBefore());
        partList.add(functionPart);

        if (left + 1 < right) {
            String argString = part.substring(left + 1, right);
            String[] functionArgs = argString.split(",");
            for (String arg : functionArgs) {
                String functionArg = arg.trim();
                PartIndexInfo partIndexInfo = this.getNextStringPart(functionArg, 0, PartType.SPEL_STRING);
                if (partIndexInfo == null) {
                    functionPart.addFunctionArg(new StringPart(functionArg, PartType.PLAIN_STRING, true));
                } else {
                    PartInfo paramPartInfo = new PartInfo();
                    paramPartInfo.buildStatus(PartType.SPEL_STRING, partIndexInfo.getIndex(), partIndexInfo.isBefore());
                    String paramPart = this.getExtractedPartFromTemplate(functionArg, paramPartInfo);
                    // 完整支持el表达式，不再加上前缀
                    functionPart.addFunctionArg(new StringPart("#" + paramPart, PartType.SPEL_STRING, partIndexInfo.isBefore()));
                }
            }
        }
    }

    private void stringPartValueInject(AnnotatedElementKey methodKey, LogEvaluationContext logEvaluationContext, StringPart stringPart, boolean before) {
        PartType partType = stringPart.getPartType();
        String part = stringPart.getPart();
        boolean partBefore = stringPart.isBefore();
        if (partType.equals(PartType.PLAIN_STRING) && partBefore == before) {
            stringPart.setValue(part);
        } else if (partType.equals(PartType.SPEL_STRING) && partBefore == before) {
            Object expressionValue = expressionEvaluator.parseValue(methodKey, part, logEvaluationContext);
            stringPart.setValue(expressionValue);
        } else {
            List<StringPart> functionParamList = stringPart.getFunctionParam();
            int idx = 0;
            Object[] subArgs = new Object[functionParamList.size()];
            for (StringPart stringPart_2 : functionParamList) {
                if (partBefore && !stringPart_2.isBefore()) {
                    throw new RuntimeException("before方法不允许使用after参数");
                }
                this.stringPartValueInject(methodKey, logEvaluationContext, stringPart_2, before);
                subArgs[idx] = stringPart_2.getValue();
                idx = idx + 1;
            }
            if (partBefore == before) {
                ILogParseFunction logParseFunction = logParseFunctionFactory.getLogParseFunction(part);
                Object functionValue = logParseFunction.parse(subArgs);
                stringPart.setValue(functionValue);
            }
        }
    }
}
