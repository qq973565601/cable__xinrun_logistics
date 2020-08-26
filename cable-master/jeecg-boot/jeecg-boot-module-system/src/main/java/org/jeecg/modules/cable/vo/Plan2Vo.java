package org.jeecg.modules.cable.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.cable.entity.Plan2;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Plan2Vo
 * @Author Xm
 * @Date 2020/5/27 15:44
 */
public class Plan2Vo extends Plan2 implements Serializable {
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateBegin;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateEnd;
}
