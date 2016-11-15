package twitter.tracker.hibernate;

import java.util.Comparator;

import persistence.entities.hibernate.SemanticRecommendation;

public class SemanticComparatorCosineSimilarity implements Comparator<SemanticRecommendation> {
	
	 @Override
	  public int compare(SemanticRecommendation account1, SemanticRecommendation account2) {
	      return Double.compare(account1.getCosineSimilarity(), account2.getCosineSimilarity());
	  }
	
}
