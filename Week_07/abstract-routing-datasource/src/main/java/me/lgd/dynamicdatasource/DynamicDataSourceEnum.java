package me.lgd.dynamicdatasource;

import lombok.Getter;

/**
 * @author lgd
 * @date 2020/12/1 21:41
 */
@Getter
public enum DynamicDataSourceEnum {
    /**
     * 主库
     */
    PRIMARY("primary"),
    /**
     * 从库
     */
    REPLICA("replica");

    private String dataSourceName;

    DynamicDataSourceEnum(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
}
