package persistence.entities;


public class URL {
	
	private int urlID;
	private String url;
	
	public URL(){
		
	}		
	
	public URL(int urlID, String url) {		
		this.urlID = urlID;
		this.url = url;
	}

	public int getUrlID() {
		return urlID;
	}

	public void setUrlID(int urlID) {
		this.urlID = urlID;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}



	
}
