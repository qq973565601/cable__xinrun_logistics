package org.jeecg.modules.cable.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.importpackage.Plan2Im;
import org.jeecg.modules.cable.mapper.*;
import org.jeecg.modules.cable.service.IPlan2Service;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: 计划表2
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
@Service
public class Plan2ServiceImpl extends ServiceImpl<Plan2Mapper, Plan2> implements IPlan2Service {

    @Override
    public IPage<Plan2> idsqueryPageList2(List<String> ids, Page<Plan2> page) {
        //TODO 构造条件，根据id的集合做条件查询
        List<Plan2> list = baseMapper.selectBatchIds(ids);
        return page.setRecords(list);
    }

    @Override
    public IPage<Plan2> pageList(Plan2 plan2, Page<Plan2> page) {
        List<Plan2> list = baseMapper.pageList(plan2, page);
        return page.setRecords(list);
    }

    @Override
    public List<Plan2Im> exportPlan2(Plan2 plan2, String explain) {
      List<Plan2Im> list = baseMapper.exportPlan2(plan2);
      for (Plan2Im plan2Im : list) {
        // 设置反馈日期
        plan2Im.setFeedbackDateTime(new Date());
        // 设置反馈说明
        plan2Im.setAnnotation(explain);
      }
      return list;
    }
}
