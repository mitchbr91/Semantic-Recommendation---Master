package persistence.entities.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "hashtag")
public class Hashtag{
	
	
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hashtags_seq_gen")
//	@SequenceGenerator(name = "hashtags_seq_gen", sequenceName = "hashtag_hashtag_id_seq1")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "hashtag_id")
	private int hashtagID;	
	
	@Column
	private String hashtag;
	

	public Hashtag(){
		
	}

	public Hashtag(String hashtag) {		
		this.hashtag = hashtag;
	}
	
	public Hashtag(int hashtagID, String hashtag) {		
		this.hashtagID = hashtagID;
		this.hashtag = hashtag;
	}

	
	public int getHashtagID() {
		return hashtagID;
	}

	public void setHashtagID(int hashtagID) {
		this.hashtagID = hashtagID;
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hashtag == null) ? 0 : hashtag.hashCode());
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
		Hashtag other = (Hashtag) obj;
		if (hashtag == null) {
			if (other.hashtag != null)
				return false;
		} else if (!hashtag.equals(other.hashtag))
			return false;
		return true;
	}

	
	

}
