package org.jeecg.modules.cable.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Plan3;
import org.jeecg.modules.cable.importPackage.Plan3Im;
import org.jeecg.modules.cable.mapper.Plan3Mapper;
import org.jeecg.modules.cable.service.IPlan3Service;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 计划表3
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Service
public class Plan3ServiceImpl extends ServiceImpl<Plan3Mapper, Plan3> implements IPlan3Service {
  @Autowired
  private ISysDictItemService sysDictItemService;

  @Override
  public IPage<Plan3> pageList(Plan3 plan3, Page<Plan3> page) {
    List<Plan3> list = baseMapper.pageList(plan3, page);
    return page.setRecords(list);
  }

  @Override
  public List<Plan3Im> exportPlan3(Plan3 plan3, String explain) {
    List<Plan3Im> list = baseMapper.exportPlan3(plan3);
    for (Plan3Im p : list) {
      // 单位之间的转换
      List<SysDictItem> dictItems = sysDictItemService.selectType("unit");
      for (SysDictItem dictItem : dictItems) {
        // 导出时若单位不为空才进行单位转换
        if (StrUtil.isNotBlank(p.getMeasuringUnit())) {
          if (p.getMeasuringUnit().equals(dictItem.getItemValue())) {
            p.setMeasuringUnit(dictItem.getItemText());
          }
        }
      }
      // 设置导出反馈说明
      p.setNote2(explain);
    }
    return list;
  }
}
