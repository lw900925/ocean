package io.lw900925.ocean.core.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@ApiModel
public class JpaIdentityEntity implements java.io.Serializable {

    /**
     * 数据库主键，使用雪花算法生成ID
     */
    @ApiModelProperty("ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ID_GENERATOR")
    @GenericGenerator(name = "ID_GENERATOR", strategy = "io.lw900925.ocean.support.spring.data.jpa.id.SnowFlakeIdentityGenerator")
    private Long oid;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }
}
