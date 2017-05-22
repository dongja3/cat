/*******************************************************************************
 * Copyright (c) 1998, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * 		dclarke - initial JPA Employee example using XML (bug 217884)
 ******************************************************************************/
package com.dianping.cat.eclipselink;

import junit.framework.TestCase;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Base test case for testing a JPA persistence unit in JavaSE using JUnit4.
 * 
 * @author dongja3
 * @since EclipseLink 1.0
 */
public abstract class EclipseLinkJPATest extends TestCase {
    
    public EclipseLinkJPATest(String name){
        super(name);
    }

    public EclipseLinkJPATest(){
        super();
    }
	/**
	 * This is he current EMF in use
	 */
    
    
	private static EntityManagerFactory emf;

	protected EntityManagerFactory getEMF() {
		if (emf == null) {
			emf = createEMF();
		}

		return emf;
	}

	private EntityManagerFactory createEMF() {
		try {
			return Persistence.createEntityManagerFactory("adt");
		} catch (RuntimeException e) {
			System.out.println("Persistence.createEMF FAILED: "
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}


}
