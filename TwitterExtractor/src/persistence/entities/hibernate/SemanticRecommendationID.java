package persistence.entities.hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SemanticRecommendationID implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "user_id")
	private Long userID;
	
	@Column(name = "recommendation_id")
    private Long recommendationID;

    public SemanticRecommendationID(){
    	
    }
    
    public SemanticRecommendationID(Long userID, Long recommendationID){
    	this.userID = userID;
    	this.recommendationID = recommendationID;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userID == null) ? 0 : userID.hashCode());
		result = prime * result
				+ ((recommendationID == null) ? 0 : recommendationID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		SemanticRecommendationID other = (SemanticRecommendationID) obj;
		
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		
		if (recommendationID == null) {
			if (other.recommendationID != null)
				return false;
		} else if (!recommendationID.equals(other.recommendationID))
			return false;
		
		return true;
	}
	
	

}
