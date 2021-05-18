package io.lw900925.ocean.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * <p>用户首选项设置</p>
 *
 * @author liuwei
 */
@Entity
@Table(name = "oc_user_profile")
@ApiModel(value = "用户首选项设置", parent = JpaIdentityEntity.class)
public class UserProfile extends JpaIdentityEntity {

    @ApiModelProperty(value = "主题")
    private String theme;

    @ApiModelProperty(value = "语言")
    private String language;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    // Getter and Setter
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
