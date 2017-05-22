package com.dianping.cat.eclipselink.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedNativeQuery(name="findAllBook", query="SELECT * FROM A_Title", resultClass=Book.class)
@Entity
@Table(name="A_Title")
public class Book {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id")
	private Long id = null;;
	private String name;
	private int price;
	private String notes;
	private String isbn;
	
	@OneToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE})
    @JoinColumn(name="S1ID", nullable=true)
    private SupportingData1 s1;
    
    @ManyToMany(
            targetEntity=SupportingData2.class,
            cascade={CascadeType.REFRESH,CascadeType.MERGE},
            fetch=FetchType.LAZY
        )
        @JoinTable(
            name="MIDDLE_TABLE",
            joinColumns={@JoinColumn(name="S2_ID")},
            inverseJoinColumns={@JoinColumn(name="MO_ID")}
        )
    private List<SupportingData2> s2;
	
	@Column(name="Isbn", length=30, nullable=false)
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	@ManyToOne
	@JoinColumn(name="PublisherId")
	private Publisher publisher;
	
    @ManyToMany(
            targetEntity=Author.class,
            cascade={CascadeType.ALL},
            fetch=FetchType.LAZY
        )
        @JoinTable(
            name="A_TitleAuthor",
            joinColumns={@JoinColumn(name="TitleId")},
            inverseJoinColumns={@JoinColumn(name="AuthorId")}
        )
         
	private List<Author> authors = new ArrayList<Author>();
	
	
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

	@Column(name="Price", nullable=false)
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

      
	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	@Column(name="Notes", length=255, nullable=true)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Version  int version;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public SupportingData1 getS1() {
		return s1;
	}

	public void setS1(SupportingData1 s1) {
		this.s1 = s1;
	}

	public List<SupportingData2> getS2() {
		return s2;
	}

	public void setS2(List<SupportingData2> s2) {
		this.s2 = s2;
	}
}
