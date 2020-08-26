package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Plan1;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.importpackage.Plan1Im;
import org.jeecg.modules.cable.vo.Plan1Vo;
import org.jeecg.modules.cable.vo.SettleAccountsDetailsVo;
import org.jeecg.modules.cable.vo.SettleAccountsVo;
import org.jeecg.modules.cable.vo.StorageLocationListVo;

import java.util.List;

/**
 * @Description: 计划表1
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
public interface IPlan1Service extends IService<Plan1> {

  /**
   * 根据ids集合条件查询
   * 条件分页查询计划1
   *
   * @param plan1Vo
   * @Author Xm
   * @Date 2020/5/27 15:09
   */
  IPage<Plan1> pageList(Plan1Vo plan1Vo, Page<Plan1> page);

  /**
   * 根据ids集合条件查询
   *
   * @param ids
   *  liu
   * @Date 2020/7/21
   */
  IPage<Plan1> idsqueryPageList(List<String> ids, Page<Plan1> page);

  /**
   * 查看库位
   *
   * @param storageLocationListVo 用来保存vo
   * @param page                  分页条件
   * @Author Xm
   * @Date 2020/5/15 11:26
   */
  IPage<StorageLocationListVo> StorageLocationListVoPage(StorageLocationListVo storageLocationListVo, Page<StorageLocationListVo> page);

  /**
   * 导出 plan1
   * bai
   * 2020/5/27
   *
   * @return
   */
  List<Plan1Im> exportPlan1(Plan1Im plan1Im, String explain);

  IPage<SettleAccountsVo> selectSettleAccounts(String backup1,String planType,String projectNo,Page<SettleAccountsVo> page);

  IPage<SettleAccountsDetailsVo> selectSettleAccountsDetails(Integer planId, Integer planName, Page<SettleAccountsDetailsVo> page);
}
