package com.dianping.cat.eclipselink;

import com.dianping.cat.eclipselink.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class TestBookDelete extends EclipseLinkJPATest {
    public TestBookDelete(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public void testDelete() throws Exception {
        SqlTransactionContext.setCallMethod("testDelete");
        EntityManager em = getEMF().createEntityManager();

        Query q = em.createQuery("select t from Author t");
        @SuppressWarnings("unchecked")
		List<Author> list = q.getResultList();
        em.getTransaction().begin();
        Author a = list.get(0);
        List<Book> books = a.getTitles();
        
        
        for(Book b: books){
            b.setAuthors(null);
            if("123456".equals(b.getIsbn())){
                b.setIsbn("654321");
            }else{
                b.setIsbn("123456");
            }
            em.remove(b);
            Publisher p = b.getPublisher();
           
            b.setPublisher(null);
            em.remove(p);
            em.remove(b);
            System.out.println("removed book: " + b.getName());
        }
        a.setTitles(null);
        if("12345".equals(a.getContact().getAddress())){
            a.getContact().setAddress("54321");
        }else{
            a.getContact().setAddress("12345");
        }
        
        em.remove(a);
        em.getTransaction().commit();

        System.in.read();
    }
}
