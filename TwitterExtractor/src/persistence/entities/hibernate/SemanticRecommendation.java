package persistence.entities.hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "`SEMANTIC_RECOMMENDATION`")
public class SemanticRecommendation implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SemanticRecommendationID id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserAccount user;
	
	@ManyToOne
	@JoinColumn(name = "recommendation_id", insertable = false, updatable = false)
	private UserAccount recommendation;
	
	@Column(name = "cosine_similarity")
	private double cosineSimilarity;
	
	public SemanticRecommendation(){
		
	}
	
	public SemanticRecommendation(UserAccount user, UserAccount recommendation, double cosineSimilarity){
		
		this.id = new SemanticRecommendationID(user.getIDUser(), recommendation.getIDUser());
		
		this.user = user;
		this.recommendation = recommendation;
		this.cosineSimilarity = cosineSimilarity;
				
		//user.getSemanticRecommendations().add(this);
	}
	
		
	public UserAccount getUser() {
		return user;
	}

	public UserAccount getRecommendation() {
		return recommendation;
	}
	
	public double getCosineSimilarity(){
		return cosineSimilarity;
	}

}
