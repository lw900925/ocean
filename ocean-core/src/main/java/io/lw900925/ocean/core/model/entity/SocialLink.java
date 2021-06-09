package io.lw900925.ocean.core.model.entity;

import io.lw900925.ocean.support.enums.EnumAttribute;
import io.lw900925.ocean.support.spring.data.jpa.usertype.EnumerationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>用户与一些社交账号的连接（如微信，微博等）</p>
 *
 * @author lw900925
 */
@Entity
@Table(name = "oc_social_link")
@TypeDef(name = "enumerationType", typeClass = EnumerationType.class)
@ApiModel(value = "社交账号链接")
public class SocialLink implements java.io.Serializable {

    @ApiModelProperty(value = "用户名")
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
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
     * @author lw900925
     */
    public enum Source implements EnumAttribute<Integer> {
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
