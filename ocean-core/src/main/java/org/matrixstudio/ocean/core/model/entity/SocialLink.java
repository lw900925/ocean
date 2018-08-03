package org.matrixstudio.ocean.core.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.matrixstudio.ocean.support.spring.data.jpa.usertype.EnumerationType;
import org.matrixstudio.ocean.support.spring.data.jpa.usertype.JpaEnumAttribute;

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
@Data
@EqualsAndHashCode(callSuper = true)
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
    public enum Source implements JpaEnumAttribute {
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
}
