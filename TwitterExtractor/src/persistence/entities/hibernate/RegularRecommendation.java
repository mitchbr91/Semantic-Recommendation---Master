package persistence.entities.hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "`REGULAR_RECOMMENDATION`")
public class RegularRecommendation implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RegularRecommendationID id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserAccount user;
	
	@ManyToOne
	@JoinColumn(name = "recommendation_id", insertable = false, updatable = false)
	private UserAccount recommendation;
	
	@Column(name = "cosine_similarity")
	private double cosineSimilarity;
	
	public RegularRecommendation(){
		
	}
	
	public RegularRecommendation(UserAccount user, UserAccount recommendation, double cosineSimilarity){
		
		this.id = new RegularRecommendationID(user.getIDUser(), recommendation.getIDUser());
		
		this.user = user;
		this.recommendation = recommendation;
		this.cosineSimilarity = cosineSimilarity;
				
		user.getRegularRecommendations().add(this);
	}
	
		
	public UserAccount getUser() {
		return user;
	}

	public UserAccount getRecommendation() {
		return recommendation;
	}

	public double getCosineSimilarity() {
		// TODO Auto-generated method stub
		return cosineSimilarity;
	}

}
