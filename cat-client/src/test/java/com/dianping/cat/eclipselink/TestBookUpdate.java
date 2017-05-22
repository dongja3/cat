package com.dianping.cat.eclipselink;

import com.dianping.cat.eclipselink.entity.*;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class TestBookUpdate extends EclipseLinkJPATest{
    @Test
    public void testupdateBook () throws Exception{
        CalSqlTransactionContext.setCallMethod("testbookUpdate");
        for(int i=0;i<1;i++){
            EntityManager em = getEMF().createEntityManager();
            Query q = em.createQuery("select t from Author t");
            @SuppressWarnings("unchecked")
			List<Author> list = q.getResultList();
            em.getTransaction().begin();
            Author a = list.get(0);
            
            
            if("address test update".equals(a.getContact().getAddress())){
                a.getContact().setAddress("address test update2");
            }else{
                a.getContact().setAddress("address test update");
            }
            
            if("name test update".equals(a.getName())){
                a.setName("name test update2");
            }else{
                a.setName("name test update");
            }

          //   em.flush();
             
             if("address test update".equals(a.getContact().getAddress())){
                 a.getContact().setAddress("address test update2");
             }else{
                 a.getContact().setAddress("address test update");
             }
             

            em.persist(a);
            
            em.getTransaction().commit(); 
        }

        System.in.read();
    }
    
   
}
