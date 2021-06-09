package io.lw900925.ocean.core.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oc_role")
@ApiModel(value = "角色")
public class Role implements GrantedAuthority {

    @ApiModelProperty(value = "角色名称")
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    @NotEmpty
    private String authority;

    @ApiModelProperty(value = "描述")
    private String description;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "oc_role_resource_ref",
            joinColumns = {@JoinColumn(name = "authority", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "resource_id", nullable = false)})
    private List<Resource> resources = new ArrayList<>();

    public Role() {
        super();
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}
