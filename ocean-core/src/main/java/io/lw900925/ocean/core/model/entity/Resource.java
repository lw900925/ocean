package io.lw900925.ocean.core.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

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
}
