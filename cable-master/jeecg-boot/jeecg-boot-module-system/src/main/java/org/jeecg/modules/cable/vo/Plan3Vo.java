package org.jeecg.modules.cable.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.modules.cable.entity.Plan3;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: bai
 * @DateTime: 2020/8/28 13:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Plan3Vo extends Plan3 implements Serializable {
    private static final long serialVersionUID = 2926201890933829553L;

    /**
     * 可出库数量[批量完单操作时使用此属性] ----2020/8/26 bai
     */
    private int inventoryQuantity;
    /**
     * 批量完单的计划 id集合
     */
    private String plan3Ids;
    /**
     * 完单类型[0:出库\1:入库]
     */
    private String operatorSchema;
    /**
     * 交接单号
     */
    private String plan3ReceiptNo;
    /**
     * 回单照片
     */
    private String receiptPhotos;
    /**
     * 任务日期
     */
    private String taskTime;
    /**
     * 合并完单填写的数据集
     */
    private List<?> completeOrderList;
}


