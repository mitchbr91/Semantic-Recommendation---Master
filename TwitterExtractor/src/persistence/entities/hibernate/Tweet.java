package persistence.entities.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity(name = "tweet")
public class Tweet{


	@Id
	@Column(name = "tweet_id", unique = true, nullable = false)	
	private long tweetID;	
	
	@Column(name = "created_at")
    @Type(type="date")  
	private Date createdAt;
	
	@Column
	private String text;
	
	@Column
	private String lang;
	
	@Column(name = "retweet_count")
	private int retweetCount;	
	
	@Column(name = "is_retweet")
	private boolean isRetweet;
	
	@Column(name = "in_reply_to_user_id")
	private long inReplyToUserId;	
	
	@Column(name = "in_reply_to_status_id")
	private long inReplyToStatusId;	
	
	@Column(name = "in_reply_to_screen_name")
	private String inReplyToScreenName; 
	
	@Column(name = "favorite_count")
	private int favoriteCount;	
	
	@ManyToOne
    @JoinColumn(name="user_id")
	private UserAccount tweetPublisher;

	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="`TWEET_USER`",
        joinColumns={@JoinColumn(name="tweet_id")},
        inverseJoinColumns={@JoinColumn(name="user_id")})
    private List<UserAccount> mentions;
	
	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="`TWEET_URL`",
        joinColumns={@JoinColumn(name="tweet_id")},
        inverseJoinColumns={@JoinColumn(name="url_id")})
    private List<URL> urls;

	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="`TWEET_HASHTAG`",
        joinColumns={@JoinColumn(name="tweet_id")},
        inverseJoinColumns={@JoinColumn(name="hashtag_id")})
    private List<Hashtag> hashtags;
	
	
	public Tweet(){
		
	}
	
	public Tweet(long tweetID, UserAccount tweetPublisher){
		this.tweetID = tweetID;
		this.tweetPublisher = tweetPublisher;
	}
	
	public Tweet(long tweetID, Date createdAt, String text, String lang, int retweetCount,
			boolean isRetweet, long inReplyToUserId, long inReplyToStatusId,
			String inReplyToScreenName, int favoriteCount, UserAccount tweetPublisher) {
		
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
		hashtags = new ArrayList<Hashtag>();
		
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

	public UserAccount getTweetPublisher() {
		return tweetPublisher;
	}

	public void setTweetPublisher(UserAccount tweetPublisher) {
		this.tweetPublisher = tweetPublisher;
	}	
	
	public List<UserAccount> getMentions(){
		return mentions;
	}
	
	public void addMention(UserAccount mention){
		if(!mentions.contains(mention))
			mentions.add(mention);
	}

	public List<Hashtag> getHashtags(){
		return hashtags;
	}
	
	public void addHashtag(Hashtag hashtag){
		if(!hashtags.contains(hashtag))
			hashtags.add(hashtag);
	}
	
	public List<URL> getURLs(){
		return urls;
	}
	
	public void addURL(URL url){
		if(!urls.contains(url))
			urls.add(url);
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (tweetID ^ (tweetID >>> 32));
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
		Tweet other = (Tweet) obj;
		if (tweetID != other.tweetID)
			return false;
		return true;
	}

	
	
	
}
