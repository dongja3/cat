package com.dianping.cat.eclipselink;

import com.dianping.cat.eclipselink.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class BookUpdateTest extends EclipseLinkJPATest{
    public BookUpdateTest(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }
    
    public BookUpdateTest(){
        super();
    }

    public void testUpdateBook () throws Exception{
        
        EntityManager em = getEMF().createEntityManager();
        Query q = em.createQuery("select t from Author t where t.id=1000");
        @SuppressWarnings("unchecked")
		List<Author> list = q.getResultList();
        em.getTransaction().begin();
        Author a = list.get(0);
        
        
        a.getContact().setAddress("address test update");
        a.setName("name test update");
       
        
        em.remove(a.getComputers().get(0));
 
        List<Computer> computers = new ArrayList<Computer>();
        Computer pc = new Computer();
        pc.setId(Long.valueOf("205"));
        pc.setName("pc1_dongja3");
        pc.setAuthor(a);
        computers.add(pc);
        pc = new Computer();
        pc.setId(Long.valueOf("206"));
        pc.setName("pc2_dongja3");
        pc.setAuthor(a);
        computers.add(pc);
        
        pc = a.getComputers().get(1);
        if("pc3_dongja3".equals(pc.getName())){
            pc.setName("pc4_dongja3");
        }else{
            pc.setName("pc3_dongja3");
        }
        computers.add(pc);
        a.setComputers(computers);
        
        
        
        List<Book> books = a.getTitles();
        for(Book b: books){
            b.setAuthors(null);
        }
        a.setTitles(null);
        
        books = new ArrayList<Book>();
        Book b = new Book();
        b.setId(Long.valueOf("505"));
        b.setIsbn("ISBN");
        books.add(b);
       
        a.setTitles(books);
        a.setLastUpdatedBy("UpdateBy2");
        List<Author> authors = new ArrayList<Author>();
        authors.add(a);
        b.setAuthors(authors);
        
        Publisher publisher = new Publisher();
        publisher.setId(Long.valueOf("405"));
        PubInfo pubInfo = new PubInfo();
        pubInfo.setDescription("des");
        pubInfo.setId(Long.valueOf("600"));
      
        publisher.setPubInfo(pubInfo);
        
        b.setPublisher(publisher);
        em.persist(publisher);
        
        em.persist(b);
        em.persist(a);
        
        em.getTransaction().commit();
    }

    
   
}
