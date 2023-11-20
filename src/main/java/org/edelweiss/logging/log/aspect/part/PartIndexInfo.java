package org.edelweiss.logging.log.aspect.part;

import lombok.Data;

@Data
public class PartIndexInfo {
    /**
     * 该part的开头的起始位
     */
    private int index;
    /**
     * 该part是否before
     */
    private boolean before;

    public PartIndexInfo(int index, boolean before) {
        this.index = index;
        this.before = before;
    }
}
