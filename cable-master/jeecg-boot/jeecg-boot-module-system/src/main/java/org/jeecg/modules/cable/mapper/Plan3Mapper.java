package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan3;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.importpackage.Plan3Im;
import org.jeecg.modules.cable.vo.Plan1Vo;
import org.jeecg.modules.cable.vo.Plan3Vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 计划表3
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface Plan3Mapper extends BaseMapper<Plan3> {
    /**
     * 查询计划3批量出库完单的数据
     * 2020/8/28 bai
     *
     * @param ids 批量出库完单 ids
     * @return 计划3批量出库完单的数据
     */
    List<Plan3Vo> getPlan3ReceivingStorageList(@Param("ids") List<Serializable> ids);

    /**
     * 查询计划3批量入库完单的数据
     * 2020/8/28 bai
     *
     * @param ids 批量入库完单 ids
     * @return 计划3批量入库完单的数据
     */
    List<Plan3> getPlan3DeliverStorage(@Param("ids") List<Serializable> ids);

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
