package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Plan4;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.importPackage.Plan4Im;
import org.jeecg.modules.cable.vo.Plan4Vo;

import java.util.List;

/**
 * @Description: 计划表4
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface Plan4Mapper extends BaseMapper<Plan4> {
    /**
     * 分页展示计划表4数据
     * bai
     * 2020/5/29
     *
     * @param plan4
     * @return
     */
    List<Plan4> pageList(@Param("plan4") Plan4 plan4, @Param("page") Page<Plan4> page);

    /**
     * 导出计划表4数据
     * bai
     * 2020/5/27
     *
     * @return
     */
    List<Plan4Im> exportPlan4(@Param("plan4") Plan4 plan4);

    /**
     * 导出计划表4汇总数据
     * bai
     * 2020/5/28
     * @return
     */
    List<Plan4Vo> exportFeedbackSummary();
}
