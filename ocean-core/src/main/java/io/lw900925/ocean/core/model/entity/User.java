package io.lw900925.ocean.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.lw900925.ocean.support.enums.EnumAttribute;
import io.lw900925.ocean.support.spring.data.jpa.usertype.EnumerationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>用户</p>
 *
 * @author liuwei
 */
@Entity
@Table(name = "oc_user")
@TypeDef(name = "enumerationType", typeClass = EnumerationType.class)
@ApiModel(value = "用户")
public class User implements UserDetails {

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户名")
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    @NotEmpty
    private String username;

    @ApiModelProperty(value = "账号是否未过期")
    private boolean accountNonExpired = true;

    @ApiModelProperty(value = "账号是否未锁定")
    private boolean accountNonLocked = true;

    @ApiModelProperty(value = "密码是否未过期")
    private boolean credentialsNonExpired = true;

    @ApiModelProperty(value = "是否可用")
    private boolean enabled = true;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "oc_user_role_ref",
            joinColumns = {@JoinColumn(name = "username", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "authority", nullable = false)})
    private Set<Role> roles = new HashSet<>();

    // ~ Additional fields
    // =================================================================================================================
    @ApiModelProperty(value = "邮箱")
    @Column(unique = true)
    private String email;

    @ApiModelProperty(value = "手机号")
    @Column(unique = true)
    private String phoneNumber;

    @ApiModelProperty(value = "客户端能力")
    @Type(type = "enumerationType")
    private ClientCapability clientCapability;

    @ApiModelProperty(value = "显示名称")
    private String displayName;

    @ApiModelProperty(value = "头像地址")
    private String avatarUrl;

    @ApiModelProperty(value = "生日")
    private LocalDate birthday;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private UserProfile profile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.roles = authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // ~ Additional fields getter & setter
    // =================================================================================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ClientCapability getClientCapability() {
        return clientCapability;
    }

    public void setClientCapability(ClientCapability clientCapability) {
        this.clientCapability = clientCapability;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    /**
     * 自定义枚举类字段
     */
    public enum ClientCapability implements EnumAttribute<Integer> {
        CUSTOMER(1),
        AUDITOR(2),
        SYS_ADMINISTRATOR(3);

        private Integer value;

        ClientCapability(Integer value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public Integer getValue() {
            return value;
        }
    }
}
