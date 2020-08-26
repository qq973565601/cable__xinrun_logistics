package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Plan2;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.importpackage.Plan2Im;

import java.util.List;

/**
 * @Description: 计划表2
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface Plan2Mapper extends BaseMapper<Plan2> {
    /**
     * 分页展示计划表2数据
     *  bai
     *  2020/5/29
     * @param plan2
     * @return
     */
    List<Plan2> pageList(@Param("plan2") Plan2 plan2,@Param("page") Page<Plan2> page);

    /**
     * 导出计划表2数据
     * bai
     * 2020/5/27
     *
     * @return
     */
    List<Plan2Im> exportPlan2(@Param("plan2") Plan2 plan2);
}
