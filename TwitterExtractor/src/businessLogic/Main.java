package businessLogic;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;




import persistence.dao.UserDao;
import persistence.entities.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;


public class Main {
	
	public static void main(String args[]){
		
//		List<String> twitterList = new ArrayList<String>();		
//        Map<User, ArrayList<User>> users;
//		
//
//        twitterList.add("danielamercury");
//	    twitterList.add("dilmabr");
//	    twitterList.add("mitchbr91");
//	    twitterList.add("Le_Figaro");
//	    twitterList.add("LeNouvelObs");
//	    twitterList.add("lemondefr");
//	    twitterList.add("bbcbrasil");
//	    twitterList.add("UOLNoticias");
//	    twitterList.add("folha");    
//	    twitterList.add("exame_com");
//	    twitterList.add("Pragmatismo_");
//	    twitterList.add("LearnXDGerman");
//	    twitterList.add("ivetesangalo");
//	    twitterList.add("WesleySafadao");
//	    twitterList.add("ClaudiaLeitte");
//	    
//	     String ontologyURI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology#";
//	     //OntologyManagerOWL2 manager = new OntologyManagerOWL2(ontologyURI);
//	     //manager.createData();
//	    
	     Twitter twitter = TwitterFactory.getSingleton();	    
	     
	  
//	    try{
//	    	 
//	    	UserDao daoUser = new UserDao();
//		   
//		     persistence.entities.UserAccount u = daoUser.getUserByID(twitter.showUser("mitchbr91").getId());
//		   
//		     for(Tweet tweet: u.getTweets()){
//		    	 System.out.println("Tweet - Tweet");
//		    	 System.out.println("Text: " + tweet.getText());
//		    	 
//		    	 if(tweet.getMentions().size() > 0){
//		    		System.out.print("Mentions: " );
//		    		for(persistence.entities.UserAccount mention: tweet.getMentions()){
//		    			 System.out.print(mention.getScreenName() + " ");
//		    		}
//		    		 System.out.println();
//		    	 }
//		    	 
//		    	 if(tweet.getHashtags().size() > 0){
//		    		 System.out.print("Hashtags: " );
//			    		for(persistence.entities.Hashtag hashtag: tweet.getHashtags()){
//			    			 System.out.print(hashtag.getHashtag() + " ");
//			    		}
//			    		System.out.println();
//		    	 }
//		    	 
//		    	 if(tweet.getURLs().size() > 0){
//		    		 System.out.print("URLs: " );
//			    		for(persistence.entities.URL url: tweet.getURLs()){
//			    			 System.out.print(url.getUrl() + " ");
//			    		}
//			    		System.out.println();
//		    	 }
//		    	 System.out.println("----------------------------------------");
//		     }	
//		     
//		     for(Tweet retweet: u.getRetweets()){
//		    	 System.out.println("Tweet - Retweet");
//		    	 System.out.println("Text: " + retweet.getText());
//		    	 
//		    	 if(retweet.getMentions().size() > 0){
//		    		System.out.print("Mentions: " );
//		    		for(persistence.entities.UserAccount mention: retweet.getMentions()){
//		    			 System.out.print(mention.getScreenName() + " ");
//		    		}
//		    		 System.out.println();
//		    	 }
//		    	 
//		    	 if(retweet.getHashtags().size() > 0){
//		    		 System.out.print("Hashtags: " );
//			    		for(persistence.entities.Hashtag hashtag: retweet.getHashtags()){
//			    			 System.out.print(hashtag.getHashtag() + " ");
//			    		}
//			    		System.out.println();
//		    	 }
//		    	 
//		    	 if(retweet.getURLs().size() > 0){
//		    		 System.out.print("URLs: " );
//			    		for(persistence.entities.URL url: retweet.getURLs()){
//			    			 System.out.print(url.getUrl() + " ");
//			    		}
//			    		System.out.println();
//		    	 }
//		    	 System.out.println("----------------------------------------");
//		     }
//		     
//		     for(Tweet favorite: u.getFavorites()){
//		    	 System.out.println("Tweet - Favorite");
//		    	 System.out.println("Text: " + favorite.getText());
//		    	 
//		    	 if(favorite.getMentions().size() > 0){
//		    		System.out.print("Mentions: " );
//		    		for(persistence.entities.UserAccount mention: favorite.getMentions()){
//		    			 System.out.print(mention.getScreenName() + " ");
//		    		}
//		    		 System.out.println();
//		    	 }
//		    	 
//		    	 if(favorite.getHashtags().size() > 0){
//		    		 System.out.print("Hashtags: " );
//			    		for(persistence.entities.Hashtag hashtag: favorite.getHashtags()){
//			    			 System.out.print(hashtag.getHashtag() + " ");
//			    		}
//			    		System.out.println();
//		    	 }
//		    	 
//		    	 if(favorite.getURLs().size() > 0){
//		    		 System.out.print("URLs: " );
//			    		for(persistence.entities.URL url: favorite.getURLs()){
//			    			 System.out.print(url.getUrl() + " ");
//			    		}
//			    		System.out.println();
//		    	 }
//		    	 System.out.println("----------------------------------------");
//		     }     
//		     
//		     
//		     for(persistence.entities.UserAccount user: u.getReplies()){
//		    	 System.out.println("Tweet - Reply");
//		    	 System.out.println("User: " + user.getScreenName());
//		    	
//		    	 System.out.println("----------------------------------------");
//		     }
//		     
//		     for(persistence.entities.List list: u.getLists()){
//		    	 System.out.println("List ID: " + list.getIDList());
//		    	 System.out.println("Name: " + list.getName());
//		    	 System.out.println("Description: " + list.getDescription());
//		    	 System.out.println("List Owner screen name: " + list.getListOwner().getScreenName());
//		    	 
//		    	 
//		    	 System.out.println("----------------------------------------");
//		     }
//		     
//		   
//		} catch (TwitterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//	    
//	     try {
//			for(Status s: twitter.getUserTimeline()){
//				if(s.isRetweet()){
//					System.out.println("True");
//					System.out.println(s.getRetweetedStatus().getUser().getScreenName());
//				}									
//				else{					
//					System.out.println("False");
//					System.out.println(s.getUser().getScreenName());
//				}
//				System.out.println("-------------------------------");
//				
//			 }
//		} catch (TwitterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 DataSetExtractor ex = new DataSetExtractor();
//		 
//		 List<TwitterAccount> l = new ArrayList<TwitterAccount>();
//		
//		 Map<String, List<TwitterAccount>> mapInfered;
//		 Map<String, List<String>> mapFollowed;
//		 
//		 mapInfered = new HashMap<String, List<TwitterAccount>>(); //manager.generateMapInfered();
//		 mapFollowed = new HashMap<String, List<String>>(); //manager.generateMapFollowed();
//		 
//		 TwitterAccount t;
//		 List<TwitterAccount> lif = new ArrayList<TwitterAccount>();
//		 
//		 t = new TwitterAccount("u7");
//		 t.setInferedPoints(5);
//		 lif.add(t);
//		 t = new TwitterAccount("u2");
//		 t.setInferedPoints(7);
//		 lif.add(t);
//		 t = new TwitterAccount("u3");
//		 t.setInferedPoints(3);
//		 lif.add(t);
//		 t = new TwitterAccount("u4");
//		 t.setInferedPoints(1);
//		 lif.add(t);
//		 t = new TwitterAccount("u5");
//		 t.setInferedPoints(8);
//		 lif.add(t);
//		 
//		 mapInfered.put("bla", lif);
//		 
//		 String s;
//		 List<String> lof = new ArrayList<String>();
//		 
//		 s = "u1";
//		 lof.add(s);
//		 s = "u2";
//		 lof.add(s);
//		 s = "u3";
//		 lof.add(s);
//		 s = "u4";
//		 lof.add(s);
//		 s = "u5";
//		 lof.add(s);
//		 
//		 mapFollowed.put("bla", lof);
//		 manager.printMapInfered(mapInfered);
//		 System.out.println("---------------------------------------------------");
//		 manager.printMapFollowed(mapFollowed);
//		
//		 
//		 manager.calculateMetrics(0, mapFollowed, mapInfered);		 
				
		 //ex.extractData(twitterList);
//		 ex.extractPages(twitterList);
//		 ex.extractTweets();
//		 ex.extractFavorites();
		 //ex.extractLists();
//		 Map<String, List<Long>> l = ex.extractLists();
//		 ex.extractListMembers(l);
//		 ex.convertIDs("training-file-3");
		 //users = ex.generateFiles(twitterList, 2);
//	     twitterList = ex.extractPages(users);
//	     ex.extractTweets(twitterList);
//	     ex.extractFavorites();
 
//	    SocialNetworkInference s = new SocialNetworkInference();
//	    s.createPages();
//	    s.createTweets();
//	    s.createFavorites();
        
   
	}

}
