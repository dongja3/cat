package com.dianping.cat.eclipselink.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="A_Publisher")
public class Publisher {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="Id")
	private Long id = null;
	
	private String name;
	
	@OneToOne(cascade= CascadeType.ALL)
	  @JoinColumn(name="PUBINFO_ID", insertable=true, updatable=true)
	private PubInfo pubInfo;
	
	@OneToMany(mappedBy="publisher", cascade={CascadeType.ALL},
            fetch= FetchType.LAZY, targetEntity=Book.class)
	private List<Book> titles = new ArrayList<Book>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="Name", length=30, nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	
	
	
	public List<Book> getTitles() {
		return titles;
	}

	public void setTitles(List<Book> titles) {
		this.titles = titles;
	}

	public PubInfo getPubInfo() {
		return pubInfo;
	}

	public void setPubInfo(PubInfo pubInfo) {
		this.pubInfo = pubInfo;
	}

	
}
