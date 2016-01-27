package persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import persistence.entities.Hashtag;
import persistence.entities.URL;
import persistence.factory.ConnectionFactory;

public class URLDao {
	
	private Connection connection;
	private String sql;
	private  PreparedStatement stmt;
	private ResultSet rs;
	private int urlID;
	private List<URL> urls;
	
	
	public URLDao(String databaseName){	
		urls = new ArrayList<URL>();
		this.connection = new ConnectionFactory().getConnection(databaseName);
	}
	
	public void add(String url){		
		
		urlID = 0; 
		try {
			
			urlID = getURLByName(url);		
			
			if(urlID == 0){
				sql = "INSERT INTO \"URL\"(url) VALUES (?)";

				// prepared statement para inserção
		        stmt = connection.prepareStatement(sql);

			    // seta os valores
		        stmt.setString(1, url);			    	              
			       
			    // executa
			    stmt.execute();
			    stmt.close();
			}
			
			
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
	}
	
	public List<URL> listURLs(){

		PreparedStatement stmt;
		ResultSet rs;
		
		URL url;
		try {
			sql = "SELECT url_id FROM \"URL\"";
	         
	         stmt = this.connection.prepareStatement(sql);
	         rs = stmt.executeQuery();
	 	        
	         while (rs.next()) {

	        	 // Creating hashtag's object	        	 
	        	 url = getURLByID(rs.getInt("url_id")); 
	 	        	 
	             // Adding hashtag into the list
	        	 urls.add(url); 
	        	 
	         }	
	         
	         rs.close();
	         stmt.close();

	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
		
		return urls;

	}
	
	public URL getURLByID(int urlID){
		
		URL url = null;		
		try {
			sql = "SELECT * FROM \"URL\" WHERE url_id = " + urlID;
			
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				url = new URL();
				url.setUrlID(rs.getInt("url_id"));
				url.setUrl(rs.getString("url"));
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return url;
		
	}

	public int getURLByName(String url){
		
		urlID = 0;
		try {
			sql = "SELECT url_id FROM \"URL\" WHERE url = \'" +  url + "\'";
			stmt = connection.prepareStatement(sql);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				urlID = rs.getInt("url_id");
			}
			
			rs.close();
		    stmt.close();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return urlID;
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
