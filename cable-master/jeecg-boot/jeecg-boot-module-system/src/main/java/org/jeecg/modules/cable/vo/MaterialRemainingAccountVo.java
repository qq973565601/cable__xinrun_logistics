package org.jeecg.modules.cable.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物料余留台账Vo
 * bai
 * 2020/5/27
 */
@Data
public class MaterialRemainingAccountVo implements Serializable {
  private static final long serialVersionUID = -7485816670928860657L;

  private String planType;
  private String projectNo;
  private String projectName;
  private String serial;
  private String name;
  private BigDecimal now51;
  private BigDecimal now50;
  private BigDecimal now41;
  private BigDecimal now40;
  private BigDecimal now31;
  private BigDecimal now30;
  private BigDecimal now21;
  private BigDecimal now20;
  private BigDecimal now11;
  private BigDecimal now10;
  private BigDecimal now01;
  private BigDecimal now00;
  /**
   * 余留入库总数
   */
  private BigDecimal ylrkNums;
  /**
   * 余留出库总数
   */
  private BigDecimal ylckNums;
}
