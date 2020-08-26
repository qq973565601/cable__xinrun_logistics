package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan3;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.importpackage.Plan3Im;

import java.util.List;

/**
 * @Description: 计划表3
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface IPlan3Service extends IService<Plan3> {

    /**
     * 根据ids集合条件查询
     *
     * @param ids
     *  liu
     * @Date 2020/7/21
     */
    IPage<Plan3> idsqueryPageList3(List<String> ids, Page<Plan3> page);
    /**
     * 分页展示计划表3数据
     * @param plan3
     * @return
     */
    IPage<Plan3> pageList(Plan3 plan3, Page<Plan3> page);
    /**
     * 导出计划表3数据
     * bai
     * 2020/5/27
     * @return
     */
    List<Plan3Im> exportPlan3(Plan3 plan3, String explain);
}
