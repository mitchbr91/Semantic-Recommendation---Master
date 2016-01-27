package persistence.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import persistence.entities.Hashtag;
import persistence.entities.Tweet;
import persistence.entities.URL;
import persistence.entities.UserAccount;
import persistence.factory.ConnectionFactory;

public class TweetDao{

	private Connection connection;
	private String sql;
	private PreparedStatement stmt;
	private ResultSet rs;
	private List<UserAccount> mentions;
	private List<Hashtag> hashtags;
	private List<URL> urls;
	private List<Tweet> tweets;
	private Tweet tweet;
	private UserAccount user;
	
	public TweetDao(String databaseName) {		
		tweets = new ArrayList<Tweet>();
		this.connection = new ConnectionFactory().getConnection(databaseName);
	}
	
	public void add(Tweet tweet){
		
		long tweetID = 0;
		try {
			
			sql = "SELECT tweet_id FROM \"TWEET\" WHERE tweet_id = " + tweet.getTweetID();			
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				tweetID = rs.getLong("tweet_id");
			}		
			
			
			if(tweetID == 0){
				sql = "INSERT INTO \"TWEET\"(tweet_id, created_at, retweet_count, is_retweet, "
						+ "in_reply_to_user_id, in_reply_to_status_id, in_reply_to_screen_name,"
						+ "favorite_count, text, user_id, lang) "
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		        // prepared statement para inserção
		        stmt = connection.prepareStatement(sql);

		        // seta os valores

		        
		        stmt.setLong(1,tweet.getTweetID());
		        stmt.setDate(2, new Date(tweet.getCreatedAt().getTime()));
		        stmt.setInt(3, tweet.getRetweetCount());
		        stmt.setBoolean(4, tweet.isRetweet());
		        stmt.setLong(5, tweet.getInReplyToUserId());
		        stmt.setLong(6, tweet.getInReplyToStatusId());
		        stmt.setString(7,tweet.getInReplyToScreenName());
		        stmt.setInt(8, tweet.getFavoriteCount());
		        stmt.setString(9,tweet.getText());
		        stmt.setLong(10, tweet.getTweetPublisher());
		        stmt.setString(11,tweet.getLanguage());
		      	       
		        // executa
		        stmt.execute();		       
			}
			
			rs.close();
			stmt.close();
			
			
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public List<Tweet> listTweets(){
		 
		PreparedStatement stmt;
		ResultSet rs;
		 try {
			 sql = "SELECT tweet_id FROM \"TWEET\"";
	         
	         stmt = this.connection.prepareStatement(sql);
	         rs = stmt.executeQuery();
	 	        
	         while (rs.next()) {
	             // criando o objeto User
	        	 
	        	 tweet = getTweetByID(rs.getLong("tweet_id")); 
	 
	        	 
	             // Adding user into the list
	        	 tweets.add(tweet); 
	        	 
	         }	
	         
	         rs.close();
	         stmt.close();

	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
	     
	     return tweets;
	}
	
	public Tweet getTweetByID(long tweetID){
		
		 mentions = new ArrayList<UserAccount>();
		 sql = "SELECT * FROM \"TWEET\" WHERE tweet_id = " + tweetID;
				 
		 try {
	              
			 stmt = connection.prepareStatement(sql);
	         rs = stmt.executeQuery();	    
	         
	         
	         while (rs.next()) {
	             // criando o objeto User
	            
	        	 tweet = new Tweet();
		         tweet.setTweetID(rs.getLong("tweet_id"));
		 	     tweet.setCreatedAt(rs.getDate("created_at"));
		 	     tweet.setRetweetCount(rs.getInt("retweet_count"));
		 	     tweet.setRetweet(rs.getBoolean("is_retweet"));
		 	     tweet.setInReplyToUserId(rs.getLong("in_reply_to_user_id"));
		 	     tweet.setInReplyToStatusId(rs.getLong("in_reply_to_status_id"));
		 	     tweet.setInReplyToScreenName(rs.getString("in_reply_to_screen_name"));
		 	     tweet.setFavoriteCount(rs.getInt("favorite_count"));
		 	     tweet.setText(rs.getString("text"));
		 	     
		 	     tweet.setLanguage(rs.getString("lang"));
	            	
	         }	  
	         
	        
	         
	         sql = "SELECT * FROM \"TB_User\" u WHERE u.user_id IN (SELECT user_id FROM \"TWEET\" WHERE tweet_id = " + tweetID + ")";
	         
	         stmt = connection.prepareStatement(sql);			
			 rs = stmt.executeQuery();
			
			 long userID = 0;
			 while (rs.next()) {		   
				 userID = rs.getLong("user_id");		            	 	            
		     }	   
	         
			 
	         
	         // Updating tweet publisher
	         tweet.setTweetPublisher(userID);

	         // Updating mentions
			 mentions = listMentionsByTweetID(tweetID);
	         tweet.setMention(mentions);	    
	         
	         // Updating hashtags
	         hashtags = listHashtagsByTweetID(tweetID);
	         tweet.setHashtags(hashtags);	   
	         
	         // Updating urls
	         urls = listURLsByTweetID(tweetID);
	         tweet.setURLS(urls);
	         
			
	         rs.close();
	         stmt.close();
		 }catch (SQLException e) {
		      throw new RuntimeException(e);
		 }
		 
		 
		return tweet;
		
	}
	
	List<URL> listURLsByTweetID(long tweetID){
		
		urls = new ArrayList<URL>();
		
		sql = "SELECT * FROM \"URL\" u WHERE u.url_id IN"
		 		+ " (SELECT url_id FROM \"TWEET_URL\" WHERE tweet_id = " + tweetID + ")";
		
		try {
			
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			URL url;
	        while (rs.next()) {
	             // creating hashtag object
	            
	        	 url = new URL();
	             
	        	 url.setUrlID(rs.getInt("url_id"));
	        	 url.setUrl(rs.getString("url"));
	        	 
	             urls.add(url);
	             
	        }
	      
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
		return urls;
		
	}
	
	public void addIntoTweet_UserTB(long tweetID, long userID){
		
		
		long tweet_id = 0;
		try {
		
			sql = "SELECT tweet_id FROM \"TWEET_USER\" WHERE tweet_id =  " + tweetID + " AND user_id = " + userID; 
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				tweet_id = rs.getLong("tweet_id");
	        }
			
			if(tweet_id == 0){
				sql = "INSERT INTO \"TWEET_USER\"(tweet_id, user_id) VALUES (?, ?)";
			    // prepared statement para inserção
			    stmt = connection.prepareStatement(sql);
    
			    // seta os valores
			        
			    stmt.setLong(1,tweetID);
			    stmt.setLong(2,userID);
			       	      	       
			    // executa
			    stmt.execute();
			}
			
		    rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
	}
	
	List<UserAccount> listMentionsByTweetID(long tweetID){
		
		 mentions = new ArrayList<UserAccount>();		
		 
		 sql = "SELECT * FROM \"TB_User\" u WHERE u.user_id IN"
		 		+ " (SELECT user_id FROM \"TWEET_USER\" WHERE tweet_id = " + tweetID + ")";
		 try {
			stmt = connection.prepareStatement(sql);
			
			rs = stmt.executeQuery();	         
		        
	        UserAccount user;
	        while (rs.next()) {
	             // criando o objeto User
	            
	        	 user = new UserAccount();
	             user.setIDUser(rs.getLong("user_id"));
	             user.setCreatedAt(rs.getDate("created_at"));
	             user.setFavouritesCount(rs.getInt("favourites_count"));
	             user.setFriendsCount(rs.getInt("friends_count"));
	             user.setFollowersCount(rs.getInt("followers_count"));
	             user.setLanguage(rs.getString("lang"));
	             user.setLocation(rs.getString("user_location"));
	             user.setListedCount(rs.getInt("listed_count"));
	             user.setName(rs.getString("name"));
	             user.setDescription(rs.getString("description"));
	             user.setUrl(rs.getString("url"));
	             user.setScreenName(rs.getString("screen_name"));
	             user.setStatusesCount(rs.getInt("statuses_count"));
	             user.setVerified(rs.getBoolean("is_verified"));	
	             
	             mentions.add(user);
	             
	        }
	        
	        rs.close();
	        stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return mentions;
	}
	
	List<Hashtag> listHashtagsByTweetID(long tweetID){
		
		hashtags = new ArrayList<Hashtag>();
		 
		sql = "SELECT * FROM \"HASHTAG\" h WHERE h.hashtag_id IN"
		 		+ " (SELECT hashtag_id FROM \"TWEET_HASHTAG\" WHERE tweet_id = " + tweetID + ")";
		
		try {
			
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			Hashtag hashtag;
	        while (rs.next()) {
	             // creating hashtag object
	            
	        	 hashtag = new Hashtag();
	             
	        	 hashtag.setHashtagID(rs.getInt("hashtag_id"));
	        	 hashtag.setHashtag(rs.getString("hashtag"));
	             
	             hashtags.add(hashtag);
	             
	        }
	      
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
		return hashtags;
	}
	
	public void addIntoTweet_HashtagTB(long tweetID, int hashtagID){
		
			
		long tweet_id = 0;
		try {
			
			sql = "SELECT tweet_id FROM \"TWEET_HASHTAG\" WHERE tweet_id = " + tweetID + " AND hashtag_id = " + hashtagID;
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
	        while (rs.next()) {
	             tweet_id = rs.getLong("tweet_id");	             
	        }
	        
	        if(tweet_id == 0){
	        	sql = "INSERT INTO \"TWEET_HASHTAG\"(tweet_id, hashtag_id) VALUES (?, ?)";
		        // prepared statement para inserção
		        stmt = connection.prepareStatement(sql);

		        // seta os valores
		        
		        stmt.setLong(1,tweetID);
		        stmt.setLong(2,hashtagID);
		       	      	       
		        // executa
		        stmt.execute();
	        }
			
			rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
	}
	
	public void addIntoTweet_URLTB(long tweetID, int urlID){
		
		
		long tweet_id = 0;
		try {
			
			sql = "SELECT tweet_id FROM \"TWEET_URL\" WHERE tweet_id = " + tweetID + " AND url_id = " + urlID;
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				tweet_id = rs.getLong("tweet_id");
			}
			
			if(tweet_id == 0){

				sql = "INSERT INTO \"TWEET_URL\"(tweet_id, url_id) VALUES (?, ?)";
		        // prepared statement para inserção
		        stmt = connection.prepareStatement(sql);

		        // seta os valores
		        
		        stmt.setLong(1,tweetID);
		        stmt.setLong(2,urlID);
		       	      	       
		        // executa
		        stmt.execute();
			}
			
			rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
	}	
	

	public void shutdownConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}