package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cable.entity.SendOrders;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.vo.PlanVo;
import org.jeecg.modules.cable.vo.SendOrdersTaskVo;
import org.jeecg.modules.cable.vo.SendOrdersVo;
import org.jeecg.modules.cable.vo.TaskVo;

import java.util.Date;
import java.util.List;

/**
 * @Description: 派单表
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface ISendOrdersService extends IService<SendOrders> {

    /**
     *   根据年份和月份查询当月每日的车辆任务的数量
     *
     * @Author Xm
     * @Date 2020/5/19 14:21
     */
    List<TaskVo> selectTheSameMonthSendOrders(String date);

    /**
     *   根据日期查询当天车辆任务信息
     *
     * @param date
     * @Author Xm
     * @Date 2020/5/19 17:52
     */
    IPage<SendOrdersTaskVo> taskList(String date, Page<SendOrdersTaskVo> page);

    /**
     *	返回近5年年份
     *
     * @Author Xm
     * @Date 2020/5/22 14:08
     */
    List<String> yearsList();

    /**
    *   派单
    *
    * @param sendOrdersVo
    * @Author Xm
    * @Date 2020/5/26 15:09
    */
    Integer saveSendOrdersVo(SendOrdersVo sendOrdersVo, Date date,String name);

    /**
     *   根据项目编号查询入库出库完单信息
     *
     * @param projectNo
     * @Author Xm
     * @Date 2020/5/25 16:14
     */
    List<PlanVo> selectPlan2Accomplish(String projectNo, String id,String planType,String sendOrdersId,Page<PlanVo> page);

    /**
     *   完单操作
     *
     * @param planVo
     * @Author Xm
     * @Date 2020/5/27 10:53
     */
    Result<?> planedit(PlanVo planVo);

  /**
   *  根据计划id和计划类型查询历史派单记录
   * @param planId
   * @param planType
   * @return
   */
  IPage<SendOrdersVo> selectSendOrdersController(String planId, String planType,Page<SendOrdersVo> page);

  void updatePlanState(Integer planId,String planType);

  void removeSendOrders(String id);

  void updateSendOrders(SendOrdersVo sendOrdersVo);

    IPage<SendOrdersVo> selectPlanSendOrdersTheSameDay(Page<SendOrdersVo> page);
}
