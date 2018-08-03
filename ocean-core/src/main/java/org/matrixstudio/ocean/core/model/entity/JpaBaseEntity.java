package org.matrixstudio.ocean.core.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
@ApiModel(parent = JpaAuditEntity.class)
public class JpaBaseEntity extends JpaAuditEntity {

    @ApiModelProperty("版本")
    @Version
    private Long version = 1L;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
