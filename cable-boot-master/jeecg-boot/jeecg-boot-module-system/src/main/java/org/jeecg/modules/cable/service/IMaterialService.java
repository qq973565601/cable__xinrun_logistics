package org.jeecg.modules.cable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cable.entity.Material;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cable.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 物料表
 * @Author: jeecg-boot
 * @Date: 2020-05-18
 * @Version: V1.0
 */
public interface IMaterialService extends IService<Material> {

  /**
   * 物料出入库台账
   * bai
   * 2020/6/9
   */
  IPage<MaterialOutPutAccountVo> getMaterialOutPutAccountList(MaterialOutPutAccountVo materialOutPutAccountVo, Page<MaterialOutPutAccountVo> page);

  /**
   * 物料余留台账
   * bai
   * 2020/5/22
   *
   * @return
   */
  IPage<MaterialRemainingAccountVo> getMaterialRemainingAccountList(String serial, String name, String projectNo, Page<MaterialRemainingAccountVo> page);

  /**
   * 物料年度出入台账统计
   * bai
   * 2020/5/21
   *
   * @return
   */
  IPage<AnnualReportVo> getAnnualAccountList(String dateTime, String serial, String name, String projectNo, Page<AnnualReportVo> page);

  /**
   * 分页查询物料信息
   * bai
   * 2020/5/21
   *
   * @return
   */
  IPage<Material> getMaterialPageList(String serial, String name, String supplier, Page<Material> page);

  /**
   * 新增物料
   * bai
   * 2020/5/21
   *
   * @return
   */
  Integer saveMaterial(Material material);

  /**
   * 修改物料信息
   * bai
   * 2020/5/22
   *
   * @return
   */
  Integer editMaterial(Material material);

  IPage<OutPutWarehouseVo> getOutPutWarehouseList(String planType, String serial, String name, String projectNo, String supplier, Page<OutPutWarehouseVo> page);

  List<Map<String,String>> materialOutPutList(MaterialOutPutAccountVo materialOutPutAccountVo);
}
