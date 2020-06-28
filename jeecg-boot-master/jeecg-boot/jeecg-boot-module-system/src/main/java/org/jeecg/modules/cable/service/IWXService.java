package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.vo.pc.Deliver_Receiving_StorageVo;
import org.jeecg.modules.cable.vo.pc.PickUpTheTaskVo;

/**
 * WX小程序
 * bai
 * 2020/6/11
 */
public interface IWXService {
    /**
     * 接任务
     * bai
     * 2020/6/11
     */
    IPage<PickUpTheTaskVo> pickUpTheTask(Integer completeState, Page<PickUpTheTaskVo> page);

    /**
     * 入库处置
     * bai
     * 2020/6/15
     */
    Integer deliverStorage(Deliver_Receiving_StorageVo vo);

    /**
     * 出库处置
     * bai
     * 2020/6/15
     */
    Integer receivingStorage(Deliver_Receiving_StorageVo vo);
}
