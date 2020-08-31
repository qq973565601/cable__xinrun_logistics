package org.jeecg.modules.cable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.cable.entity.Warehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cable.vo.InventoryIocationListVo;
import org.jeecg.modules.cable.vo.InventoryVo;
import org.jeecg.modules.cable.vo.KuweiVo;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 仓库表
 * @Author: jeecg-boot
 * @Date: 2020-05-22
 * @Version: V1.0
 */
public interface WarehouseMapper extends BaseMapper<Warehouse> {

    List<InventoryIocationListVo> InventoryIocationListVoPage(@Param("inventoryIocationListVo") InventoryIocationListVo inventoryIocationListVo, @Param("page") Page<InventoryIocationListVo> page);

    List<KuweiVo> keweiQuery(@Param("id") String id, @Param("type") String type, @Param("warehouseId") String warehouseId);

    List<InventoryVo> selectPageinventory(@Param("inventoryVo") InventoryVo inventoryVo, @Param("page") Page<InventoryVo> page);

    List<InventoryVo> selectInfo(@Param("inventoryVo") InventoryVo inventoryVo, @Param("page") Page<InventoryVo> page);

    List<KuweiVo> kewei(@Param("ids") String[] ids);

    /**
     * 根据项目编号查询对应的库存库位存储数量
     * 2020/8/31 bai
     *
     * @param projectNo 项目编号
     * @return 库存库位信息
     */
    @Select("SELECT " +
            "i.inventory_quantity, " +
            "s.storage_location_name " +
            "FROM " +
            "inventory i " +
            "LEFT JOIN storage_location s ON i.storage_location_id = s.id " +
            "WHERE i.project_no = #{projectNo}")
    List<KuweiVo> queryInventory(@Param("projectNo") Serializable projectNo);
}
