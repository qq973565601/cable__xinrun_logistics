package org.jeecg.modules.cable.vo;

import lombok.Data;
import org.jeecg.modules.cable.entity.SendOrders;

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
     * 人员 id 集合
     */
    private List<String> realname;

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
}
