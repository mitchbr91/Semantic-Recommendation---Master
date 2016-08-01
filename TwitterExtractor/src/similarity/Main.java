package similarity;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ptstemmer.Stemmer;
import ptstemmer.exceptions.PTStemmerException;
import ptstemmer.implementations.OrengoStemmer;
import twitter.tracker.hibernate.MyComparatorCosineSimilarity;
import twitter.tracker.hibernate.TwitterAccount;

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

         
         //users = treater.eliminateStopWords(users);
         
//         tweet = tweet.replaceAll("RT", "").replaceAll("RT", "")
//     			.replaceAll("\\?", "").replaceAll("!", "")
//     			.replaceAll(",", "").replaceAll(":", "")
//     			.replaceAll(";", "").replace("\\.", "");	
    
         
         //System.out.println(pro);
         
//         String ss = "la voce in,, festa e canto.... Corro nel vento e canto:io canto";
//         
//         for(String s: ss.split("\\.+|,+ |:| ")){
//        	 System.out.println("Token: " + s);
//         }
         
//         Stemmer stemmer;
//         try {
//				stemmer = new OrengoStemmer();			
//				stemmer.enableCaching(1000);   //Optional		
//				
//				System.out.println("Stemm: " + stemmer.getWordStem("love"));
//									
//			} catch (PTStemmerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}	
         
         String s = "you're love is a lovelove";         
         //System.out.println(s.split(" ").length);
         
         List<TwitterAccount> l = new ArrayList<TwitterAccount>();
         
         TwitterAccount t = new TwitterAccount("a");
         t.setCosineSimilarity(0.4578781545);
         
         l.add(t);
         
         t = new TwitterAccount("b");
         t.setCosineSimilarity(0.8751548548545);
         
         l.add(t);
         
         t = new TwitterAccount("c");
         t.setCosineSimilarity(0.1487856568);
         
         l.add(t);
         
         l.sort(new MyComparatorCosineSimilarity());
                 
         for(TwitterAccount n:l){
        	 System.out.println(n.getCosineSimilarity());
         }
         
	}

}
