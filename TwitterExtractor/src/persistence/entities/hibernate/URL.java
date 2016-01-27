package persistence.entities.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "url")
public class URL {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urls_seq_gen")
//	@SequenceGenerator(name = "urls_seq_gen", sequenceName = "url_url_id_seq1")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "url_id")
	private int urlID;
	
	@Column
	private String url;
	
	public URL(){
		
	}		
	
	public URL(String url) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		URL other = (URL) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	
	
	



	
}
