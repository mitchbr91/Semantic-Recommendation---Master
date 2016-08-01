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
import persistence.entities.hibernate.UserAccount;
import twitter.tracker.hibernate.MyComparatorInferedPoints;
import twitter.tracker.hibernate.TwitterAccount;

public class Metrics {
	
	private UserDao daoUser;
	
	public Metrics(){
		daoUser = new UserDao();
	}
		
	public void calculateMetrics(int metricRate, Map<String, List<TwitterAccount>> inferedFollowees){
		
		
		Map<String, List<String>> usersFolloweeNetwork = new HashMap<String, List<String>>(); 
		
		List<UserAccount> targetUsers = daoUser.listUsers(true, false);
		List<String> followees;
		
		for(UserAccount targetUser: targetUsers){
			
			followees = new ArrayList<String>();
			for(UserAccount followee: targetUser.getFollowees()){
				followees.add(followee.getScreenName());
			}			
			
			usersFolloweeNetwork.put(targetUser.getScreenName(), followees);			
		}		
		
		System.out.println("MAP: " + calculateMAP(usersFolloweeNetwork, inferedFollowees));
		System.out.println("Recall: " + calculateRecall(metricRate, usersFolloweeNetwork,inferedFollowees));
	}
	
	public void calculateMetrics(int metricRate, Map<String, List<String>> usersFolloweeNetwork, Map<String, List<TwitterAccount>> inferedFollowees){
		System.out.println("MAP: " + calculateMAP(usersFolloweeNetwork, inferedFollowees));
		calculateRecall(metricRate, usersFolloweeNetwork,inferedFollowees);
	}
	
	private double calculateMAP(Map<String, List<String>> usersFolloweeNetwork, Map<String, List<TwitterAccount>> inferedFollowees){
		
		List<TwitterAccount> inferedUsers;
		List<String> followedUsers;
		double averagePrecisionSum = 0;		
		
	
	    for(String user :usersFolloweeNetwork.keySet()){
				
	    	inferedUsers = inferedFollowees.get(user);
						
			inferedUsers.sort(new MyComparatorInferedPoints());
			Collections.reverse(inferedUsers);
				
			followedUsers = usersFolloweeNetwork.get(user);
						
			averagePrecisionSum += calculateAveragePrecision(inferedUsers, followedUsers);
				
		}	
		
		return averagePrecisionSum/usersFolloweeNetwork.keySet().size();
	}
	

	private boolean checkUserRelevance(String user, List<String> usersSet){
		
		boolean relevant = false;
		
		if(usersSet.contains(user)){
			relevant = true;
		}		
		
		return relevant;
	}
	
	private double calculateAveragePrecision(List<TwitterAccount> inferedUsers, List<String> followedUsers){
		
		boolean relevant;
		double totalHits = 0;
		double relevantHits = 0;
		double partialAverage = 0;		
		
		for(TwitterAccount account: inferedUsers){		
					
			
			relevant = checkUserRelevance(account.getName(), followedUsers);
			totalHits++;
			
			if(relevant){				
				relevantHits++;
				
				partialAverage += relevantHits/totalHits;
				System.out.println("PartialAverage: " + relevantHits/totalHits);
			}
			
		}
		
		return partialAverage/relevantHits;		
	}
	
	private double calculateRecall(int metricRate, Map<String, List<String>> usersFolloweeNetwork, Map<String, List<TwitterAccount>> inferedFollowees){
		
		
		List<TwitterAccount> inferedUsers;
		List<String> followedUsers;
		double averageRecallSum = 0;	
		
	
	    for(String user :usersFolloweeNetwork.keySet()){
				
	    	inferedUsers = inferedFollowees.get(user);
				
			//Ordering the List
			inferedUsers.sort(new MyComparatorInferedPoints());
			Collections.reverse(inferedUsers);
				
			followedUsers = usersFolloweeNetwork.get(user);
						
			averageRecallSum += calculateIndivualRecall(metricRate, inferedUsers, followedUsers);
				
		}	
		
		return averageRecallSum/usersFolloweeNetwork.keySet().size();
		
	}
	
	private double calculateIndivualRecall(int metricRate, List<TwitterAccount> inferedUsers, List<String> followedUsers){
		
		boolean relevant;		
		double relevantHits = 0;
		int limit;
		
		if(metricRate == 0){
			limit = inferedUsers.size();
		}else{
			limit = metricRate;
		}
		
		for(int i = 0; i < limit; i++){
			relevant = checkUserRelevance(inferedUsers.get(i).getName(), followedUsers);
			
			if(relevant){				
				relevantHits++;			
			}
		}			
		
		return relevantHits/followedUsers.size();
		
		
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
			list.sort(new MyComparatorInferedPoints());
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
