package org.edelweiss.logging.context;


import org.edelweiss.logging.aspect.LogTemplateHandler;

public class TemplateHandlerContext {

    private final LogTemplateHandler successHandler;

    private final LogTemplateHandler failHandler;

    public TemplateHandlerContext(LogTemplateHandler successHandler,
                                  LogTemplateHandler failHandler) {
        this.successHandler = successHandler;
        this.failHandler = failHandler;
    }

    public LogTemplateHandler getSuccessHandler() {
        return successHandler;
    }

    public LogTemplateHandler getFailHandler() {
        return failHandler;
    }
}
