package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Plan1;
import org.jeecg.modules.cable.entity.Plan2;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.importpackage.Plan2Im;

import java.util.List;

/**
 * @Description: 计划表2
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface IPlan2Service extends IService<Plan2> {

    /**
     * 根据ids集合条件查询
     *
     * @param ids
     *  liu
     * @Date 2020/7/21
     */
    IPage<Plan2> idsqueryPageList2(List<String> ids, Page<Plan2> page);
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
