package cn.wl.study.entity;

import cn.wl.basics.baseClass.WlBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_appraise")
@TableName("a_appraise")
@ApiModel(value = "课程评价")
public class Appraise extends WlBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程ID")
    private String curriculumId;

    @ApiModelProperty(value = "课程名称")
    private String curriculumName;

    @ApiModelProperty(value = "评价人ID")
    private String userId;

    @ApiModelProperty(value = "评价人")
    private String userName;

    @ApiModelProperty(value = "评价内容")
    private String content;

    @ApiModelProperty(value = "评价时间")
    private String time;
}