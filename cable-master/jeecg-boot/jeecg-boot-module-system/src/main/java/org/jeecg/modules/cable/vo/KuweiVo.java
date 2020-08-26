package org.jeecg.modules.cable.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * ruan
 * ClassName: KuweiVo <br/>
 * Description: <br/>
 * date: 2020/5/31 18:15<br/>
 *
 * @author Administrator<br />
 * @since JDK 1.8
 */
@Data
public class KuweiVo implements Serializable
{
    private String storageLocationId;
    private String storageLocationName;
    private String currentInventory;
}
