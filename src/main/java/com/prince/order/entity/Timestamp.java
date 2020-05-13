package com.prince.order.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Timestamp {

    @CreatedDate
    @Column(nullable = false, insertable = true, updatable = false, columnDefinition = "datetime default current_timestamp")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date createdOn;

    @LastModifiedDate
    @Column(nullable = false, insertable = true, updatable = true, columnDefinition = "datetime default current_timestamp")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date modifiedOn;

    @PrePersist
    protected void onCreate() {
        createdOn = modifiedOn = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedOn = new Date();
    }
}