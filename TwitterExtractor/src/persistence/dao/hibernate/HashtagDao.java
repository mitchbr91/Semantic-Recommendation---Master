package persistence.dao.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import persistence.entities.hibernate.Hashtag;
import persistence.entities.hibernate.HibernateUtil;
import persistence.entities.hibernate.URL;


public class HashtagDao {
	
	private SessionFactory sf;
	private Session session;
	private Transaction tx;
	
	public HashtagDao() {	
		
	}
	
	public Hashtag insertHashtag(Hashtag hashtag){
		
		sf = HibernateUtil.getSessionFactory();
		session = sf.openSession();       
		tx = null; 
	    
		Hashtag hashtagToSearch = null;
		int hashtagID;
	     
	    try{
	    	tx = session.beginTransaction();	
	    	
	    	//----------------- Check to see if URL has not been already inserted
	    	
	    	List<Hashtag> hashtags = session.createQuery("FROM hashtag").list();
	    	
	    	for(Hashtag h: hashtags){
	    		
	    		if(h.equals(hashtag)){
	    			hashtagToSearch = h;
			    	break;
			    }
	    	}	
		    
		    //--------------------------------------------------------------------
	    	
	    	if(hashtagToSearch == null){
	    		hashtagID = (int) session.save(hashtag);
	    		hashtag.setHashtagID(hashtagID);
	    		hashtagToSearch = hashtag;
		    	
	    	}  	
	       
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	        session.close();	        
	    }
	      
	    return hashtagToSearch;
		
	}
	
	
	

}
