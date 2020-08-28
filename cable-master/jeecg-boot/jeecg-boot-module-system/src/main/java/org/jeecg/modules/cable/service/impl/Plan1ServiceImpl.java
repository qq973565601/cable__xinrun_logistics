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
import org.jeecg.modules.cable.importpackage.Plan1Im;
import org.jeecg.modules.cable.mapper.Plan1Mapper;
import org.jeecg.modules.cable.service.*;
import org.jeecg.modules.cable.vo.Plan1Vo;
import org.jeecg.modules.cable.vo.SettleAccountsDetailsVo;
import org.jeecg.modules.cable.vo.SettleAccountsVo;
import org.jeecg.modules.cable.vo.StorageLocationListVo;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
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
 * @Description: 计划表1
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Service
public class Plan1ServiceImpl extends ServiceImpl<Plan1Mapper, Plan1> implements IPlan1Service {
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> consolidationCompleted(List<Serializable> plan1Ids, String operatorSchema, String receiptNo, String receiptPhotos, String taskTime, List<?> completeOrderList) {
        switch (operatorSchema) {
            case "1":
                List<DeliverStorage> deliverStorageList = new LinkedList<DeliverStorage>();
                for (int i = 0; i < completeOrderList.size(); i++) {
                    // 将泛型集合转换
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    DeliverStorage deliverStorage = new DeliverStorage();
                    deliverStorage.setPlanId(Integer.parseInt(plan1Ids.get(i).toString())); // 计划id
                    deliverStorage.setPlanType(1); // 计划类型(1\2\3\4表)
                    // 拿到物料描述去查询物料信息,取出物料id来使用
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("rawMaterialText").toString()));
                    deliverStorage.setMaterialId(material == null ? 0 : material.getId()); // 物料id
                    deliverStorage.setWarehouseId(Integer.parseInt(map.get("warehouseId").toString())); // 仓库id
                    deliverStorage.setStorageLocationId(Integer.parseInt(map.get("storageLocationId").toString())); // 库位id
                    deliverStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))); // 完单数量
                    deliverStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString())); // 完单数量单位
                    deliverStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))); // 完单容积
                    deliverStorage.setSceneSituation(Integer.parseInt(map.get("sceneSituation").toString())); // 是否异常
                    deliverStorage.setReceiptNo(receiptNo); // 交接单号
                    deliverStorage.setState(1); // 完单状态 默认已完成
                    deliverStorage.setReceiptPhotos(receiptPhotos); // 回单照片路径
                    deliverStorage.setDeliverTime(DateUtil.parse(taskTime)); // 入库日期
                    deliverStorage.setAnnotation(map.get("annotation").toString()); // 说明
                    deliverStorage.setCreateTime(new Date()); // 创建时间
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    deliverStorage.setCreateBy(sysUser == null ? "无" : sysUser.getUsername()); // 创建人
                    deliverStorageList.add(deliverStorage);
                    // 向库存表中存数据
                    // 根据仓库、库位、项目编号、物料编号、资产编号查询要添加的库存信息
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
                    wrapper.eq("warehouse_id", Integer.parseInt(map.get("warehouseId").toString())); // 仓库id
                    wrapper.eq("storage_location_id", Integer.parseInt(map.get("storageLocationId").toString())); // 库位id
                    Plan1 plan1 = this.getOne(new QueryWrapper<Plan1>().eq("id", plan1Ids.get(i).toString()));
                    wrapper.eq("project_no", plan1.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material == null ? null : material.getId()); // 物料编号
                    wrapper.eq("asset_no", plan1.getAssetNo()); // 资产编号
                    if (plan1.getAlreadyDeliverStorage() == null) {
                        plan1.setAlreadyDeliverStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    } else {
                        plan1.setAlreadyDeliverStorage(plan1.getAlreadyDeliverStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())))); // 已入库数
                    }
                    boolean res = this.updateById(plan1);
                    System.err.println("计划1更新数据是否成功:" + res);
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
                        Inventory entity = new Inventory(Integer.parseInt(map.get("warehouseId").toString()), Integer.parseInt(map.get("storageLocationId").toString()), plan1.getProjectNo(), plan1.getProjectName(), material == null ? null : material.getId(), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())), new Date(), sysUser == null ? "无" : sysUser.getUsername(), Integer.parseInt(plan1Ids.get(i).toString()), 1, Integer.parseInt(map.get("unit").toString()), BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())), map.get("assetNo").toString());
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
                    // 将泛型集合转换
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) completeOrderList.get(i);
                    ReceivingStorage receivingStorage = new ReceivingStorage();
                    receivingStorage.setPlanId(Integer.parseInt(plan1Ids.get(i).toString())); // 计划id
                    receivingStorage.setPlanType(1); // 计划类型(1\2\3\4表)
                    // 拿到物料描述去查询物料信息,取出物料id来使用
                    Material material = materialService.getOne(new QueryWrapper<Material>().eq("name", map.get("rawMaterialText").toString()));
                    receivingStorage.setMaterialId(material == null ? 0 : material.getId()); // 物料id
                    receivingStorage.setWarehouseId(Integer.parseInt(map.get("warehouseId").toString())); // 仓库id
                    receivingStorage.setStorageLocationId(Integer.parseInt(map.get("storageLocationId").toString())); // 库位id
                    receivingStorage.setAccomplishNum(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))); // 出库完单数量
                    receivingStorage.setAccomplishNumUnit(Integer.parseInt(map.get("unit").toString())); // 出库完单数量单位
                    receivingStorage.setAccomplishVolume(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                    receivingStorage.setReceiptNo(receiptNo); // 交接单号
                    receivingStorage.setState(1); // 完单状态 默认已完成
                    receivingStorage.setReceiptPhotos(receiptPhotos); // 回单图片路径
                    receivingStorage.setReceivingTime(DateUtil.parse(taskTime)); // 出库日期
                    receivingStorage.setCreateTime(new Date()); // 创建时间
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    receivingStorage.setCreateBy(sysUser == null ? "无" : sysUser.getUsername()); // 创建人
                    receivingStorageList.add(receivingStorage);
                    // 根据仓库、库位、项目编号、物料编号、资产编号查询此库存是否存在
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<Inventory>();
                    wrapper.eq("warehouse_id", Integer.parseInt(map.get("warehouseId").toString())); // 仓库id
                    wrapper.eq("storage_location_id", Integer.parseInt(map.get("storageLocationId").toString())); // 库位id
                    Plan1 plan1 = this.getOne(new QueryWrapper<Plan1>().eq("id", plan1Ids.get(i).toString()));
                    wrapper.eq("project_no", plan1.getProjectNo()); // 项目编号
                    wrapper.eq("material_id", material.getId()); // 物料编号
                    wrapper.eq("asset_no", plan1.getAssetNo()); // 资产编号
                    if (plan1.getAlreadyReceivingStorage() == null) {
                        plan1.setAlreadyReceivingStorage(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                    } else {
                        plan1.setAlreadyReceivingStorage(plan1.getAlreadyReceivingStorage().add(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())))); //TODO 已出库数
                    }
                    boolean res = this.updateById(plan1);
                    System.err.println("计划1更新数据是否成功:" + res);
                    Inventory inventory = inventoryService.getOne(wrapper);
                    if (inventory != null) {
                        // 查看库存表中是否有充足数量够出库操作、够则进行出库、不够则提示库存不足
                        // BigDecimal 类型比较大小返回值为 {-1:小于\0:等于\1:大于}
                        int result = inventory.getInventoryQuantity().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString())));
                        int result2 = inventory.getBackup4().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                        if (result == 0 || result == 1) {
                            // 表示库存数够进行出库完单操作,执行更新操作,将库存数减少
                            inventory.setInventoryQuantity(inventory.getInventoryQuantity().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishNum").toString()))));
                            if (inventory.getBackup4() != null) {
                                if (result2 == 0 || result2 == 1) {
                                    inventory.setBackup4(inventory.getBackup4().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                                } else {
                                    return Result.error("当前库存容积不足, 无法进行出库完单操作,第 " + (i + 1) + "  条库存容积为[" + inventory.getBackup4() + "]");
                                }
                            } else {
                                return Result.error("此库存容积并不存在, 无法进行出库完单操作");
                            }
                            boolean flag = inventoryService.updateById(inventory); // 执行更新库存操作
                            System.err.println("出库是否成功:" + flag + ",出库后库存数为[" + inventory.getInventoryQuantity() + "]");
                        } else {
                            return Result.error("库存不足,不足以完成出库,第 " + (i + 1) + "  条库存数为[" + inventory.getInventoryQuantity() + "]");
                        }
                    } else {
                        return Result.error("此库存并不存在, 无法进行出库完单操作");
                    }
                    StorageLocation storageLocation = storageLocationService.getById(Integer.parseInt(map.get("storageLocationId").toString()));
                    if (storageLocation != null) {
                        int result = storageLocation.getTheCurrentVolume().compareTo(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString())));
                        if (result == 0 || result == 1) {
                            storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(BigDecimal.valueOf(Double.parseDouble(map.get("accomplishVolume").toString()))));
                            boolean flag = storageLocationService.updateById(storageLocation);
                            System.err.println("更新库位容积是否成功:" + flag + ",当前库位[" + storageLocation.getStorageLocationName() + "]容积为[" + storageLocation.getTheCurrentVolume() + "]");
                        } else {
                            return Result.error("当前库存容积不足, 无法进行出库完单操作");
                        }
                    } else {
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
    public List<Plan1Vo> getPlan1ReceivingStorageList(List<Serializable> ids) {
        return baseMapper.getPlan1ReceivingStorageList(ids);
    }

    @Override
    public List<Plan1> getPlan1DeliverStorage(List<Serializable> ids) {
        return baseMapper.getPlan1DeliverStorage(ids);
    }

    @Override
    public IPage<Plan1> pageList(Plan1Vo plan1Vo, Page<Plan1> page) {
        List<Plan1> list = baseMapper.pageList(plan1Vo, page);
        return page.setRecords(list);
    }

    @Override
    public IPage<Plan1> idsqueryPageList(List<String> ids, Page<Plan1> page) {
        //TODO 构造条件，根据id的集合做条件查询
        List<Plan1> list = baseMapper.selectBatchIds(ids);
        return page.setRecords(list);
    }

    @Override
    public IPage<StorageLocationListVo> StorageLocationListVoPage(StorageLocationListVo storageLocationListVo, Page<StorageLocationListVo> page) {
        List<StorageLocationListVo> list = baseMapper.StorageLocationListVoPage(storageLocationListVo, page);
        return page.setRecords(list);
    }

    @Override
    public List<Plan1Im> exportPlan1(Plan1Im plan1Im, String explain) {
        List<Plan1Im> list = baseMapper.exportPlan1(plan1Im);
        for (Plan1Im p : list) {
            // 单位之间的转换
            List<SysDictItem> dictItems = sysDictItemService.selectType("unit");
            for (SysDictItem dictItem : dictItems) {
                // 导出时判断单位是否存在，存在则进行单位转换
                if (StrUtil.isNotBlank(p.getRawMaterialUnit())) {
                    if (p.getRawMaterialUnit().equals(dictItem.getItemValue())) {
                        p.setRawMaterialUnit(dictItem.getItemText());
                    }
                }
                if (StrUtil.isNotBlank(p.getWasteMaterialUnit())) {
                    if (p.getWasteMaterialUnit().equals(dictItem.getItemValue())) {
                        p.setWasteMaterialUnit(dictItem.getItemText());
                    }
                }
            }
            // 设置反馈说明
            p.setExplain(explain);
        }
        return list;
    }

    @Override
    public IPage<SettleAccountsVo> selectSettleAccounts(String backup1, String planType, String projectNo, Page<SettleAccountsVo> page) {
        List<SettleAccountsVo> list = baseMapper.selectSettleAccounts(backup1, planType, projectNo, page);
        return page.setRecords(list);
    }

    @Override
    public IPage<SettleAccountsDetailsVo> selectSettleAccountsDetails(Integer planId, Integer planName, Page<SettleAccountsDetailsVo> page) {
        return page.setRecords(baseMapper.selectSettleAccountsDetails(planId, planName, page));
    }
}
