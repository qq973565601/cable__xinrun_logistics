package org.jeecg.modules.cable.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cable.entity.*;
import org.jeecg.modules.cable.mapper.*;
import org.jeecg.modules.cable.service.*;
import org.jeecg.modules.cable.utils.CollectionCopyUtil;
import org.jeecg.modules.cable.vo.PlanVo;
import org.jeecg.modules.cable.vo.SendOrdersTaskVo;
import org.jeecg.modules.cable.vo.SendOrdersVo;
import org.jeecg.modules.cable.vo.TaskVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 派单表
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
@Service
public class SendOrdersServiceImpl extends ServiceImpl<SendOrdersMapper, SendOrders> implements ISendOrdersService {

    @Autowired
    private SendOrdersMapper sendOrdersMapper;

    @Autowired
    private ISendOrdersSubtabulationService sendOrdersSubtabulationService;

    @Autowired
    private DeliverStorageMapper deliverStorageMapper;

    @Autowired
    private ReceivingStorageMapper receivingStorageMapper;

    @Autowired
    private StorageLocationMapper storageLocationMapper;

    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private IPlan1Service plan1Service;

    @Autowired
    private IPlan2Service plan2Service;

    @Autowired
    private IPlan3Service plan3Service;

    @Autowired
    private IPlan4Service plan4Service;

    @Override
    public List<TaskVo> selectTheSameMonthSendOrders(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        try {
            date1 = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sendOrdersMapper.selectTheSameMonthSendOrders(date1);
    }

    @Override
    public IPage<SendOrdersTaskVo> taskList(String date, Page<SendOrdersTaskVo> page) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        try {
            if (StrUtil.isNotBlank(date)) {
                // 日期不为空则进行日期转换
                date1 = sdf.parse(date);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List<SendOrdersTaskVo> list = sendOrdersMapper.taskList(date1, page);
        for (SendOrdersTaskVo sendOrdersTaskVo : list) {
            SendOrdersTaskVo sendOrdersTaskVo1 = sendOrdersMapper.getPlan(sendOrdersTaskVo.getPlanTypeName(), sendOrdersTaskVo.getPlanId());
            if (sendOrdersTaskVo.getPlanTypeName().equals("4")) {
                sendOrdersTaskVo.setPlanType("电缆2");
            }
            if (sendOrdersTaskVo.getPlanTypeName().equals("2")) {
                sendOrdersTaskVo.setPlanType("备品");
            }
            if (sendOrdersTaskVo.getPlanType() == null) {
                sendOrdersTaskVo.setPlanType(sendOrdersTaskVo1.getPlanType());
            }
            sendOrdersTaskVo.setProjectNo(sendOrdersTaskVo1.getProjectNo());
            sendOrdersTaskVo.setProjectName(sendOrdersTaskVo1.getProjectName());
            sendOrdersTaskVo.setEngineeringAddress(sendOrdersTaskVo1.getEngineeringAddress());
        }
        return page.setRecords(list);
    }

    @Override
    public List<String> yearsList() {
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String time = sdf.format(date);
        Integer tim = Integer.parseInt(time);
        for (int i = 0; i < 5; i++) {
            Integer ye = tim - i;
            list.add(ye.toString());
        }
        return list;
    }

    @Override
    public Integer saveSendOrdersVo(SendOrdersVo sendOrdersVo, Date date, String name) {
        baseMapper.saveSendOrders(sendOrdersVo, date, name);
        return sendOrdersVo.getId();
    }

    @Override
    public List<PlanVo> selectPlan2Accomplish(String projectNo, String id, String planType, String sendOrdersId, Page<PlanVo> page) {
        List<PlanVo> list = deliverStorageMapper.selectPlan2DS(projectNo, id, planType, sendOrdersId, page);
        for (PlanVo planVo : list) {
            if (planVo.getReceiptPhotoss() != null) {
                String[] photos = planVo.getReceiptPhotoss().split(",");
                planVo.setReceiptPhotos(photos[0]);
            }
            if (planVo.getScenePhotos() != null) {
                String[] photos2 = planVo.getScenePhotos().split(",");
                planVo.setScenePhotos1(photos2[0]);
                if (photos2.length > 1) {
                    planVo.setScenePhotos2(photos2[1]);
                }
            }
        }
        return list;
    }

    @Transactional
    @Override
    public Result<?> planedit(PlanVo planVo) {
        planVo.setState(1);
        if (planVo.getScenePhotos1() != null) {
            planVo.setScenePhotos(planVo.getScenePhotos1());
            if (planVo.getScenePhotos2() != null) {
                planVo.setScenePhotos(planVo.getScenePhotos1() + "," + planVo.getScenePhotos2());
            }
        }
        if (planVo.getOperatorSchema() == 1) {
            deliverStorageMapper.updateDS(planVo);
            DeliverStorage deliverStorage = deliverStorageMapper.selectById(planVo.getId());
            StorageLocation storageLocation = storageLocationMapper.selectById(planVo.getStorageLocationId());
            SendOrders sendOrders = sendOrdersMapper.selectById(deliverStorage.getSendOrdersId());
            sendOrders.setStorageLocationId(planVo.getStorageLocationId());
            sendOrders.setWarehouseId(planVo.getWarehouseId());
            sendOrdersMapper.updateById(sendOrders);
            QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("project_no", sendOrders.getProjectNo());
            queryWrapper.eq("storage_location_id", sendOrders.getStorageLocationId());
            queryWrapper.eq("material_id", deliverStorage.getMaterialId());
            queryWrapper.eq("backup3",deliverStorage.getAccomplishNumUnit());
            Inventory inventory1 = inventoryService.getOne(queryWrapper);
            if (inventory1 != null) {
                inventory1.setInventoryQuantity(inventory1.getInventoryQuantity().add(deliverStorage.getAccomplishNum()));
                inventoryService.updateById(inventory1);
            } else {
                Inventory inventory = new Inventory();
                inventory.setWarehouseId(sendOrders.getWarehouseId());
                inventory.setMaterialId(deliverStorage.getMaterialId());
                inventory.setProjectNo(sendOrders.getProjectNo());
                inventory.setStorageLocationId(sendOrders.getStorageLocationId());
                inventory.setInventoryQuantity(deliverStorage.getAccomplishNum());
                inventory.setBackup1(sendOrders.getPlanId().toString());
                inventory.setBackup2(sendOrders.getPlanType());
                inventory.setBackup3(deliverStorage.getAccomplishNumUnit().toString());
                //BigDecimal.ROUND_HALF_UP : 保留2位小数
                inventory.setBackup4(deliverStorage.getAccomplishVolume().divide(deliverStorage.getAccomplishNum(),BigDecimal.ROUND_HALF_UP));
                inventoryService.save(inventory);
            }
            if (planVo.getAccomplishVolume() != null) {
                storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().add(planVo.getAccomplishVolume()));
                storageLocationMapper.updateById(storageLocation);
                return Result.ok("完单成功!");
            }
            //回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("完单失败!");
        } else {
            receivingStorageMapper.updateDS(planVo);
            ReceivingStorage receivingStorage = receivingStorageMapper.selectById(planVo.getId());
            SendOrders sendOrders = sendOrdersMapper.selectById(receivingStorage.getSendOrdersId());
            sendOrders.setStorageLocationId(planVo.getStorageLocationId());
            sendOrders.setWarehouseId(planVo.getWarehouseId());
            sendOrdersMapper.updateById(sendOrders);
            StorageLocation storageLocation = storageLocationMapper.selectById(receivingStorage.getStorageLocationId());
            QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("backup1", sendOrders.getPlanId());
            queryWrapper.eq("backup2", sendOrders.getPlanType());
            queryWrapper.eq("storage_location_id", sendOrders.getStorageLocationId());
            queryWrapper.eq("material_id", receivingStorage.getMaterialId());
            queryWrapper.eq("backup3",receivingStorage.getAccomplishNumUnit());
            BigDecimal bigDecimal = new BigDecimal(0);
            Inventory inventory = inventoryService.getOne(queryWrapper);
            if (inventory != null) {
                if(receivingStorage.getAccomplishNum().doubleValue()>inventory.getInventoryQuantity().doubleValue()){
                    //回滚事务
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.error("数量不足!库存数为"+inventory.getInventoryQuantity());
                }
                inventory.setInventoryQuantity(inventory.getInventoryQuantity().subtract(receivingStorage.getAccomplishNum()));
                bigDecimal = bigDecimal.add(inventory.getBackup4());
                if (inventory.getInventoryQuantity().doubleValue()<= 0) {
                    inventoryService.removeById(inventory);
                }
                inventoryService.updateById(inventory);
            }
            //.setScale(2,BigDecimal.ROUND_HALF_UP)保留2位小数
            BigDecimal num = storageLocation.getTheCurrentVolume().subtract(planVo.getAccomplishNum().multiply(bigDecimal).setScale(2,BigDecimal.ROUND_HALF_UP));
            if (num.intValue() < 0) {
                BigDecimal bd2 = new BigDecimal(0);
                storageLocation.setTheCurrentVolume(bd2);
                storageLocationMapper.updateById(storageLocation);
                return Result.ok();
            }
            storageLocation.setTheCurrentVolume(num);
            storageLocationMapper.updateById(storageLocation);
            return Result.ok("完单成功!");
        }
    }

    @Override
    public IPage<SendOrdersVo> selectSendOrdersController(String planId, String planType, Page<SendOrdersVo> page) {
        List<SendOrdersVo> list = baseMapper.selectSendOrdersController(planId, planType, page);
        if (list != null) {
            for (SendOrdersVo sendOrdersVo : list) {
                sendOrdersVo.setLicense(new ArrayList<>());
                sendOrdersVo.setRealname(new ArrayList<>());
                if (sendOrdersVo.getA0() != null) {
                    String[] str = sendOrdersVo.getA0().split(",");
                    for (String s : str) {
                        sendOrdersVo.getLicense().add(s);
                    }
                }
                if (sendOrdersVo.getA1() != null) {
                    String[] str1 = sendOrdersVo.getA1().split(",");
                    for (String s : str1) {
                        sendOrdersVo.getRealname().add(s);
                    }
                }
            }
        }
        return page.setRecords(list);
    }

    @Override
    public void updatePlanState(Integer planId, String planType) {
        baseMapper.updatePlanState(planId, planType);
    }

    @Transactional
    @Override
    public void removeSendOrders(String id) {
        SendOrders sendOrders = sendOrdersMapper.selectById(id);

        Material material = new Material();
        BigDecimal bigDecimalc = new BigDecimal(0);
        BigDecimal bigDecimalr = new BigDecimal(0);

        //删除派单表数据
        sendOrdersMapper.deleteById(id);

        //删除派单详细表数据
        QueryWrapper<SendOrdersSubtabulation> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("send_orders_id", id);
        sendOrdersSubtabulationService.remove(queryWrapper1);

        QueryWrapper<DeliverStorage> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("send_orders_id", id);

        QueryWrapper<ReceivingStorage> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("send_orders_id", id);

        QueryWrapper<ReceivingStorage> queryWrapper4 = new QueryWrapper<>();
        queryWrapper3.eq("send_orders_id", id);
        queryWrapper3.eq("state", 1);

        QueryWrapper<DeliverStorage> queryWrapper5 = new QueryWrapper<>();
        queryWrapper2.eq("send_orders_id", id);
        queryWrapper2.eq("state", 1);

        //删除出库完单时计算总出库数
        List<ReceivingStorage> receivingStorageList = receivingStorageMapper.selectList(queryWrapper4);
        if (receivingStorageList.size() > 0) {
            for (ReceivingStorage receivingStorage : receivingStorageList) {
                if (receivingStorage.getAccomplishVolume() != null) {
                    bigDecimalc.add(receivingStorage.getAccomplishVolume());
                    QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("backup1", sendOrders.getPlanId());
                    queryWrapper.eq("backup2", sendOrders.getPlanType());
                    queryWrapper.eq("storage_location_id", sendOrders.getStorageLocationId());
                    queryWrapper.eq("material_id", receivingStorage.getMaterialId());
                    Inventory inventory = inventoryService.getOne(queryWrapper);
                    if (inventory != null) {
                        StorageLocation storageLocation = storageLocationMapper.selectById(inventory.getStorageLocationId());
                        storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().add(inventory.getInventoryQuantity().multiply(inventory.getBackup4())));
                        storageLocationMapper.updateById(storageLocation);
                        inventory.setInventoryQuantity(inventory.getInventoryQuantity().add(receivingStorage.getAccomplishNum()));
                        inventoryService.updateById(inventory);
                    }
                }
            }
        }

        //删除入库完单时计算总入库数
        List<DeliverStorage> deliverStorageList = deliverStorageMapper.selectList(queryWrapper5);
        if (deliverStorageList.size() > 0) {
            for (DeliverStorage deliverStorage : deliverStorageList) {
                if (deliverStorage.getAccomplishVolume() != null) {
                    bigDecimalr.add(deliverStorage.getAccomplishVolume());
                    QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("backup1", sendOrders.getPlanId());
                    queryWrapper.eq("backup2", sendOrders.getPlanType());
                    queryWrapper.eq("backup3", deliverStorage.getAccomplishNumUnit());
                    queryWrapper.eq("material_id", deliverStorage.getMaterialId());
                    List<Inventory> list = inventoryService.list(queryWrapper);
                    if (list != null) {
                        BigDecimal bigDecimal = new BigDecimal(0);
                        for (Inventory inventory : list) {
                            StorageLocation storageLocation = storageLocationMapper.selectById(inventory.getStorageLocationId());
                            BigDecimal bigDecimal1 = inventory.getInventoryQuantity().add(bigDecimal);
                            if(deliverStorage.getAccomplishNum().doubleValue()<bigDecimal1.doubleValue()){
                                storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(inventory.getInventoryQuantity().multiply(inventory.getBackup4())));
                                storageLocationMapper.updateById(storageLocation);
                                inventory.setInventoryQuantity(inventory.getInventoryQuantity().subtract(deliverStorage.getAccomplishNum()).add(bigDecimal));
                                inventoryService.removeById(inventory.getId());
                                if (inventory.getInventoryQuantity().doubleValue() < 0) {
                                    inventoryService.removeById(inventory.getId());
                                }
                            }else {
                                bigDecimal = bigDecimal.add(inventory.getInventoryQuantity());
                                storageLocation.setTheCurrentVolume(storageLocation.getTheCurrentVolume().subtract(inventory.getInventoryQuantity().multiply(inventory.getBackup4())));
                                storageLocationMapper.updateById(storageLocation);
                                inventoryService.removeById(inventory.getId());
                            }
                        }
                    }
                }
            }
        }

        //删除出库完单表数据
        receivingStorageMapper.delete(queryWrapper3);

        //删除入库完单表数据
        deliverStorageMapper.delete(queryWrapper2);

        //查询计划所有派单信息
        QueryWrapper<SendOrders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_type", sendOrders.getPlanType());
        queryWrapper.eq("plan_id", sendOrders.getPlanId());
        List<SendOrders> list = sendOrdersMapper.selectList(queryWrapper);

        if (list.size() == 0) {
            if (sendOrders.getPlanType().equals("1")) {
                Plan1 plan1 = plan1Service.getById(sendOrders.getPlanId());
                plan1.setSendOrdersState(0);
                plan1Service.updateById(plan1);
            } else if (sendOrders.getPlanType().equals("2")) {
                Plan2 plan2 = plan2Service.getById(sendOrders.getPlanId());
                plan2.setSendOrdersState(0);
                plan2Service.updateById(plan2);
            } else if (sendOrders.getPlanType().equals("3")) {
                Plan3 plan3 = plan3Service.getById(sendOrders.getPlanId());
                plan3.setSendOrdersState(0);
                plan3Service.updateById(plan3);
            } else {
                Plan4 plan4 = plan4Service.getById(sendOrders.getPlanId());
                plan4.setSendOrdersState(0);
                plan4Service.updateById(plan4);
            }
        }
    }

    @Override
    public void updateSendOrders(SendOrdersVo sendOrdersVo) {
        SendOrders sendOrders = new SendOrders();
        BeanUtils.copyProperties(sendOrdersVo, sendOrders);
        baseMapper.updateById(sendOrders);

        QueryWrapper queryWrappers = new QueryWrapper();
        queryWrappers.eq("send_orders_id", sendOrdersVo.getId());
        sendOrdersSubtabulationService.remove(queryWrappers);

        if (sendOrdersVo.getRealname() != null) {
            SendOrdersSubtabulation sendOrdersSubtabulation = new SendOrdersSubtabulation();
            sendOrdersSubtabulation.setTaskTime(new Date());
            sendOrdersSubtabulation.setSendOrdersId(sendOrdersVo.getId());
            sendOrdersSubtabulation.setDistributionType(1);
            for (String s : sendOrdersVo.getRealname()) {
                sendOrdersSubtabulation.setTypeId(s);
                sendOrdersSubtabulationService.save(sendOrdersSubtabulation);
            }
        }

        if (sendOrdersVo.getLicense() != null) {
            SendOrdersSubtabulation sendOrdersSubtabulation = new SendOrdersSubtabulation();
            sendOrdersSubtabulation.setSendOrdersId(sendOrdersVo.getId());
            sendOrdersSubtabulation.setTaskTime(new Date());
            sendOrdersSubtabulation.setDistributionType(0);
            for (String s : sendOrdersVo.getLicense()) {
                sendOrdersSubtabulation.setTypeId(s);
                sendOrdersSubtabulationService.save(sendOrdersSubtabulation);
            }
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("send_orders_id", sendOrdersVo.getId());
        if (sendOrdersVo.getOperatorSchema() == 1) {
            DeliverStorage deliverStorage = deliverStorageMapper.selectOne(queryWrapper);
            if (deliverStorage != null) {
                if (sendOrdersVo.getWarehouseId() != null) {
                    deliverStorage.setWarehouseId(sendOrdersVo.getWarehouseId());
                }
                if (sendOrdersVo.getStorageLocationId() != null) {
                    deliverStorage.setStorageLocationId(sendOrdersVo.getStorageLocationId());
                }
                deliverStorageMapper.updateById(deliverStorage);
            }
        } else {
            ReceivingStorage receivingStorage = receivingStorageMapper.selectOne(queryWrapper);
            if (receivingStorage != null) {
                if (sendOrdersVo.getWarehouseId() != null) {
                    receivingStorage.setWarehouseId(sendOrdersVo.getWarehouseId());
                }
                if (sendOrdersVo.getStorageLocationId() != null) {
                    receivingStorage.setStorageLocationId(sendOrdersVo.getStorageLocationId());
                }
                receivingStorageMapper.updateById(receivingStorage);
            }
        }
    }

    @Override
    public IPage<SendOrdersVo> selectPlanSendOrdersTheSameDay(Page<SendOrdersVo> page) {
        List<SendOrdersVo> list = sendOrdersMapper.selectPlanSendOrdersTheSameDay(page);
        return page.setRecords(list);
    }
}
