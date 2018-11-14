package io.sdb.vo;

import lombok.Data;

@Data
public class GrouponDetailVO {
    String grouponId;
    Integer totalCount;
    Integer joinedCount;
    String headUrl;
    String name;

    public Integer getSurplus() {
        Integer surplus = totalCount - joinedCount;
        if (surplus < 0) {
            return 0;
        }
        return surplus;
    }
}
