package persistence.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserAccount{
	
	private long idUser;	
    private List<persistence.entities.List> lists;	
    private List<UserAccount> replies;      
    private List<Tweet> tweets;  
    private List<Tweet> retweets;  
    private List<Tweet> favorites;
    private List<UserAccount> followees;
	private Date createdAt;	
	private int favouritesCount;	
	private int friendsCount;
	private int followersCount;	
	private String lang;	
	private String userLocation;	
	private int listedCount;	
	private String name;	
	private String description;	
	private String url;	
	private String screenName;		
	private int statusesCount;	
	private boolean isVerified;
	private boolean targetUser;
	
	
	public UserAccount(){
		
	}
	
	public UserAccount(long idUser,
			Date createdAt, int favouritesCount, int friendsCount,
			int followersCount, String lang, String location,
			int listedCount, String name, String description, String url,
			String screenName, int statusesCount, boolean isVerified, boolean targetUser) {		
		
		this.idUser = idUser;
		this.createdAt = createdAt;
		this.favouritesCount = favouritesCount;
		this.friendsCount = friendsCount;
		this.followersCount = followersCount;
		this.lang = lang;
		this.userLocation = location;
		this.listedCount = listedCount;
		this.name = name;
		this.description = description;
		this.url = url;
		this.screenName = screenName;
		this.statusesCount = statusesCount;
		this.isVerified = isVerified;
		this.setTargetUser(targetUser);
		
		favorites = new ArrayList<Tweet>();
		setFollowees(new ArrayList<UserAccount>());
		retweets = new ArrayList<Tweet>();  
		lists = new ArrayList<persistence.entities.List>();
		tweets = new ArrayList<Tweet>();	
		replies = new ArrayList<UserAccount>();   
		
	}
	public UserAccount(long idUser) {
		this.idUser = idUser;
	}
	
	public long getIDUser() {
		return idUser;
	}
	public void setIDUser(long id) {
		this.idUser = id;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public int getFavouritesCount() {
		return favouritesCount;
	}
	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	public int getFollowersCount() {
		return followersCount;
	}
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	public String getLanguage() {
		return lang;
	}
	public void setLanguage(String language) {
		this.lang = language;
	}
	public String getLocation() {
		return userLocation;
	}
	public void setLocation(String location) {
		this.userLocation = location;
	}
	public int getListedCount() {
		return listedCount;
	}
	public void setListedCount(int listedCount) {
		this.listedCount = listedCount;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public int getStatusesCount() {
		return statusesCount;
	}
	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}
	public boolean isVerified() {
		return isVerified;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	
	public List<UserAccount> getReplies() {
		return replies;
	}
	
	public void setReplies(List<UserAccount> replies){
		this.replies = replies;
	}
	
	public List<persistence.entities.List> getLists(){
		return lists;
	}
	
	public void setLists(List<persistence.entities.List> lists){
		this.lists = lists;
	}
	
	public List<Tweet> getTweets(){
		return tweets;
	}
	
	public List<Tweet> getRetweets(){
		return retweets;
	}
	
	public void setRetweets(List<Tweet> retweets){
		this.retweets = retweets;
	}

	public void setTweets(List<Tweet> tweets){
		this.tweets = tweets;
	}
	
	public List<Tweet> getFavorites(){
		return favorites;
	}
	
	public void setFavorites(List<Tweet> favorites){
		this.favorites = favorites;
	}

	public boolean isTargetUser() {
		return targetUser;
	}

	public void setTargetUser(boolean targetUser) {
		this.targetUser = targetUser;
	}

	public List<UserAccount> getFollowees() {
		return followees;
	}

	public void setFollowees(List<UserAccount> followees) {
		this.followees = followees;
	}


}
