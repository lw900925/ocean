package org.matrixstudio.ocean.core.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@ApiModel(parent = JpaIdentityEntity.class )
public class JpaAuditEntity extends JpaIdentityEntity {

    @ApiModelProperty("创建者")
    @CreatedBy
    private String creator;

    @ApiModelProperty("创建时间")
    @CreatedDate
    private LocalDateTime creationDate;

    @ApiModelProperty("修改者")
    @LastModifiedBy
    private String modifier;

    @ApiModelProperty("修改时间")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
