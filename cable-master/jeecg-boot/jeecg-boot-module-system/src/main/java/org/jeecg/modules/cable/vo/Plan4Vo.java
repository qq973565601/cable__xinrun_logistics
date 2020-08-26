package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Plan4Vo implements Serializable {

    // 工程账号
    @Excel(name = "工程账号", width = 15)
    private String projectNo;

    // 电缆截面
    @Excel(name = "电缆截面", width = 15)
    private String cableCross;

    // 材质
    @Excel(name = "材质", width = 15)
    private String texture;

    // 完单数量
    @Excel(name = "完单数量", width = 15)
    private BigDecimal accomplishNum;

    // 完单重量
    @Excel(name = "完单重量", width = 15)
    private BigDecimal accomplishWeight;

    // 长度(M)
    @Excel(name = "长度(M)", width = 15)
    private String length;

    // 重量(吨)
    @Excel(name = "重量(吨)", width = 15)
    private String weight;

    // 退役日期
    @Excel(name = "退役日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date decommissioningDate;

    // 情况说明
    @Excel(name = "情况说明", width = 15)
    private String description;

    // 交接单号
    @Excel(name = "交接单号", width = 15)
    private String itemSlip;

    // 备注
    @Excel(name = "备注", width = 15)
    private String remark;

    // 资产编号
    @Excel(name = "资产编号", width = 15)
    private String assetNo;

    // 设备号
    @Excel(name = "设备号", width = 15)
    private String equipmentDeveui;

    // 电缆名称
    @Excel(name = "电缆名称", width = 15)
    private String cableName;

    // 电压等级
    @Excel(name = "电压等级", width = 15)
    private String voltageGrade;

    // 测绘长度(M)
    @Excel(name = "测绘长度(M)", width = 15)
    private String mappingLength;

    // 足量情况
    @Excel(name = "足量情况", width = 15)
    private String sufficiency;

    // 报废流程节点
    @Excel(name = "报废流程节点", width = 15)
    private String scrapProcessNode;

}
