package org.jeecg.modules.cable.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.service.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.cable.vo.PlanVo;
import org.jeecg.modules.cable.vo.SendOrdersTaskVo;
import org.jeecg.modules.cable.vo.SendOrdersVo;
import org.jeecg.modules.cable.vo.TaskVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 派单表
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Api(tags = "派单表")
@RestController
@RequestMapping("/cable/sendOrders")
@Slf4j
public class SendOrdersController extends JeecgController<SendOrders, ISendOrdersService> {
    @Autowired
    private ISendOrdersService sendOrdersService;

    @Autowired
    private ISendOrdersSubtabulationService sendOrdersSubtabulationService;

    @Autowired
    private IReceivingStorageService receivingStorageService;

    @Autowired
    private IDeliverStorageService deliverStorageService;

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private IPlan1Service plan1Service;

    @Autowired
    private IPlan2Service plan2Service;

    @Autowired
    private IPlan3Service plan3Service;

    @Autowired
    private IPlan4Service plan4Service;


    /**
     * 分页列表查询
     *
     * @param sendOrders
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "派单表-分页列表查询")
    @ApiOperation(value = "派单表-分页列表查询", notes = "派单表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SendOrders sendOrders,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SendOrders> queryWrapper = QueryGenerator.initQueryWrapper(sendOrders, req.getParameterMap());
        Page<SendOrders> page = new Page<SendOrders>(pageNo, pageSize);
        IPage<SendOrders> pageList = sendOrdersService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @return
     */
    @Transactional
    @AutoLog(value = "派单表-添加")
    @ApiOperation(value = "派单表-添加", notes = "派单表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
        SendOrdersVo sendOrdersVo = JSON.parseObject(jsonObject.toJSONString(), SendOrdersVo.class);
        QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("serial", sendOrdersVo.getSerial());
        Material material = materialService.getOne(queryWrapper);
        if (material != null) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Integer id = sendOrdersService.saveSendOrdersVo(sendOrdersVo, new Date(), sysUser.getUsername());
            if (sendOrdersVo.getOperatorSchema() == 0) {
                ReceivingStorage receivingStorage = new ReceivingStorage();
                receivingStorage.setWarehouseId(sendOrdersVo.getWarehouseId());
                receivingStorage.setStorageLocationId(sendOrdersVo.getStorageLocationId());
                receivingStorage.setSendOrdersId(id);
                receivingStorage.setState(0);
                receivingStorage.setMaterialId(material.getId());
                receivingStorageService.save(receivingStorage);
            } else {
                DeliverStorage deliverStorage = new DeliverStorage();
                deliverStorage.setWarehouseId(sendOrdersVo.getWarehouseId());
                deliverStorage.setStorageLocationId(sendOrdersVo.getStorageLocationId());
                deliverStorage.setSendOrdersId(id);
                deliverStorage.setState(0);
                deliverStorage.setMaterialId(material.getId());
                deliverStorageService.save(deliverStorage);
            }
            if (sendOrdersVo.getLicense() != null) {
                for (String s : sendOrdersVo.getLicense()) {
                    sendOrdersSubtabulationService.saveSendOrdersSubtabulation(id, 0, s, sendOrdersVo.getTaskTime());
                }
            }
            if (sendOrdersVo.getRealname() != null) {
                for (String s : sendOrdersVo.getRealname()) {
                    sendOrdersSubtabulationService.saveSendOrdersSubtabulation(id, 1, s, sendOrdersVo.getTaskTime());
                }
            }
            sendOrdersService.updatePlanState(sendOrdersVo.getPlanId(), sendOrdersVo.getPlanType());
            if (sendOrdersVo.getPlanType().equals("1")) {
                Plan1 plan1 = plan1Service.getById(sendOrdersVo.getPlanId());
                plan1.setSendOrdersState(1);
                plan1Service.updateById(plan1);
            } else if (sendOrdersVo.getPlanType().equals("2")) {
                Plan2 plan2 = plan2Service.getById(sendOrdersVo.getPlanId());
                plan2.setSendOrdersState(1);
                plan2Service.updateById(plan2);
            } else if (sendOrdersVo.getPlanType().equals("3")) {
                Plan3 plan3 = plan3Service.getById(sendOrdersVo.getPlanId());
                plan3.setSendOrdersState(1);
                plan3Service.updateById(plan3);
            } else {
                Plan4 plan4 = plan4Service.getById(sendOrdersVo.getPlanId());
                plan4.setSendOrdersState(1);
                plan4Service.updateById(plan4);
            }
            return Result.ok("派单成功！");
        }
        return Result.error("该计划物料不存在！");
    }

    /**
     * 编辑
     *
     * @return
     */
    @AutoLog(value = "派单表-编辑")
    @ApiOperation(value = "派单表-编辑", notes = "派单表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SendOrdersVo sendOrdersVo) {
        sendOrdersService.updateSendOrders(sendOrdersVo);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "派单表-通过id删除")
    @ApiOperation(value = "派单表-通过id删除", notes = "派单表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sendOrdersService.removeSendOrders(id);
        return Result.ok("删除成功!");
    }

//	/**
//	 *  批量删除
//	 *
//	 * @param ids
//	 * @return
//	 */
//	@AutoLog(value = "派单表-批量删除")
//	@ApiOperation(value="派单表-批量删除", notes="派单表-批量删除")
//	@DeleteMapping(value = "/deleteBatch")
//	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		this.sendOrdersService.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.ok("批量删除成功!");
//	}

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "派单表-通过id查询")
    @ApiOperation(value = "派单表-通过id查询", notes = "派单表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SendOrders sendOrders = sendOrdersService.getById(id);
        if (sendOrders == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(sendOrders);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sendOrders
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SendOrders sendOrders) {
        return super.exportXls(request, sendOrders, SendOrders.class, "派单表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SendOrders.class);
    }

    /**
     * 根据年份和月份查询当月每日的车辆任务的数量
     *
     * @param date
     * @Author Xm
     * @Date 2020/5/21 12:09
     */
    @GetMapping(value = "/selectTheSameMonthSendOrders")
    public Result<?> selectTheSameMonthVehicleTask(String date) {
        List<TaskVo> list = sendOrdersService.selectTheSameMonthSendOrders(date);
        return Result.ok(list);
    }

    /**
     * 根据日期查询当天车辆任务信息
     *
     * @param date
     * @param pageNo
     * @param pageSize
     * @Author Xm
     * @Date 2020/5/21 12:09
     */
    @GetMapping(value = "/taskList")
    public Result<?> taskList(String date,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SendOrdersTaskVo> page = new Page<SendOrdersTaskVo>(pageNo, pageSize);
        IPage<SendOrdersTaskVo> list = sendOrdersService.taskList(date, page);
        return Result.ok(list);
    }

    /**
     * 返回近5年年份
     *
     * @Author Xm
     * @Date 2020/5/22 14:07
     */
    @GetMapping(value = "/yearsList")
    public Result<?> yearsList() {
        List<String> list = sendOrdersService.yearsList();
        return Result.ok(list);
    }

    /**
     * 根据项目编号查询入库出库完单信息
     *
     * @param projectNo
     * @Author Xm
     * @Date 2020/5/25 17:03
     */
    @GetMapping(value = "/selectPlanAccomplish")
    public Result<?> selectPlanAccomplish(@RequestParam(name = "projectNo", required = true) String projectNo,
                                          @RequestParam(name = "planId", required = true) String planId,
                                          @RequestParam(name = "planType", required = true) String planType,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(name = "sendOrdersId", required = true) String sendOrdersId) {
        if (sendOrdersId.equals("www")) {
            sendOrdersId = null;
        }
        Page<PlanVo> page = new Page<>(pageNo, pageSize);
        List<PlanVo> list = sendOrdersService.selectPlan2Accomplish(projectNo, planId, planType, sendOrdersId, page);
        return Result.ok(list);
    }

    /**
     * 完单操作
     *
     * @Author Xm
     * @Date 2020/5/27 10:52
     */
    @PutMapping(value = "/planedit")
    public Result<?> planedit(@RequestBody JSONObject jsonObject) {
        PlanVo planVo = JSON.parseObject(jsonObject.toJSONString(), PlanVo.class);
        sendOrdersService.planedit(planVo);
        return Result.ok("完单成功!");
    }

    /**
     * 根据计划id和计划类型查询历史派单信息
     *
     * @param planId
     * @param planType
     * @return
     */
    @GetMapping(value = "/selectSendOrdersController")
    public Result<?> selectSendOrdersController(@RequestParam(name = "planId", required = true) String planId,
                                                @RequestParam(name = "planType", required = true) String planType,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SendOrdersVo> page = new Page<>(pageNo, pageSize);
        IPage<SendOrdersVo> iPage = sendOrdersService.selectSendOrdersController(planId, planType, page);
        //List<SendOrdersVo> list= sendOrdersService.selectSendOrdersController(planId,planType);
        return Result.ok(iPage);
    }

    /**
     * 查询当天所有的派单
     *
     * @return
     */
    @GetMapping(value = "/selectPlanSendOrdersTheSameDay")
    public Result<?> selectPlanSendOrdersTheSameDay(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SendOrdersVo> page = new Page<>(pageNo, pageSize);
        IPage<SendOrdersVo> iPage = sendOrdersService.selectPlanSendOrdersTheSameDay(page);
        return Result.ok(iPage);
    }

}
