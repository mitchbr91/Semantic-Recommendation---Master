package inference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import persistence.dao.hibernate.UserDao;
import persistence.entities.hibernate.RegularRecommendation;
import persistence.entities.hibernate.SemanticRecommendation;
import persistence.entities.hibernate.UserAccount;
import twitter.tracker.hibernate.SemanticComparatorCosineSimilarity;
import twitter.tracker.hibernate.MyComparatorInferedPoints;
import twitter.tracker.hibernate.RegularComparatorCosineSimilarity;
import twitter.tracker.hibernate.TwitterAccount;

public class Metrics {
	
	private UserDao daoUser;
	
	public Metrics(){
		daoUser = new UserDao();
	}
		
	public void calculateMetrics(int metricRate, boolean semantic){
		
		
		Map<String, List<String>> recommendedFollowees = new HashMap<String, List<String>>(); 
		Map<String, List<String>> acceptedFollowees = new HashMap<String, List<String>>();
		
		
		List<UserAccount> targetUsers = daoUser.listUsers(true, false);
		List<String> recommendations;
		List<SemanticRecommendation> sRec;
		List<RegularRecommendation> rRec;
		List<String> aFollowees;
		
		for(UserAccount targetUser: targetUsers){
			
			recommendations = new ArrayList<String>();
			aFollowees = new ArrayList<String>();
			
			if (semantic){
				
				sRec = targetUser.getSemanticRecommendations();
				sRec.sort(new SemanticComparatorCosineSimilarity());
				
				for(SemanticRecommendation recommendation: sRec){
					recommendations.add(recommendation.getRecommendation().getScreenName());
				}
				
				for(UserAccount su: targetUser.getSemanticUnfollows()){
					aFollowees.add(su.getScreenName());
				}
				
			}else{
				
				rRec = targetUser.getRegularRecommendations();
			    rRec.sort(new RegularComparatorCosineSimilarity());
				
				for(RegularRecommendation recommendation: rRec){
					recommendations.add(recommendation.getRecommendation().getScreenName());
				}
				
				for(UserAccount ru: targetUser.getRegularUnfollows()){
					aFollowees.add(ru.getScreenName());
				}				
			}	
			
			
			recommendedFollowees.put(targetUser.getScreenName(), recommendations);
			acceptedFollowees.put(targetUser.getScreenName(), aFollowees);
		}		
		
		System.out.println("MAP: " + calculateMAP(recommendedFollowees, acceptedFollowees));
		System.out.println("Recall: " + calculateRecall(metricRate, recommendedFollowees, acceptedFollowees));
	}
	
	public void calculateMetrics(int metricRate, Map<String, List<String>> recommendedFollowees, Map<String, List<String>> acceptedFollowees){
		System.out.println("MAP: " + calculateMAP(recommendedFollowees, acceptedFollowees));
		calculateRecall(metricRate, recommendedFollowees,acceptedFollowees);
	}
	
	private double calculateMAP(Map<String, List<String>> recommendedFollowees, Map<String, List<String>> acceptedFollowees){
		
		List<String> acceptedUsers = null;
		List<String> recommendedUsers;
		double averagePrecisionSum = 0.0;		
		
	
	    for(String user :recommendedFollowees.keySet()){
				
	    	acceptedUsers = acceptedFollowees.get(user);
			recommendedUsers = recommendedFollowees.get(user);
						
			averagePrecisionSum += calculateAveragePrecision(acceptedUsers, recommendedUsers);
				
		}	
		
		return averagePrecisionSum/acceptedFollowees.size();
	}
	

	private boolean checkUserRelevance(String user, List<String> usersSet){
		
		boolean relevant = false;
		
		if(usersSet != null)
			if(usersSet.contains(user)){
				relevant = true;
			}		
		
		return relevant;
	}
	
	private double calculateAveragePrecision(List<String> acceptedUsers, List<String> recommendedUsers){
		
		boolean relevant;
		double totalHits = 0.0;
		double relevantHits = 0.0;
		double partialAverage = 0.0;		
		
		for(String account: recommendedUsers){		
					
			
			relevant = checkUserRelevance(account, acceptedUsers);
			totalHits++;
			
			if(relevant){				
				relevantHits++;
				
				partialAverage += relevantHits/totalHits;				
			}
			
		}		
		
		if(partialAverage == 0.0){
			return 0.0;
		}else{
			return partialAverage/relevantHits;
		}		
				
	}
	
	private double calculateRecall(int metricRate, Map<String, List<String>> recommendedFollowees, Map<String, List<String>> acceptedFollowees){
		
		
		List<String> acceptedUsers;
		List<String> recommendedUsers;
		double averageRecallSum = 0;	
		
	
	    for(String user :recommendedFollowees.keySet()){
				
	    	acceptedUsers = acceptedFollowees.get(user);
			recommendedUsers = recommendedFollowees.get(user);
						
			averageRecallSum += calculateIndivualRecall(metricRate, acceptedUsers, recommendedUsers);
				
		}	
		
		return averageRecallSum/recommendedFollowees.keySet().size();
		
	}
	
	private double calculateIndivualRecall(int metricRate, List<String> acceptedUsers, List<String> recommendedUsers){
		
		boolean relevant;		
		double relevantHits = 0;
		int limit;
		
		if(metricRate == 0){
			limit = acceptedUsers.size();
		}else{
			limit = metricRate;
		}
		
		for(int i = 0; i < limit; i++){
			
			relevant = checkUserRelevance(recommendedUsers.get(i), acceptedUsers);
			
			if(relevant){				
				relevantHits++;			
			}
		}			
		
		return relevantHits/recommendedUsers.size();
		
		
	}
	
	public Map<String, List<TwitterAccount>> generateMapInfered(){
		
		Map<String, List<TwitterAccount>> map = new HashMap<String, List<TwitterAccount>>();
		List<TwitterAccount> l = new ArrayList<TwitterAccount>();
		int n;
		
		Random gerador = new Random();
	    TwitterAccount t;
	    
	    for(int i = 1; i <= 10; i++){
	    	t = new TwitterAccount("user"+ i);
	    	n = gerador.nextInt(10) + 1;
	    	t.setInferedPoints(n);
	    	l.add(t);
	    	
	    }
	    
	    map.put("bla", l);
		
		return map;		
		
	}
	
	public  Map<String, List<String>> generateMapFollowed(){
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> l = new ArrayList<String>();
		int n;
		
		Random gerador = new Random();
	    TwitterAccount t;
	    
	    for(int i = 1; i <= 10; i++){
	    	n = gerador.nextInt(50) + 1;	    	
	    	l.add("user"+ n);	    
	    }
	    
	    map.put("bla", l);
		
		return map;		
	}
	
	public void printMapInfered(Map<String, List<TwitterAccount>> map){
		
		List<TwitterAccount> list;
		for(String key: map.keySet()){
			list = map.get(key);
			//list.sort(new MyComparatorInferedPoints());
			Collections.reverse(list);
			
			System.out.println(key);
			for(TwitterAccount ta: list){
				System.out.print(ta.getName() + " ");
				System.out.print(ta.getInferedPoints());
				System.out.println();
			}
		}
	}
	
	public void printMapFollowed(Map<String, List<String>> map){
		
		List<String> list;
		for(String key: map.keySet()){
			list = map.get(key);
			Collections.sort(list);
			Collections.reverse(list);
			
			System.out.println(key);
			for(String ta: list){
				System.out.println(ta);					
			}
		}
	}


}
