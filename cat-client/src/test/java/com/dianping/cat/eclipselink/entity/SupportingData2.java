package com.dianping.cat.eclipselink.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SUPPORTINGDATA2")
public class SupportingData2 { 
    
    public SupportingData2(){
        
    }
    
    public SupportingData2(Long s2Id, String data){
        this.s2Id=s2Id;
        this.data=data;
    }
    
    @Id
    @Column(name="S2ID")
    private Long s2Id;
    
    private String data;

    @Column(name="Data", length=30, nullable=true)
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getS2Id() {
        return s2Id;
    }

    public void setS2Id(Long s2Id) {
        this.s2Id = s2Id;
    }

}
