package persistence.entities.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
   

    public static SessionFactory sessionFactory;

    public HibernateUtil() {
    	
    }

    public static SessionFactory getSessionFactory() {
       
        if (sessionFactory == null) {
            
        	try{
        		sessionFactory = new Configuration().configure().buildSessionFactory();
             }catch (Throwable ex) { 
                System.err.println("Failed to create sessionFactory object." + ex);
                throw new ExceptionInInitializerError(ex); 
             }
        	        
            return sessionFactory;
            
        } else {
        	
            return sessionFactory;
        }
    }
    
    public static void closeSessionFactory(){
    	if(sessionFactory != null){
    		sessionFactory.close();    		
    	}
    }
}