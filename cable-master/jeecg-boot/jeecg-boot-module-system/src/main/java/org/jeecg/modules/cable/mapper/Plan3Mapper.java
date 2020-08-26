package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Plan3;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.importpackage.Plan3Im;

import java.util.List;

/**
 * @Description: 计划表3
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface Plan3Mapper extends BaseMapper<Plan3> {
    /**
     * 分页展示计划表3数据
     *
     * @param plan3
     * @return
     */
    List<Plan3> pageList(@org.apache.ibatis.annotations.Param("plan3") Plan3 plan3, @org.apache.ibatis.annotations.Param("page") Page<Plan3> page);
    /**
     * 导出计划表3数据
     * bai
     * 2020/5/27
     *
     * @return
     */
    List<Plan3Im> exportPlan3(@Param("plan3") Plan3 plan3);
}
