package com.dianping.cat.eclipselink.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class FrmAbstractDomainObject{
    @Column(name = "UPDATEBY", length = 15)
    private String lastUpdatedBy;

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    @Id
    @Column(name="Id")
    private Long id = null; 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
