package org.edelweiss.logging.log.context;


import org.edelweiss.logging.log.aspect.LogOperationTemplateHandler;

public class TemplateHandlerContext {

    private final LogOperationTemplateHandler successHandler;

    private final LogOperationTemplateHandler failHandler;

    public TemplateHandlerContext(LogOperationTemplateHandler successHandler,
                                  LogOperationTemplateHandler failHandler) {
        this.successHandler = successHandler;
        this.failHandler = failHandler;
    }

    public LogOperationTemplateHandler getSuccessHandler() {
        return successHandler;
    }

    public LogOperationTemplateHandler getFailHandler() {
        return failHandler;
    }
}
