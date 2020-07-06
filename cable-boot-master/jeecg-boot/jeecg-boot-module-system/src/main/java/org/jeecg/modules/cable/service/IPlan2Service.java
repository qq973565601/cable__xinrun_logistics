package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Plan2;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.importPackage.Plan2Im;
import org.jeecg.modules.cable.vo.PlanVo;

import java.util.List;

/**
 * @Description: 计划表2
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface IPlan2Service extends IService<Plan2> {
    /**
     * 分页展示计划表2数据
     *  bai
     *  2020/5/29
     * @return
     */
    IPage<Plan2> pageList(Plan2 plan2, Page<Plan2> page);
    /**
     * 导出 plan2
     * bai
     * 2020/5/27
     * @return
     */
    List<Plan2Im> exportPlan2(Plan2 plan2, String explain);
}
