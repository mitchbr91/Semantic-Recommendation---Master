package persistence.dao.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import persistence.entities.hibernate.Hashtag;
import persistence.entities.hibernate.HibernateUtil;
import persistence.entities.hibernate.URL;

public class URLDao {
	
	private SessionFactory sf;
	private Session session;
	private Transaction tx;
	
	public URLDao(){	
		
	}
	
	public URL insertURL(URL url){
				
		sf = HibernateUtil.getSessionFactory();
		session = sf.openSession();       
		tx = null; 
	    
		URL urlToSearch = null;
		int urlID = 0;
	     
	    try{
	    	tx = session.beginTransaction();	
	    	
	    	//----------------- Check to see if URL has not been already inserted
	    	
	    	List<URL> urls = session.createQuery("FROM URL").list();
	    	
	    	for(URL u: urls){
	    		
	    		if(u.getUrl().equalsIgnoreCase(url.getUrl())){
	    			urlToSearch = u;
			    	break;
			    }
	    	}		    
	    	
	    	
		    //--------------------------------------------------------------------
	    	
	    	if(urlToSearch == null){
		    	urlID = (int) session.save(url);		    	
		    	url.setUrlID(urlID);
		    	urlToSearch = url;
	    	}  	
	       
	    	session.flush();
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        //e.printStackTrace();
	        
	        System.out.println("URL: " + url.getUrl());
	    }finally {
	    	session.clear();
	        session.close(); 	 
	        
	    }
	      
	    return urlToSearch;
	}
	
	
}