package persistence.entities;

public class Hashtag{
	

	private int hashtagID;	
	private String hashtag;
	

	public Hashtag(){
		
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

	
	
	
	
	
	
	
	

}
