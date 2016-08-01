package twitter.tracker.hibernate;

import java.util.Comparator;

public class MyComparatorCosineSimilarity implements Comparator<TwitterAccount> {
	
	 @Override
	  public int compare(TwitterAccount account1, TwitterAccount account2) {
	      return Double.compare(account1.getCosineSimilarity(), account2.getCosineSimilarity());
	  }
	
}
