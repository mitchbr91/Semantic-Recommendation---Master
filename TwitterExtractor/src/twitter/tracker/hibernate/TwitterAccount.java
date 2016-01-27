package twitter.tracker.hibernate;


public class TwitterAccount {
	
	private String name;
	private int inferedPoints;
	private double cosineSimilarity;
	
	public TwitterAccount(String name){
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInferedPoints() {
		return inferedPoints;
	}

	public void setInferedPoints(int inferedPoints) {
		this.inferedPoints += inferedPoints;
	}
	
	public double getCosineSimilarity(){
		return cosineSimilarity;
	}
	
	public void setCosineSimilarity(double cosineSimilarity){
		this.cosineSimilarity = cosineSimilarity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		TwitterAccount other = (TwitterAccount) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

}