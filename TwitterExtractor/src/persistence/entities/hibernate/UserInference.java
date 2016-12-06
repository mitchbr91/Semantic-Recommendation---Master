package persistence.entities.hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "`USER_INFERENCE`")
public class UserInference implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserInferenceID id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserAccount user;
	
	@ManyToOne
	@JoinColumn(name = "inference_id", insertable = false, updatable = false)
	private UserAccount inference;
	
	@Column(name = "infered_points")
	private int inferedPoints;
	
	public UserInference(){
		
	}
	
	public UserInference(UserAccount user, UserAccount inference, int inferedPoints){
		
		this.id = new UserInferenceID(user.getIDUser(), inference.getIDUser());
		
		this.user = user;
		this.inference = inference;
		this.inferedPoints = inferedPoints;
				
		//user.getInferences().add(this);
	}
	
		
	public UserAccount getUser() {
		return user;
	}

	public UserAccount getInference() {
		return inference;
	}
	
	public int getInferedPoints(){
		return inferedPoints;
	}

}
