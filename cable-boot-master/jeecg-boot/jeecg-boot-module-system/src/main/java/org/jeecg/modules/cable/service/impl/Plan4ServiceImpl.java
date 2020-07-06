package org.jeecg.modules.cable.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Plan4;
import org.jeecg.modules.cable.importPackage.Plan4Im;
import org.jeecg.modules.cable.mapper.Plan4Mapper;
import org.jeecg.modules.cable.service.IPlan4Service;
import org.jeecg.modules.cable.vo.Plan4Vo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 计划表4
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
@Service
public class Plan4ServiceImpl extends ServiceImpl<Plan4Mapper, Plan4> implements IPlan4Service {
    @Override
    public IPage<Plan4> pageList(Plan4 plan4, Page<Plan4> page) {
        List<Plan4> list = baseMapper.pageList(plan4, page);
        return page.setRecords(list);
    }
    @Override
    public List<Plan4Im> exportPlan4(Plan4 plan4, String explain) {
      List<Plan4Im> list = baseMapper.exportPlan4(plan4);
      for (Plan4Im plan4Im : list) {
        // 设置反馈说明
        plan4Im.setFeedback(explain);
      }
      return list;
    }

    @Override
    public List<Plan4Vo> exportFeedbackSummary() {
        return baseMapper.exportFeedbackSummary();
    }
}
