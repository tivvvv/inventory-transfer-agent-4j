package com.tiv.inventory.transfer.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调拨单状态枚举
 */
@Getter
@AllArgsConstructor
public enum TransferOrderStatusEnum {

    PENDING_AUDIT(0, "待审核"),

    AUDITED(1, "已审核"),

    TRANSFERRING(2, "调拨中"),

    COMPLETED(3, "已完成"),

    CANCELLED(4, "已取消");

    private final Integer code;

    private final String description;

    /**
     * 根据 code 获取枚举
     */
    public static TransferOrderStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TransferOrderStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

}
