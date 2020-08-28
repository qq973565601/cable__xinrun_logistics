package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan2;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.importpackage.Plan2Im;
import org.jeecg.modules.cable.vo.Plan1Vo;
import org.jeecg.modules.cable.vo.Plan2Vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 计划表2
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
public interface Plan2Mapper extends BaseMapper<Plan2> {
    /**
     * 查询计划2批量出库完单的数据
     * 2020/8/28 bai
     *
     * @param ids 批量出库完单 ids
     * @return 计划2批量出库完单的数据
     */
    List<Plan2Vo> getPlan2ReceivingStorageList(@Param("ids") List<Serializable> ids);

    /**
     * 查询计划2批量入库完单的数据
     * 2020/8/28 bai
     *
     * @param ids 批量入库完单 ids
     * @return 计划2批量入库完单的数据
     */
    List<Plan2> getPlan2DeliverStorage(@Param("ids") List<Serializable> ids);

    /**
     * 分页展示计划表2数据
     * bai
     * 2020/5/29
     *
     * @param plan2
     * @return
     */
    List<Plan2> pageList(@Param("plan2") Plan2 plan2, @Param("page") Page<Plan2> page);

    /**
     * 导出计划表2数据
     * bai
     * 2020/5/27
     *
     * @return
     */
    List<Plan2Im> exportPlan2(@Param("plan2") Plan2 plan2);
}
