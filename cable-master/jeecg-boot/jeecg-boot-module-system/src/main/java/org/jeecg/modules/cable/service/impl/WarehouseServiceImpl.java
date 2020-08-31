package org.jeecg.modules.cable.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cable.entity.Warehouse;
import org.jeecg.modules.cable.mapper.WarehouseMapper;
import org.jeecg.modules.cable.service.IWarehouseService;
import org.jeecg.modules.cable.vo.InventoryIocationListVo;
import org.jeecg.modules.cable.vo.InventoryVo;
import org.jeecg.modules.cable.vo.KuweiVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 仓库表
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {

    @Override
    public IPage<InventoryIocationListVo> InventoryIocationListVoPage(InventoryIocationListVo inventoryIocationListVo, Page<InventoryIocationListVo> page) {
        List<InventoryIocationListVo> list = baseMapper.InventoryIocationListVoPage(inventoryIocationListVo, page);
        return page.setRecords(list);
    }

    @Override
    public List<KuweiVo> keweiQuery(String id, String type, String warehouseId) {
        return baseMapper.keweiQuery(id, type, warehouseId);
    }

    @Override
    public IPage<InventoryVo> selectPageinventory(InventoryVo inventoryVo, Page<InventoryVo> page) {
        List<InventoryVo> list = baseMapper.selectPageinventory(inventoryVo, page);
        return page.setRecords(list);
    }

    @Override
    public IPage<InventoryVo> selectInfo(InventoryVo inventoryVo, Page<InventoryVo> page) {
        List<InventoryVo> list = baseMapper.selectInfo(inventoryVo, page);
        return page.setRecords(list);
    }

    @Override
    public List<KuweiVo> kewei(String id) {
        String[] ids = id.split(",");
        return baseMapper.kewei(ids);
    }

    @Override
    public List<KuweiVo> queryInventory(Serializable projectNo) {
        return baseMapper.queryInventory(projectNo);
    }
}
