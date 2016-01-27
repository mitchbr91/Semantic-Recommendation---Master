package persistence.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tweet{


	private long tweetID;	
	private Date createdAt;
	private String text;		
	private String lang;	
	private int retweetCount;	
	private boolean isRetweet;
	private long inReplyToUserId;	
	private long inReplyToStatusId;	
	private String inReplyToScreenName; 
	private int favoriteCount;	
    private List<UserAccount> mentions;			
	private long tweetPublisher;
    private List<URL> urls;	
    private List<Hashtag> hashtags = new ArrayList<Hashtag>();
	
	
	public Tweet(){
		
	}
	
	public Tweet(long tweetID, Date createdAt, String text, String lang, int retweetCount,
			boolean isRetweet, long inReplyToUserId, long inReplyToStatusId,
			String inReplyToScreenName, int favoriteCount, long tweetPublisher) {
		
		this.tweetID = tweetID;
		this.createdAt = createdAt;
		this.text = text;				
		this.lang = lang;
		this.retweetCount = retweetCount;
		this.isRetweet = isRetweet;
		this.inReplyToUserId = inReplyToUserId;
		this.inReplyToStatusId = inReplyToStatusId;
		this.inReplyToScreenName = inReplyToScreenName;
		this.favoriteCount = favoriteCount;
		this.tweetPublisher = tweetPublisher;	
		
		mentions = new ArrayList<UserAccount>();
		urls = new ArrayList<URL>();	
		
	}	
	
	public long getTweetID() {
		return tweetID;
	}
	public void setTweetID(long id) {
		this.tweetID = id;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getLanguage() {
		return lang;
	}
	public void setLanguage(String language) {
		this.lang = language;
	}
	public int getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}
	public boolean isRetweet() {
		return isRetweet;
	}
	public void setRetweet(boolean isRetweet) {
		this.isRetweet = isRetweet;
	}
	public long getInReplyToUserId() {
		return inReplyToUserId;
	}
	public void setInReplyToUserId(long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}
	public long getInReplyToStatusId() {
		return inReplyToStatusId;
	}
	public void setInReplyToStatusId(long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}
	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}
	public int getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public long getTweetPublisher() {
		return tweetPublisher;
	}


	public void setTweetPublisher(long tweetPublisher) {
		this.tweetPublisher = tweetPublisher;
	}
	
	public List<UserAccount> getMentions(){
		return mentions;
	}
	
	public void setMention(List<UserAccount> mentions){
		this.mentions = mentions;
	}
	
	
	public List<Hashtag> getHashtags(){
		return hashtags;
	}	
	
	public void setHashtags(List<Hashtag> hashtags){
		this.hashtags = hashtags;
	}
	
	public List<URL> getURLs(){
		return urls;
	}
	
	public void setURLS(List<URL> urls){
		this.urls = urls;
	}

	
	
}
