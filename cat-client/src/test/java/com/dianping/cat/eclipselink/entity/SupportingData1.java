package com.dianping.cat.eclipselink.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SUPPORTINGDATA1")
public class SupportingData1 { 
    
    public SupportingData1(){

    }
    
    public SupportingData1(Long s1Id, String data){
        this.s1Id=s1Id;
        this.data=data;
    }
    
    @Id
    @Column(name="S1ID")
    private Long s1Id;
    
    private String data;

    @Column(name="Data", length=30, nullable=true)
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getS1Id() {
        return s1Id;
    }

    public void setS1Id(Long s1Id) {
        this.s1Id = s1Id;
    }


}
