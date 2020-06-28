package org.jeecg.modules.cable.vo;

import lombok.Data;
import org.jeecg.modules.cable.entity.SendOrders;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SendOrdersVo
 * @Author Xm
 * @Date 2020/5/26 15:07
 */
@Data
public class SendOrdersVo extends SendOrders implements Serializable {

    /**物料编码*/
    private String serial;

    private List<String> license;

    private List<String> realname;

    private String a0;

    private String a1;

    private String storageLocationName;

    /**任务详情*/
    private String projectName;

    /**联系人*/
    private String linkman;

    /**联系电话*/
    private String phone;

    /**地址*/
    private String address;

    private String pPlanType;
}
