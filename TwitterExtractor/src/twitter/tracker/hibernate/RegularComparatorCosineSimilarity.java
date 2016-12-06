package twitter.tracker.hibernate;

import java.util.Comparator;

import persistence.entities.hibernate.RegularRecommendation;

public class RegularComparatorCosineSimilarity implements Comparator<RegularRecommendation> {
	
	 @Override
	  public int compare(RegularRecommendation account1, RegularRecommendation account2) {
	      return Double.compare(account1.getCosineSimilarity(), account2.getCosineSimilarity());
	  }
	
}
