package similarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import inference.InferenceManager;
import persistence.dao.hibernate.UserDao;
import persistence.entities.hibernate.RegularRecommendation;
import persistence.entities.hibernate.SemanticRecommendation;
import persistence.entities.hibernate.Tweet;
import persistence.entities.hibernate.UserAccount;
import twitter.tracker.hibernate.SemanticComparatorCosineSimilarity;
import twitter.tracker.hibernate.TAComparatorCosineSimilarity;
import twitter.tracker.hibernate.TwitterAccount;

public class SimilarityManager {
	
	private UserDao daoUser;
	private TweetsTreater treater = new TweetsTreater();
	private TF_IDF tdf_idfCalculator = new TF_IDF();
	private CosineSimilarity cosine = new CosineSimilarity();
	
	public SimilarityManager(){
		daoUser = new UserDao();
	}
	
	public Map<String, java.util.List<TwitterAccount>> calculateSimilarity(int size, boolean semantic){
		
		Map<String, String> normalizedTweets;
		Map<String, java.util.List<Double>> tf_idfs;
		java.util.List<TwitterAccount> unsimilarUsers;
		List<UserAccount> usersWithLowInteraction;
		Map<String, java.util.List<TwitterAccount>> usersToBeUnfollowed = new HashMap<String, java.util.List<TwitterAccount>>();
		Map<String, List<UserAccount>> usersNetwork = null;
		InferenceManager infManager = new InferenceManager();
				
		for(UserAccount user: daoUser.listUsers(true, false)){
			if(semantic)
				usersNetwork = infManager.extractUsersWithNoInteractions();
			else
				usersNetwork = getUsersNetwork();
		}
		
		for(String targetUserScreenname: usersNetwork.keySet()){
			usersWithLowInteraction = usersNetwork.get(targetUserScreenname);
			
			normalizedTweets = normalizeTweets(targetUserScreenname, usersWithLowInteraction);
			System.out.println("Normalized");
			tf_idfs = calculateTF_IDF(targetUserScreenname, normalizedTweets);
			System.out.println("TF-IDF Calculated");
			unsimilarUsers = calculateCosineSimilarity(targetUserScreenname, tf_idfs, size, semantic);
			System.out.println("Cosine similarity calculated");
			
			usersToBeUnfollowed.put(targetUserScreenname, unsimilarUsers);
			
		}
		
		System.out.println("calculateSimilarity - Method");
		
		return usersToBeUnfollowed;
	}
	
	private Map<String, String> normalizeTweets(String targetUserScreenname, List<UserAccount> usersWithLowInteraction){
		
		//-------------------- Get All tweets from the database
		
		Map<String, String> tweets = new HashMap<String, String>();
		//java.util.List<UserAccount> users = daoUser.listUsers(true, true); 
			
		UserAccount targetUser = daoUser.getUserByScreenname(targetUserScreenname, true);
		StringBuilder tweetSet = new StringBuilder();  		

		//------------------- Adding tweets
		for(Tweet tweet: targetUser.getTweets()){
			
			if(tweet.getText() != null){
				if(tweet.getText().startsWith("rt"))
					tweetSet.append(tweet.getText().replaceFirst("rt", "") + " \n");		
				
				tweetSet.append(tweet.getText() + " \n");
			}
			
		}
					
		tweets.put(targetUser.getScreenName(), tweetSet.toString());	
		tweetSet.setLength(0);
		
		int i = 0;
		boolean found = false;
		for(UserAccount followee: targetUser.getFollowees()){
			
			while(i < usersWithLowInteraction.size() && !found){
				if(followee.getScreenName().equalsIgnoreCase(usersWithLowInteraction.get(i).getScreenName())){
					found = true;						
				}
				
				i++;
			}
			
			if(found){
				//------------------- Adding tweets
				for(Tweet tweet: followee.getTweets()){
					
					if(tweet.getText() != null){
						if(tweet.getText().startsWith("rt"))
							tweetSet.append(tweet.getText().replaceFirst("rt", "") + " \n");		
						
						tweetSet.append(tweet.getText() + " \n");
					}
					
				}		

				tweets.put(followee.getScreenName(), tweetSet.toString());
			}	
			
			found = false;
			i = 0;		
			tweetSet.setLength(0);
			
		}		
		
			  
		//------------------- Eliminate Emojis, stopwords. Applying stemming
		
		String treatedTweets = "";
		
		String tweet = "";
		for(String key: tweets.keySet()){
			
			tweet = tweets.get(key);			
			
			//------------------ Eliminate stopwords
			treatedTweets = treater.eliminateStopWords(tweet.replaceAll("[^A-Za-zÀ-ú ]", ""));
				
			//------------------ Applying stemming
			treatedTweets = treater.generateStemmedWords(treatedTweets);		
						
			tweets.put(key, treatedTweets);
		}	
		
		return tweets;
	}
		
	private Map<String, java.util.List<Double>> calculateTF_IDF(String targetUserScreenname, Map<String, String> tweets){
		
		//-------------------- Calculate TF-IDF for each tweet
		Map<String, java.util.List<Double>> tdf_idfUsersMap = new HashMap<String, java.util.List<Double>>();
		java.util.List<Double> tf_idfs = null;		
		String tweet = "";
		String targetUserTweets = tweets.get(targetUserScreenname);
		List<String> distinctTerms = distinctTermsGenerator(targetUserTweets);
		//Remove target user entry
		tweets.remove(targetUserScreenname);
		
		System.out.println("DistinctTerms: " + distinctTerms.size());
		System.out.println("Tweets: " + tweets.size());
		List<String> tweetSet = new ArrayList<String>();
		Map<String, Double> idfsTargetUser = new HashMap<String, Double>();
		
		double tdf_idf;
		
		System.out.println("INSIDE");
		//Get the tweets from all users. Each set of tweets from an specific user represents its profile.
		for(String key: tweets.keySet()){
			
			tweet = tweets.get(key);
			tweetSet.add(tweet);					
			
		}
		
		System.out.println("OUTSIDE");
		
		//--------------------- Calculate TF-IDF for target user
		
		double tf = 0, idf = 0;
		tf_idfs = new ArrayList<Double>();
		for(String termToCheck: distinctTerms){

			tf = tdf_idfCalculator.tfCalculator(targetUserTweets, termToCheck);
			idf = tdf_idfCalculator.idfCalculator(tweetSet, termToCheck);
					
			idfsTargetUser.put(termToCheck, idf);
			tf_idfs.add(tf*idf);
							
		}
				
		tdf_idfUsersMap.put(targetUserScreenname, tf_idfs);
	
		
		//-------------------- Calculating TF_IDF for followees
		for(String key: tweets.keySet()){
			
			tweet = tweets.get(key);	
			tf_idfs = new ArrayList<Double>();
			
			for(String termToCheck: distinctTerms){

				tf = tdf_idfCalculator.tfCalculator(tweet, termToCheck);
				tf_idfs.add(tf*idfsTargetUser.get(termToCheck));	
												
			}
			
			System.out.println("Before: " + key);
			tdf_idfUsersMap.put(key, tf_idfs);	
			System.out.println("After: " + key);
		}
		
		System.out.println("Distinct terms size: " + distinctTerms.size());
		System.out.println("Users size: " + tweets.keySet().size());
		System.out.println("WOW");
		
		return tdf_idfUsersMap;
	}
	
	private List<String> distinctTermsGenerator(String tweet){
				
		List<String> distinctTerms = new ArrayList<String>();
		StringTokenizer st;
		String termToCheck;
	
		st = new StringTokenizer(tweet);
				
			while(st.hasMoreElements()){
					
				termToCheck = (String) st.nextElement();
					
				if(!distinctTerms.contains(termToCheck)){
					distinctTerms.add(termToCheck);
				}
			}
			
		
		
		return distinctTerms;
	}
	
	private java.util.List<TwitterAccount> calculateCosineSimilarity(String targetUserScreenname, Map<String, java.util.List<Double>> tf_idfUsersMap, int size, boolean semantic){
	
		//----------------------- Apply cosine similitary function
		
		java.util.List<Double> tf_idf2 = new ArrayList<Double>();	
		java.util.List<TwitterAccount> similarity = null;
		UserDao daoUser = new UserDao();
		java.util.List<Double> tf_idfTargetUser;
		
		double[] vet1;
		double[] vet2;
		
		tf_idfTargetUser = tf_idfUsersMap.get(targetUserScreenname);
		tf_idfUsersMap.remove(targetUserScreenname);
	
		TwitterAccount twitterAccount;
		similarity = new ArrayList<TwitterAccount>();
		for(String user: tf_idfUsersMap.keySet()){
			
			
			tf_idf2 = tf_idfUsersMap.get(user);				
		
			vet1 = tdf_idfCalculator.convertListToArray(tf_idfTargetUser);
			vet2 = tdf_idfCalculator.convertListToArray(tf_idf2);
			
			twitterAccount = new TwitterAccount(user);
			twitterAccount.setCosineSimilarity(cosine.cosineSimilarity(vet1,vet2));
			
			similarity.add(twitterAccount);

			
		}
	
		// I need to take the n followees with lowest value of cosine similarity
		List<TwitterAccount> unsimilarUsers = new ArrayList<TwitterAccount>();
		similarity.sort(new TAComparatorCosineSimilarity());
		for (int i = 0; i < size; i++){
			unsimilarUsers.add(similarity.get(i));
		}
		
		UserAccount tUser = daoUser.getUserByScreenname(targetUserScreenname, false);
		UserAccount recommendation;
		if(semantic){
			SemanticRecommendation semanticRecommendation;
			
			for(TwitterAccount ta: unsimilarUsers){
				recommendation = daoUser.getUserByScreenname(ta.getName(), false);				
				semanticRecommendation = new SemanticRecommendation(tUser, recommendation, ta.getCosineSimilarity());
				daoUser.insertSemanticRecommendation(tUser.getIDUser(), semanticRecommendation);
			}
		}else{
			RegularRecommendation regularRecommendation;
			
			for(TwitterAccount ta: unsimilarUsers){
				recommendation = daoUser.getUserByScreenname(ta.getName(), false);				
				regularRecommendation = new RegularRecommendation(tUser, recommendation, ta.getCosineSimilarity());
				daoUser.insertRegularRecommendation(tUser.getIDUser(), regularRecommendation);
			}
		}
		
		return unsimilarUsers;
	}
	
	private java.util.List<TwitterAccount> calculatePearsonsCorrelation(String targetUserScreenname, Map<String, java.util.List<Double>> tf_idfUsersMap){
		
		//----------------------- Apply Pearsons Correlation function		
		
		java.util.List<Double> tdf_idfs2 = new ArrayList<Double>();		
		java.util.List<TwitterAccount> unsimilarUsers;
		
		java.util.List<Double> tf_idfTargetUser;
		
		double[] vet1;
		double[] vet2;
		PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
				
		tf_idfTargetUser = tf_idfUsersMap.get(targetUserScreenname);
		tf_idfTargetUser.remove(targetUserScreenname);		
		
		unsimilarUsers = new ArrayList<TwitterAccount>();
		TwitterAccount twitterAccount;
		for(String user: tf_idfUsersMap.keySet()){
			tdf_idfs2 = tf_idfUsersMap.get(user);
			
			vet1 = tdf_idfCalculator.convertListToArray(tf_idfTargetUser);
			vet2 = tdf_idfCalculator.convertListToArray(tdf_idfs2);
			
			twitterAccount = new TwitterAccount(user);
			twitterAccount.setPearsonsCorrelation(pearsonsCorrelation.correlation(vet1, vet2));
			
			unsimilarUsers.add(twitterAccount);			
			
		}
				
		return unsimilarUsers;
		
	}
	
	private java.util.List<TwitterAccount> calculateEuclideanDistance(String targetUserScreenname, Map<String, java.util.List<Double>> tf_idfUsersMap){
		//----------------------- Apply Euclidean Distance function
		
		java.util.List<Double> tdf_idfs2 = new ArrayList<Double>();		
		java.util.List<TwitterAccount> unsimilarUsers = new ArrayList<TwitterAccount>();
		
		java.util.List<Double> tdf_idfsTargetUser;
		
		double[] vet1;
		double[] vet2;
				
		EuclideanDistance euclideanDistance = new EuclideanDistance();
		
		tdf_idfsTargetUser = tf_idfUsersMap.get(targetUserScreenname);
		tdf_idfsTargetUser.remove(targetUserScreenname);
		
		TwitterAccount twitterAccount;
		for(String user: tf_idfUsersMap.keySet()){
			tdf_idfs2 = tf_idfUsersMap.get(user);
			
			vet1 = tdf_idfCalculator.convertListToArray(tdf_idfsTargetUser);
			vet2 = tdf_idfCalculator.convertListToArray(tdf_idfs2);
			
			twitterAccount = new TwitterAccount(user);
			twitterAccount.setEuclideanDistance(euclideanDistance.compute(vet1, vet2));
			
			unsimilarUsers.add(twitterAccount);
			
		}
					
		return unsimilarUsers;
	
	}
	
	/**
	 * Return for each target user the list of his/her followees. This set will be used to calculate similarity between a user 
	 * and his followees without analyse semantic interaction between users.
	 * @return
	 */
	public Map<String, List<UserAccount>> getUsersNetwork(){
		
		Map<String, List<UserAccount>> usersNetwork = new HashMap<String, List<UserAccount>>();
		
		List<UserAccount> followees; 
		for(UserAccount targetUser: daoUser.listUsers(true, false)){
			
			followees = new ArrayList<UserAccount>();
			for(UserAccount followee: targetUser.getFollowees()){
				followees.add(followee);
			}
			
			usersNetwork.put(targetUser.getScreenName(), followees);			
			
		}
		
		return usersNetwork;
	}
			

}
