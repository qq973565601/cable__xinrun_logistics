package org.jeecg.modules.cable.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 出入库台账Vo
 * bai
 * 2020/5/31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class OutPutWarehouseVo implements Serializable {
  private static final long serialVersionUID = 1L;
  // 计划类型
  private String planType;

  // 项目编号
  private String projectNo;

  // 项目名称
  private String projectName;

  // 物料编号
  private String serial;

  // 物料名称
  private String name;

  // 单位
  @Dict(dicCode = "unit")
  private Integer unit;

  // 供应商名称
  private String supplier;

  // 实际入库数量
  private BigDecimal deliverNum;

  // 实际出库数量
    private BigDecimal receivingNum;

  // 当前库存(出库-入库)
  private BigDecimal KCnums;

  private String jihuaruk;

}
