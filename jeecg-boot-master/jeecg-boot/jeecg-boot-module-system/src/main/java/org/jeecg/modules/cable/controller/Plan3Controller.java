package org.jeecg.modules.cable.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.cable.entity.Material;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan3;
import org.jeecg.modules.cable.importPackage.Plan3Im;
import org.jeecg.modules.cable.service.IMaterialService;
import org.jeecg.modules.cable.service.IPlan3Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
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
 * @Description: 计划表3
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Api(tags = "计划表3")
@RestController
@RequestMapping("/cable/plan3")
@Slf4j
public class Plan3Controller extends JeecgController<Plan3, IPlan3Service> {
  @Autowired
  private IPlan3Service plan3Service;

  @Autowired
  private IMaterialService materialService;

  @Autowired
  private ISysDictItemService sysDictItemService;

  /**
   * 分页列表查询
   * bai
   * 2020/5/25
   *
   * @return
   */
  @AutoLog(value = "计划表3-分页列表查询")
  @ApiOperation(value = "计划表3-分页列表查询", notes = "计划表3-分页列表查询")
  @GetMapping(value = "/list")
  public Result<?> queryPageList(Plan3 plan3,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
    Page<Plan3> page = new Page<>(pageNo, pageSize);
    IPage<Plan3> pageList = plan3Service.pageList(plan3, page);
    return Result.ok(pageList);
  }

  /**
   * 添加
   *
   * @param plan3
   * @return
   */
  @AutoLog(value = "计划表3-添加")
  @ApiOperation(value = "计划表3-添加", notes = "计划表3-添加")
  @PostMapping(value = "/add")
  public Result<?> add(@RequestBody Plan3 plan3) {
    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    plan3.setUpdateTime(new Date());
    plan3.setUpdateBy(sysUser.getUsername());
    plan3Service.save(plan3);
    return Result.ok("添加成功！");
  }

  /**
   * 编辑
   *
   * @param plan3
   * @return
   */
  @AutoLog(value = "计划表3-编辑")
  @ApiOperation(value = "计划表3-编辑", notes = "计划表3-编辑")
  @PutMapping(value = "/edit")
  public Result<?> edit(@RequestBody Plan3 plan3) {
    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    plan3.setUpdateTime(new Date());
    plan3.setUpdateBy(sysUser.getUsername());
    plan3Service.updateById(plan3);
    return Result.ok("编辑成功!");
  }

  /**
   * 通过id删除
   *
   * @param id
   * @return
   */
  @AutoLog(value = "计划表3-通过id删除")
  @ApiOperation(value = "计划表3-通过id删除", notes = "计划表3-通过id删除")
  @DeleteMapping(value = "/delete")
  public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
    Plan3 plan3 = plan3Service.getById(id);
    if (plan3.getSendOrdersState() == 0) {
      plan3Service.removeById(id);
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
//	@AutoLog(value = "计划表3-批量删除")
//	@ApiOperation(value="计划表3-批量删除", notes="计划表3-批量删除")
//	@DeleteMapping(value = "/deleteBatch")
//	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		this.plan3Service.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.ok("批量删除成功!");
//	}

  /**
   * 通过id查询
   *
   * @param id
   * @return
   */
  @AutoLog(value = "计划表3-通过id查询")
  @ApiOperation(value = "计划表3-通过id查询", notes = "计划表3-通过id查询")
  @GetMapping(value = "/queryById")
  public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
    Plan3 plan3 = plan3Service.getById(id);
    if (plan3 == null) {
      return Result.error("未找到对应数据");
    }
    return Result.ok(plan3);
  }

  /**
   * 导出excel
   * bai
   * 2020/5/28
   *
   * @return
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(Plan3 plan3,
                                @RequestParam(name = "explain", required = false) String explain) {
    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    String title = "新品/临措";
    // 获取导出数据集
    List<Plan3Im> list = plan3Service.exportPlan3(plan3, explain);
    // 导出 excel
    ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
    mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
    mv.addObject(NormalExcelConstants.CLASS, Plan3Im.class);
    mv.addObject(NormalExcelConstants.PARAMS, new ExportParams());
    mv.addObject(NormalExcelConstants.DATA_LIST, list);
    return mv;
  }

  /**
   * 通过excel导入数据
   * bai
   * 2020/5/28
   *
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
        List<Plan3Im> list = ExcelImportUtil.importExcel(file.getInputStream(), Plan3Im.class, params);
        Plan3 plan3 = new Plan3();
        for (Plan3Im plan3Im : list) {
          // 单位之间的转换
          List<SysDictItem> dictItems = sysDictItemService.selectType("unit");
          for (SysDictItem dictItem : dictItems) {
            // 导入时若单位不为空则进行单位转换
            if (StrUtil.isNotBlank(plan3Im.getMeasuringUnit())) {
              if (plan3Im.getMeasuringUnit().equals(dictItem.getItemText())) {
                plan3.setMeasuringUnit(Integer.parseInt(dictItem.getItemValue()));
              }
            }
          }
          // 属性拷贝 将 plan3Im 接收的excel数据转换为 plan3 实体类数据
          BeanUtils.copyProperties(plan3Im, plan3);
          // 设置导入类型 [正常/抢修]
//          List<SysDictItem> dictItems1 = sysDictItemService.selectType("plan3_type");
//          for (SysDictItem item : dictItems1) {
//            if (planType.equals(item.getItemText())) {
//              plan3.setPlanType(item.getItemValue());
//            }
//          }
          plan3.setPlanType(planType);
          QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
          queryWrapper.eq("serial", plan3.getMainContractor());
          List<Material> list1 = materialService.list(queryWrapper);
          if (list1.size() == 0) {
            in++;
            // 添加计划3同时对物料进行添加操作
            material.setSerial(plan3.getMaterialCode());
            material.setUnit(plan3.getMeasuringUnit());
            material.setName(plan3.getMaterialDescribe());
            materialService.save(material);
          }
          plan3.setSendOrdersState(0);
          plan3.setCompleteState(0);
          plan3.setUpdateBy(sysUser.getUsername());
          plan3.setCreateBy(sysUser.getUsername());
          plan3.setUpdateTime(new Date());
          plan3.setCreateTime(new Date());
          plan3Service.save(plan3);
        }
        if (in != 0) {
          return Result.ok("文件导入成功！数据行数：" + list.size() + ",有" + in + "个物料信息需要设置");
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
}
