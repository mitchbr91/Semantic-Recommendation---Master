package persistence.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import persistence.entities.hibernate.HibernateUtil;
import persistence.entities.hibernate.List;
import persistence.entities.hibernate.Tweet;
import persistence.entities.hibernate.UserAccount;

public class ListDao {

	private SessionFactory sf;
	private Session session;
	private Transaction tx;
	
	public ListDao(){
		
	}
	
	public java.util.List<List> listLists(){
		
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();       
		Transaction tx = null;
		 
		java.util.List<List> lists = new ArrayList<List>();
		String sql = "from list";
		
			 
		try{
	        tx = session.beginTransaction();
	        java.util.List<List> listsRetrieved = session.createQuery(sql).list();
	         
	        for(List list: listsRetrieved){
	        	lists.add(getList(list.getIDList()));	
	        }
			         
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	       session.close();	       
	    }
		 
		 return lists;
		
	}
	
	public List getList(long listID){
		
		sf = HibernateUtil.getSessionFactory();
		session = sf.openSession();       
		tx = null; 
		 
	    List list = null;
		try{
	        tx = session.beginTransaction();
	        list = (List)session.get(List.class, listID); 	        
	        Hibernate.initialize(list.getListMembers());
	        
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	        session.close(); 	        
	    }
		 
		return list;
		
	}
	
	public Long insertList(List list){
		sf = HibernateUtil.getSessionFactory();
		session = sf.openSession();       
		tx = null; 
	    
		Long listID = null;
		String sql;
	     
	    try{
	    	sql = "from list WHERE list_id = " + list.getIDList();
	    	tx = session.beginTransaction();	
	    	
	    	java.util.List<List> listsRetrieved = session.createQuery(sql).list();
	    	 
	    	 if(!listsRetrieved.contains(list))	
	    		 listID = (Long) session.save(list); 	
	    	 
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	        session.close(); 	        
	    }
	      
	    return listID;
	}	
	
	
}
