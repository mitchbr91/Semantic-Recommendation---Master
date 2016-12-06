package persistence.entities.hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserInferenceID implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "user_id")
	private Long userID;
	
	@Column(name = "inference_id")
    private Long inferenceID;

    public UserInferenceID(){
    	
    }
    
    public UserInferenceID(Long userID, Long inferenceID){
    	this.userID = userID;
    	this.inferenceID = inferenceID;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userID == null) ? 0 : userID.hashCode());
		result = prime * result
				+ ((inferenceID == null) ? 0 : inferenceID.hashCode());
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
		
		UserInferenceID other = (UserInferenceID) obj;
		
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		
		if (inferenceID == null) {
			if (other.inferenceID != null)
				return false;
		} else if (!inferenceID.equals(other.inferenceID))
			return false;
		
		return true;
	}
	
	

}
