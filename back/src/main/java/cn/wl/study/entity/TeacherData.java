package cn.wl.study.entity;

import cn.wl.data.entity.User;
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
@Table(name = "teacher")
@TableName("teacher")
@ApiModel(value = "教师信息")
public class TeacherData extends User {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教师级别")
    private String title;

    @ApiModelProperty(value = "教师简介")
    private String content;
}