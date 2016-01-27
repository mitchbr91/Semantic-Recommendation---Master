package persistence.dao.hibernate;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

import persistence.entities.hibernate.Hashtag;
import persistence.entities.hibernate.URL;
import persistence.entities.hibernate.UserAccount;
import persistence.entities.hibernate.HibernateUtil;
import persistence.entities.hibernate.Tweet;
import persistence.factory.ConnectionFactory;

public class TweetDao{
	
	private SessionFactory sf;
	private Session session;
	private Transaction tx;
	
	public TweetDao() {
		
	}
	
	public Tweet getTweet(long tweetID){
		sf = HibernateUtil.getSessionFactory();
		session = sf.openSession();       
		tx = null; 
		 
		Tweet tweet = null;
		try{
	        tx = session.beginTransaction();
	        tweet = (Tweet)session.get(Tweet.class, tweetID); 	    
	        
	        Hibernate.initialize(tweet.getHashtags());
	        Hibernate.initialize(tweet.getMentions());
	        Hibernate.initialize(tweet.getURLs());
	        
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	        session.close(); 	       
	    }
		 
		return tweet;
	}
	
	public Long insertTweet(Tweet tweet){
		
		sf = HibernateUtil.getSessionFactory();
		session = sf.openSession();       
		tx = null; 
	    
		Long tweetID = null;
	     
		String sql = "";
	    try{
	    	
	    	sql = "from tweet WHERE tweet_id = " + tweet.getTweetID();
	    	tx = session.beginTransaction();	    
	    	
	    	List<Tweet> tweetsRetrieved = session.createQuery(sql).list();
	    	 
	    	 if(!tweetsRetrieved.contains(tweet))	
	    		 tweetID = (Long) session.save(tweet); 
	    	
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	        session.close(); 	       
	    }
	      
	    return tweetID;
		
	}
	
	
	public SessionFactory getFactory(){
		return HibernateUtil.getSessionFactory();
	}
}
