package persistence.dao.hibernate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.postgresql.util.PSQLException;

import persistence.entities.hibernate.HibernateUtil;
import persistence.entities.hibernate.RegularRecommendation;
import persistence.entities.hibernate.SemanticRecommendation;
import persistence.entities.hibernate.Tweet;
import persistence.entities.hibernate.UserAccount;
import persistence.entities.hibernate.UserInference;

public class UserDao{
	
	 private SessionFactory sf;
	 private Session session;
	 private Transaction tx;
	 
	 public UserDao() {	
		 
	 }
	 
	 public List<UserAccount> listUsers(boolean targetUsers, boolean initialize){
		 
		 SessionFactory sf = HibernateUtil.getSessionFactory();
		 Session session = sf.openSession();       
		 Transaction tx = null;
		 
		 List<UserAccount> users = new ArrayList<UserAccount>();
		 String sql = "";
		 
		 if(targetUsers){
			 sql = "from tb_user WHERE target_user = true";
		 }else{
			 sql = "from tb_user ";
		 }
		 
		 try{
	         tx = session.beginTransaction();
	         List<UserAccount> usersRetrieved = session.createQuery(sql).list();
	         
	         for(UserAccount user: usersRetrieved){
	        	 users.add(getUser(user.getIDUser(), initialize));	
	         }
			 	         
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close();
	        
	      }
		 
		 return users;
		 
	 }
	 
	 public UserAccount getUserByScreenname(String screenname, boolean initialize){
		 
		 SessionFactory sf = HibernateUtil.getSessionFactory();
		 Session session = sf.openSession();       
		 Transaction tx = null;
		
		
		 String sql = "from tb_user WHERE screen_name = " + "\'" + screenname + "\'";
		 
		 UserAccount user = null;
		 try{
	         tx = session.beginTransaction();
	         List<UserAccount> usersRetrieved = session.createQuery(sql).list();
	         
	         if(usersRetrieved.size() == 1)
	        	 user = getUser(usersRetrieved.get(0).getIDUser(), initialize);
			 	         
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close();
	        
	      }
		 
		 return user;
		 
	 }
	 
	 public UserAccount getUser(long userID, boolean initialize){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		 
	     UserAccount user = null;
		 try{
	         tx = session.beginTransaction();
	         user = (UserAccount)session.get(UserAccount.class, userID); 	
	         
	         Hibernate.initialize(user.getFollowees());  	   
	         Hibernate.initialize(user.getInferences());
	         Hibernate.initialize(user.getRegularRecommendations());
	         Hibernate.initialize(user.getSemanticRecommendations());
	         Hibernate.initialize(user.getRegularUnfollows());
	         Hibernate.initialize(user.getSemanticUnfollows());
	         
	         if(initialize){
	        	
	        	 Hibernate.initialize(user.getTweets()); 	        	 
	        	 Hibernate.initialize(user.getFavorites());
		         Hibernate.initialize(user.getLists());		               
		         Hibernate.initialize(user.getReplies());
		         Hibernate.initialize(user.getRetweets());		        
		         		         
		         for(persistence.entities.hibernate.List list: user.getLists()){
		        	 Hibernate.initialize(list.getListMembers());
		         }
		         
		         for(Tweet tweet: user.getTweets()){
	        		 Hibernate.initialize(tweet.getHashtags());
	        		 Hibernate.initialize(tweet.getURLs());
	        		 Hibernate.initialize(tweet.getMentions());
	        	 }
		         
		         for(Tweet fav: user.getFavorites()){
	        		 Hibernate.initialize(fav.getHashtags());
	        		 Hibernate.initialize(fav.getURLs());
	        		 Hibernate.initialize(fav.getMentions());
	        	 }
		         
		         for(Tweet retweet: user.getRetweets()){
	        		 Hibernate.initialize(retweet.getHashtags());
	        		 Hibernate.initialize(retweet.getURLs());
	        		 Hibernate.initialize(retweet.getMentions());
	        	 }
		         
		         for(Tweet reply: user.getReplies()){
	        		 Hibernate.initialize(reply.getHashtags());
	        		 Hibernate.initialize(reply.getURLs());
	        		 Hibernate.initialize(reply.getMentions());
	        	 }
		         
		         
		         for(UserAccount followee: user.getFollowees()){
		        	 Hibernate.initialize(followee.getTweets());
		        	 
		        	 for(Tweet tweet: followee.getTweets()){
		        		 Hibernate.initialize(tweet.getHashtags());
		        		 Hibernate.initialize(tweet.getURLs());
		        		 Hibernate.initialize(tweet.getMentions());
		        	 }
		        	 
		        	 Hibernate.initialize(followee.getRetweets());
		        	 
		        	 for(Tweet retweet: followee.getRetweets()){
		        		 Hibernate.initialize(retweet.getHashtags());
		        		 Hibernate.initialize(retweet.getURLs());
		        		 Hibernate.initialize(retweet.getMentions());
		        	 }
		        	 
		         } 
	         }	         
	         
	         
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close();	         
	      }
		 
		 return user;
		 
	 }
	 
	 public Long insertUser(UserAccount user){
		
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
	    
		 Long userID = null;
	     
		 String sql;
	     try{
	    	 
	    	 sql = "from tb_user WHERE user_id = " + user.getIDUser();
	    	 
	    	 tx = session.beginTransaction();	
	    	 List<UserAccount> usersRetrieved = session.createQuery(sql).list();
	    	 
	    	 if(!usersRetrieved.contains(user))	
	    		 userID = (Long) session.save(user); 
	         
	    	 tx.commit();
	     }catch (HibernateException e) {	    	 
	    	 
	    	 if (tx!=null) tx.rollback();
	    	 e.printStackTrace(); 
	    	 
	    	 
	    	 if(e.getCause() instanceof PSQLException){
	    		 System.out.println("User " + user.getScreenName() + " already inserted!!!");
	    	 }
	         
	     }finally {
	         session.close();         
	     }
	      
	     return userID;		 
	 }
	
	 
	 public void insertReply(Long userID, Tweet tweet){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		 
		 List<Tweet> replies;
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
	         
	         replies = user.getReplies();
	         
	         if(!replies.contains(tweet)){	         
	        	 user.getReplies().add(tweet);
	        	 session.update(user); 
	         }
	         
			 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }
		 
	 }	 
	 
	 public void insertRetweet(Long userID, Tweet retweet){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		 
		 List<Tweet> retweets;
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
	         
	         retweets = user.getRetweets();
	         
	         if(!retweets.contains(retweet)){	         
	        	 user.getRetweets().add(retweet);
	        	 session.update(user); 
	         }
	         
			 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }
		 
	 }	 
	 
	 public void insertFavorite(Long userID, Tweet favorite){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		 
		 List<Tweet> favorites;
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
	         
	         favorites = user.getFavorites();
	         
	         if(!favorites.contains(favorite)){	         
	        	 user.getFavorites().add(favorite);
	        	 session.update(user); 
	         }
	         
			 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }
		 
	 }
	 
	 public void insertInference(Long userID, UserInference userInference){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		 
		 	 
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
	        
	                  
	         user.insertInference(userInference);
	         session.update(user); 
	        
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }       
	      
		 
	 }
	 
	 public void insertRegularUnfollow(Long userID, UserAccount unfollow){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		 
		 List<UserAccount> unfollows;
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
	         
	         unfollows = user.getRegularUnfollows();
	         
	         if(!unfollows.contains(unfollow)){	         
	        	 user.getRegularUnfollows().add(unfollow);
	        	 session.update(user); 
	         }
	         
			 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }
		 
	 }
	 
	 public void insertSemanticUnfollow(Long userID, UserAccount unfollow){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		 
		 List<UserAccount> unfollows;
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
	         
	         unfollows = user.getSemanticUnfollows();
	         
	         if(!unfollows.contains(unfollow)){	         
	        	 user.getSemanticUnfollows().add(unfollow);
	        	 session.update(user); 
	         }
	         
			 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }
		 
	 }
	 
	 public void insertSemanticRecommendation(Long userID, SemanticRecommendation semanticRecommendation){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		 
		 	 
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
	        
	                  
	         user.getSemanticRecommendations().add(semanticRecommendation);
	         session.update(user); 
	        
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }
		 
	 }

	 public void insertRegularRecommendation(Long userID, RegularRecommendation regularRecommendation){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		 
			 
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
	        
	                  
	         user.getRegularRecommendations().add(regularRecommendation);
	         session.update(user); 
	        
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }
		 
	 }
	 
	 public void deleteUser(Long userID){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
        	       
	         session.delete(user);	        
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }
		 
	 }
	
	 
	 public void updateTweetsCollection(Long userID){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
	        
	         user.setTweetsCollected(true);	         
	         session.update(user); 
	         	        			 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }
	 }
	
	 public void updateInfered(Long userID){
		 
		 sf = HibernateUtil.getSessionFactory();
		 session = sf.openSession();       
		 tx = null; 
		
		 try{
	         tx = session.beginTransaction();
	         UserAccount user = 
	                    (UserAccount)session.get(UserAccount.class, userID); 
	        
	         user.setInfered(true);	         
	         session.update(user); 
	         	        			 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 	        
	      }
	 }
	 
	 public void readUnfollows(){
		 File file = new File("unfollows1");
	    	File afile[] = file.listFiles();
	    	
	    	StringTokenizer st;
	    	String filename, user, type, linha;
	    	BufferedReader lerArq;
	    	UserAccount tUser, rec;
	    	for (int i = 0; i < afile.length; i++) {
	    		filename = afile[i].getName();
	    		st = new StringTokenizer(filename, "-");
	    		user = st.nextToken();
	    		type = st.nextToken().replace(".txt", "");
	    		    		
	    		try {
					lerArq = new BufferedReader(new FileReader("unfollows1/"+filename));
					linha = lerArq.readLine();
					
					tUser = getUserByScreenname(user, false);
					System.out.println("Arquivo:  " + filename);
					while(linha != null){					
						
						rec = getUserByScreenname(linha, false);
						if(type.equals("1")){
							insertRegularUnfollow(tUser.getIDUser(), rec);
						}else{
							insertSemanticUnfollow(tUser.getIDUser(), rec);
						}
						linha = lerArq.readLine();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	 }
	
}