package persistence.entities.hibernate;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity(name = "list")
public class List {
	
	@Id
	@Column(name = "list_id")
	private long idList;	
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinTable(name="`LIST_USER`",
        joinColumns={@JoinColumn(name="list_id")},
        inverseJoinColumns={@JoinColumn(name="user_id")})
    private java.util.List<UserAccount> members = new ArrayList<UserAccount>();	
	
	@ManyToOne
    @JoinColumn(name="user_id")
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

	public void addMember(UserAccount member){
		if(!members.contains(member))
			members.add(member);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idList ^ (idList >>> 32));
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
		List other = (List) obj;
		if (idList != other.idList)
			return false;
		return true;
	}
	
	

}
