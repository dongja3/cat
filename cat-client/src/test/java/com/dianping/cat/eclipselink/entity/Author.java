package com.dianping.cat.eclipselink.entity;

import org.eclipse.persistence.descriptors.changetracking.ChangeTracker;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

//import com.oocl.frm.audittrail.AuditTrailPropertyEventListener;
//@EntityListeners({ AuditTrailPropertyEventListener.class })
//@ChangeTracking(ChangeTrackingType.ATTRIBUTE)
@Entity
@Table(name="A_Author")
public class Author extends FrmAbstractDomainObject implements ChangeTracker {
    
    
    @Column(name="Name", length=20, nullable=false)
    private String name;
    
    private ContactInfo contact;
    @ManyToMany(fetch= FetchType.LAZY, mappedBy="authors")
    private List<Book> titles = new ArrayList<Book>();
    
    @OneToMany(mappedBy="author", cascade={CascadeType.ALL},
            fetch= FetchType.LAZY, targetEntity=Computer.class)
    private List<Computer> computers = new ArrayList<Computer>();
    
    

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @ManyToMany(
            cascade={CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy="authors",
            targetEntity=Book.class
        )
    public List<Book> getTitles() {
        return titles;
    }

    public void setTitles(List<Book> titles) {
        this.titles = titles;
    }

    public ContactInfo getContact() {
        return contact;
    }

    public void setContact(ContactInfo contact) {
        this.contact = contact;
    }

    public List<Computer> getComputers() {
        return computers;
    }

    public void setComputers(List<Computer> computers) {
        this.computers = computers;
    }


    public PropertyChangeListener _persistence_getPropertyChangeListener() {
        // TODO Auto-generated method stub
        return null;
    }


    public void _persistence_setPropertyChangeListener(PropertyChangeListener arg0) {
        // TODO Auto-generated method stub
        
    }
    
    public String toString(){
        return this.name + " " + this.getContact().getAddress();
        
    }


}
