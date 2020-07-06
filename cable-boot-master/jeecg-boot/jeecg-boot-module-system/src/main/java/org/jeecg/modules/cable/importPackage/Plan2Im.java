package org.jeecg.modules.cable.importPackage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ruan
 * ClassName: Plan2Im <br/>
 * Description: <br/>
 * date: 2020/5/27 14:30<br/>
 *
 * @author Administrator<br />
 * @since JDK 1.8
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Plan2Im implements Serializable {
    /**
     * 计划表2id
     */
    @Excel(name = "序号", width = 15)
    private Integer id;

    /**计划类型(字典 备品\临措)*/
//    @Excel(name = "计划类型", width = 15)
//    private String planType;

    /**
     * 站点
     */
    @Excel(name = "站名", width = 15)
    private String site;
    /**
     * 设备名
     */
    @Excel(name = "设备名称", width = 15)
    private String equipmentName;
    /**
     * 实施工程项目编号
     */
    @Excel(name = "实施工程项目编号", width = 15)
    private String projectNo;
    /**
     * 退役设备资产编号
     */
    @Excel(name = "退役设备资产编号", width = 15)
    private String retiredAssetNo;
    /**
     * 容量
     */
    @Excel(name = "容量", width = 15)
    private String capacity;
    /**
     * 型号
     */
    @Excel(name = "型号", width = 15)
    private String model;
    /**
     * ERP现资产状态
     */
    @Excel(name = "ERP现资产状态", width = 15)
    private String assetStatus;
    /**
     * 退役时间
     */
    @Excel(name = "退役时间", width = 25, format = "yyyy/mm/dd")
    private Date retiredDate;
    /**
     * 是否有入库单
     */
    @Excel(name = "是否有入库单", width = 15)
    private String receiptIs;
    /**
     * 库存地点
     */
    @Excel(name = "库存地点", width = 15)
    private String theLocation;
    /**
     * 处置方式
     */
    @Excel(name = "处置方式", width = 15)
    private String disposalWay;
    /**
     * 新资产编号
     */
    @Excel(name = "新资产编号", width = 15)
    private String assetNo;
    /**
     * 项目分类
     */
    @Excel(name = "项目分类", width = 15)
    private String projectType;
    /**
     * 设备主人
     */
    @Excel(name = "设备主人", width = 15)
    private String equipmentOwners;
    /**
     * 地址
     */
    @Excel(name = "地址", width = 15)
    private String address;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private String note;
    /**
     * 设备号
     */
    @Excel(name = "设备号", width = 15)
    private String equipmentNo;
    /**
     * 实物已退役但未处置
     */
    @Excel(name = "实物已退役但未处置", width = 15)
    private String disposed;
    /**
     * 入库单号
     */
    @Excel(name = "入库单号", width = 15)
    private String receiptNo;

    //    @Excel(name = "入库单号", width = 15)
////    private String receiptNo;
    @Excel(name = "入库日期", width = 15)
    private String deliverTime;
    @Excel(name = "入库数量", width = 15)
    private String deliverNum;
    @Excel(name = "入库仓库", width = 15)
    private String name;
    @Excel(name = "出库数量", width = 15)
    private String receivingNum;
    @Excel(name = "出库日期", width = 15)
    private String receivingTime;
    //
    @Excel(name = "终点仓库", width = 15)
    private String endWarehouse;
    @Excel(name = "施工队", width = 15)
    private String team;
    @Excel(name = "联系人", width = 15)
    private String contacts;
    @Excel(name = "联系电话", width = 15)
    private String contactsPhone;
    //
    @Excel(name = "反馈日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date feedbackDateTime;
    @Excel(name = "情况说明", width = 15)
    private String annotation;


}
