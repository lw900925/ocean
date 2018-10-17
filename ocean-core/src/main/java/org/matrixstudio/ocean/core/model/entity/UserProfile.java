package org.matrixstudio.ocean.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * <p>用户首选项设置</p>
 *
 * @author liuwei
 */
@Entity
@Table(name = "oc_user_profile")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户首选项设置", parent = JpaIdentityEntity.class)
public class UserProfile extends JpaIdentityEntity {

    @ApiModelProperty(value = "主题")
    private String theme;

    @ApiModelProperty(value = "语言")
    private String language;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
