package org.edelweiss.logging.aspect.processor;


import org.edelweiss.logging.aspect.MethodExecuteResult;
import org.edelweiss.logging.pojo.vo.Result;

public class ControllerResultPostProcessor implements ResultPostProcessor {
    @Override
    public Object process(Object result, MethodExecuteResult methodExecuteResult) {
        if (result instanceof Result<?>) {
            Result<?> res = (Result<?>) result;
            methodExecuteResult.setBusinessFail(!res.isSuccess());
        }
        return null;
    }
}
