package org.matrixstudio.ocean.core.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@MappedSuperclass
@ApiModel
public class JpaIdentityEntity implements java.io.Serializable {

    @ApiModelProperty("ID")
    @Id
    @TableGenerator(name = "ID_GENERATOR")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ID_GENERATOR")
    private Long oid;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }
}
