package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Warehouse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.vo.InventoryIocationListVo;
import org.jeecg.modules.cable.vo.InventoryVo;
import org.jeecg.modules.cable.vo.KuweiVo;

import java.util.List;

/**
 * @Description: 仓库表
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
public interface IWarehouseService extends IService<Warehouse> {
    /**
     * 库存库位查询
     *
     * @param inventoryIocationListVo 用来保存vo
     * @param page                    分页条件
     * @Author Xm
     * @Date 2020/5/15 11:26
     */
    IPage<InventoryIocationListVo> InventoryIocationListVoPage(InventoryIocationListVo inventoryIocationListVo, Page<InventoryIocationListVo> page);

    List<KuweiVo> keweiQuery(String id, String type, String warehouseId);

    IPage<InventoryVo> selectPageinventory(InventoryVo inventoryVo, Page<InventoryVo> page);

    List<KuweiVo> kewei(String id);
}
