package persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import persistence.entities.Hashtag;
import persistence.factory.ConnectionFactory;

public class HashtagDao {
	
	private Connection connection;
	private String sql;
	private  PreparedStatement stmt;
	private ResultSet rs;
	private int hashtagID;
	private List<Hashtag> hashtags;
	
	
	public HashtagDao(String databaseName) {	
		hashtags = new ArrayList<Hashtag>();
		this.connection = new ConnectionFactory().getConnection(databaseName);
	}
	
	public void add(String hashtag){		
				
		hashtagID = 0;
		
		try {
			
			getHashtagByName(hashtag);
			
			if(hashtagID == 0){
				sql = "INSERT INTO \"HASHTAG\"(hashtag) VALUES (?)";
				
				// prepared statement para inserção
		        stmt = connection.prepareStatement(sql);

			    // seta os valores
		        stmt.setString(1, hashtag);			   	              
			       
			    // executa
			    stmt.execute();
			    stmt.close();
			}
			
		   
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
	}
	
	public List<Hashtag> listHashtags(){		
		
		PreparedStatement stmt;
		ResultSet rs;
		
		Hashtag hashtag;
		try {
			sql = "SELECT hashtag_id FROM \"HASHTAG\"";
	         
	         stmt = this.connection.prepareStatement(sql);
	         rs = stmt.executeQuery();
	 	        
	         while (rs.next()) {

	        	 // Creating hashtag's object	        	 
	        	 hashtag = getHashtagByID(rs.getInt("hashtag_id")); 
	 	        	 
	             // Adding hashtag into the list
	        	 hashtags.add(hashtag); 
	        	 
	         }	
	         
	         rs.close();
	         stmt.close();

	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
		
		return hashtags;
	}
	
	public Hashtag getHashtagByID(int hashtagID){
		
		Hashtag hashtag = null;		
		try {
			sql = "SELECT * FROM \"HASHTAG\" WHERE hashtag_id = " + hashtagID;
			
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				hashtag = new Hashtag();
				hashtag.setHashtagID(rs.getInt("hashtag_id"));
				hashtag.setHashtag(rs.getString("hashtag"));
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hashtag;
        
	}

	public int getHashtagByName(String hashtag){
		
		hashtagID = 0;
		try {
			sql = "SELECT hashtag_id FROM \"HASHTAG\" WHERE hashtag = \'" + hashtag + "\'";
			stmt = connection.prepareStatement(sql);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				hashtagID = rs.getInt("hashtag_id");
			}
			
			 rs.close();
			 stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		return hashtagID;
		
	}
	
	
	
	public void shutdownConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
