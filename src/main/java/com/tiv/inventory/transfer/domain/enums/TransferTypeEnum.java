package com.tiv.inventory.transfer.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调拨方式枚举
 */
@Getter
@AllArgsConstructor
public enum TransferTypeEnum {

    MANUAL(0, "人工"),

    INTELLIGENT(1, "智能推荐");

    private final Integer code;

    private final String description;

    /**
     * 根据 code 获取枚举
     */
    public static TransferTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TransferTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

}