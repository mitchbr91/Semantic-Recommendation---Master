package similarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import persistence.dao.hibernate.UserDao;
import persistence.entities.hibernate.Tweet;
import persistence.entities.hibernate.UserAccount;
import twitter.tracker.hibernate.TwitterAccount;

public class SimilarityManager {
	
	private UserDao daoUser;
	private TweetsTreater treater = new TweetsTreater();
	private TF_IDF tdf_idfCalculator = new TF_IDF();
	private CosineSimilarity cosine = new CosineSimilarity();
	
	public SimilarityManager(){
		daoUser = new UserDao();
	}
	
	public Map<String, java.util.List<TwitterAccount>> calculateSimilarity(){
		
		Map<String, String> normalizedTweets;
		Map<String, java.util.List<Double>> tf_idfs;
		Map<String, java.util.List<TwitterAccount>> similarUsers;
				
		normalizedTweets = normalizeTweets();
		System.out.println("Normalized");
		tf_idfs = calculateTF_IDF(normalizedTweets);
		System.out.println("TF-IDF Calculated");
		similarUsers = calculateCosineSimilarity(tf_idfs);
		System.out.println("Cosine similarity calculated");
	
		
//		List<TwitterAccount> inferedUsers;
//		for(String key: similarUsers.keySet()){
//			System.out.println(key);
//			inferedUsers = similarUsers.get(key);
//			
//			for(TwitterAccount inf: inferedUsers){
//				System.out.println("User: " + inf.getName() + " - Cosine Similarity: " + inf.getCosineSimilarity());
//			}
//		}
//				
//		System.out.println("Similar Users: " + similarUsers.size());
		
		return similarUsers;
	}
	
	private Map<String, String> normalizeTweets(){
		
		//-------------------- Get All tweets from the database
		
		Map<String, String> tweets = new HashMap<String, String>();
		java.util.List<UserAccount> users = daoUser.listUsers(true, true); 
			
		StringBuilder tweetSet = new StringBuilder();  		

		for(UserAccount user: users){
			
			//------------------- Adding tweets
			for(Tweet tweet: user.getTweets()){
				
				if(tweet.getText() != null){
					if(tweet.getText().startsWith("rt"))
						tweetSet.append(tweet.getText().replaceFirst("rt", "") + " \n");		
					
					tweetSet.append(tweet.getText() + " \n");
				}
				
			}
						
//			//------------------- Adding retweets
//			for(Tweet tweet: user.getRetweets()){
//				if(tweet.getText().startsWith("rt"))
//					tweetSet.append(tweet.getText().replaceFirst("rt", "") + " \n");		
//				
//				tweetSet.append(tweet.getText() + " \n");		
//			}		
			
			tweets.put(user.getScreenName(), tweetSet.toString());
		
			tweetSet.setLength(0);
			for(UserAccount followee: user.getFollowees()){
				
				//------------------- Adding tweets
				for(Tweet tweet: followee.getTweets()){
					
					if(tweet.getText() != null){
						if(tweet.getText().startsWith("rt"))
							tweetSet.append(tweet.getText().replaceFirst("rt", "") + " \n");		
						
						tweetSet.append(tweet.getText() + " \n");
					}
					
				}
				
//				//------------------- Adding retweets
//				for(Tweet tweet: followee.getRetweets()){
//					if(tweet.getText().startsWith("rt"))
//						tweetSet.append(tweet.getText().replaceFirst("rt", "") + " \n");		
//					
//					tweetSet.append(tweet.getText() + " \n");
//				}	
				
				tweets.put(followee.getScreenName(), tweetSet.toString());
				tweetSet.setLength(0);
			}
			
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
		
	private Map<String, java.util.List<Double>> calculateTF_IDF(Map<String, String> tweets){
		
		//-------------------- Calculate TF-IDF for each tweet
		
		String tweet = "";
		List<String> distinctTerms = distinctTermsGenerator(tweets);
		System.out.println("DistinctTerms: " + distinctTerms.size());
		System.out.println("Tweets: " + tweets.size());
		List<String> tweetSet = new ArrayList<String>();
		
		Map<String, java.util.List<Double>> tdf_idfUsersMap = new HashMap<String, java.util.List<Double>>();
		double tdf_idf;
		java.util.List<Double> tdf_idfs;
		
		System.out.println("INSIDE");
		//Get the tweets from all users. Each set of tweets from an specific user represents its profile.
		for(String key: tweets.keySet()){
			
			tweet = tweets.get(key);
			tweetSet.add(tweet);					
			
		}
		
		System.out.println("OUTSIDE");
		
		int k;
		//-------------------- Calculating TF_IDF
		for(String key: tweets.keySet()){
			
			tweet = tweets.get(key);
	
			tdf_idfs = new ArrayList<Double>();
			
			k = 1;
			for(String termToCheck: distinctTerms){

				tdf_idf = tdf_idfCalculator.TF_IDFCalculator(tweet, tweetSet, termToCheck);						
				tdf_idfs.add(tdf_idf);	
				System.out.println(k++ + " - " + termToCheck);
								
			}
			
			System.out.println("Before: " + key);
			tdf_idfUsersMap.put(key, tdf_idfs);	
			System.out.println("After: " + key);
		}
		
		System.out.println("WOW");
		
		return tdf_idfUsersMap;
	}
	
	private List<String> distinctTermsGenerator(Map<String, String> tweets){
		
		String tweet; 
		List<String> distinctTerms = new ArrayList<String>();
		StringTokenizer st;
		String termToCheck;
		
		for(String key: tweets.keySet()){
			tweet = tweets.get(key);
			
			st = new StringTokenizer(tweet);
				
			while(st.hasMoreElements()){
					
				termToCheck = (String) st.nextElement();
					
				if(!distinctTerms.contains(termToCheck)){
					distinctTerms.add(termToCheck);
				}
			}
			
		}
		
		return distinctTerms;
	}
	
	private Map<String, java.util.List<TwitterAccount>> calculateCosineSimilarity(Map<String, java.util.List<Double>> tdf_idfUsersMap){
	
		//----------------------- Apply cosine similitary function
		
		java.util.List<Double> tdf_idfs2 = new ArrayList<Double>();
		Map<String, java.util.List<TwitterAccount>> similarityMap = new HashMap<String, java.util.List<TwitterAccount>>();
		Map<Double, String> similarityDic;
		java.util.List<TwitterAccount> similarUsers;
		
		java.util.List<Double> tdf_idfs;
		
		double[] vet1;
		double[] vet2;
		
		java.util.List<String> targetUsers = new ArrayList<String>();
		
		for(UserAccount targetUser: daoUser.listUsers(true, false)){
			targetUsers.add(targetUser.getScreenName());
		}
		
		
		// The similarity set is only calculated for the target users
		for(String user1: tdf_idfUsersMap.keySet()){
			
			if(targetUsers.contains(user1)){
				tdf_idfs = tdf_idfUsersMap.get(user1);
				
				similarityDic = new HashMap<Double, String>();			
			
				for(String user2: tdf_idfUsersMap.keySet()){
					tdf_idfs2 = tdf_idfUsersMap.get(user2);				
				
					if(!user1.equalsIgnoreCase(user2)){
											
						vet1 = tdf_idfCalculator.convertListToArray(tdf_idfs);
						vet2 = tdf_idfCalculator.convertListToArray(tdf_idfs2);
						
						similarityDic.put(cosine.cosineSimilarity(vet1,vet2), user2);
					}							
					
				}
				
				similarUsers = cosine.getHighestsCosineValuedUsers(similarityDic, similarityDic.size());
				similarityMap.put(user1, similarUsers);
			}
				
		}
		
		return similarityMap;
	}
	
	private Map<String, java.util.List<TwitterAccount>> calculatePearsonsCorrelation(Map<String, java.util.List<Double>> tdf_idfUsersMap, int usersSize){
		
		//----------------------- Apply Pearsons Correlation function		
		
		java.util.List<Double> tdf_idfs2 = new ArrayList<Double>();
		Map<String, java.util.List<TwitterAccount>> similarityMap = new HashMap<String, java.util.List<TwitterAccount>>();
		Map<Double, String> similarityDic;
		java.util.List<TwitterAccount> similarUsers;
		
		java.util.List<Double> tdf_idfs;
		
		double[] vet1;
		double[] vet2;
		PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
				
		for(String user1: tdf_idfUsersMap.keySet()){
			
			tdf_idfs = tdf_idfUsersMap.get(user1);
			
			similarityDic = new HashMap<Double, String>();			
			
			for(String user2: tdf_idfUsersMap.keySet()){
				tdf_idfs2 = tdf_idfUsersMap.get(user2);
				
				if(!user1.equalsIgnoreCase(user2)){
										
					vet1 = tdf_idfCalculator.convertListToArray(tdf_idfs);
					vet2 = tdf_idfCalculator.convertListToArray(tdf_idfs2);
					
					similarityDic.put(pearsonsCorrelation.correlation(vet1, vet2), user2);
				}							
				
			}
			
			similarUsers = cosine.getHighestsCosineValuedUsers(similarityDic, usersSize);
			similarityMap.put(user1, similarUsers);
						
		}
		
		return similarityMap;
		
	}
	
	private Map<String, java.util.List<TwitterAccount>> calculateEuclideanDistance(Map<String, java.util.List<Double>> tdf_idfUsersMap, int usersSize){
		//----------------------- Apply Euclidean Distance function
		
		java.util.List<Double> tdf_idfs2 = new ArrayList<Double>();
		Map<String, java.util.List<TwitterAccount>> similarityMap = new HashMap<String, java.util.List<TwitterAccount>>();
		Map<Double, String> similarityDic;
		java.util.List<TwitterAccount> similarUsers;
		
		java.util.List<Double> tdf_idfs;
		
		double[] vet1;
		double[] vet2;
				
		EuclideanDistance euclideanDistance = new EuclideanDistance();
		for(String user1: tdf_idfUsersMap.keySet()){
			
			tdf_idfs = tdf_idfUsersMap.get(user1);
			
			similarityDic = new HashMap<Double, String>();			
			
			for(String user2: tdf_idfUsersMap.keySet()){
				tdf_idfs2 = tdf_idfUsersMap.get(user2);
				
				if(!user1.equalsIgnoreCase(user2)){
										
					vet1 = tdf_idfCalculator.convertListToArray(tdf_idfs);
					vet2 = tdf_idfCalculator.convertListToArray(tdf_idfs2);
					
					similarityDic.put(euclideanDistance.compute(vet1, vet2), user2);
				}							
				
			}
			
			similarUsers = cosine.getHighestsCosineValuedUsers(similarityDic, usersSize);
			similarityMap.put(user1, similarUsers);
		}
		
		return similarityMap;
	
	}
			

}
