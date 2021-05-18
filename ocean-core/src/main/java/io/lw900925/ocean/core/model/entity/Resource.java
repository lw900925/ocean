package io.lw900925.ocean.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oc_resource")
@ApiModel(value = "资源")
public class Resource extends JpaBaseEntity {

    @ApiModelProperty(value = "资源名称")
    @NotEmpty
    private String resourceName;

    @ApiModelProperty(value = "资源描述")
    private String description;

    @ApiModelProperty(value = "URI")
    @NotEmpty
    private String uri;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "oc_resource_role_ref",
            joinColumns = { @JoinColumn(name = "oid", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "authority", nullable = false) })
    private List<Role> roles = new ArrayList<>();

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
