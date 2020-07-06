package org.jeecg.modules.cable.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.importPackage.Plan1Im;
import org.jeecg.modules.cable.mapper.Plan1Mapper;
import org.jeecg.modules.cable.service.IPlan1Service;
import org.jeecg.modules.cable.vo.Plan1Vo;
import org.jeecg.modules.cable.vo.SettleAccountsDetailsVo;
import org.jeecg.modules.cable.vo.SettleAccountsVo;
import org.jeecg.modules.cable.vo.StorageLocationListVo;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 计划表1
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Service
public class Plan1ServiceImpl extends ServiceImpl<Plan1Mapper, Plan1> implements IPlan1Service {
  @Autowired
  private ISysDictItemService sysDictItemService;

  @Override
  public IPage<Plan1> pageList(Plan1Vo plan1Vo, Page<Plan1> page) {
    List<Plan1> list = baseMapper.pageList(plan1Vo, page);
    return page.setRecords(list);
  }

  @Override
  public IPage<StorageLocationListVo> StorageLocationListVoPage(StorageLocationListVo storageLocationListVo, Page<StorageLocationListVo> page) {
    List<StorageLocationListVo> list = baseMapper.StorageLocationListVoPage(storageLocationListVo, page);
    return page.setRecords(list);
  }

  @Override
  public List<Plan1Im> exportPlan1(Plan1Im plan1Im, String explain) {
    List<Plan1Im> list = baseMapper.exportPlan1(plan1Im);
    for (Plan1Im p : list) {
      // 单位之间的转换
      List<SysDictItem> dictItems = sysDictItemService.selectType("unit");
      for (SysDictItem dictItem : dictItems) {
        // 导出时判断单位是否存在，存在则进行单位转换
        if (StrUtil.isNotBlank(p.getRawMaterialUnit())) {
          if (p.getRawMaterialUnit().equals(dictItem.getItemValue())) {
            p.setRawMaterialUnit(dictItem.getItemText());
          }
        }
        if (StrUtil.isNotBlank(p.getWasteMaterialUnit())) {
          if (p.getWasteMaterialUnit().equals(dictItem.getItemValue())) {
            p.setWasteMaterialUnit(dictItem.getItemText());
          }
        }
      }
      // 设置反馈说明
      p.setExplain(explain);
    }
    return list;
  }

  @Override
  public IPage<SettleAccountsVo> selectSettleAccounts(String backup1,String planType,String projectNo, Page<SettleAccountsVo> page) {
    List<SettleAccountsVo> list = baseMapper.selectSettleAccounts(backup1,planType,projectNo,page);
    return page.setRecords(list);
  }

  @Override
  public IPage<SettleAccountsDetailsVo> selectSettleAccountsDetails(Integer planId, Integer planName, Page<SettleAccountsDetailsVo> page) {
    return page.setRecords(baseMapper.selectSettleAccountsDetails(planId, planName, page));
  }
}
