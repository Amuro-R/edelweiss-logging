package org.edelweiss.logging.context;


import lombok.Getter;
import org.edelweiss.logging.aspect.LogTemplateHandler;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Getter
public class TemplateHandlerContext {

    private final LogTemplateHandler successHandler;

    private final LogTemplateHandler failHandler;

    public TemplateHandlerContext(LogTemplateHandler successHandler,
                                  LogTemplateHandler failHandler) {
        this.successHandler = successHandler;
        this.failHandler = failHandler;
    }

}
