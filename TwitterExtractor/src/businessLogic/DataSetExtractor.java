package businessLogic;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import persistence.dao.HashtagDao;
import persistence.dao.ListDao;
import persistence.dao.TweetDao;
import persistence.dao.URLDao;
import persistence.dao.UserDao;
import persistence.entities.Tweet;
import persistence.entities.UserAccount;
import twitter4j.HashtagEntity;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserMentionEntity;

public class DataSetExtractor {
	
	private TweetDao daoTweet;
	private UserDao daoUser;
	private HashtagDao daoHashtag;
	private URLDao daoURL;	
	private ListDao daoList;	
	private BufferedWriter out, outAppendDatasetTwitter, outAppendTweets;
	
	public DataSetExtractor(){
		
		String s = "";
		
		daoTweet = new TweetDao(s);
		daoUser = new UserDao(s);
		daoHashtag = new HashtagDao(s);
		daoURL = new URLDao(s);	
		daoList = new ListDao(s);
	}
	
//	private Map<User, List<User>> extractUsersAndFollowees(List<String> twitterList){
//		
//		Map<User, List<User>> usersInMemory = null;
//		Map<String, List<String>> usersFolloweeNetwork = null;
//		
//		User id, u;	 
//		try {
//            Twitter twitter = new TwitterFactory().getInstance();                        
//              
//            Map<String, User> usersSet = new HashMap<String,User>();
//            usersInMemory = new HashMap<User, List<User>>();  
//            usersFolloweeNetwork = new HashMap<String, List<String>>();
//    	
//            out = new BufferedWriter(new FileWriter("targetUsers"));             
//    	    for(String page: twitterList){
//    	    	u = twitter.showUser(page);
//    	    	usersSet.put(page, u);
//    	    	out.write(page);
//    	    }
//    	    out.close();
//    	    
//    	    PagableResponseList<User> users;
//    	    
//    	    Iterator<User> it;
//    	    int cursor = -1;	        
//	              
//	        
//	        List<User> usersList;
//	  	    List<String> twitterAccountList;
//	        
//	        for(String page: twitterList){
//	        	users = twitter.getFriendsList(page, cursor, 1);	        	
//	   	        it = users.iterator();   
//	   	        usersList = new ArrayList<User>();
//	   	        twitterAccountList = new ArrayList<String>();
//	   	        
//	   	        while(it.hasNext())
//	   	        {
//	   	          	u = it.next();
//
//	   	          		
//	   	          		if(!usersSet.containsKey(u.getScreenName())){
//	   	          			id = u;
//	   	          			usersSet.put(u.getScreenName(), id);	 
//	   	          			
//	   	          		}else{
//	   	          			id = usersSet.get(u.getScreenName());
//	   	          		}
//	   	          		
//	   	          	    System.out.println(usersSet.get(page).getId() + " " + page + "  " + u.getName() + "  " + id.getId());
//	   	          		usersList.add(id);
//	   	          		twitterAccountList.add(id.getScreenName());
//	   	        }
//	   	      
//	   	        usersInMemory.put(usersSet.get(page), usersList);
//	   	        usersFolloweeNetwork.put(usersSet.get(page).getScreenName(), twitterAccountList);
//	        }
//	   	        	
//	        ObjectOutputStream objectOut;
//			try {
//				objectOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("UsersFolloweeNetwork.txt")));
//				objectOut.writeObject(usersFolloweeNetwork);  
//		        objectOut.close(); 
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}              
//	        
//	             
//        } catch (TwitterException te) {
//            te.printStackTrace();
//            System.out.println("Failed to get friends' ids: " + te.getMessage());
//            System.exit(-1);        
//        } catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}		
//		
//		return usersInMemory;
//	}	
//	
//	
//	/**
//	 * These method receives as parameters the list of tweets to extract 
//	 * @param tweets
//	 * @param favoriterID
//	 */
//	public void extractTweets(ResponseList<Status> tweets){
//		
//		Status tweet = null;
//		twitter4j.User newUser = null;		
//		UserAccount tweetPublisher = null;
//		
//				
//		for(Status s: tweets){
//			
//			tweet = s;
//			
//			if(s.isRetweet()){
//				newUser = s.getRetweetedStatus().getUser();													
//			}else{
//				newUser = s.getUser();				
//			}
//			
//			tweetPublisher = new UserAccount(newUser.getId(), newUser.getCreatedAt(),
//					newUser.getFavouritesCount(), newUser.getFriendsCount(), newUser.getFollowersCount(),
//					newUser.getLang(), newUser.getLocation(),newUser.getListedCount(), newUser.getName(), 
//					newUser.getDescription(), newUser.getURL(), newUser.getScreenName(), 
//					newUser.getStatusesCount(), newUser.isVerified());
//			
//			daoUser.add(tweetPublisher);
//			
//			daoTweet.add(new Tweet(tweet.getId(), tweet.getCreatedAt(), tweet.getText(), tweet.getLang()
//					,tweet.getRetweetCount(), tweet.isRetweet(), tweet.getInReplyToUserId(),
//					tweet.getInReplyToStatusId(), tweet.getInReplyToScreenName(), 
//					tweet.getFavoriteCount(), tweetPublisher));
//								
//		
//			//----------------- Creating Mentions
//			
//			if(s.isRetweet()){						 
//				tweet = s;
//				
//				//Adding retweeter
//				
//				UserAccount retweeter = new UserAccount(tweet.getUser().getId(), tweet.getUser().getCreatedAt(),
//						tweet.getUser().getFavouritesCount(), tweet.getUser().getFriendsCount(), tweet.getUser().getFollowersCount(),
//						tweet.getUser().getLang(), tweet.getUser().getLocation(),tweet.getUser().getListedCount(), tweet.getUser().getName(), 
//						tweet.getUser().getDescription(), tweet.getUser().getURL(), tweet.getUser().getScreenName(), 
//						tweet.getUser().getStatusesCount(), tweet.getUser().isVerified());
//				
//				daoUser.add(retweeter);
//				
//				//-------------- Setting the retweets set
//				daoUser.addIntoUser_RetweetTB(retweeter.getIDUser(), tweet.getId());											
//			
//				for(UserMentionEntity mention: tweet.getUserMentionEntities()){
//					
//					if(!mention.getScreenName().equalsIgnoreCase(tweet.getRetweetedStatus().getUser().getScreenName())){
//						
//						daoUser.add(new UserAccount(mention.getId(), null, 0, 0, 0, "", "", 0, mention.getName(), "", "", mention.getScreenName(), 0, false));
//														
//						//-------------- updating the mentions set
//						daoTweet.addIntoTweet_UserTB(tweet.getId(), mention.getId());								
//					}							
//					 
//				}			
//				
//			}else{
//				if(s.getInReplyToScreenName() != null){
//					
//					tweet = s;
//					
//					if(!s.getUser().getScreenName().equals(s.getInReplyToScreenName())){
//						//Adding user that was replied
//						daoUser.add(new UserAccount(s.getInReplyToUserId(), null,
//								0, 0, 0, "", "", 0, "", 
//								"", "", s.getInReplyToScreenName(),
//								0, false));
//						
//						//-------------- Setting the replies set		
//						daoUser.addIntoUser_ReplyTB(tweetPublisher.getIDUser(), s.getInReplyToUserId());		
//					}
//						
//					
//					for(UserMentionEntity mention: s.getUserMentionEntities()){
//						if(!mention.getScreenName().equalsIgnoreCase(s.getInReplyToScreenName())){
//							daoUser.add(new UserAccount(mention.getId(), null, 0, 0, 0, "", "", 0, mention.getName(), "", "", mention.getScreenName(), 0, false));
//							
//							
//							//-------------- updating the mentions set
//							daoTweet.addIntoTweet_UserTB(s.getId(), mention.getId());
//						}
//					}
//					
//					
//				}else{			
//					
//					tweet = s;
//					
//					for(UserMentionEntity mention: s.getUserMentionEntities()){
//						
//						if(!mention.getScreenName().equalsIgnoreCase(s.getUser().getScreenName())){
//							
//							daoUser.add(new UserAccount(mention.getId(), null, 0, 0, 0, "", "", 0, mention.getName(), "", "", mention.getScreenName(), 0, false));
//							
//							//-------------- updating the mentions set
//							daoTweet.addIntoTweet_UserTB(s.getId(), mention.getId());
//						}
//					
//					}	
//				}
//			}
//			
//			//----------------- Creating hashtags
//			
//			if(tweet.getHashtagEntities().length > 0){
//				
//				for(HashtagEntity hashtag: tweet.getHashtagEntities()){	
//					
//					// Creating hashtag
//			   		daoHashtag.add(hashtag.getText().toLowerCase());
//			   		 
//			   		//Updating Tweet/Hashtag table
//			   		daoTweet.addIntoTweet_HashtagTB(tweet.getId(), daoHashtag.getHashtagByName(hashtag.getText().toLowerCase()));
//					
//				}	
//				
//			}
//			
//			//----------------- Creating urls
//								
//			if(tweet.getURLEntities().length > 0){
//				
//				for(URLEntity url: tweet.getURLEntities()){														   
//					
//					// Creating URL
//					daoURL.add(url.getText());
//					
//					//Updating Tweet/URL table
//					daoTweet.addIntoTweet_URLTB(tweet.getId(), daoURL.getURLByName(url.getText()));
//				}
//				
//			}			
//		}
//	}
//
//	public void extractFavorites(ResponseList<Status> tweets, twitter4j.User favoriter){
//				
//		// Tweets creation
//		extractTweets(tweets);
//		
//		// Favoriter's creation		
//		daoUser.add(new UserAccount(favoriter.getId(), favoriter.getCreatedAt(),
//				favoriter.getFavouritesCount(), favoriter.getFriendsCount(), favoriter.getFollowersCount(),
//				favoriter.getLang(), favoriter.getLocation(),favoriter.getListedCount(), favoriter.getName(), 
//				favoriter.getDescription(), favoriter.getURL(), favoriter.getScreenName(), 
//				favoriter.getStatusesCount(), favoriter.isVerified()));
//		
//		
//		for(Status tweet: tweets){
//			daoUser.addIntoUser_FavoriteTB(favoriter.getId(), tweet.getId());
//		}
//	}
//	
//	public void extractLists(ResponseList<UserList> lists){
//			
//		UserAccount listOwner;
//		
//		for(UserList list: lists){	
//			
//			listOwner = daoUser.getUserByID(list.getUser().getId());
//			daoList.add(new persistence.entities.List(list.getId(), list.getName(), list.getDescription(), listOwner));					
//		}
//		
//	}
//	
//	public void extractLists(ResponseList<UserList> lists, twitter4j.User listOwner){
//		
//		//List Owner creation
//		daoUser.add(new UserAccount(listOwner.getId(), listOwner.getCreatedAt(),
//				listOwner.getFavouritesCount(), listOwner.getFriendsCount(), listOwner.getFollowersCount(),
//				listOwner.getLang(), listOwner.getLocation(),listOwner.getListedCount(), listOwner.getName(), 
//				listOwner.getDescription(), listOwner.getURL(), listOwner.getScreenName(), 
//				listOwner.getStatusesCount(), listOwner.isVerified()));
//		
//		extractLists(lists);
//	}
//	
//	public void extracListsMembers(long listID, PagableResponseList<User> members){
//		
//		UserAccount user;
//		for(twitter4j.User member: members){
//			user = new UserAccount(member.getId(), member.getCreatedAt(),
//					member.getFavouritesCount(), member.getFriendsCount(), member.getFollowersCount(),
//					member.getLang(), member.getLocation(),member.getListedCount(), member.getName(), 
//					member.getDescription(), member.getURL(), member.getScreenName(), 
//					member.getStatusesCount(), member.isVerified());
//			
//			
//			//Creating member's list
//			daoUser.add(user);
//			
//			//
//			daoList.addIntoList_UserTB(listID, user.getIDUser());
//		}
//				
//	}
}
