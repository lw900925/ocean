package io.lw900925.ocean.core.model.entity;

import io.lw900925.ocean.support.spring.data.jpa.usertype.EnumerationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import io.lw900925.ocean.support.enums.EnumAttribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>用户与一些社交账号的连接（如微信，微博等）</p>
 *
 * @author liuwei
 */
@Entity
@Table(name = "oc_social_link")
@TypeDef(name = "enumerationType", typeClass = EnumerationType.class)
@ApiModel(value = "社交账号链接", parent = JpaIdentityEntity.class)
public class SocialLink extends JpaIdentityEntity {

    @ApiModelProperty(value = "用户名")
    @NotEmpty
    private String username;

    @ApiModelProperty(value = "账号源")
    @Type(type = "enumerationType")
    @NotNull
    private Source source;

    @ApiModelProperty(value = "账号UID")
    @Column(unique = true)
    @NotEmpty
    private String uid;

    /**
     * <p>第三方账号来源类型</p>
     *
     * @author liuwei
     */
    public enum Source implements EnumAttribute {
        WEIBO(1),
        WECHAT(2);

        private Integer value;

        Source(Integer value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }

    // Getter and Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
