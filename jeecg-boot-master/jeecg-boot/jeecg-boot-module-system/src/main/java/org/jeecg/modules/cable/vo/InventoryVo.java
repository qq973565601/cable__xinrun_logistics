package org.jeecg.modules.cable.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ruan
 * ClassName: InventoryVo <br/>
 * Description: <br/>
 * date: 2020/6/10 10:52<br/>
 *
 * @author Administrator<br />
 * @since JDK 1.8
 */
@Data
public class InventoryVo implements Serializable {
    private String id;
    // 计划类型
    private String pType;
    // 仓库名称
    private String warehouseName;
    private String projectNo;
    private String projectName;
    private String serial;
    private String materialName;
    private BigDecimal inventoryQuantity;
    @Dict(dicCode="unit")
    private String dw;
}
