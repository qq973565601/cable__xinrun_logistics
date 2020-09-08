package org.jeecg.modules.cable.controller;

import java.io.IOException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.util.StrUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.cable.entity.Material;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan2;
import org.jeecg.modules.cable.importpackage.Plan2Im;
import org.jeecg.modules.cable.service.IMaterialService;
import org.jeecg.modules.cable.service.IPlan2Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.cable.vo.Plan2Vo;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 计划表2
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Api(tags = "计划表2")
@RestController
@RequestMapping("/cable/plan2")
@Slf4j
public class Plan2Controller extends JeecgController<Plan2, IPlan2Service> {
    @Autowired
    private IPlan2Service plan2Service;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private ISysDictItemService sysDictItemService;

    /**
     * 计划2合并完单
     * 2020/8/28 bai
     *
     * @param plan2Vo 合并完单中的表单数据
     * @return 受影响的行数
     */
    @PostMapping(value = "/consolidationCompleted")
    public Result<?> consolidationCompleted(@RequestBody Plan2Vo plan2Vo) {
        Result<?> result = plan2Service.consolidationCompleted(Arrays.asList(plan2Vo.getPlan2Ids().split(",")), plan2Vo.getOperatorSchema(), plan2Vo.getPlan2ReceiptNo(), plan2Vo.getReceiptPhotos(), plan2Vo.getTaskTime(), plan2Vo.getCompleteOrderList());
        if (result.getCode() == 200) {
            return Result.ok(result.getMessage());
        } else {
            return Result.error(result.getMessage());
        }
    }

    /**
     * 查询计划2批量出库完单的数据
     * bai
     * 2020/8/28
     *
     * @param ids 批量出库完单 ids
     * @return 计划2批量出库完单的数据
     */
    @GetMapping(value = "/getPlan2ReceivingStorageList")
    public Result<?> getPlan2ReceivingStorageList(@RequestParam(name = "ids") String ids) {
        List<Plan2Vo> list = plan2Service.getPlan2ReceivingStorageList(Arrays.asList(ids.split(",")));
        for (Plan2Vo item : list) {
            if (!list.get(0).getProjectNo().equals(item.getProjectNo())) {
                return Result.error("工程账号必须一致");
            }
        }
        return Result.ok(list);
    }

    /**
     * 查询计划2批量入库完单的数据
     * bai
     * 2020/8/28
     *
     * @param ids 批量入库完单 ids
     * @return 计划2批量入库完单的数据
     */
    @GetMapping(value = "/getPlan2DeliverStorage")
    public Result<?> getPlan2DeliverStorage(@RequestParam(name = "ids") String ids) {
        List<Plan2> list = plan2Service.getPlan2DeliverStorage(Arrays.asList(ids.split(",")));
        for (Plan2 item : list) {
            if (!list.get(0).getProjectNo().equals(item.getProjectNo())) {
                return Result.error("工程账号必须一致");
            }
        }
        return Result.ok(list);
    }

    /**
     * 分页列表查询
     * bai
     * 2020/5/29
     *
     * @return
     */
    @AutoLog(value = "计划表2-分页列表查询")
    @ApiOperation(value = "计划表2-分页列表查询", notes = "计划表2-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Plan2 plan2,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        // 条件构造
        QueryWrapper<Plan2> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(plan2.getSite())) {
            wrapper.like("site", plan2.getSite());
        }
        if (StrUtil.isNotBlank(plan2.getEquipmentName())) {
            wrapper.like("equipment_name", plan2.getEquipmentName());
        }
        if (StrUtil.isNotBlank(plan2.getAssetNo())) {
            wrapper.like("asset_no", plan2.getAssetNo());
        }
        if (StrUtil.isNotBlank(plan2.getProjectNo())) {
            wrapper.like("project_no", plan2.getProjectNo());
        }
        if (null != plan2.getCompleteState()) {
            wrapper.eq("complete_state", plan2.getCompleteState());
        }
        Page<Plan2> page = new Page<Plan2>(pageNo, pageSize);
        IPage<Plan2> pageList = plan2Service.page(page, wrapper);
        return Result.ok(pageList);
    }


    /**
     * 根据ids集合
     * 实现分页列表查询
     *
     * @return
     */
    @AutoLog(value = "计划表1-分页列表查询")
    @ApiOperation(value = "计划表1-分页列表查询", notes = "计划表1-分页列表查询")
    @GetMapping(value = "/idslist")
    public Result<?> idsqueryPageList(@RequestParam(name = "ids") String ids) {
        List<Plan1> list = new ArrayList<>();
        Plan1 plan;
        List<Plan2> pageList = plan2Service.idsqueryPageList2(Arrays.asList(ids.split(",")));
        for (int i = 0; i < pageList.size(); i++) {
            if (!pageList.get(0).getProjectNo().equals(pageList.get(i).getProjectNo()))
                return Result.error("工程账号必须一致");
            plan = new Plan1();
            plan.setId(pageList.get(i).getId());                     //计划id
            plan.setProjectNo(pageList.get(i).getReceiptNo());       //工程账号
            plan.setProjectName(pageList.get(i).getSite());          //项目名称
            plan.setWasteMaterialText(pageList.get(i).getBackup2()); //物料描述
            plan.setWasteMaterialCode(pageList.get(i).getBackup3()); //物料代码
            list.add(plan);
        }

        return Result.ok(list);
    }

    /**
     * 添加
     *
     * @param plan2
     * @return
     */
    @AutoLog(value = "计划表2-添加")
    @ApiOperation(value = "计划表2-添加", notes = "计划表2-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Plan2 plan2) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        plan2.setUpdateTime(new Date());
        plan2.setUpdateBy(sysUser.getUsername());
        plan2Service.save(plan2);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param plan2
     * @return
     */
    @AutoLog(value = "计划表2-编辑")
    @ApiOperation(value = "计划表2-编辑", notes = "计划表2-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Plan2 plan2) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        plan2.setUpdateTime(new Date());
        plan2.setUpdateBy(sysUser.getUsername());
        plan2Service.updateById(plan2);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "计划表2-通过id删除")
    @ApiOperation(value = "计划表2-通过id删除", notes = "计划表2-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        Plan2 plan2 = plan2Service.getById(id);
        if (plan2.getSendOrdersState() == 0) {
            plan2Service.removeById(id);
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
//	@AutoLog(value = "计划表2-批量删除")
//	@ApiOperation(value="计划表2-批量删除", notes="计划表2-批量删除")
//	@DeleteMapping(value = "/deleteBatch")
//	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		this.plan2Service.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.ok("批量删除成功!");
//	}

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "计划表2-通过id查询")
    @ApiOperation(value = "计划表2-通过id查询", notes = "计划表2-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Plan2 plan2 = plan2Service.getById(id);
        if (plan2 == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(plan2);
    }

    /**
     * 导出excel
     * bai 2020/9/7
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(Plan2 plan2, @RequestParam(name = "explain", required = false) String explain) {
        /*LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String title = "备品计划";*/
        // 获取导出数据
        List<Plan2Im> list = plan2Service.exportPlan2(plan2, explain);
        // 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //此处设置的filename无效 ,前端会重更新设置一下
        /*mv.addObject(NormalExcelConstants.FILE_NAME, title);*/
        mv.addObject(NormalExcelConstants.CLASS, Plan2Im.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams());
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }

    /**
     * 通过excel导入数据
     * bai
     * 2020/6/2
     *
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request) {
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
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Plan2Im> list = ExcelImportUtil.importExcel(file.getInputStream(), Plan2Im.class, params);
                //List<Plan2> list1 = CollectionCopyUtil.copyList(list, Plan2.class);
                Plan2 plan2 = new Plan2();
                for (Plan2Im plan2Im : list) {
                    // 属性拷贝 将 plan2Im 接收的excel数据转换为 plan2 实体类数据
                    BeanUtils.copyProperties(plan2Im, plan2);
                    plan2.setPlanType("备品");
                    plan2.setSendOrdersState(0);
                    plan2.setCompleteState(0);
                    plan2.setUpdateBy(sysUser.getUsername());
                    plan2.setCreateBy(sysUser.getUsername());
                    plan2.setUpdateTime(new Date());
                    plan2.setCreateTime(new Date());
                    plan2Service.save(plan2);
                    QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("serial", plan2.getModel());
                    List<Material> lists = materialService.list(queryWrapper);
                    if (lists.size() == 0) {
                        if (plan2.getAssetNo() != null) {
                            in++;
                            // 添加计划2同时对物料进行添加操作

//                          material.setSerial(plan2.getEquipmentNo());// 设备号作为物料编号

                            material.setSerial(plan2.getReceiptNo()); // 入库单号 作为物料编号
                            // 设备名称作为物料名称
                            material.setName(plan2.getEquipmentName());
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

    /**
     * 查询所有普通成员
     *
     * @param
     * @Author Xm
     * @Date 2020/5/26 17:27
     */
    @GetMapping(value = "/queryUserList")
    public Result<?> queryUserList() {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.ne("username", "admin");
        List<SysUser> list = sysUserService.list(queryWrapper);
        return Result.ok(list);
    }
}
