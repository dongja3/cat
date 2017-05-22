/*
 *  This file was created by Rick Hightower of ArcMinds Inc.
 *
 */
package com.dianping.cat.eclipselink.entity;

import javax.persistence.*;

@Entity
@Table(name="A_PubInfo")
public class PubInfo {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id")
	private Long id ; 
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="Description", length=255, nullable=true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
