package persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import persistence.entities.List;
import persistence.entities.UserAccount;
import persistence.factory.ConnectionFactory;

public class ListDao {

	private Connection connection;
	private String sql;
	private PreparedStatement stmt;
	private ResultSet rs;
	private java.util.List<UserAccount> members;
	private java.util.List<List> lists;
	
	public ListDao(String databaseName){
		members = new ArrayList<UserAccount>();
		lists = new ArrayList<List>();
		this.connection = new ConnectionFactory().getConnection(databaseName);
	}
	
	public java.util.List<List> listLists(){
		
		sql = "select list_id from \"LIST\"";
		 
		PreparedStatement stmt;
		ResultSet rs;
		 try {	    	
	         
	         stmt = this.connection.prepareStatement(sql);
	         rs = stmt.executeQuery();
	 
	         List list;
	         while (rs.next()) {
	             // criando o objeto List
	        	 
	        	 list = getListByID(rs.getLong("list_id")); 
	 
	             // Adding list into the list
	             lists.add(list);
	             
	         }	        
	         
	         rs.close();
	         stmt.close();

	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
	     
	     return lists;
	}
	
	public void add(List list){
		
		
		long listID = 0;
		
		try {
			
			sql = "SELECT list_id FROM \"LIST\" WHERE list_id = " + list.getIDList();
			stmt = connection.prepareStatement(sql);
	        rs = stmt.executeQuery();
	        
	        while (rs.next()) {
	            listID = rs.getLong("list_id");	 
	        }	
	        
	        if(listID == 0){
	        	sql = "INSERT INTO \"LIST\"(list_id, name, description, user_id) VALUES (?, ?, ?, ?)";

				// prepared statement para inserção
		        stmt = connection.prepareStatement(sql);

			    // seta os valores
		        stmt.setLong(1, list.getIDList());
		        stmt.setString(2, list.getName());
		        stmt.setString(3, list.getDescription());
			    stmt.setLong(4,list.getListOwner().getIDUser());		              
			       
			    // executa
			    stmt.execute();
	        }
	        
			rs.close();
		    stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public List getListByID(long listID){
		
		 sql = "SELECT * FROM \"LIST\" WHERE list_id = " + listID;
	 
		 List list = null;		 
		 UserAccount user = null;
		 
		 try {
	              
			 stmt = connection.prepareStatement(sql);
	         rs = stmt.executeQuery();	    
	         
	         while (rs.next()) {
	             // criando o objeto User
	          
	        	 list = new List();
	        	 list.setIDList(rs.getLong("list_id"));
	        	 list.setName(rs.getString("name"));
	        	 list.setDescription(rs.getString("description"));	        	 
		         
	            	
	         }	           
	         
	         
	         sql = "SELECT * FROM \"TB_User\" u WHERE u.user_id IN (SELECT user_id FROM \"LIST\" WHERE list_id = " + listID + ")";
	         
	         stmt = connection.prepareStatement(sql);			
			 rs = stmt.executeQuery();
				
			 while (rs.next()) {
		     // criando o objeto User
		            
		       	 user = new UserAccount();
		         user.setIDUser(rs.getLong("user_id"));
		         user.setCreatedAt(rs.getDate("created_at"));
		         user.setFavouritesCount(rs.getInt("favourites_count"));
		         user.setFriendsCount(rs.getInt("friends_count"));
		         user.setFollowersCount(rs.getInt("followers_count"));
		         user.setLanguage(rs.getString("lang"));
		         user.setLocation(rs.getString("user_location"));
		         user.setListedCount(rs.getInt("listed_count"));
		         user.setName(rs.getString("name"));
		         user.setDescription(rs.getString("description"));
		         user.setUrl(rs.getString("url"));
		         user.setScreenName(rs.getString("screen_name"));
		         user.setStatusesCount(rs.getInt("statuses_count"));
		         user.setVerified(rs.getBoolean("is_verified"));
		            	 	            
		     }
			 
			 // Updating list owner
			 list.setListOwner(user);
	         

	         // Updating members
	         members = listUsersByListID(listID);
	         list.setListMembers(members);
			 
	         rs.close();
	         stmt.close();
		 }catch (SQLException e) {
		      throw new RuntimeException(e);
		 }
		 
		 
		return list;
		
	}
	
	public java.util.List<List> getListsByOwnerID(long userID){
		
		PreparedStatement stmt;
		ResultSet rs;
		
		sql = "SELECT list_id FROM \"LIST\" u WHERE u.user_id IN"
		 		+ " (SELECT user_id FROM \"TB_User\" WHERE user_id = " + userID + ")";		
		
		
		lists = new ArrayList<List>();
		try {
			
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();			
			
			List list;
	        while (rs.next()) {
	            
	        	list = getListByID(rs.getLong("list_id"));	        	
		        lists.add(list);	             
	        }
	      
	        stmt.close();
	        rs.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
		return lists;
		
	}
	
	java.util.List<UserAccount> listUsersByListID(long listID){
		
		sql = "SELECT * FROM \"TB_User\" u WHERE u.user_id IN"
		 		+ " (SELECT user_id FROM \"LIST_USER\" WHERE list_id = " + listID + ")";
		
		members = new ArrayList<UserAccount>();
		try {
			
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			UserAccount user;
	        while (rs.next()) {
	             // creating User object
	            
	        	user = new UserAccount();
		        user.setIDUser(rs.getLong("user_id"));
		        user.setCreatedAt(rs.getDate("created_at"));
		        user.setFavouritesCount(rs.getInt("favourites_count"));
		        user.setFriendsCount(rs.getInt("friends_count"));
		        user.setFollowersCount(rs.getInt("followers_count"));
		        user.setLanguage(rs.getString("lang"));
		        user.setLocation(rs.getString("user_location"));
		        user.setListedCount(rs.getInt("listed_count"));
		        user.setName(rs.getString("name"));
		        user.setDescription(rs.getString("description"));
		        user.setUrl(rs.getString("url"));
		        user.setScreenName(rs.getString("screen_name"));
		        user.setStatusesCount(rs.getInt("statuses_count"));
		        user.setVerified(rs.getBoolean("is_verified"));
		        
		        members.add(user);	             
	        }
	      
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		
		return members;
		
	}
	
	public void addIntoList_UserTB(long listID, long userID){
		
		
		long list_id = 0;
		try {
			
			sql = "SELECT list_id FROM \"LIST_USER\" WHERE list_id = " + listID + " AND user_id = " + userID;
			stmt = connection.prepareStatement(sql);			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				list_id = rs.getLong("list_id");
			}
			
			if(list_id == 0){
				sql = "INSERT INTO \"LIST_USER\"(list_id, user_id) VALUES (?, ?)";
		        // prepared statement para inserção
		       stmt = connection.prepareStatement(sql);

		        // seta os valores
		        
		        stmt.setLong(1,listID);
		        stmt.setLong(2,userID);
		       	      	       
		        // executa
		        stmt.execute();
			}
			
			rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
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
