package persistence.test;

import java.util.ArrayList;

import similarity.TweetsTreater;
import twitter.tracker.hibernate.DataSetAnnotator;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class Main {
	
	public static void main(String args[]){
			
		String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
		
		//Metrics metrics = new Metrics("db-twitter-ordinary");
	
		TweetsTreater treater = new TweetsTreater();
		DataSetAnnotator annotator = new DataSetAnnotator(ontologyIRI);
		//DataSetExtractor extractor = new DataSetExtractor("db-twitter-ordinary");
		
		java.util.List<User> twitterList = new ArrayList<User>();
		java.util.List<String> users = new ArrayList<String>();
		Twitter twitter = TwitterFactory.getSingleton();
//		try {
//			Paging page = new Paging();
//			page.setCount(200);
////		
////			
////			ResponseList<Status> tweets = twitter.getUserTimeline("mitchbr91", page);
////			
////			int i = 1;
////			String s = "";
////			
////			
////			for(Status tweet: tweets){
////				
////				s = tweet.getText();				
////				s = treater.eliminateEmojis(s);			    
////			    System.out.println(i++ + " - Tweet: " + s);			    
////								
////			}
//			
////			users.add("Jukelmer");
////			users.add("NeuvooItaja");
////			users.add("Valsilva111");
////			users.add("marcelolinz");
////			users.add("SimoneGhetti");
////			
////			for(String user: users){
////				twitterList.add(twitter.showUser(user));
////			}
////			
////			extractor.extractUsersAndFollowees(twitterList);
//				
////			users = daoUser.listUsers(true);
////			
////			for(UserAccount user: users){
////				twitterList.add(twitter.showUser(user.getScreenName()));
////			}
//			
//			twitterList.add("TVNBR");
//			twitterList.add("M02a0810");
//			twitterList.add("pedrodash");
//			twitterList.add("thundergatu");
//			twitterList.add("MzSeager");
//			twitterList.add("SethRollins_1");
//			twitterList.add("CHNGymnastics");
//			twitterList.add("HectorLombard");
//			
//			for(String user: twitterList){
//				System.out.println(user + " - Tweets size: " + twitter.getUserTimeline(user, page).size());
//			}
//			
//			
//			
//		
////			UserAccount u = daoUser.getUserByScreenName("mitchbr91");
////			
////			SimilarityManager sm = new SimilarityManager();							
////		
////			metrics.calculateMetrics(10, sm.calculateSimilarity());		
//			
//
//		} catch (TwitterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
		
		
//		users.add("Jukelmer");
//		users.add("kamila__sg");
//		users.add("valsilva111");
//		users.add("marcelolinz");
//		users.add("SimoneGhetti");
//		
//		for(String user: users){
//			try {
//				twitterList.add(twitter.showUser(user));
//			} catch (TwitterException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		//extractor.extractTweets();
		
		
//		InferenceManager inferenceManager = new InferenceManager(ontologyIRI);
//		java.util.List<String> targetUsers = new ArrayList<String>();
//		targetUsers.add("mitchbr91");		
//		Map<String, java.util.List<TwitterAccount>> inf = inferenceManager.infer(targetUsers);
//		System.out.println("Objeto inferido: " + inf);
//		metrics.calculateMetrics(10, inf);
		
	
		
		
		
		
		
		
			
	}
		

}
