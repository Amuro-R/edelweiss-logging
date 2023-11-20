package org.edelweiss.logging.pojo.log;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jingyun
 * @date 2022-07-22
 */
@Data
@NoArgsConstructor
public class ResultVO<T> {

    private boolean success;

    private T data;
}
