package persistence.entities;

import java.util.ArrayList;

public class List {
	
	private long idList;	
	private String name;	
	private String description;	
    private java.util.List<UserAccount> members = new ArrayList<UserAccount>();
	
    private UserAccount listOwner;
	
	public List(){
		
	}
	
	public List(long idList, String name, String description, UserAccount listOwner) {
		
		this.idList = idList;
		this.name = name;
		this.description = description;		
		this.listOwner = listOwner;		
	}
	
	public List(long idList) {
		this.idList = idList;
	}

	public UserAccount getListOwner() {
		return listOwner;
	}
	
	public void setListOwner(UserAccount user) {
		this.listOwner = user;
	}
	public long getIDList() {		
		return idList;
	}
	public void setIDList(long id) {
		this.idList = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public java.util.List<UserAccount> getListMembers(){
		return members;
	}
	
	public void setListMembers(java.util.List<UserAccount> members){
		this.members = members;
	}
	

}
