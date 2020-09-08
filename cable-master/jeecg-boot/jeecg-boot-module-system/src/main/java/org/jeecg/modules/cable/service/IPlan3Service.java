package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan3;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.importpackage.Plan3Im;
import org.jeecg.modules.cable.vo.Plan3Vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 计划表3
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
public interface IPlan3Service extends IService<Plan3> {
    /**
     * 计划3合并完单
     * 2020/8/28 bai
     *
     * @param ids               批量完单的计划 id集合
     * @param operatorSchema    完单类型[0:出库\1:入库]
     * @param receiptNo         交接单号
     * @param receiptPhotos     回单照片
     * @param taskTime          任务日期
     * @param completeOrderList 合并完单填写的数据集
     * @return 受影响的行数
     */
    Result<?> consolidationCompleted(List<Serializable> ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList);

    /**
     * 查询计划3批量出库完单的数据
     * 2020/8/28 bai
     *
     * @param ids 批量出库完单 ids
     * @return 计划3批量出库完单的数据
     */
    List<Plan3Vo> getPlan3ReceivingStorageList(List<Serializable> ids);

    /**
     * 查询计划3批量入库完单的数据
     * 2020/8/28 bai
     *
     * @param ids 批量入库完单 ids
     * @return 计划3批量入库完单的数据
     */
    List<Plan3> getPlan3DeliverStorage(List<Serializable> ids);

    /**
     * 根据ids集合条件查询
     *
     * @param ids liu
     * @Date 2020/7/21
     */
    List<Plan3> idsqueryPageList3(List<String> ids);

    /**
     * 分页展示计划表3数据
     *
     * @param plan3
     * @return
     */
    IPage<Plan3> pageList(Plan3 plan3, Page<Plan3> page);

    /**
     * 导出excel
     * bai 2020/9/8
     */
    List<Plan3Im> exportPlan3(Plan3 plan3, String explain);
}
