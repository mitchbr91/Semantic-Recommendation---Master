package similarity;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import persistence.dao.UserDao;
import persistence.entities.Tweet;
import persistence.entities.UserAccount;
import ptstemmer.Stemmer;
import ptstemmer.exceptions.PTStemmerException;
import ptstemmer.implementations.OrengoStemmer;

public class Main {
	
	public static void main(String args[]){
		
         int i = 1;
         
//         System.out.println("With Stopwords: ");
//         for(Tweet tweet: user.getTweets()){
//        	 profile += tweet.getText() + "\n";        	 
//         }
//         
//         for(Tweet retweet: user.getRetweets()){        	 
//        	 profile += retweet.getText() + "\n";
//         }    
//         
//         System.out.println("User profile: " + profile);
//         System.out.println("------------------------------------------------------------------------------------------------------------------------");         
//         System.out.println("Without Stopwords: ");
         
//         String s = "Viola Davis!!!!! fui Rock in ";
//         
//         System.out.println("Before: " + s);
//         System.out.println("After: " + s.replaceAll("!", ""));
         
         List<List<String>> users = new ArrayList<List<String>>();
         List<String> u = new ArrayList<String>();
         //u.add(profile);
         
         users.add(u);
         
         //users = treater.eliminateStopWords(users);
         
//         tweet = tweet.replaceAll("RT", "").replaceAll("RT", "")
//     			.replaceAll("\\?", "").replaceAll("!", "")
//     			.replaceAll(",", "").replaceAll(":", "")
//     			.replaceAll(";", "").replace("\\.", "");	
         
         String pro = "";
         i = 1;
         for(String p: users.get(0)){
        	 
        	 if(i % 10 == 0){
        		 pro += "\n";
        	 }else{
        		 pro += p + " ";
        	 }
         }
         
         //System.out.println(pro);
         
//         String ss = "la voce in,, festa e canto.... Corro nel vento e canto:io canto";
//         
//         for(String s: ss.split("\\.+|,+ |:| ")){
//        	 System.out.println("Token: " + s);
//         }
         
         Stemmer stemmer;
         try {
				stemmer = new OrengoStemmer();			
				stemmer.enableCaching(1000);   //Optional		
				
				System.out.println("Stemm: " + stemmer.getWordStem("drogaria"));
									
			} catch (PTStemmerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
         
	}

}
