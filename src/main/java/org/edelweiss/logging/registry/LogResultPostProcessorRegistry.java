package org.edelweiss.logging.registry;

import lombok.NoArgsConstructor;
import org.edelweiss.logging.aspect.processor.ResultPostProcessor;

import java.util.List;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@NoArgsConstructor
public class LogResultPostProcessorRegistry extends SpringBasedRegistry<ResultPostProcessor> {

    public LogResultPostProcessorRegistry(List<ResultPostProcessor> processors) {
        for (ResultPostProcessor processor : processors) {
            this.register(processor.getClass(), processor);
        }
    }
}
