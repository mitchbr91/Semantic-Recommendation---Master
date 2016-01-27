package persistence.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import persistence.entities.Hashtag;
import persistence.entities.Tweet;
import persistence.entities.URL;
import persistence.entities.UserAccount;
import persistence.factory.ConnectionFactory;


public class UserDao{

	 private Connection connection;
	 private String sql;
	 private PreparedStatement stmt;
	 private ResultSet rs;
	 private ResultSet rsPublisher;
	 private List<Tweet> tweets;
	 private List<Tweet> retweets;
	 private List<Tweet> favorites;
	 private List<UserAccount> replies;
	 private List<persistence.entities.List> lists;
	 private List<UserAccount> followees;
	 private List<UserAccount> users;
	 private Tweet tweet;
	 private UserAccount user;
	 private List<UserAccount> mentions;
	 private List<Hashtag> hashtags;
	 private List<URL> urls;
	 private TweetDao daoTweet;		
	 private ListDao daoList;
	 
	 public UserDao(String databaseName) {	
		 daoList = new ListDao(databaseName);
		 daoTweet = new TweetDao(databaseName);
		 replies = new ArrayList<UserAccount>();
		 lists = new ArrayList<persistence.entities.List>();
		 users = new ArrayList<UserAccount>();
		 followees = new ArrayList<UserAccount>();
	     this.connection = new ConnectionFactory().getConnection(databaseName);	   
	 }
	 
	 public void add(UserAccount user){
		 
		 PreparedStatement stmt;
		 ResultSet rs;		 
		 long userID = 0;
		 try {
			 
			   sql = "SELECT user_id FROM \"TB_User\" WHERE user_id = " + user.getIDUser();	
			   stmt = this.connection.prepareStatement(sql);
		       rs = stmt.executeQuery();
			   
		       while (rs.next()) {		            
		    	   userID = rs.getLong("user_id");
		       }
		       
		       if(userID == 0){
		    	   sql = "INSERT INTO \"TB_User\""
					 		+ "(created_at, favourites_count, friends_count, followers_count, listed_count, name, "
					 		+ "description, url, screen_name, statuses_count, is_verified, user_id, lang, user_location, target_user)" + 
			            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					 
		    					 
			        // prepared statement para inserção
			       stmt = connection.prepareStatement(sql);

			        // seta os valores

			        if(user.getCreatedAt() == null){
			        	stmt.setDate(1, null);
			        }else{
			        	stmt.setDate(1, new Date(user.getCreatedAt().getTime()));
			        }
			        
			        stmt.setInt(2,user.getFavouritesCount());
			        stmt.setInt(3,user.getFriendsCount());
			        stmt.setInt(4,user.getFollowersCount());
			        stmt.setInt(5,user.getListedCount());
			        stmt.setString(6,user.getName());
			        stmt.setString(7,user.getDescription());
			        stmt.setString(8,user.getUrl());
			        stmt.setString(9,user.getScreenName());
			        stmt.setInt(10,user.getStatusesCount());
			        stmt.setBoolean(11, user.isVerified());
			        stmt.setLong(12, user.getIDUser());
			        stmt.setString(13,user.getLanguage());
			        stmt.setString(14,user.getLocation());
			        stmt.setBoolean(15, user.isTargetUser());
			       
			        // executa
			        stmt.execute();
		       }
		       
			    rs.close();
		        stmt.close();
		    } catch (SQLException e) {
		        throw new RuntimeException(e);
		    }
		 
	 }

	 public List<UserAccount> listUsers(boolean targetUsers) {		 
		
		 List<UserAccount> users = new ArrayList<UserAccount>();
		 
		 if(targetUsers){
			 sql = "select user_id from \"TB_User\" WHERE target_user = true";
		 }else{
			 sql = "select user_id from \"TB_User\"";
		 }
	
		 ResultSet rs;
		 PreparedStatement stmt;		 
		 
		 try {	    	
	         
	         stmt = this.connection.prepareStatement(sql);
	         rs = stmt.executeQuery();
	 
	         UserAccount user;
	         while (rs.next()) {
	        	 
	             // criando o objeto User	        	 
	        	 user = getUserByID(rs.getLong("user_id")); 
	 
	             // Adding user into the list	        	 
	             users.add(user);        	 
	         
	         }	
	      
	         rs.close();
	         stmt.close();
	         
	         

	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
	     
	     return users;
	 }	 
	 
	 
	 private List<UserAccount> createUserObject(ResultSet rs){
		
		 users = new ArrayList<UserAccount>();
		 
		 try {
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
			     user.setTargetUser(rs.getBoolean("target_user"));
			    	 	            
			     users.add(user);
			 }
			
			 rs.close();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return users;
	 }
	 
	 public UserAccount getUserByID(long userID){
		 
		 sql = "SELECT * FROM \"TB_User\" WHERE user_id = " + userID;
		 
		 UserAccount user = null;
		 		 
		 PreparedStatement stmt;
		 ResultSet rs;
		 
		 try {
	         stmt = connection.prepareStatement(sql);
	         rs = stmt.executeQuery();
	        
	         user = createUserObject(rs).get(0);	        
	         
	         // Updating tweets	         
	         tweets = listTweetsByUserID(user.getIDUser()); 
	         user.setTweets(tweets);
	         
	         // Updating retweets
	         retweets = listReweetsByUserID(user.getIDUser());
	         user.setRetweets(retweets);
	        
	         // Updating favorites
	         favorites = listFavoritesByUserID(user.getIDUser());
	         user.setFavorites(favorites);
	         
	         // Updating lists
	         lists = listListsByUserID(user);
	         user.setLists(lists);
	         
	         //Updating followees
	         followees = listFolloweesByUserID(user.getIDUser());
	         user.setFollowees(followees);
	         
	 	     // Updating replies
	         replies = listRepliesByUserID(user.getIDUser());
	         user.setReplies(replies);
	         
	         stmt.close();
	         rs.close();
	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }		 
		
		 return user;
	}
	 
	public UserAccount getUserByScreenName(String screenname){
		
		 sql = "SELECT * FROM \"TB_User\" WHERE screen_name = \'" + screenname + "\'";
		
		 UserAccount user = null;
		 
		 PreparedStatement stmt;
		 ResultSet rs;
		 
		 try {
	         stmt = connection.prepareStatement(sql);
	         rs = stmt.executeQuery();
	        
	         user = createUserObject(rs).get(0);	        
	         
	         // Updating tweets	         
	         tweets = listTweetsByUserID(user.getIDUser()); 
	         user.setTweets(tweets);
	         
	         // Updating retweets
	         retweets = listReweetsByUserID(user.getIDUser());
	         user.setRetweets(retweets);
	        
	         // Updating favorites
	         favorites = listFavoritesByUserID(user.getIDUser());
	         user.setFavorites(favorites);
	         
	         // Updating lists
	         lists = listListsByUserID(user);
	         user.setLists(lists);
	         
	         //Updating followees
	         followees = listFolloweesByUserID(user.getIDUser());
	         user.setFollowees(followees);
	         
	 	     // Updating replies
	         replies = listRepliesByUserID(user.getIDUser());
	         user.setReplies(replies);
	                
	         stmt.close();
	         rs.close();
	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }		 
		
		 return user;
	}
	
	private UserAccount updateUserObjects(UserAccount user){
		
		// Updating tweets	         
        tweets = listTweetsByUserID(user.getIDUser()); 
        user.setTweets(tweets);
        
        // Updating retweets
        retweets = listReweetsByUserID(user.getIDUser());
        user.setRetweets(retweets);
       
        // Updating favorites
        favorites = listFavoritesByUserID(user.getIDUser());
        user.setFavorites(favorites);
        
        // Updating lists
        lists = listListsByUserID(user);
        user.setLists(lists);
        
        //Updating followees
        followees = listFolloweesByUserID(user.getIDUser());
        user.setFollowees(followees);
        
	    // Updating replies
        replies = listRepliesByUserID(user.getIDUser());
        user.setReplies(replies);
                
        return user;
	}
	 
	private List<persistence.entities.List> listListsByUserID(UserAccount user){
		
		sql = "SELECT * FROM \"LIST\" WHERE user_id = " + user.getIDUser();
		
		persistence.entities.List list;
		PreparedStatement stmt;
		ResultSet rs, rsMembers;		
		
		lists = new ArrayList<persistence.entities.List>();
		try {
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			 List<UserAccount> members;
			 while (rs.next()) {
			     // criando o objeto List			    
				
				list = new persistence.entities.List();
				list.setIDList(rs.getLong("list_id"));
				list.setName(rs.getString("name"));
				list.setDescription(rs.getString("description"));
				list.setListOwner(user);
			    
				members = daoList.listUsersByListID(list.getIDList());
				
				list.setListMembers(members);	
			    lists.add(list);
			    	 	            
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lists;
	}
	
	public List<UserAccount> listFolloweesByUserID(long userID){
		
		PreparedStatement stmt;
		
		sql = "SELECT * FROM \"TB_User\" u WHERE u.user_id IN"
		 		+ " (SELECT followee_id FROM \"FOLLOWER_FOLLOWEE\" WHERE follower_id = " + userID + ")";
		
		try {	    	
	         
	         stmt = this.connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery();
	 
	         followees = new ArrayList<UserAccount>();
	         	         
	         for(UserAccount user: createUserObject(rs)){
	        	 
	        	 // Updating tweets	         
	             tweets = listTweetsByUserID(user.getIDUser()); 
	             user.setTweets(tweets);
	             
	             // Updating retweets
	             retweets = listReweetsByUserID(user.getIDUser());
	             user.setRetweets(retweets);
	        	 
	        	 followees.add(user);	        	
	         }
	        
	         rs.close();
	         stmt.close();

	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
		
		return followees;
	}
	
	private List<UserAccount> listRepliesByUserID(long userID){
		
		PreparedStatement stmt;
		
		sql = "SELECT * FROM \"TB_User\" u WHERE u.user_id IN"
		 		+ " (SELECT reply_id FROM \"USER_REPLY\" WHERE user_id = " + userID + ")";
		
		try {	    	
	         
	         stmt = this.connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery();
	 
	         replies = new ArrayList<UserAccount>();
	         for(UserAccount user: createUserObject(rs)){
	        	 replies.add(updateUserObjects(user));
	         }	       

	         rs.close();
	         stmt.close();

	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
		
		return replies;
	}
	 
	private List<Tweet> listFavoritesByUserID(long userID){
		
		PreparedStatement stmt;
		
		sql = "SELECT * FROM \"TWEET\"  WHERE tweet_id IN"
		 		+ " (SELECT favorite_id FROM \"USER_FAVORITE\" WHERE user_id = " + userID + ")";
		
		Tweet tweet;
		UserAccount user = null;
		
		List<UserAccount> mentions;
		List<Hashtag> hashtags;
		List<URL> urls;
		
		favorites = new ArrayList<Tweet>();
		try {
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			 
			 
			 while (rs.next()) {
			     // criando o objeto Tweet
			    
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
			   				
				tweet.setTweetPublisher(userID);		
				
				// Updating mentions
				 mentions = daoTweet.listMentionsByTweetID(tweet.getTweetID());
		         tweet.setMention(mentions);	         
		         
		         // Updating hashtags
		         hashtags = daoTweet.listHashtagsByTweetID(tweet.getTweetID());
		         tweet.setHashtags(hashtags);
		         
		         // Updating urls
		         urls = daoTweet.listURLsByTweetID(tweet.getTweetID());
		         tweet.setURLS(urls);
				
			    favorites.add(tweet);
			    
			 }		 
				
			rs.close();
			stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return favorites;
		
	}
	 
	private List<Tweet> listReweetsByUserID(long userID) {
		
		PreparedStatement stmt;
		
		retweets = new ArrayList<Tweet>();
		
		sql = "SELECT * FROM \"TWEET\" WHERE tweet_id IN"
		 		+ " (SELECT retweet_id FROM \"USER_RETWEET\" WHERE user_id = " + userID + ")";
		
		try {
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			
			 
			 while (rs.next()) {
			     // criando o objeto Tweet
			    
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
			   
				tweet.setTweetPublisher(userID);		
				
				// Updating mentions
				 mentions = daoTweet.listMentionsByTweetID(tweet.getTweetID());
		         tweet.setMention(mentions);	         
		         
		         // Updating hashtags
		         hashtags = daoTweet.listHashtagsByTweetID(tweet.getTweetID());
		         tweet.setHashtags(hashtags);
		         
		         // Updating urls
		         urls = daoTweet.listURLsByTweetID(tweet.getTweetID());
		         tweet.setURLS(urls);
				
				
			     retweets.add(tweet);
			    	 	            
			}			 
		
		rs.close();	
		stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retweets;
		
	}
	
	
	public void addIntoUser_ReplyTB(long userID, long replyID){
		
		PreparedStatement stmt;
		long user_id = 0;
		
		try {
			
			sql = "SELECT user_id FROM \"USER_REPLY\" WHERE user_id = " + userID + " AND reply_id = " + replyID;
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				user_id = rs.getLong("user_id");
			}
			
			if(user_id == 0){
				sql = "INSERT INTO \"USER_REPLY\"(user_id, reply_id) VALUES (?, ?)";
		        // prepared statement para inserção
		        stmt = connection.prepareStatement(sql);

		        // seta os valores
		        
		        stmt.setLong(1,userID);
		        stmt.setLong(2,replyID);
		       	      	       
		        // executa
		        stmt.execute();
			}
			
			rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
	}
	

	public void addIntoFollower_FolloweeTB(long followerID, long followeeID){
		
		PreparedStatement stmt;
		long followee_id = 0;
		
		try {
			
			sql = "SELECT follower_id FROM \"FOLLOWER_FOLLOWEE\" WHERE follower_id = " + followerID + " AND followee_id = " + followeeID;
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				followee_id = rs.getLong("follower_id");
			}
			
			if(followee_id == 0){
				sql = "INSERT INTO \"FOLLOWER_FOLLOWEE\"(follower_id, followee_id) VALUES (?, ?)";
		        // prepared statement para inserção
		        stmt = connection.prepareStatement(sql);

		        // seta os valores
		        
		        stmt.setLong(1,followerID);
		        stmt.setLong(2,followeeID);
		       	      	       
		        // executa
		        stmt.execute();
			}
			
			rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
	}
	
	public void addIntoUser_FavoriteTB(long userID, long tweetID){
		
		PreparedStatement stmt;
		long user_id = 0;		
		try {
			
			sql = "SELECT user_id FROM \"USER_FAVORITE\" WHERE user_id = " + userID + " AND favorite_id = " + tweetID;
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				user_id = rs.getLong("user_id");
			}
			
			if(user_id == 0){
				sql = "INSERT INTO \"USER_FAVORITE\"(user_id, favorite_id) VALUES (?, ?)";
		        // prepared statement para inserção
		       stmt = connection.prepareStatement(sql);

		        // seta os valores
		        
		        stmt.setLong(1,userID);
		        stmt.setLong(2,tweetID);
		       	      	       
		        // executa
		        stmt.execute();
			}
			
			rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}	
	
	public void addIntoUser_RetweetTB(long userID, long tweetID){
		
		PreparedStatement stmt;
		long user_id = 0;		
		try {
			
			sql = "SELECT user_id FROM \"USER_RETWEET\" WHERE user_id = " + userID + " AND retweet_id = " + tweetID;
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				user_id = rs.getLong("user_id");
			}
			
			if(user_id == 0){
				sql = "INSERT INTO \"USER_RETWEET\"(user_id, retweet_id) VALUES (?, ?)";

		        // prepared statement para inserção
		        stmt = connection.prepareStatement(sql);

		        // seta os valores
		        
		        stmt.setLong(1,userID);
		        stmt.setLong(2,tweetID);
		       	      	       
		        // executa
		        stmt.execute();
			}
			
			rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
	}

	private List<Tweet> listTweetsByUserID(long userID) {
		
		tweets = new ArrayList<Tweet>();
		
		PreparedStatement stmt;
		sql = "SELECT * FROM \"TWEET\" WHERE user_id = " + userID;
		try {
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			 
			 while (rs.next()) {
			     // criando o objeto Tweet
			    
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
			    tweet.setTweetPublisher(userID);
			    tweet.setLanguage(rs.getString("lang"));
			 

		         // Updating mentions
				 mentions = daoTweet.listMentionsByTweetID(tweet.getTweetID());
		         tweet.setMention(mentions);	         
		         
		         // Updating hashtags
		         hashtags = daoTweet.listHashtagsByTweetID(tweet.getTweetID());
		         tweet.setHashtags(hashtags);
		         
		         // Updating urls
		         urls = daoTweet.listURLsByTweetID(tweet.getTweetID());
		         tweet.setURLS(urls);
			    
			     tweets.add(tweet);
			    	 	            
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
		return tweets;
	}
	 
	 public void update(UserAccount user){
		 
		 PreparedStatement stmt;
		 String sql = "UPDATE \"TB_User\""
		 		+ "  SET created_at=?, favourites_count=?, friends_count=?, followers_count=?,	"
		 		+ " listed_count=?, name=?, description=?, url=?, screen_name=?, "
		 		+ " statuses_count=?, is_verified=?, user_id=?, lang=?, user_location=?, target_user=? "
		 		+ " WHERE user_id = ?";
	     try {
	         stmt = connection.prepareStatement(sql);
	         
	         stmt.setDate(1, (java.sql.Date) new Date(user.getCreatedAt().getTime()));
	         stmt.setInt(2, user.getFavouritesCount());
	         stmt.setInt(3, user.getFriendsCount());
	         stmt.setInt(4, user.getFollowersCount());
	         stmt.setInt(5, user.getListedCount());
	         stmt.setString(6, user.getName());
	         stmt.setString(7, user.getDescription());
	         stmt.setString(8, user.getUrl());
	         stmt.setString(9, user.getScreenName());
	         stmt.setInt(10, user.getStatusesCount());
	         stmt.setBoolean(11, user.isVerified());
	         stmt.setLong(12, user.getIDUser());
	         stmt.setString(13, user.getLanguage());
	         stmt.setString(14, user.getLocation());
	         stmt.setLong(15, user.getIDUser());
	         stmt.setBoolean(15, user.isTargetUser());
	         
	         stmt.execute();
	         stmt.close();
	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
		 
	 }
	 
	 public void delete(UserAccount user){
		 
		 PreparedStatement stmt;
		 String sql = "DELETE FROM \"TB_User\" WHERE user_id = ?";
		 try {
	         stmt = connection.prepareStatement(sql);
	         
	         stmt.setLong(1, user.getIDUser());
	         stmt.execute();
	         stmt.close();
	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
		 
	 }
	 
	 public void shutdownConnetion(){
		 try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}