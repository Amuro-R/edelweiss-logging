package org.edelweiss.logging.log.aspect.part;

import lombok.Data;

@Data
public class PartInfo {

    /**
     * 上一part的结尾起始位
     */
    private int partLastEnd;
    /**
     * 当前part的开头的起始位
     */
    private int partStart;
    /**
     * 当前part的结尾起始位
     */
    private int partEnd;
    /**
     * 当前part类型
     */
    private PartType partType;
    /**
     * 当前part是否before
     */
    private boolean before;

    /**
     * 默认值
     */
    public PartInfo() {
        this.partLastEnd = -2;
        this.partStart = -2;
        this.partEnd = -2;
        this.partType = PartType.PLAIN_STRING;
//        this.startIdentifierIndex = -1;
        this.before = false;
    }

    public void buildStatus(PartType partType, int partStart, boolean before) {
        this.partType = partType;
        this.partStart = partStart;
        this.before = before;
    }
}
