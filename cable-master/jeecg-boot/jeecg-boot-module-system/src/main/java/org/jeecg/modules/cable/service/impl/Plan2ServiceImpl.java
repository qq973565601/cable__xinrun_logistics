package org.jeecg.modules.cable.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.importpackage.Plan2Im;
import org.jeecg.modules.cable.mapper.*;
import org.jeecg.modules.cable.service.*;
import org.jeecg.modules.cable.vo.Plan2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 计划表2
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Service
public class Plan2ServiceImpl extends ServiceImpl<Plan2Mapper, Plan2> implements IPlan2Service {
    @Autowired
    private IDeliverStorageService deliverStorageService;
    @Autowired
    private IReceivingStorageService receivingStorageService;
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IInventoryService inventoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> consolidationCompleted(List<Serializable> ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList) {
        switch (operatorSchema) {
            case "1":
                List<DeliverStorage> deliverStorageList = new LinkedList<DeliverStorage>();
                for (int i = 0; i < completeOrderList.size(); i++) {
                    // 转换泛型集合
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    // 向入库完单表存数据
                    DeliverStorage deliverStorage = new DeliverStorage();
                    deliverStorage.setPlanId(Integer.parseInt(ids.get(i).toString()));
                    deliverStorage.setPlanType(2);
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("equipmentName").toString()));
                    deliverStorage.setMaterialId(material == null ? 0 : material.getId());
                    deliverStorage.setWarehouseId(Integer.parseInt(map.get("warehouseId").toString()));
                    deliverStorage.setStorageLocationId(Integer.parseInt(map.get("storageLocationId").toString()));
                    deliverStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    deliverStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString()));
                    deliverStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    deliverStorage.setSceneSituation(Integer.parseInt(map.get("sceneSituation").toString()));
                    deliverStorage.setReceiptNo(receiptNo);
                    deliverStorage.setState(1);
                    deliverStorage.setReceiptPhotos(receiptPhotos);
                    deliverStorage.setDeliverTime(DateUtil.parse(taskTime));
                    deliverStorage.setAnnotation(map.get("annotation").toString());
                    deliverStorage.setCreateTime(new Date());
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    deliverStorage.setCreateBy(sysUser == null ? "无" : sysUser.getUsername());
                    deliverStorageList.add(deliverStorage);
                    // 向库存表中存数据
                    // 根据仓库、库位、项目编号、物料编号、资产编号查询要添加的库存信息
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
                    wrapper.eq("warehouse_id", Integer.parseInt(map.get("warehouseId").toString()));
                    wrapper.eq("storage_location_id", Integer.parseInt(map.get("storageLocationId").toString()));
                    Plan2 plan2 = this.getOne(new QueryWrapper<Plan2>().eq("id", ids.get(i).toString()));
                    wrapper.eq("project_no", plan2.getProjectNo());
                    wrapper.eq("material_id", material == null ? null : material.getId());
                    wrapper.eq("asset_no", plan2.getAssetNo());
                    // 向计划表2中添加积累的已入库数量
                    if (plan2.getAlreadyDeliverStorage() == null) {
                        plan2.setAlreadyDeliverStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    } else {
                        plan2.setAlreadyDeliverStorage(plan2.getAlreadyDeliverStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                    }
                    boolean res = this.updateById(plan2);
                    System.err.println("计划2更新数据是否成功:" + res);
                    Inventory inventory = inventoryService.getOne(wrapper);
                    if (inventory != null) {
                        // 存在库存,在原本的库存数上增加入库数量即可
                        System.err.println("入库前库存数为[" + inventory.getInventoryQuantity() + "]");
                        BigDecimal num = inventory.getInventoryQuantity().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                        inventory.setInventoryQuantity(num);
                        boolean flag = inventoryService.updateById(inventory);
                        System.err.println("入库完单是否成功:" + flag + ",入库后库存数为[" + inventory.getInventoryQuantity() + "]");
                    } else {
                        // 没有库存信息,需要新增一条库存信息,库存数就等于入库数量即可
                        Inventory entity = new Inventory(Integer.parseInt(map.get("warehouseId").toString()), Integer.parseInt(map.get("storageLocationId").toString()), plan2.getProjectNo(), plan2.getSite(), material == null ? null : material.getId(), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())), new Date(), sysUser == null ? "无" : sysUser.getUsername(), Integer.parseInt(ids.get(i).toString()), 2, Integer.parseInt(map.get("unit").toString()), map.get("assetNo").toString());
                        boolean flag = inventoryService.save(entity);
                        System.err.println("新增库存是否成功:" + flag + ",新增后库存数为[" + entity.getInventoryQuantity() + "]");
                    }
                }
                deliverStorageService.saveBatch(deliverStorageList);
                return Result.ok("入库完单成功");
            case "0":
                List<ReceivingStorage> receivingStorageList = new LinkedList<ReceivingStorage>();
                for (int i = 0; i < completeOrderList.size(); i++) {
                    // 转换泛型集合
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    // 向出库完单表中添加数据
                    ReceivingStorage receivingStorage = new ReceivingStorage();
                    receivingStorage.setPlanId(Integer.parseInt(ids.get(i).toString()));
                    receivingStorage.setPlanType(2);
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("equipmentName").toString()));
                    receivingStorage.setMaterialId(material == null ? 0 : material.getId());
                    receivingStorage.setWarehouseId(Integer.parseInt(map.get("warehouseId").toString()));
                    receivingStorage.setStorageLocationId(Integer.parseInt(map.get("storageLocationId").toString()));
                    receivingStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    receivingStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString()));
                    receivingStorage.setReceiptNo(receiptNo);
                    receivingStorage.setState(1);
                    receivingStorage.setReceiptPhotos(receiptPhotos);
                    receivingStorage.setReceivingTime(DateUtil.parse(taskTime));
                    receivingStorage.setCreateTime(new Date());
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    receivingStorage.setCreateBy(sysUser == null ? "无" : sysUser.getUsername());
                    receivingStorageList.add(receivingStorage);
                    // 向库存表中存数据
                    // 根据仓库、库位、项目编号、物料编号、资产编号查询要添加的库存信息
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
                    wrapper.eq("warehouse_id", Integer.parseInt(map.get("warehouseId").toString()));
                    wrapper.eq("storage_location_id", Integer.parseInt(map.get("storageLocationId").toString()));
                    Plan2 plan2 = this.getOne(new QueryWrapper<Plan2>().eq("id", ids.get(i).toString()));
                    wrapper.eq("project_no", plan2.getProjectNo());
                    wrapper.eq("material_id", material.getId());
                    wrapper.eq("asset_no", plan2.getAssetNo());
                    // 向计划表2中添加积累的已出库数量
                    if (plan2.getAlreadyReceivingStorage() == null) {
                        plan2.setAlreadyReceivingStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    } else {
                        plan2.setAlreadyReceivingStorage(plan2.getAlreadyReceivingStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                    }
                    boolean res = this.updateById(plan2);
                    System.err.println("计划2更新数据是否成功:" + res);
                    Inventory inventory = inventoryService.getOne(wrapper);
                    if (inventory != null) {
                        // 查看库存表中是否有充足数量够出库操作、够则进行出库、不够则提示库存不足
                        // BigDecimal 类型比较大小返回值为 {-1:小于\0:等于\1:大于}
                        int result = inventory.getInventoryQuantity().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                        if (result == 0 || result == 1) {
                            // 表示库存数够进行出库完单操作,执行更新操作,将库存数减少
                            System.err.println("出库前库存数为[" + inventory.getInventoryQuantity() + "]");
                            BigDecimal num = inventory.getInventoryQuantity().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                            inventory.setInventoryQuantity(num);
                            boolean flag = inventoryService.updateById(inventory);
                            System.err.println("出库是否成功:" + flag + ",出库后库存数为[" + inventory.getInventoryQuantity() + "]");
                        } else {
                            return Result.error("库存不足,不足以完成出库,第 " + (i + 1) + "  条库存数为[" + inventory.getInventoryQuantity() + "]");
                        }
                    } else {
                        return Result.error("此库存并不存在, 无法进行出库完单操作");
                    }
                }
                receivingStorageService.saveBatch(receivingStorageList);
                return Result.ok("出库完单成功");
            default:
                return Result.ok("完单操作失败");
        }
    }

    @Override
    public List<Plan2Vo> getPlan2ReceivingStorageList(List<Serializable> ids) {
        return baseMapper.getPlan2ReceivingStorageList(ids);
    }

    @Override
    public List<Plan2> getPlan2DeliverStorage(List<Serializable> ids) {
        return baseMapper.getPlan2DeliverStorage(ids);
    }

    @Override
    public IPage<Plan2> idsqueryPageList2(List<String> ids, Page<Plan2> page) {
        //TODO 构造条件，根据id的集合做条件查询
        List<Plan2> list = baseMapper.selectBatchIds(ids);
        return page.setRecords(list);
    }

    @Override
    public IPage<Plan2> pageList(Plan2 plan2, Page<Plan2> page) {
        List<Plan2> list = baseMapper.pageList(plan2, page);
        return page.setRecords(list);
    }

    @Override
    public List<Plan2Im> exportPlan2(Plan2 plan2, String explain) {
        List<Plan2Im> list = baseMapper.exportPlan2(plan2);
        for (Plan2Im plan2Im : list) {
            // 设置反馈日期
            plan2Im.setFeedbackDateTime(new Date());
            // 设置反馈说明
            plan2Im.setAnnotation(explain);
        }
        return list;
    }
}
