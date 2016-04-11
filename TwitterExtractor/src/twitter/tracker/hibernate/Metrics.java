package twitter.tracker.hibernate;

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

import businessLogic.MyComparator;
import businessLogic.TwitterAccount;

public class Metrics {
	
	public Metrics(){
		
	}
	
	@SuppressWarnings("unchecked")
	public void calculateMetrics(int metricRate, Map<String, List<TwitterAccount>> inferedFollowees){
		
		FileInputStream fileStream;
		Map<String, List<String>> usersFolloweeNetwork = null; 
		try {
			fileStream = new FileInputStream("UsersFolloweeNetwork.txt");
			 ObjectInputStream os = new ObjectInputStream(fileStream);
			
			 usersFolloweeNetwork  = (Map<String, List<String>>) os.readObject();				 
			
			 fileStream.close();
			 os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		calculateMAP(metricRate, usersFolloweeNetwork, inferedFollowees);
		calculateRecall(metricRate, usersFolloweeNetwork,inferedFollowees);
	}
	
	public void calculateMetrics(int metricRate, Map<String, List<String>> usersFolloweeNetwork, Map<String, List<TwitterAccount>> inferedFollowees){
		System.out.println("MAP: " + calculateMAP(metricRate, usersFolloweeNetwork, inferedFollowees));
		calculateRecall(metricRate, usersFolloweeNetwork,inferedFollowees);
	}
	
	private double calculateMAP(int metricRate, Map<String, List<String>> usersFolloweeNetwork, Map<String, List<TwitterAccount>> inferedFollowees){
		
		List<TwitterAccount> inferedUsers;
		List<String> followedUsers;
		double averagePrecisionSum = 0;		
		int count = 1;
	
	    for(String user :usersFolloweeNetwork.keySet()){
				
	    	inferedUsers = inferedFollowees.get(user);
				
			//Ordering the List
			inferedUsers.sort(new MyComparator());
			Collections.reverse(inferedUsers);
				
			followedUsers = usersFolloweeNetwork.get(user);
						
			averagePrecisionSum += calculateAveragePrecision(metricRate, inferedUsers, followedUsers);
				
		}	
		
		return averagePrecisionSum/usersFolloweeNetwork.keySet().size();
	}
	

	private boolean checkUserRelevance(String user, List<String> usersSet){
		
		int i = 0;
		boolean found = false, relevant = false;
		while((i < usersSet.size()) && !found){
			if(usersSet.get(i).equals(user)){
				relevant = true;
				found = true;
			}
			i++;
		}
		return relevant;
	}
	
	private double calculateAveragePrecision(int metricRate, List<TwitterAccount> inferedUsers, List<String> followedUsers){
		
		boolean relevant;
		double totalHits = 0;
		double relevantHits = 0;
		double partialAverage = 0;
		int count = 1;
		
		for(TwitterAccount account: inferedUsers){		
					
			
			relevant = checkUserRelevance(account.getName(), followedUsers);
			totalHits++;
			
			if(relevant){				
				relevantHits++;
				
				partialAverage += relevantHits/totalHits;
				System.out.println("PartialAverage: " + relevantHits/totalHits);
			}
			
			if(metricRate > 0){					
				if(metricRate == count)
					break;				
				count++;					
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
			inferedUsers.sort(new MyComparator());
			Collections.reverse(inferedUsers);
				
			followedUsers = usersFolloweeNetwork.get(user);
						
			averageRecallSum += calculateIndivualRecall(metricRate, inferedUsers, followedUsers);
				
		}	
		
		return averageRecallSum/usersFolloweeNetwork.keySet().size();
		
	}
	
	private double calculateIndivualRecall(int metricRate, List<TwitterAccount> inferedUsers, List<String> followedUsers){
		
		boolean relevant;
		double totalHits = 0;
		double relevantHits = 0;
		double falseNegatives = 0;
		double partialAverage = 0;
		int count = 1;
		
		for(TwitterAccount account: inferedUsers){		
					
			
			relevant = checkUserRelevance(account.getName(), followedUsers);
			totalHits++;
			
			if(relevant){				
				relevantHits++;			
			}
			
			if(metricRate > 0){					
				if(metricRate == count)
					break;				
				count++;					
			}	
			
		}
		
		falseNegatives = followedUsers.size() - relevantHits;
		
		if(metricRate > 0){
			return relevantHits/(relevantHits + falseNegatives);
		}else{
			return partialAverage/inferedUsers.size();	
		}	
		
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
			list.sort(new MyComparator());
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
