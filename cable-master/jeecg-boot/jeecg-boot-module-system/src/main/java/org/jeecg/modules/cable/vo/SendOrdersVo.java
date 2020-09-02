package org.jeecg.modules.cable.vo;

import lombok.Data;
import org.jeecg.modules.cable.entity.SendOrders;
import org.jeecg.modules.cable.entity.SendOrdersSubtabulation;
import org.jeecg.modules.cable.entity.Vehicle;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SendOrdersVo
 * @Author Xm
 * @Date 2020/5/26 15:07
 */
@Data
public class SendOrdersVo extends SendOrders implements Serializable {

    /**
     * zhu
     * 2020-08-28
     */
    private String wname;
    private String storagename;
    private String tasktime;
    private String accomplishnum;
    private String accomplishnumunit;
    private String scenesituation;
    private String phontos;//异常图片（多张）
    private String scenePhotos1;//第一张异常图片
    private String scenePhotos2;//第二张异常图片
    private String receiptPhotos;//回单照片
    private String receiptPhotoss;
    private String anomalousCause;


    /**
     * 计划id的集合
     */
    private String ids;

    /**
     * 物料编码
     */
    private String serial;

    /**
     * 车牌号集合
     */
    private List<String> license;

    /**
     * 车牌号
     */
    private String a0;

    /**
     * 人员 id
     */
    private String a1;

    /**
     * 库位
     */
    private String storageLocationName;

    /**
     * 任务详情
     */
    private String projectName;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 计划类型
     */
    private String pPlanType;

    /**
     * 物料描述
     * liu
     * 2020/07/15
     */
    private String rawMaterialText;

    /**
     * 交接单数量
     * liu
     * 2020/07/15
     */
    private String numReceipts;

    /**
     * 资产编号
     * liu
     * 2020/08/21
     */
    private String assetNo;

    /**
     * 派单信息集合
     */
    private List<SendOrders> jeecgOrderCustomerList;

    /**
     * 派单车辆集合
     */
    private List<SendOrdersTaskVo> jeecgOrderTicketList;

    /**
     * 人员 id 集合
     */
    private List<String> realname;
}
