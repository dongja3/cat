package com.dianping.cat.eclipselink;

import com.dianping.cat.eclipselink.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class TestBookInsert extends EclipseLinkJPATest {
	public TestBookInsert(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }


	public void testbook() throws Exception {
		SqlTransactionContext.setCallMethod("testbookInsert");
		EntityManager em = getEMF().createEntityManager();
		em.getTransaction().begin();
		Author author1 = new Author();
		author1.setId((long)1000);
		author1.setName("Author1");
		ContactInfo contact1 = new ContactInfo();
		contact1.setAddress("Contact Address1");
		contact1.setPhone("111111");
		author1.setContact(contact1);
		
		List<Computer> computers = new ArrayList<Computer>();
		Computer pc = new Computer();
		pc.setName("pc1");
		pc.setAuthor(author1);
		computers.add(pc);
		pc = new Computer();
	    pc.setName("pc2");
	    pc.setAuthor(author1);
	    computers.add(pc);
	    author1.setComputers(computers);
		
		
		PubInfo pubInfo = new PubInfo();
		pubInfo.setDescription("Pub Infomation");

		Publisher publisher = new Publisher();
		publisher.setName("ECUST Pub");
		publisher.setPubInfo(pubInfo);
		
		
		Book title = new Book();
		title.setName("Machel Jackson, the legend");
		title.setPrice(85);
		title.setIsbn("12322-34234-11");
		title.setPublisher(publisher);
		title.getAuthors().add(author1);
		
		em.persist(author1);
		em.persist(publisher);
		em.persist(title);
		em.getTransaction().commit();

		System.in.read();
	}

	public void testBookSearch(){
		EntityManager em = getEMF().createEntityManager();
		em.getTransaction().begin();
		Query query = em.createNamedQuery("findAllBook");

		List<Book> result = query.getResultList();
		em.getTransaction().commit();

	}
}
