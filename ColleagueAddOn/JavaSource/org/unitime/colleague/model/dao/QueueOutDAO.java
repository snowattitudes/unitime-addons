/*
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * The Apereo Foundation licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/

package org.unitime.colleague.model.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.unitime.colleague.model.QueueOut;
import org.unitime.colleague.model.base.BaseQueueOutDAO;
import org.unitime.colleague.queueprocessor.exception.LoggableException;



public class QueueOutDAO extends BaseQueueOutDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public QueueOutDAO () {}

    /**
     * Finds all QueueOuts in the database based on status
     * @param status
     * @return
     * @throws LoggableException 
     */
    public List findByStatus(String status) throws LoggableException {
        List queueOuts = null;
        Transaction tx = null;
        try {
            
            tx = getSession().beginTransaction();
            
            String hql = "from QueueOut "
            			+ "where status = :status " +
            					"order by postDate ";
            
            Query query = getSession().createQuery(hql);
            query.setString("status", status);
            
            queueOuts =  query.list();
            tx.commit();
            
        } catch (HibernateException e) {
        	tx.rollback();
        	throw new LoggableException(e);
        } finally {
        	getSession().close();
        }
        return queueOuts;
    }
    
	public QueueOut findById(Long queueId) throws LoggableException {

        List list = null;
        Transaction tx = null;
        try {
            
            tx = getSession().beginTransaction();
            
            String hql = "from QueueOut "
            			+ "where uniqueId = :queueId ";
            
            Query query = getSession().createQuery(hql);
            query.setLong("queueId", queueId);
            
            list = query.list();
            tx.commit();
            
        } catch (HibernateException e) {
        	tx.rollback();
        	throw new LoggableException(e);
        } finally {
        	getSession().close();
        }
		
        if(list.size() > 0) return (QueueOut) list.get(0);
        
        return null;
	}
	
	public QueueOut findFirstByStatus(String status) throws LoggableException {
		List<QueueOut> list = null;
		Transaction tx = null;
		try {
			tx = getSession().beginTransaction();
			
			Query query = getSession().createQuery("from QueueOut where status = :status order by uniqueId");
			query.setString("status", status);
			query.setMaxResults(1);
			
			list = query.list();
			tx.commit();
        } catch (HibernateException e) {
        	tx.rollback();
        	throw new LoggableException(e);
        } finally {
        	getSession().close();
        }
		return (list == null || list.isEmpty() ? null : list.get(0));
	}
}