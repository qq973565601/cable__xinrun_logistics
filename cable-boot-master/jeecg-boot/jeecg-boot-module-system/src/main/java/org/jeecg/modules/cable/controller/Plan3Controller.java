package org.jeecg.modules.cable.controller;

import java.io.IOException;
import java.util.*;
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
        // 构建要添加的物料对象
        Material material = new Material();
        // 构建要添加的计划3对象
        Plan3 plan3 = new Plan3();
        // 构建要添加的计划3对象集合   todo 通过 saveBatch 方法一次性添加集合中所有计划3的数据
        List<Plan3> plan3List = new ArrayList<Plan3>();
        // 构建要添加的物料对象集合    todo 通过 saveBatch 方法一次性添加集合中所有物料的数据
        List<Material> materialList = new ArrayList<Material>();
        // 构建查询构造器
        QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
        // 拿到当前登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // todo 构造物料需要的基本信息 [创建人、创建时间等]
        material.setCreateBy(sysUser.getUsername());
        material.setUpdateBy(sysUser.getUsername());
        material.setCreateTime(new Date());
        material.setUpdateTime(new Date());
        // TODO 记录添加了多少物料数量
        Integer in = 0;
        // 单位之间的转换
        List<SysDictItem> dictItems = sysDictItemService.selectType("unit");
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Plan3Im> list = ExcelImportUtil.importExcel(file.getInputStream(), Plan3Im.class, params);
                for (Plan3Im plan3Im : list) {
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
                    plan3.setPlanType(planType);
                    plan3.setSendOrdersState(0);
                    plan3.setCompleteState(0);
                    plan3.setUpdateBy(sysUser.getUsername());
                    plan3.setCreateBy(sysUser.getUsername());
                    plan3.setUpdateTime(new Date());
                    plan3.setCreateTime(new Date());
                    //todo 每次都向计划3集合中添加单个计划对象信息
                    plan3List.add(plan3);

                    // 根据计划3的物料代码构造条件
                    queryWrapper.eq("serial", plan3.getMaterialCode());
                    // 根据条件查询数据库中是否存在此物料
                    List<Material> list1 = materialService.list(queryWrapper);
                    if (list1.size() == 0) {
                        // 不存在此物料那么就向数据库中新增一条物料信息
                        in++;   // 记录新增的物料信息数量
                        material.setSerial(plan3.getMaterialCode());    // 物料编码
                        material.setUnit(plan3.getMeasuringUnit());     // 物料单位
                        material.setName(plan3.getMaterialDescribe());  // 物料描述
                        //todo 每次都向物料集合中添加单个物料对象信息
                        materialList.add(material);
                    }
                }
                // todo 最后通过 saveBatch 方法全部添加所有的物料和计划3中的数据到库中
                long start = System.currentTimeMillis();
                plan3Service.saveBatch(plan3List);
                materialService.saveBatch(materialList);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
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
