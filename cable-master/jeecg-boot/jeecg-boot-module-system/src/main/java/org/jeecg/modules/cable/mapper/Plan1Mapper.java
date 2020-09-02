package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Plan1;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.importpackage.Plan1Im;
import org.jeecg.modules.cable.vo.Plan1Vo;
import org.jeecg.modules.cable.vo.SettleAccountsDetailsVo;
import org.jeecg.modules.cable.vo.SettleAccountsVo;
import org.jeecg.modules.cable.vo.StorageLocationListVo;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 计划表1
 * @Author: jeecg-boot
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface Plan1Mapper extends BaseMapper<Plan1> {
    /**
     * 查询计划1批量出库完单的数据
     * 2020/8/26 bai
     *
     * @param ids 批量出库完单 ids
     * @return 计划1批量出库完单的数据
     */
    List<Plan1Vo> getPlan1ReceivingStorageList(@Param("ids") List<Serializable> ids);

    /**
     * 查询计划1批量入库完单的数据
     * 2020/8/26 bai
     *
     * @param ids 批量入库完单 ids
     * @return 计划1批量入库完单的数据
     */
    List<Plan1> getPlan1DeliverStorage(@Param("ids") List<Serializable> ids);

    /**
    * 条件分页查询计划1
    *
    * @param plan1Vo
    * @param page
    * @Author Xm
    * @Date 2020/5/27 15:12
    */
    List<Plan1> pageList(@Param("plan1Vo") Plan1Vo plan1Vo,@Param("page") Page<Plan1> page);

    List<Plan1> idsqueryPageList(@Param("ids") List<String> ids,@Param("page") Page<Plan1> page);

    List<StorageLocationListVo> StorageLocationListVoPage(@Param("storageLocationListVo")StorageLocationListVo storageLocationListVo, @Param("page") Page<StorageLocationListVo> page);

    /**
     * 导出计划表1数据
     * bai
     * 2020/5/27
     *
     * @return
     */
    List<Plan1Im> exportPlan1(@Param("plan1Im") Plan1Im plan1Im);

    List<SettleAccountsVo> selectSettleAccounts(@Param("backup1")String backup1,@Param("planType")String planType,@Param("projectNo")String projectNo,@Param("page")Page<SettleAccountsVo> page);

    List<SettleAccountsDetailsVo> selectSettleAccountsDetails(@Param("projectNo") String projectNo,
                                                              @Param("page") Page<SettleAccountsDetailsVo> page);
}
