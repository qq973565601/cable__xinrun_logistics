package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan4;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.importpackage.Plan4Im;
import org.jeecg.modules.cable.vo.Plan4Vo;

import java.util.List;

/**
 * @Description: 计划表4
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface IPlan4Service extends IService<Plan4> {

    /**
     * 根据ids集合条件查询
     *
     * @param ids
     *  liu
     * @Date 2020/7/21
     */
    IPage<Plan4> idsqueryPageList4(List<String> ids, Page<Plan4> page);
    /**
     * 分页展示计划表4数据
     * bai
     * 2020/5/29
     *
     * @param plan4
     * @return
     */
    IPage<Plan4> pageList(Plan4 plan4, Page<Plan4> page);

    /**
     * 导出计划表4数据
     * bai
     * 2020/5/27
     *
     * @return
     */
    List<Plan4Im> exportPlan4(Plan4 plan4, String explain);

    /**
     * 导出计划表4汇总数据
     * bai
     * 2020/5/28
     * @return
     */
    List<Plan4Vo> exportFeedbackSummary();
}
