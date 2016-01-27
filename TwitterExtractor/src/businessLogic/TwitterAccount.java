package businessLogic;


public class TwitterAccount {
	
	private String name;
	private int inferedPoints;
	
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
		this.inferedPoints = inferedPoints;
	}
	
	

}
