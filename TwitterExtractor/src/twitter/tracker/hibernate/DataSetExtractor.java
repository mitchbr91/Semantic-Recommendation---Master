package twitter.tracker.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import persistence.dao.hibernate.HashtagDao;
import persistence.dao.hibernate.ListDao;
import persistence.dao.hibernate.TweetDao;
import persistence.dao.hibernate.URLDao;
import persistence.dao.hibernate.UserDao;
import persistence.entities.hibernate.Hashtag;
import persistence.entities.hibernate.Tweet;
import persistence.entities.hibernate.URL;
import persistence.entities.hibernate.UserAccount;
import twitter4j.HashtagEntity;
import twitter4j.HttpResponseCode;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
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
	private	Twitter twitter;
		

	
	public DataSetExtractor(){
		daoTweet = new TweetDao();
		daoUser = new UserDao();
		daoHashtag = new HashtagDao();
		daoURL = new URLDao();	
		daoList = new ListDao();
		twitter = new TwitterFactory().getInstance();
		
	}
	
	public void extractUsersAndFollowees(List<User> twitterList){	
		
		User newUser;	
    	    
    	PagableResponseList<User> users = null;
    	    
    	Iterator<User> it;
    	long cursor = -1;	        
    	UserAccount follower, followee;
	    int i, remainingRateLimit;
    	RateLimitStatus rateStatus;
	    
    	twitter.addRateLimitStatusListener(new RateLimitStatusListener() {
    	    @Override
    	    public void onRateLimitStatus(RateLimitStatusEvent event ) {    	       
    	    }

    	    @Override
    	    public void onRateLimitReached(RateLimitStatusEvent event ) {

    	        try {
   	        		System.out.println ("Hora de dormir");
   	        		Thread.sleep(15*60*1000); } 
   	       		catch (InterruptedException ex) {  
   	       			System.out.println ("Puxa, estava dormindo! Você me acordou");    	       		
   	       		}
    	    }
    	} );
    	
	    int total = 0;
	    for(User page: twitterList){
	        	
	       	cursor = -1;
	       	i = 1;
	       
	       	follower = new UserAccount(page.getId(), page.getCreatedAt(),
	   	       		page.getFavouritesCount(), page.getFriendsCount(), page.getFollowersCount(),
	   	       		page.getLang(), page.getLocation(),page.getListedCount(), page.getName(), 
	   	       		page.getDescription(), page.getURL(), page.getScreenName(), 
	   	       		page.getStatusesCount(), page.isVerified(), true);
	   	    
	       	   	
			   	
		   	try{
   	        		
		   		do{
		   			
		   			rateStatus = twitter.getRateLimitStatus().get("/friends/list");
		   			remainingRateLimit = rateStatus.getRemaining(); 		   			   	
		   			
			        users = twitter.getFriendsList(page.getScreenName(), cursor, 200); 
			        
			        total += users.size();
			        	
			        
			   	    it = users.iterator();
			   	    while(it.hasNext())
			   	    {
			   	       	newUser = it.next();
				   	        	
			   	       	followee = new UserAccount(newUser.getId(), newUser.getCreatedAt(),
			   					newUser.getFavouritesCount(), newUser.getFriendsCount(), newUser.getFollowersCount(),
			   					newUser.getLang(), newUser.getLocation(),newUser.getListedCount(), newUser.getName(), 
			   					newUser.getDescription(), newUser.getURL(), newUser.getScreenName(), 
			   					newUser.getStatusesCount(), newUser.isVerified(), false);
				   	       	
			   	       	daoUser.insertUser(followee);
			   	       	
			   	       	// Inserting followee into user
			   	       	follower.addFollowee(followee);
				   	        	
			   	    }
		   				   	       
		  	        	
		        }while((cursor = users.getNextCursor()) != 0);
			   		
   	       	}catch(TwitterException te){   	        	
   	       		te.printStackTrace();   	        
   	        }  	
	   	   
		   	daoUser.insertUser(follower);	
		   
	   }   
	    
		System.out.println("Total de usuários recuperados: " + total); 
	     
		
	}	
	
	public void extractTweets(){
		
		Paging page = new Paging();
		page.count(50);
		int remainingRateLimit;
		RateLimitStatus rateStatus;
		ResponseList<Status> tweets;
		List<UserAccount> twitterList = new ArrayList<UserAccount>();
		
		twitter.addRateLimitStatusListener(new RateLimitStatusListener() {
    	    @Override
    	    public void onRateLimitStatus(RateLimitStatusEvent event ) {    	       
    	    }

    	    @Override
    	    public void onRateLimitReached(RateLimitStatusEvent event ) {

    	        try {
   	        		System.out.println ("Hora de dormir");
   	        		Thread.sleep(15*60*1000); } 
   	       		catch (InterruptedException ex) {  
   	       			System.out.println ("Puxa, estava dormindo! Você me acordou");    	       		
   	       		}
    	    }
    	} );
		
		for(UserAccount user: daoUser.listUsers(true, false)){
			
			if(!user.tweetsCollected()){
				if(!twitterList.contains(user))
					twitterList.add(user);
			}
				
			
			for(UserAccount followee: user.getFollowees()){
				
				if(!followee.tweetsCollected()){
					if(!twitterList.contains(followee))
						twitterList.add(followee);
				}
					
			}
		}
		
		System.out.println("Twitter list: " + twitterList.size());
		
		int i = 1, j = 1;				
		
		for(UserAccount user: twitterList){
			
			try {
				
				rateStatus = twitter.getRateLimitStatus().get("/statuses/user_timeline");
				remainingRateLimit = rateStatus.getRemaining();
				
				tweets = twitter.getUserTimeline(user.getScreenName(), page);	
					
				System.out.println(j++ + " - " +  user.getScreenName());
				extractTweets(tweets, user.isTargetUser());
					
				// Tweets' collection has been succesfully done
				daoUser.updateTweetsCollection(user.getIDUser());
				System.out.println("Remaining rate limit: " + remainingRateLimit + " - Seconds to reset: " + rateStatus.getSecondsUntilReset());
			
				
			} catch (TwitterException e) {
				// do not throw if user has protected tweets, or if they deleted their account
		        if (e.getStatusCode() == HttpResponseCode.UNAUTHORIZED ||
		            e.getStatusCode() == HttpResponseCode.NOT_FOUND) {

		        	daoUser.deleteUser(user.getIDUser());
		            System.out.println("Coleta de tweets não autorizada: " + user.getScreenName() + " - Usuário deletado!!!");
		        }
		        else {
		            e.printStackTrace();
		        }
			}
		}
	}
	
	/**
	 * These method receives as parameters the list of tweets to extract 
	 * @param tweets
	 * @param favoriterID
	 */
	private void extractTweets(ResponseList<Status> tweets, boolean targetUser){
		
		Status tweet = null;
		twitter4j.User newUser = null;		
		UserAccount tweetPublisher = null;		
		
		for(Status s: tweets){
			
			tweet = s;
			
			// Tweet is a RETWEET
			if(tweet.isRetweet()){
				newUser = tweet.getUser();			
				tweet = s;
				
				//-------------------- Insert retweet publisher
				
				tweetPublisher = new UserAccount(newUser.getId(), newUser.getCreatedAt(),
						newUser.getFavouritesCount(), newUser.getFriendsCount(), newUser.getFollowersCount(),
						newUser.getLang(), newUser.getLocation(),newUser.getListedCount(), newUser.getName(), 
						newUser.getDescription(), newUser.getURL(), newUser.getScreenName(), 
						newUser.getStatusesCount(), newUser.isVerified(), false);
				
				daoUser.insertUser(tweetPublisher);
				
				
				//------------------- Insert retweet
				
				Tweet retweet = new Tweet(tweet.getId(), tweet.getCreatedAt(), tweet.getText().toLowerCase(), tweet.getLang()
						,tweet.getRetweetCount(), tweet.isRetweet(), tweet.getInReplyToUserId(),
						tweet.getInReplyToStatusId(), tweet.getInReplyToScreenName(), 
						tweet.getFavoriteCount(), tweetPublisher);
				
				
				//----------------- Creating Mentions
				
				UserAccount userMentioned;
				for(UserMentionEntity mention: tweet.getUserMentionEntities()){
					
					if(!mention.getScreenName().equalsIgnoreCase(tweet.getUser().getScreenName()) 
							&& !mention.getScreenName().equalsIgnoreCase(s.getRetweetedStatus().getUser().getScreenName())){
						
						userMentioned = new UserAccount(mention.getId(), null, 0, 0, 0, 
								"", "", 0, mention.getName(), "", "", mention.getScreenName(), 0, false, false);
						
						daoUser.insertUser(userMentioned);
						
						//-------------- Inserting mention into tweet							
						retweet.addMention(userMentioned);
						
					}
				
				}	
				
				//----------------- Creating hashtags
				
				if(tweet.getHashtagEntities().length > 0){
					
					Hashtag hashtag;						
					for(HashtagEntity h: tweet.getHashtagEntities()){	
						
						// Creating hashtag
						hashtag = daoHashtag.insertHashtag(new Hashtag(h.getText().toLowerCase()));					   		
				   		 
				   		//Updating Tweet/Hashtag table
				   		retweet.addHashtag(hashtag);
						
					}	
					
				}
				
				//----------------- Creating urls
									
				if(tweet.getURLEntities().length > 0){
					
					URL url;						
					for(URLEntity u: tweet.getURLEntities()){														   
													
						// Creating URL
						url = daoURL.insertURL(new URL(u.getText()));
													
						//Updating Tweet/URL table
						retweet.addURL(url);
					}
					
				}
				
				
				daoTweet.insertTweet(retweet);
					
				
				if(targetUser){
					
					tweet = s.getRetweetedStatus();
					newUser = tweet.getUser();  
					
					//------------------- Insert retweeter
					
					UserAccount retweeter = new UserAccount(s.getUser().getId(), tweet.getUser().getCreatedAt(),
							tweet.getUser().getFavouritesCount(), tweet.getUser().getFriendsCount(), tweet.getUser().getFollowersCount(),
							tweet.getUser().getLang(), tweet.getUser().getLocation(),tweet.getUser().getListedCount(), tweet.getUser().getName(), 
							tweet.getUser().getDescription(), tweet.getUser().getURL(), tweet.getUser().getScreenName(), 
							tweet.getUser().getStatusesCount(), tweet.getUser().isVerified(), false);


					daoUser.insertUser(retweeter);			
					
					//------------------- Insert user that posted the tweet
					
					tweetPublisher = new UserAccount(newUser.getId(), newUser.getCreatedAt(),
							newUser.getFavouritesCount(), newUser.getFriendsCount(), newUser.getFollowersCount(),
							newUser.getLang(), newUser.getLocation(),newUser.getListedCount(), newUser.getName(), 
							newUser.getDescription(), newUser.getURL(), newUser.getScreenName(), 
							newUser.getStatusesCount(), newUser.isVerified(), false);
					
					daoUser.insertUser(tweetPublisher);
					
					//------------------- Insert the original tweet
					
					Tweet orignalPost = new Tweet(tweet.getId(), tweetPublisher);  
					
					daoTweet.insertTweet(orignalPost);
					
					//-------------- Insert retweet into retweets set
					daoUser.insertRetweet(retweeter.getIDUser(), orignalPost);
				}
				
				
				
			}else{
				
				if((tweet.getInReplyToStatusId() != -1) || (tweet.getInReplyToStatusId() == -1 && tweet.getInReplyToUserId() == -1)
						||  (tweet.getInReplyToStatusId() == -1 && tweet.getInReplyToScreenName() != null)){
					
					tweet = s;
					newUser = tweet.getUser();
					
					//-------------------- Insert tweet publisher
					
					tweetPublisher = new UserAccount(newUser.getId(), newUser.getCreatedAt(),
							newUser.getFavouritesCount(), newUser.getFriendsCount(), newUser.getFollowersCount(),
							newUser.getLang(), newUser.getLocation(),newUser.getListedCount(), newUser.getName(), 
							newUser.getDescription(), newUser.getURL(), newUser.getScreenName(), 
							newUser.getStatusesCount(), newUser.isVerified(), false);
					
					daoUser.insertUser(tweetPublisher);
					
					//------------------- Insert tweet
					
					Tweet t = new Tweet(tweet.getId(), tweet.getCreatedAt(), tweet.getText().toLowerCase(), tweet.getLang()
							,tweet.getRetweetCount(), tweet.isRetweet(), tweet.getInReplyToUserId(),
							tweet.getInReplyToStatusId(), tweet.getInReplyToScreenName(), 
							tweet.getFavoriteCount(), tweetPublisher);		
					
					
					// Tweet is a REPLY
					if(tweet.getInReplyToStatusId() != -1 && targetUser){					
						
						if(!s.getUser().getScreenName().equals(s.getInReplyToScreenName())){
												
							
							UserAccount repliedUser = new UserAccount(s.getInReplyToUserId(), null,
									0, 0, 0, "", "", 0, "", 
									"", "", s.getInReplyToScreenName(),
									0, false, false);
							
							Tweet repliedTweet = new Tweet(s.getInReplyToStatusId(), repliedUser);
													
							//Adding user that was replied
							daoUser.insertUser(repliedUser);
							
							//Adding tweet that was replied
							daoTweet.insertTweet(repliedTweet);
							
							//-------------- Setting the replies set		
							daoUser.insertReply(tweetPublisher.getIDUser(), repliedTweet);
								
						}
					
						
					}
					
					// Tweet is a POST
					//----------------- Creating Mentions
					
					UserAccount userMentioned;
					for(UserMentionEntity mention: tweet.getUserMentionEntities()){
						
						if(!mention.getScreenName().equalsIgnoreCase(tweet.getUser().getScreenName())
								&& !mention.getScreenName().equalsIgnoreCase(tweet.getInReplyToScreenName())){
							
							userMentioned = new UserAccount(mention.getId(), null, 0, 0, 0, 
									"", "", 0, mention.getName(), "", "", mention.getScreenName(), 0, false, false);
							
							daoUser.insertUser(userMentioned);
							
							//-------------- Inserting mention into tweet							
							t.addMention(userMentioned);
							
						}
					
					}	
					
					
					
					//----------------- Creating hashtags
					
					if(tweet.getHashtagEntities().length > 0){
						
						Hashtag hashtag;						
						for(HashtagEntity h: tweet.getHashtagEntities()){	
							
							// Creating hashtag
							hashtag = daoHashtag.insertHashtag(new Hashtag(h.getText().toLowerCase()));					   		
					   		 
					   		//Updating Tweet/Hashtag table
					   		t.addHashtag(hashtag);
							
						}	
						
					}
					
					//----------------- Creating urls
										
					if(tweet.getURLEntities().length > 0){
						
						URL url;						
						for(URLEntity u: tweet.getURLEntities()){														   
														
							// Creating URL
							url = daoURL.insertURL(new URL(u.getText()));
														
							//Updating Tweet/URL table
							t.addURL(url);
						}
						
					}
									
					// Insert tweet
					daoTweet.insertTweet(t);
				}
					
			}
				
						
		}
				
		
	}

	public void extractFavorites(){			
	
		ResponseList<Status> tweets;
		int remainingRateLimit;
		RateLimitStatus rateStatus;
		Paging page = new Paging();
		page.setCount(50);
				
		
		twitter.addRateLimitStatusListener(new RateLimitStatusListener() {
    	    @Override
    	    public void onRateLimitStatus(RateLimitStatusEvent event ) {    	       
    	    }

    	    @Override
    	    public void onRateLimitReached(RateLimitStatusEvent event ) {

    	        try {
   	        		System.out.println ("Hora de dormir");
   	        		Thread.sleep(15*60*1000); } 
   	       		catch (InterruptedException ex) {  
   	       			System.out.println ("Puxa, estava dormindo! Você me acordou");    	       		
   	       		}
    	    }
    	} );
						
		try {			
			
			int j = 0;
			for(UserAccount favoriter: daoUser.listUsers(true, false)){
				
				rateStatus = twitter.getRateLimitStatus().get("/favorites/list");
				remainingRateLimit = rateStatus.getRemaining();			
				
				
				tweets = twitter.getFavorites(favoriter.getScreenName(), page);
					
				
				// Tweets creation
				System.out.println(j++ + " - " +  favoriter.getScreenName());
				extractTweets(tweets, false);
						
				twitter4j.User newUser = null;		
				UserAccount tweetPublisher = null;
				Tweet favorite;
					
				for(Status tweet: tweets){
						
					newUser = tweet.getUser();
					tweetPublisher = new UserAccount(newUser.getId(), newUser.getCreatedAt(),
							newUser.getFavouritesCount(), newUser.getFriendsCount(), newUser.getFollowersCount(),
							newUser.getLang(), newUser.getLocation(),newUser.getListedCount(), newUser.getName(), 
							newUser.getDescription(), newUser.getURL(), newUser.getScreenName(), 
							newUser.getStatusesCount(), newUser.isVerified(), false);
							
					favorite = new Tweet(tweet.getId(), tweet.getCreatedAt(), tweet.getText().toLowerCase(), tweet.getLang()
							,tweet.getRetweetCount(), tweet.isRetweet(), tweet.getInReplyToUserId(),
							tweet.getInReplyToStatusId(), tweet.getInReplyToScreenName(), 
							tweet.getFavoriteCount(), tweetPublisher);
							

					//Adding favorites in user
					daoUser.insertFavorite(favoriter.getIDUser(), favorite);
							
				}
				
				System.out.println("Remaining rate limit: " + remainingRateLimit + " - Seconds to reset: " + rateStatus.getSecondsUntilReset());
				
				
			}
			
			
				
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
				
	}
	
	public void extractLists(){
		
		ResponseList<UserList> lists;
		RateLimitStatus rateStatus;
		int remainingRateLimit;
		
		persistence.entities.hibernate.List l;
		
		twitter.addRateLimitStatusListener(new RateLimitStatusListener() {
    	    @Override
    	    public void onRateLimitStatus(RateLimitStatusEvent event ) {    	       
    	    }

    	    @Override
    	    public void onRateLimitReached(RateLimitStatusEvent event ) {

    	        try {
   	        		System.out.println ("Hora de dormir");
   	        		Thread.sleep(15*60*1000); } 
   	       		catch (InterruptedException ex) {  
   	       			System.out.println ("Puxa, estava dormindo! Você me acordou");    	       		
   	       		}
    	    }
    	} );
		
		for(UserAccount listOwner: daoUser.listUsers(true, false)){
			
			try {
				
				rateStatus = twitter.getRateLimitStatus().get("/lists/list");
				remainingRateLimit = rateStatus.getRemaining();
				
				
				lists = twitter.getUserLists(listOwner.getScreenName());				
					
				//Garantir que as listas pertencem aos usuários alvos. Para tanto, filtrar dentro do for!!!
				System.out.println("List Owner: " + listOwner.getScreenName());
				for(UserList list: lists){			
					
					if (listOwner.getScreenName().equalsIgnoreCase(list.getUser().getScreenName())){
						System.out.println("ListOwner - " + list.getUser().getScreenName() + " ----- list - " + list.getName());
						listOwner = daoUser.getUser(list.getUser().getId(), false);
						
						l = new persistence.entities.hibernate.List(list.getId(), list.getName(), list.getDescription(), listOwner);
							
						//Extract members from list and insert them into it
						extracListsMembers(l);
							
						//Persist list
						daoList.insertList(l);		
					}
												
				}
				
				
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
	}
		
	private void extracListsMembers(persistence.entities.hibernate.List list){
		
		PagableResponseList<User> members = null;
		int remainingRateLimit;
		RateLimitStatus rateStatus;
		long cursor = -1;
		UserAccount user;
		
		twitter.addRateLimitStatusListener(new RateLimitStatusListener() {
    	    @Override
    	    public void onRateLimitStatus(RateLimitStatusEvent event ) {    	       
    	    }

    	    @Override
    	    public void onRateLimitReached(RateLimitStatusEvent event ) {

    	        try {
   	        		System.out.println ("Hora de dormir");
   	        		Thread.sleep(15*60*1000); } 
   	       		catch (InterruptedException ex) {  
   	       			System.out.println ("Puxa, estava dormindo! Você me acordou");    	       		
   	       		}
    	    }
    	} );
				
		try {
				
			do{
				
				rateStatus = twitter.getRateLimitStatus().get("/lists/members");
				remainingRateLimit = rateStatus.getRemaining();
				
				
				members = twitter.getUserListMembers(list.getIDList(), cursor);
			    remainingRateLimit = members.getRateLimitStatus().getRemaining();
				       	
			    for(twitter4j.User member: members){
			    	user = new UserAccount(member.getId(), member.getCreatedAt(),
							member.getFavouritesCount(), member.getFriendsCount(), member.getFollowersCount(),
							member.getLang(), member.getLocation(),member.getListedCount(), member.getName(), 
							member.getDescription(), member.getURL(), member.getScreenName(), 
							member.getStatusesCount(), member.isVerified(), false);
							
							
					 //Creating member's list
					 daoUser.insertUser(user);
													
					 //Adding member into list
					 list.addMember(user); 
				}
				
			  	       	
		    }while((cursor = members.getNextCursor()) != 0);
				
			cursor = -1;
				
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
			
				
	}
	
}
