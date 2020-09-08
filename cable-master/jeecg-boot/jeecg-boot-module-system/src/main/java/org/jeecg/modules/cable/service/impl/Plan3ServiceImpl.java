package org.jeecg.modules.cable.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.importpackage.Plan3Im;
import org.jeecg.modules.cable.mapper.Plan3Mapper;
import org.jeecg.modules.cable.service.*;
import org.jeecg.modules.cable.vo.Plan3Vo;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 计划表3
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Service
public class Plan3ServiceImpl extends ServiceImpl<Plan3Mapper, Plan3> implements IPlan3Service {
    @Autowired
    private ISysDictItemService sysDictItemService;
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IDeliverStorageService deliverStorageService;
    @Autowired
    private IReceivingStorageService receivingStorageService;
    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private IStorageLocationService storageLocationService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IPlan3Service plan3Service;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> consolidationCompleted(List<Serializable> ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList) {
        switch (operatorSchema) {
            case "1":
                List<DeliverStorage> deliverStorageList = new LinkedList<DeliverStorage>();
                for (int i = 0; i < completeOrderList.size(); i++) {
                    // 转换泛型集合
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    // 添加入库完单信息
                    DeliverStorage deliverStorage = new DeliverStorage();
                    deliverStorage.setPlanId(Integer.parseInt(ids.get(i).toString()));
                    deliverStorage.setPlanType(3);
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("materialDescribe").toString()));
                    deliverStorage.setMaterialId(material == null ? 0 : material.getId());
                    deliverStorage.setWarehouseId(Integer.parseInt(map.get("warehouseId").toString()));
                    deliverStorage.setStorageLocationId(Integer.parseInt(map.get("storageLocationId").toString()));
                    deliverStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    deliverStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString()));
                    deliverStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    deliverStorage.setSceneSituation(Integer.parseInt(map.get("sceneSituation").toString()));
                    deliverStorage.setAnomalousCause(map.get("anomalousCause").toString()); // 异常原因
                    if (map.get("scenePhotos1") != null) { // 判断是否上传了多张异常图片
                        LinkedHashMap<String, Object> photos1 = (LinkedHashMap<String, Object>) map.get("scenePhotos1");
                        deliverStorage.setScenePhotos(photos1.get("path").toString()); // 添加异常图片1
                        if (map.get("scenePhotos2") != null) {
                            LinkedHashMap<String, Object> photos2 = (LinkedHashMap<String, Object>) map.get("scenePhotos2");
                            deliverStorage.setScenePhotos(photos1.get("path").toString() + "," + photos2.get("path").toString()); // 添加异常图片2
                        }
                    }
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
                    Plan3 plan3 = this.getOne(new QueryWrapper<Plan3>().eq("id", ids.get(i).toString()));
                    wrapper.eq("project_no", plan3.getProjectNo());
                    wrapper.eq("material_id", material == null ? null : material.getId());
                    wrapper.eq("asset_no", plan3.getProTheorderNo());
                    if (plan3.getAlreadyDeliverStorage() == null) {
                        plan3.setAlreadyDeliverStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    } else {
                        plan3.setAlreadyDeliverStorage(plan3.getAlreadyDeliverStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                    }
                    boolean res = this.updateById(plan3);
                    System.err.println("计划3更新数据是否成功:" + res);
                    Inventory inventory = inventoryService.getOne(wrapper);
                    if (inventory != null) {
                        // 存在库存,在原本的库存数上增加入库数量即可
                        inventory.setInventoryQuantity(inventory.getInventoryQuantity().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                        if (inventory.getBackup4() != null) {
                            inventory.setBackup4(inventory.getBackup4().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                        } else {
                            inventory.setBackup4(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                        }
                        boolean flag = inventoryService.updateById(inventory);
                        System.err.println("入库完单是否成功:" + flag + ",入库后库存数为[" + inventory.getInventoryQuantity() + "]");
                    } else {
                        // 没有库存信息,需要新增一条库存信息,库存数就等于入库数量即可
                        Inventory entity = new Inventory(Integer.parseInt(map.get("warehouseId").toString()), Integer.parseInt(map.get("storageLocationId").toString()), plan3.getProjectNo(), plan3.getEngName(), material == null ? null : material.getId(), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())), new Date(), sysUser == null ? "无" : sysUser.getUsername(), Integer.parseInt(ids.get(i).toString()), 3, Integer.parseInt(map.get("unit").toString()), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())), null, map.get("proTheorderNo").toString());
                        boolean flag = inventoryService.save(entity);
                        System.err.println("新增库存是否成功:" + flag + ",新增后库存数为[" + entity.getInventoryQuantity() + "]");
                    }
                    StorageLocation storageLocation = storageLocationService.getById(Integer.parseInt(map.get("storageLocationId").toString()));
                    if (storageLocation.getTheCurrentVolume() == null) {
                        storageLocation.setTheCurrentVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    } else {
                        storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                    }
                    boolean flag = storageLocationService.updateById(storageLocation);
                    System.err.println("更新库位容积是否成功:" + flag + ",当前库位[" + storageLocation.getStorageLocationName() + "]容积为[" + storageLocation.getTheCurrentVolume() + "]");
                }
                deliverStorageService.saveBatch(deliverStorageList);
                return Result.ok("入库完单成功");
            case "0":
                List<ReceivingStorage> receivingStorageList = new LinkedList<ReceivingStorage>();
                for (int i = 0; i < completeOrderList.size(); i++) {
                    // 转换泛型集合
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    // 添加出库信息
                    ReceivingStorage receivingStorage = new ReceivingStorage();
                    receivingStorage.setPlanId(Integer.parseInt(ids.get(i).toString()));
                    receivingStorage.setPlanType(3);
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("materialDescribe").toString()));
                    receivingStorage.setMaterialId(material == null ? 0 : material.getId());
                    Warehouse warehouse = warehouseService.getOne(new QueryWrapper<Warehouse>().eq("name", map.get("warehouseName").toString()));
                    receivingStorage.setWarehouseId(warehouse == null ? null : warehouse.getId()); // 仓库id
                    StorageLocation storageLocation = storageLocationService.getOne(new QueryWrapper<StorageLocation>().eq("storage_location_name", map.get("storageLocationName").toString()));
                    receivingStorage.setStorageLocationId(storageLocation == null ? null : storageLocation.getId()); // 库位id
                    receivingStorage.setBackup1(Integer.parseInt(map.get("endWarehouseId").toString())); // 终点仓库id
                    receivingStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    receivingStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString()));
                    receivingStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    receivingStorage.setReceiptNo(receiptNo);
                    receivingStorage.setState(1);
                    receivingStorage.setReceiptPhotos(receiptPhotos);
                    receivingStorage.setReceivingTime(DateUtil.parse(taskTime));
                    receivingStorage.setCreateTime(new Date());
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    receivingStorage.setCreateBy(sysUser == null ? "无" : sysUser.getUsername());
                    receivingStorageList.add(receivingStorage);
                    // 根据仓库、库位、项目编号、物料编号、资产编号查询此库存是否存在
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
                    wrapper.eq("warehouse_id", warehouse == null ? null : warehouse.getId()); // 仓库id
                    wrapper.eq("storage_location_id", storageLocation == null ? null : storageLocation.getId()); // 库位id
                    Plan3 plan3 = this.getOne(new QueryWrapper<Plan3>().eq("id", ids.get(i).toString()));
                    wrapper.eq("project_no", plan3.getProjectNo());
                    wrapper.eq("material_id", material != null ? material.getId() : null);
                    wrapper.eq("asset_no", plan3.getProTheorderNo());
                    if (plan3.getAlreadyReceivingStorage() == null) {
                        plan3.setAlreadyReceivingStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    } else {
                        plan3.setAlreadyReceivingStorage(plan3.getAlreadyReceivingStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                    }
                    boolean res = plan3Service.updateById(plan3);
                    System.err.println("计划3更新数据是否成功:" + res);
                    Inventory inventory = inventoryService.getOne(wrapper);
                    if (inventory != null) {
                        if (inventory.getInventoryQuantity() == null || inventory.getBackup4() == null) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                            return Result.error("该库存数量或该库存容积不存在,无法进行出库操作");
                        }
                        int result = inventory.getInventoryQuantity().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                        int result2 = inventory.getBackup4().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                        if (result == 0 || result > 0) {
                            inventory.setInventoryQuantity(inventory.getInventoryQuantity().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                            if (result2 == 0 || result2 > 0) {
                                inventory.setBackup4(inventory.getBackup4().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                                boolean flag = inventoryService.updateById(inventory);
                                System.err.println("出库是否成功:" + flag + ",出库后库存数为[" + inventory.getInventoryQuantity() + "]");
                                if (inventory.getInventoryQuantity().compareTo(BigDecimal.valueOf(0)) == 0) {
                                    //todo 库存数为0时删除此库存记录
                                    inventoryService.removeById(inventory.getId());
                                    System.err.println("删除库存记录成功");
                                }
                            } else {
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                                return Result.error("库存容积不足,无法进行出库操作");
                            }
                        } else {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                            return Result.error("库存数量不足,无法进行出库操作");
                        }
                    } else {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                        return Result.error("此库存并不存在, 无法进行出库完单操作");
                    }
                    if (storageLocation != null) {
                        int result = storageLocation.getTheCurrentVolume().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                        if (result == 0 || result > 0) {
                            storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                            boolean flag = storageLocationService.updateById(storageLocation);
                            System.err.println("更新库位容积是否成功:" + flag + ",当前库位[" + storageLocation.getStorageLocationName() + "]容积为[" + storageLocation.getTheCurrentVolume() + "]");
                        } else {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                            return Result.error("当前库存容积不足, 无法进行出库完单操作");
                        }
                    } else {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚事务
                        return Result.error("此库存容积并不存在, 无法进行出库完单操作");
                    }
                }
                receivingStorageService.saveBatch(receivingStorageList);
                return Result.ok("出库完单成功");
            default:
                return Result.ok("完单操作失败");
        }
    }

    @Override
    public List<Plan3Vo> getPlan3ReceivingStorageList(List<Serializable> ids) {
        return baseMapper.getPlan3ReceivingStorageList(ids);
    }

    @Override
    public List<Plan3> getPlan3DeliverStorage(List<Serializable> ids) {
        return baseMapper.getPlan3DeliverStorage(ids);
    }

    @Override
    public List<Plan3> idsqueryPageList3(List<String> ids) {
        //TODO 构造条件，根据id的集合做条件查询
        List<Plan3> list = baseMapper.selectBatchIds(ids);
        return list;
    }

    @Override
    public IPage<Plan3> pageList(Plan3 plan3, Page<Plan3> page) {
        List<Plan3> list = baseMapper.pageList(plan3, page);
        return page.setRecords(list);
    }

    @Override
    public List<Plan3Im> exportPlan3(Plan3 plan3, String explain) {
        // 单位之间的转换
        List<SysDictItem> dictItems = sysDictItemService.selectType("unit");
        List<Plan3Im> list = baseMapper.exportPlan3(plan3);
        for (Plan3Im p : list) {
            for (SysDictItem dictItem : dictItems) {
                // 导出时若单位不为空才进行单位转换
                if (StrUtil.isNotBlank(p.getMeasuringUnit())) {
                    if (p.getMeasuringUnit().equals(dictItem.getItemValue())) {
                        p.setMeasuringUnit(dictItem.getItemText());
                    }
                }
            }
            // 设置导出反馈说明
            p.setAnnotation(explain);
            // 设置导出返回日期
            p.setFeedbackDateTime(new Date());
        }
        return list;
    }
}
