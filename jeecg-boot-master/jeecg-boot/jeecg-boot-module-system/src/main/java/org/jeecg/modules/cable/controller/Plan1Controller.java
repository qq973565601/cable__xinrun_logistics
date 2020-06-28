package org.jeecg.modules.cable.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.importPackage.Plan1Im;
import org.jeecg.modules.cable.service.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.cable.vo.*;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecgframework.poi.excel.view.JeecgTemplateExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static org.jeecgframework.poi.excel.ExcelExportUtil.exportExcel;

/**
 * @Description: 计划表1
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Api(tags = "计划表1")
@RestController
@RequestMapping("/cable/plan1")
@Slf4j
public class Plan1Controller extends JeecgController<Plan1, IPlan1Service> {
    @Autowired
    private IPlan1Service plan1Service;

    @Autowired
    private IPlan2Service plan2Service;

    @Autowired
    private IPlan3Service plan3Service;

    @Autowired
    private IPlan4Service plan4Service;

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private ISysDictItemService sysDictItemService;

    /**
     * 分页列表查询
     *
     * @return
     */
    @AutoLog(value = "计划表1-分页列表查询")
    @ApiOperation(value = "计划表1-分页列表查询", notes = "计划表1-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Plan1Vo plan1,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        // 条件构造
        QueryWrapper<Plan1> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(plan1.getPlanType())) {
            wrapper.like("plan_type", plan1.getPlanType());
        }
        if (StrUtil.isNotBlank(plan1.getItemSlip())) {
            wrapper.like("item_slip", plan1.getItemSlip());
        }
        if (StrUtil.isNotBlank(plan1.getAssetNo())) {
            wrapper.like("asset_no", plan1.getAssetNo());
        }
        if (StrUtil.isNotBlank(plan1.getProjectNo())) {
            wrapper.like("project_no", plan1.getProjectNo());
        }
        if (null != plan1.getCompleteState()) {
            wrapper.eq("complete_state", plan1.getCompleteState());
        }
        Page<Plan1> page = new Page<>(pageNo, pageSize);
        IPage<Plan1> pageList = plan1Service.page(page, wrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param plan1
     * @return
     */
    @AutoLog(value = "计划表1-添加")
    @ApiOperation(value = "计划表1-添加", notes = "计划表1-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Plan1 plan1) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        plan1.setUpdateTime(new Date());
        plan1.setUpdateBy(sysUser.getUsername());
        plan1Service.save(plan1);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param plan1
     * @return
     */
    @AutoLog(value = "计划表1-编辑")
    @ApiOperation(value = "计划表1-编辑", notes = "计划表1-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Plan1 plan1) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        plan1.setUpdateTime(new Date());
        plan1.setUpdateBy(sysUser.getUsername());
        plan1Service.updateById(plan1);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "计划表1-通过id删除")
    @ApiOperation(value = "计划表1-通过id删除", notes = "计划表1-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        Plan1 plan1 = plan1Service.getById(id);
        if (plan1.getSendOrdersState() == 0) {
            plan1Service.removeById(id);
            return Result.ok("删除成功!");
        }
        return Result.error("该计划已派过单，暂时不能删除");
    }

//	/**
//	 *  批量删除
//	 *
//	 * @param ids
//	 * @return
//	 */
//	@AutoLog(value = "计划表1-批量删除")
//	@ApiOperation(value="计划表1-批量删除", notes="计划表1-批量删除")
//	@DeleteMapping(value = "/deleteBatch")
//	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		this.plan1Service.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.ok("批量删除成功!");
//	}

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "计划表1-通过id查询")
    @ApiOperation(value = "计划表1-通过id查询", notes = "计划表1-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Plan1 plan1 = plan1Service.getById(id);
        if (plan1 == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(plan1);
    }

    /**
     * 导出excel
     * bai
     * 2020/5/28
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(Plan1Im plan1Im,
                                  @RequestParam(name = "explain", required = false) String explain) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String title = "计划列表";
        // 获取导出数据集
        List<Plan1Im> list = plan1Service.exportPlan1(plan1Im, explain);
        list.forEach(obj -> {
            String inventoryPoint = obj.getInventoryPoint();
            if (null != inventoryPoint && inventoryPoint.length() > 0) {
                String[] s = inventoryPoint.split("_");
                obj.setInventoryPoint(s[1]);
            }
            String terminalBin = obj.getTerminalBin();
            if (null != terminalBin && terminalBin.length() > 0) {
                String[] s = terminalBin.split("_");
                obj.setTerminalBin(s[1]);
            }
        });
        // AutoPoi 导出 excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下/
        mv.addObject(NormalExcelConstants.CLASS, Plan1Im.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams());
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }

    /**
     * 通过excel导入数据
     * bai
     * 2020/5/28
     *
     * @param request
     * @param response
     * @param planType 导入excel类型
     * @return
     */
    @RequestMapping(value = "/importExcel/{planType}", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, @PathVariable String planType) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        Material material = new Material();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        material.setCreateBy(sysUser.getUsername());
        material.setUpdateBy(sysUser.getUsername());
        material.setCreateTime(new Date());
        material.setUpdateTime(new Date());
        Integer in = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Plan1Im> list = ExcelImportUtil.importExcel(file.getInputStream(), Plan1Im.class, params);
                Plan1 plan1 = new Plan1();
                for (Plan1Im plan1Im : list) {
                    // 单位之间的转换
                    List<SysDictItem> dictItems = sysDictItemService.selectType("unit");
                    for (SysDictItem dictItem : dictItems) {
                        // 导入时判断单位是否存在，存在则进行单位转换
                        if (StrUtil.isNotBlank(plan1Im.getRawMaterialUnit())) {
                            if (plan1Im.getRawMaterialUnit().equals(dictItem.getItemText())) {
                                plan1.setRawMaterialUnit(Integer.parseInt(dictItem.getItemValue()));
                            }
                        }
                        if (StrUtil.isNotBlank(plan1Im.getWasteMaterialUnit())) {
                            if (plan1Im.getWasteMaterialUnit().equals(dictItem.getItemText())) {
                                plan1.setWasteMaterialUnit(Integer.parseInt(dictItem.getItemValue()));
                            }
                        }
                    }
                    // 属性拷贝 将 plan1Im 接收的excel数据转换为 plan1 实体类数据
                    BeanUtils.copyProperties(plan1Im, plan1);
                    plan1.setPlanType(planType);
                    plan1.setSendOrdersState(0);
                    plan1.setCompleteState(0);
                    plan1.setUpdateBy(sysUser.getUsername());
                    plan1.setCreateBy(sysUser.getUsername());
                    plan1.setUpdateTime(new Date());
                    plan1.setCreateTime(new Date());
                    plan1Service.save(plan1);
                    QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("serial", plan1.getWasteMaterialCode());
                    List<Material> list1 = materialService.list(queryWrapper);
                    if (list1.size() == 0) {
                        if (StrUtil.isNotBlank(plan1.getWasteMaterialCode())) {
                            in++;
                            material.setSerial(plan1.getWasteMaterialCode());
                            material.setUnit(plan1.getWasteMaterialUnit());
                            material.setName(plan1.getWasteMaterialText());
                            materialService.save(material);
                        }
                    }
                }
                if (in != 0) {
                    return Result.ok("文件导入成功！数据行数：" + list.size());
                }
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    @PutMapping(value = "/planTheSameDayEdit")
    public Result<?> planTheSameDayEdit(@RequestBody SendOrdersVo sendOrdersVo) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sendOrdersVo.getPlanType().equals("1")) {
            Plan1 plan1 = plan1Service.getById(sendOrdersVo.getPlanId());
            plan1.setUpdateBy(sysUser.getUsername());
            plan1.setUpdateTime(new Date());
            plan1.setTheContact(sendOrdersVo.getLinkman());
            plan1.setThePhone(sendOrdersVo.getPhone());
            plan1Service.updateById(plan1);
        } else if (sendOrdersVo.getPlanType().equals("2")) {
            Plan2 plan2 = plan2Service.getById(sendOrdersVo.getPlanId());
            plan2.setUpdateBy(sysUser.getUsername());
            plan2.setUpdateTime(new Date());
            plan2.setEquipmentOwners(sendOrdersVo.getLinkman());
            plan2Service.updateById(plan2);
        } else if (sendOrdersVo.getPlanType().equals("3")) {
            Plan3 plan3 = plan3Service.getById(sendOrdersVo.getPlanId());
            plan3.setUpdateBy(sysUser.getUsername());
            plan3.setUpdateTime(new Date());
            plan3.setFieldConsignee(sendOrdersVo.getLinkman());
            plan3.setCPhone(sendOrdersVo.getLinkman());
            plan3Service.updateById(plan3);
        } else if (sendOrdersVo.getPlanType().equals("4")) {
            Plan4 plan4 = plan4Service.getById(sendOrdersVo.getPlanId());
            plan4.setUpdateBy(sysUser.getUsername());
            plan4.setUpdateTime(new Date());
            plan4.setTeamContact(sendOrdersVo.getLinkman());
            plan4Service.updateById(plan4);
        }
        return Result.ok("修改成功!");
    }

    /**
     * 查询已完成的计划（结算）
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/selectSettleAccounts")
    public Result<?> selectSettleAccounts(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(name = "backup1", required = false) String backup1,
                                          @RequestParam(name = "planType", required = false) String planType,
                                          @RequestParam(name = "projectNo", required = false) String projectNo) {
        Page<SettleAccountsVo> page = new Page<>(pageNo, pageSize);
        IPage<SettleAccountsVo> pageList = plan1Service.selectSettleAccounts(backup1, planType, projectNo, page);
        return Result.ok(pageList);
    }

    /**
     * 查询已完成的计划(结算)详情信息
     * bai
     * 2020/6/10 14:07
     *
     * @return
     */
    @GetMapping(value = "/selectSettleAccountsDetails")
    public Result<?> selectSettleAccountsDetails(@RequestParam(name = "planId", required = false) Integer planId,
                                                 @RequestParam(name = "planName", required = false) Integer planName,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SettleAccountsDetailsVo> page = new Page<>(pageNo, pageSize);
        IPage<SettleAccountsDetailsVo> pageList = plan1Service.selectSettleAccountsDetails(planId, planName, page);
        return Result.ok(pageList);
    }

    @PutMapping(value = "/updateSettleAccounts")
    public Result<?> updateSettleAccounts(@RequestBody SettleAccountsVo settleAccountsVo) {
        if (settleAccountsVo.getBackup1().equals("1")) {
            return Result.ok("该计划已结算!");
        }
        if (settleAccountsVo.getPlanName().equals("1")) {
            Plan1 plan1 = plan1Service.getById(settleAccountsVo.getPlanId());
            plan1.setBackup1("1");
            plan1Service.updateById(plan1);
        }
        if (settleAccountsVo.getPlanName().equals("2")) {
            Plan2 plan2 = plan2Service.getById(settleAccountsVo.getPlanId());
            plan2.setBackup1("1");
            plan2Service.updateById(plan2);
        }
        if (settleAccountsVo.getPlanName().equals("3")) {
            Plan3 plan3 = plan3Service.getById(settleAccountsVo.getPlanId());
            plan3.setBackup1("1");
            plan3Service.updateById(plan3);
        }
        if (settleAccountsVo.getPlanName().equals("4")) {
            Plan4 plan4 = plan4Service.getById(settleAccountsVo.getPlanId());
            plan4.setBackup1("1");
            plan4Service.updateById(plan4);
        }
        return Result.ok("结算成功!");
    }
}
