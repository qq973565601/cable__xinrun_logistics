package org.jeecg.modules.cable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.cable.entity.Material;
import org.jeecg.modules.cable.mapper.MaterialMapper;
import org.jeecg.modules.cable.service.IMaterialService;
import org.jeecg.modules.cable.vo.*;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 物料表
 * @Author: jeecg-boot
 * @Date: 2020-05-18
 * @Version: V1.0
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements IMaterialService {

    @Override
    public IPage<MaterialOutPutAccountVo> getMaterialOutPutAccountList(MaterialOutPutAccountVo materialOutPutAccountVo, Page<MaterialOutPutAccountVo> page) {
        return page.setRecords(baseMapper.getMaterialOutPutAccountList(materialOutPutAccountVo, page));
    }

    @Override
    public IPage<MaterialRemainingAccountVo> getMaterialRemainingAccountList(String serial, String name, String projectNo, Page<MaterialRemainingAccountVo> page) {
        List<MaterialRemainingAccountVo> list = baseMapper.getMaterialRemainingAccountList(serial, name, projectNo, page);
        for (MaterialRemainingAccountVo materialRemainingAccountVo : list) {
            BigDecimal bigDecimal = new BigDecimal(0);
            if (materialRemainingAccountVo.getNow_0_0() != null) {
                bigDecimal.add(materialRemainingAccountVo.getNow_0_0());
            }
            if (materialRemainingAccountVo.getNow_1_0() != null) {
                bigDecimal.add(materialRemainingAccountVo.getNow_1_0());
            }
            if (materialRemainingAccountVo.getNow_2_0() != null) {
                bigDecimal.add(materialRemainingAccountVo.getNow_2_0());
            }
            if (materialRemainingAccountVo.getNow_3_0() != null) {
                bigDecimal.add(materialRemainingAccountVo.getNow_3_0());
            }
            if (materialRemainingAccountVo.getNow_4_0() != null) {
                bigDecimal.add(materialRemainingAccountVo.getNow_4_0());
            }
            materialRemainingAccountVo.setYlckNums(bigDecimal);
            BigDecimal bigDecimal1 = new BigDecimal(0);
            if (materialRemainingAccountVo.getNow_0_1() != null) {
                bigDecimal1.add(materialRemainingAccountVo.getNow_0_1());
            }
            if (materialRemainingAccountVo.getNow_1_1() != null) {
                bigDecimal1.add(materialRemainingAccountVo.getNow_1_1());
            }
            if (materialRemainingAccountVo.getNow_2_1() != null) {
                bigDecimal1.add(materialRemainingAccountVo.getNow_2_1());
            }
            if (materialRemainingAccountVo.getNow_3_1() != null) {
                bigDecimal1.add(materialRemainingAccountVo.getNow_3_1());
            }
            if (materialRemainingAccountVo.getNow_4_1() != null) {
                bigDecimal1.add(materialRemainingAccountVo.getNow_4_1());
            }
            materialRemainingAccountVo.setYlrkNums(bigDecimal1);
        }
        return page.setRecords(list);
    }

    @Override
    public IPage<AnnualReportVo> getAnnualAccountList(String dateTime, String serial, String name, String projectNo, Page<AnnualReportVo> page) {
        List<AnnualReportVo> list = baseMapper.getAnnualAccountList(dateTime, serial, name, projectNo, page);
        for (AnnualReportVo annualReportVo : list) {
            BigDecimal bigDecimal = new BigDecimal(0);
            if (annualReportVo.getM1_0() != null) {
                bigDecimal.add(annualReportVo.getM1_0());
            }
            if (annualReportVo.getM2_0() != null) {
                bigDecimal.add(annualReportVo.getM2_0());
            }
            if (annualReportVo.getM3_0() != null) {
                bigDecimal.add(annualReportVo.getM3_0());
            }
            if (annualReportVo.getM4_0() != null) {
                bigDecimal.add(annualReportVo.getM4_0());
            }
            if (annualReportVo.getM5_0() != null) {
                bigDecimal.add(annualReportVo.getM5_0());
            }
            if (annualReportVo.getM6_0() != null) {
                bigDecimal.add(annualReportVo.getM6_0());
            }
            if (annualReportVo.getM7_0() != null) {
                bigDecimal.add(annualReportVo.getM7_0());
            }
            if (annualReportVo.getM8_0() != null) {
                bigDecimal.add(annualReportVo.getM8_0());
            }
            if (annualReportVo.getM9_0() != null) {
                bigDecimal.add(annualReportVo.getM9_0());
            }
            if (annualReportVo.getM10_0() != null) {
                bigDecimal.add(annualReportVo.getM10_0());
            }
            if (annualReportVo.getM11_0() != null) {
                bigDecimal.add(annualReportVo.getM11_0());
            }
            if (annualReportVo.getM12_0() != null) {
                bigDecimal.add(annualReportVo.getM12_0());
            }
            annualReportVo.setMh_0(bigDecimal);
            BigDecimal bigDecimal1 = new BigDecimal(0);
            if (annualReportVo.getM1_1() != null) {
                bigDecimal1.add(annualReportVo.getM1_1());
            }
            if (annualReportVo.getM2_1() != null) {
                bigDecimal1.add(annualReportVo.getM2_1());
            }
            if (annualReportVo.getM3_1() != null) {
                bigDecimal1.add(annualReportVo.getM3_1());
            }
            if (annualReportVo.getM4_1() != null) {
                bigDecimal1.add(annualReportVo.getM4_1());
            }
            if (annualReportVo.getM5_1() != null) {
                bigDecimal1.add(annualReportVo.getM5_1());
            }
            if (annualReportVo.getM6_1() != null) {
                bigDecimal1.add(annualReportVo.getM6_1());
            }
            if (annualReportVo.getM7_1() != null) {
                bigDecimal1.add(annualReportVo.getM7_1());
            }
            if (annualReportVo.getM8_1() != null) {
                bigDecimal1.add(annualReportVo.getM8_1());
            }
            if (annualReportVo.getM9_1() != null) {
                bigDecimal1.add(annualReportVo.getM9_1());
            }
            if (annualReportVo.getM10_1() != null) {
                bigDecimal1.add(annualReportVo.getM10_1());
            }
            if (annualReportVo.getM11_1() != null) {
                bigDecimal1.add(annualReportVo.getM11_1());
            }
            if (annualReportVo.getM12_1() != null) {
                bigDecimal1.add(annualReportVo.getM12_1());
            }
            annualReportVo.setMh_1(bigDecimal1);
        }
        return page.setRecords(list);
    }

    @Override
    public IPage<Material> getMaterialPageList(String serial, String name, String supplier, Page<Material> page) {
        List<Material> pageList = baseMapper.getMaterialPageList(serial, name, supplier, page);
        return page.setRecords(pageList);
    }

    @Override
    public Integer saveMaterial(Material material) {
        // 添加时首先判断物料编号是否已经存在
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.eq("serial", material.getSerial());
        Material entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return 0;   // 如果 entity != null 证明存在此物料编号
        }
        // 添加操作
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal(); // 拿到当前登录用户的信息
        material.setCreateBy(sysUser.getRealname());    // 创建人
        material.setCreateTime(new Date()); // 创建时间
        return baseMapper.saveMaterial(material);
    }

    @Override
    public Integer editMaterial(Material material) {
        return baseMapper.editMaterial(material);
    }

    @Override
    public IPage<OutPutWarehouseVo> getOutPutWarehouseList(String planType, String serial, String name, String projectNo, String supplier, Page<OutPutWarehouseVo> page) {
        page.setRecords(baseMapper.getOutPutWarehouseList(planType, serial, name, projectNo, supplier, page));
        return page;
    }

    @Override
    public List<Map<String, String>> materialOutPutList(MaterialOutPutAccountVo materialOutPutAccountVo) {
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> mapR = new HashMap<>();
        mapR.put("type", "入库");
        Map<String, String> mapC = new HashMap<>();
        mapC.put("type", "出库");
        if (materialOutPutAccountVo.getWeek() != null && materialOutPutAccountVo.getWeek()!="") {
            List<StatisticalChartVo> list1 = formattingWeek(materialOutPutAccountVo.getWeek());
            for (StatisticalChartVo statisticalChartVo : list1) {
                StatisticalChartVo statisticalChartVo1 = baseMapper.materialOutPutDayRList(statisticalChartVo.getDate(), materialOutPutAccountVo.getId());
                if (statisticalChartVo1 != null) {
                    statisticalChartVo.setY(statisticalChartVo1.getY());
                }
                mapR.put(statisticalChartVo.getX(), statisticalChartVo.getY());
            }
            List<StatisticalChartVo> list2 = formattingWeek(materialOutPutAccountVo.getWeek());
            for (StatisticalChartVo statisticalChartVo : list2) {
                StatisticalChartVo statisticalChartVo1 = baseMapper.materialOutPutDayCList(statisticalChartVo.getDate(), materialOutPutAccountVo.getId());
                if (statisticalChartVo1 != null) {
                    statisticalChartVo.setY(statisticalChartVo1.getY());
                }
                mapC.put(statisticalChartVo.getX(), statisticalChartVo.getY());
            }
            lists.add(mapR);
            lists.add(mapC);
            return lists;
        } else if (materialOutPutAccountVo.getYear() != null && materialOutPutAccountVo.getYear()!="") {
            if (materialOutPutAccountVo.getQuarter() != null && materialOutPutAccountVo.getQuarter() != "") {
                List<StatisticalChartVo> yearList1 = new ArrayList<>();
                List<StatisticalChartVo> yearList2 = new ArrayList<>();
                if ("1".equals(materialOutPutAccountVo.getQuarter())) {
                    StatisticalChartVo statisticalChartVo1 = new StatisticalChartVo();
                    statisticalChartVo1.setX("一月");
                    StatisticalChartVo statisticalChartVo11 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-01-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo11 != null) {
                        statisticalChartVo1.setY(statisticalChartVo11.getY());
                    }
                    mapR.put(statisticalChartVo1.getX(), statisticalChartVo1.getY());
                    StatisticalChartVo statisticalChartVo2 = new StatisticalChartVo();
                    statisticalChartVo2.setX("二月");
                    StatisticalChartVo statisticalChartVo22 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-02-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo22 != null) {
                        statisticalChartVo2.setY(statisticalChartVo22.getY());
                    }
                    mapR.put(statisticalChartVo2.getX(), statisticalChartVo2.getY());
                    StatisticalChartVo statisticalChartVo3 = new StatisticalChartVo();
                    statisticalChartVo3.setX("三月");
                    StatisticalChartVo statisticalChartVo33 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-03-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo33 != null) {
                        statisticalChartVo3.setY(statisticalChartVo33.getY());
                    }
                    mapR.put(statisticalChartVo3.getX(), statisticalChartVo3.getY());
                    StatisticalChartVo statisticalChartVo4 = new StatisticalChartVo();
                    statisticalChartVo4.setX("一月");
                    StatisticalChartVo statisticalChartVo44 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-01-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo44 != null) {
                        statisticalChartVo4.setY(statisticalChartVo44.getY());
                    }
                    mapC.put(statisticalChartVo4.getX(), statisticalChartVo4.getY());
                    StatisticalChartVo statisticalChartVo5 = new StatisticalChartVo();
                    statisticalChartVo5.setX("二月");
                    StatisticalChartVo statisticalChartVo55 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-02-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo55 != null) {
                        statisticalChartVo5.setY(statisticalChartVo55.getY());
                    }
                    mapC.put(statisticalChartVo5.getX(), statisticalChartVo5.getY());
                    StatisticalChartVo statisticalChartVo6 = new StatisticalChartVo();
                    statisticalChartVo6.setX("三月");
                    StatisticalChartVo statisticalChartVo66 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-03-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo66 != null) {
                        statisticalChartVo6.setY(statisticalChartVo66.getY());
                    }
                    mapC.put(statisticalChartVo6.getX(), statisticalChartVo6.getY());
                } else if ("2".equals(materialOutPutAccountVo.getQuarter())) {
                    StatisticalChartVo statisticalChartVo1 = new StatisticalChartVo();
                    statisticalChartVo1.setX("四月");
                    StatisticalChartVo statisticalChartVo11 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-04-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo11 != null) {
                        statisticalChartVo1.setY(statisticalChartVo11.getY());
                    }
                    mapR.put(statisticalChartVo1.getX(), statisticalChartVo1.getY());
                    StatisticalChartVo statisticalChartVo2 = new StatisticalChartVo();
                    statisticalChartVo2.setX("五月");
                    StatisticalChartVo statisticalChartVo22 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-05-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo22 != null) {
                        statisticalChartVo2.setY(statisticalChartVo22.getY());
                    }
                    mapR.put(statisticalChartVo2.getX(), statisticalChartVo2.getY());
                    StatisticalChartVo statisticalChartVo3 = new StatisticalChartVo();
                    statisticalChartVo3.setX("六月");
                    StatisticalChartVo statisticalChartVo33 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-06-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo33 != null) {
                        statisticalChartVo3.setY(statisticalChartVo33.getY());
                    }
                    mapR.put(statisticalChartVo3.getX(), statisticalChartVo3.getY());
                    StatisticalChartVo statisticalChartVo4 = new StatisticalChartVo();
                    statisticalChartVo4.setX("四月");
                    StatisticalChartVo statisticalChartVo44 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-04-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo44 != null) {
                        statisticalChartVo4.setY(statisticalChartVo44.getY());
                    }
                    mapC.put(statisticalChartVo4.getX(), statisticalChartVo4.getY());
                    StatisticalChartVo statisticalChartVo5 = new StatisticalChartVo();
                    statisticalChartVo5.setX("五月");
                    StatisticalChartVo statisticalChartVo55 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-05-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo55 != null) {
                        statisticalChartVo5.setY(statisticalChartVo55.getY());
                    }
                    mapC.put(statisticalChartVo5.getX(), statisticalChartVo5.getY());
                    StatisticalChartVo statisticalChartVo6 = new StatisticalChartVo();
                    statisticalChartVo6.setX("六月");
                    StatisticalChartVo statisticalChartVo66 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-06-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo66 != null) {
                        statisticalChartVo6.setY(statisticalChartVo66.getY());
                    }
                    mapC.put(statisticalChartVo6.getX(), statisticalChartVo6.getY());
                } else if ("3".equals(materialOutPutAccountVo.getQuarter())) {
                    StatisticalChartVo statisticalChartVo1 = new StatisticalChartVo();
                    statisticalChartVo1.setX("七月");
                    StatisticalChartVo statisticalChartVo11 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-07-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo11 != null) {
                        statisticalChartVo1.setY(statisticalChartVo11.getY());
                    }
                    mapR.put(statisticalChartVo1.getX(), statisticalChartVo1.getY());
                    StatisticalChartVo statisticalChartVo2 = new StatisticalChartVo();
                    statisticalChartVo2.setX("八月");
                    StatisticalChartVo statisticalChartVo22 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-08-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo22 != null) {
                        statisticalChartVo2.setY(statisticalChartVo22.getY());
                    }
                    mapR.put(statisticalChartVo2.getX(), statisticalChartVo2.getY());
                    StatisticalChartVo statisticalChartVo3 = new StatisticalChartVo();
                    statisticalChartVo3.setX("九月");
                    StatisticalChartVo statisticalChartVo33 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-09-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo33 != null) {
                        statisticalChartVo3.setY(statisticalChartVo33.getY());
                    }
                    mapR.put(statisticalChartVo3.getX(), statisticalChartVo3.getY());
                    StatisticalChartVo statisticalChartVo4 = new StatisticalChartVo();
                    statisticalChartVo4.setX("七月");
                    StatisticalChartVo statisticalChartVo44 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-07-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo44 != null) {
                        statisticalChartVo4.setY(statisticalChartVo44.getY());
                    }
                    mapC.put(statisticalChartVo4.getX(), statisticalChartVo4.getY());
                    StatisticalChartVo statisticalChartVo5 = new StatisticalChartVo();
                    statisticalChartVo5.setX("八月");
                    StatisticalChartVo statisticalChartVo55 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-08-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo55 != null) {
                        statisticalChartVo5.setY(statisticalChartVo55.getY());
                    }
                    mapC.put(statisticalChartVo5.getX(), statisticalChartVo5.getY());
                    StatisticalChartVo statisticalChartVo6 = new StatisticalChartVo();
                    statisticalChartVo6.setX("九月");
                    StatisticalChartVo statisticalChartVo66 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-09-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo66 != null) {
                        statisticalChartVo6.setY(statisticalChartVo66.getY());
                    }
                    mapC.put(statisticalChartVo6.getX(), statisticalChartVo6.getY());
                } else if ("4".equals(materialOutPutAccountVo.getQuarter())) {
                    StatisticalChartVo statisticalChartVo1 = new StatisticalChartVo();
                    statisticalChartVo1.setX("十月");
                    StatisticalChartVo statisticalChartVo11 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-10-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo11 != null) {
                        statisticalChartVo1.setY(statisticalChartVo11.getY());
                    }
                    mapR.put(statisticalChartVo1.getX(), statisticalChartVo1.getY());
                    StatisticalChartVo statisticalChartVo2 = new StatisticalChartVo();
                    statisticalChartVo2.setX("十一月");
                    StatisticalChartVo statisticalChartVo22 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-11-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo22 != null) {
                        statisticalChartVo2.setY(statisticalChartVo22.getY());
                    }
                    mapR.put(statisticalChartVo2.getX(), statisticalChartVo2.getY());
                    StatisticalChartVo statisticalChartVo3 = new StatisticalChartVo();
                    statisticalChartVo3.setX("十二月");
                    StatisticalChartVo statisticalChartVo33 = baseMapper.materialOutPutMonthRList(materialOutPutAccountVo.getYear() + "-12-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo33 != null) {
                        statisticalChartVo3.setY(statisticalChartVo33.getY());
                    }
                    mapR.put(statisticalChartVo3.getX(), statisticalChartVo3.getY());
                    StatisticalChartVo statisticalChartVo4 = new StatisticalChartVo();
                    statisticalChartVo4.setX("十月");
                    StatisticalChartVo statisticalChartVo44 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-10-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo44 != null) {
                        statisticalChartVo4.setY(statisticalChartVo44.getY());
                    }
                    mapC.put(statisticalChartVo4.getX(), statisticalChartVo4.getY());
                    StatisticalChartVo statisticalChartVo5 = new StatisticalChartVo();
                    statisticalChartVo5.setX("十一月");
                    StatisticalChartVo statisticalChartVo55 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-11-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo55 != null) {
                        statisticalChartVo5.setY(statisticalChartVo55.getY());
                    }
                    mapC.put(statisticalChartVo5.getX(), statisticalChartVo5.getY());
                    StatisticalChartVo statisticalChartVo6 = new StatisticalChartVo();
                    statisticalChartVo6.setX("十二月");
                    StatisticalChartVo statisticalChartVo66 = baseMapper.materialOutPutMonthCList(materialOutPutAccountVo.getYear() + "-12-01", materialOutPutAccountVo.getId());
                    if (statisticalChartVo66 != null) {
                        statisticalChartVo6.setY(statisticalChartVo66.getY());
                    }
                    mapC.put(statisticalChartVo6.getX(), statisticalChartVo6.getY());
                }
                lists.add(mapR);
                lists.add(mapC);
                return lists;
            } else if (materialOutPutAccountVo.getMonth() != null && materialOutPutAccountVo.getMonth() != "") {
                List<StatisticalChartVo> list1 = lastMonth(Integer.parseInt(materialOutPutAccountVo.getYear()), Integer.parseInt(materialOutPutAccountVo.getMonth()));
                for (StatisticalChartVo statisticalChartVo : list1) {
                    StatisticalChartVo statisticalChartVo1 = new StatisticalChartVo();
                    statisticalChartVo1 = baseMapper.materialOutPutDayRList(statisticalChartVo.getDate(), materialOutPutAccountVo.getId());
                    if (statisticalChartVo1 != null) {
                        statisticalChartVo.setY(statisticalChartVo1.getY());
                    }
                    mapR.put(statisticalChartVo.getX(),statisticalChartVo.getY());
                }
                List<StatisticalChartVo> list2 = lastMonth(Integer.parseInt(materialOutPutAccountVo.getYear()), Integer.parseInt(materialOutPutAccountVo.getMonth()));
                for (StatisticalChartVo statisticalChartVo : list2) {
                    StatisticalChartVo statisticalChartVo1 = new StatisticalChartVo();
                    statisticalChartVo1 = baseMapper.materialOutPutDayCList(statisticalChartVo.getDate(), materialOutPutAccountVo.getId());
                    if (statisticalChartVo1 != null) {
                        statisticalChartVo.setY(statisticalChartVo1.getY());
                    }
                    mapC.put(statisticalChartVo.getX(),statisticalChartVo.getY());
                }
                lists.add(mapR);
                lists.add(mapC);
                return lists;
            }
            List<StatisticalChartVo> yearList1 = new ArrayList<>();
            List<StatisticalChartVo> yearList2 = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                StatisticalChartVo statisticalChartVor = new StatisticalChartVo();
                StatisticalChartVo statisticalChartVoc = new StatisticalChartVo();
                if (i == 1) {
                    statisticalChartVor.setX("一月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-01-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("一月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-01-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 2) {
                    statisticalChartVor.setX("二月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-02-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("二月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-02-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 3) {
                    statisticalChartVor.setX("三月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-03-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("三月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-03-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 4) {
                    statisticalChartVor.setX("四月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-04-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("四月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-04-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 5) {
                    statisticalChartVor.setX("五月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-05-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("五月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-05-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 6) {
                    statisticalChartVor.setX("六月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-06-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("六月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-06-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 7) {
                    statisticalChartVor.setX("七月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-07-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("七月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-07-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 8) {
                    statisticalChartVor.setX("八月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-08-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("八月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-08-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 9) {
                    statisticalChartVor.setX("九月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-09-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("九月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-09-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 10) {
                    statisticalChartVor.setX("十月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-10-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("十月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-10-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 11) {
                    statisticalChartVor.setX("十一月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-11-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("十一月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-11-01");
                    yearList2.add(statisticalChartVoc);
                }
                if (i == 12) {
                    statisticalChartVor.setX("十二月");
                    statisticalChartVor.setDate(materialOutPutAccountVo.getYear() + "-12-01");
                    yearList1.add(statisticalChartVor);
                    statisticalChartVoc.setX("十二月");
                    statisticalChartVoc.setDate(materialOutPutAccountVo.getYear() + "-12-01");
                    yearList2.add(statisticalChartVoc);
                }
            }
            for (StatisticalChartVo chartVo : yearList1) {
                StatisticalChartVo statisticalChartVo1 = baseMapper.materialOutPutMonthRList(chartVo.getDate(), materialOutPutAccountVo.getId());
                if (statisticalChartVo1 != null) {
                    chartVo.setY(statisticalChartVo1.getY());
                }
                mapR.put(chartVo.getX(),chartVo.getY());
            }
            for (StatisticalChartVo chartVo1 : yearList2) {
                StatisticalChartVo statisticalChartVo1 = baseMapper.materialOutPutMonthCList(chartVo1.getDate(), materialOutPutAccountVo.getId());
                if (statisticalChartVo1 != null) {
                    chartVo1.setY(statisticalChartVo1.getY());
                }
                mapC.put(chartVo1.getX(),chartVo1.getY());
            }
            lists.add(mapR);
            lists.add(mapC);
            return lists;
        }
        return lists;
    }


    private List<StatisticalChartVo> formattingWeek(String week) {
        List<StatisticalChartVo> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if ("1".equals(week)) {
            Calendar c = Calendar.getInstance();
            // 今天bai是一周中的第几天
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if (c.getFirstDayOfWeek() == Calendar.SUNDAY) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
            // 计算一周开始的日期du
            c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
            for (int i = 1; i <= 7; i++) {
                c.add(Calendar.DAY_OF_MONTH, 1);
                StatisticalChartVo statisticalChartVo = new StatisticalChartVo();
                statisticalChartVo.setDate(sdf.format(c.getTime()));
                if (i == 1) {
                    statisticalChartVo.setX("周一");
                }
                if (i == 2) {
                    statisticalChartVo.setX("周二");
                }
                if (i == 3) {
                    statisticalChartVo.setX("周三");
                }
                if (i == 4) {
                    statisticalChartVo.setX("周四");
                }
                if (i == 5) {
                    statisticalChartVo.setX("周五");
                }
                if (i == 6) {
                    statisticalChartVo.setX("周六");
                }
                if (i == 7) {
                    statisticalChartVo.setX("周七");
                }
                list.add(statisticalChartVo);
            }
        } else {
            for (int i = 7; i >= 1; i--) {
                StatisticalChartVo statisticalChartVo = new StatisticalChartVo();
                String time = lastWeek(i);
                statisticalChartVo.setDate(time);
                if (i == 7) {
                    statisticalChartVo.setX("周一");
                }
                if (i == 6) {
                    statisticalChartVo.setX("周二");
                }
                if (i == 5) {
                    statisticalChartVo.setX("周三");
                }
                if (i == 4) {
                    statisticalChartVo.setX("周四");
                }
                if (i == 3) {
                    statisticalChartVo.setX("周五");
                }
                if (i == 2) {
                    statisticalChartVo.setX("周六");
                }
                if (i == 1) {
                    statisticalChartVo.setX("周七");
                }
                list.add(statisticalChartVo);
            }
        }
        return list;
    }

    private String lastWeek(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        calendar1.add(Calendar.DAY_OF_MONTH, offset1 - i);
        return sdf.format(calendar1.getTime());
    }

    private List<StatisticalChartVo> lastMonth(int year, int month) {
        SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
        List<StatisticalChartVo> list = new ArrayList<>(32);
        // 获得当前日期对象
        Calendar cal = Calendar.getInstance();
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        // 1月从0开始
        cal.set(Calendar.MONTH, month - 1);
        // 当月1号
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 1; j <= count; j++) {
            StatisticalChartVo statisticalChartVo = new StatisticalChartVo();
            statisticalChartVo.setX(j + "");
            statisticalChartVo.setDate(dateFormatYYYYMMDD.format(cal.getTime()));
            list.add(statisticalChartVo);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

}
