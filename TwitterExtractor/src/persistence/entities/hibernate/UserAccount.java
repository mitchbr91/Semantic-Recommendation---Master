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
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

@Entity(name = "tb_user")
public class UserAccount{
	

	@Id
	@Column(name = "user_id")
	private long idUser;	
    
	@OneToMany(fetch=FetchType.LAZY, mappedBy="listOwner")
	private List<persistence.entities.hibernate.List> lists;	    

	@OneToMany(fetch=FetchType.LAZY, mappedBy="tweetPublisher")
	private List<Tweet> tweets;  

	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="`USER_RETWEET`",
        joinColumns={@JoinColumn(name="user_id")},
        inverseJoinColumns={@JoinColumn(name="retweet_id")})
    private List<Tweet> retweets;  
    
	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="`USER_FAVORITE`",
        joinColumns={@JoinColumn(name="user_id")},
        inverseJoinColumns={@JoinColumn(name="favorite_id")})
	private List<Tweet> favorites;
    
	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="`USER_REPLY`",
        joinColumns={@JoinColumn(name="user_id")},
        inverseJoinColumns={@JoinColumn(name="reply_id")})    
    private List<Tweet> replies;
	
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="`FOLLOWER_FOLLOWEE`",
        joinColumns={@JoinColumn(name="follower_id")},
        inverseJoinColumns={@JoinColumn(name="followee_id")})    
    private List<UserAccount> followees;
	
    
    //Inferences that have been made
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade=CascadeType.ALL)  
    private List<UserInference> inferences;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade=CascadeType.ALL)
    private List<SemanticRecommendation> semanticRecommendations;
     
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade=CascadeType.ALL)
    private List<RegularRecommendation> regularRecommendations;
    
    //What users have accepted to unfollow
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="`REGULAR_UNFOLLOW`",
        joinColumns={@JoinColumn(name="user_id")},
        inverseJoinColumns={@JoinColumn(name="unfollow_id")})    
    private List<UserAccount> regularUnfollows;
    
  //What users have accepted to unfollow
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="`SEMANTIC_UNFOLLOW`",
        joinColumns={@JoinColumn(name="user_id")},
        inverseJoinColumns={@JoinColumn(name="unfollow_id")})    
    private List<UserAccount> semanticUnfollows;
    
    @Column(name = "created_at")
    @Type(type="date")
    private Date createdAt;	
    
    @Column(name = "favourites_count")
	private int favouritesCount;	
	
    @Column(name = "friends_count")
    private int friendsCount;
	
    @Column(name = "followers_count")
    private int followersCount;	
	
    @Column(name = "lang")
    private String lang;	
	
    @Column(name = "user_location")
    private String userLocation;	
	
    @Column(name = "listed_count")
    private int listedCount;	
	
    @Column(name = "name")
    private String name;	
	
    @Column(name = "description")
    private String description;	
	
    @Column(name = "url")
    private String url;	
    
    @Column(name = "screen_name")
	private String screenName;		
	
    @Column(name = "statuses_count")
    private int statusesCount;	
	
    @Column(name = "is_verified")
	private boolean isVerified;
	
	@Column(name = "target_user")
	private boolean targetUser;
	
	@Column(name = "tweets_collected")
	private boolean tweetsCollected;
	
	@Column(name = "infered")
	private boolean infered;
	
	@Column(name = "infered_points")
	private int inferedPoints;
	
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
		this.targetUser = targetUser;		
		
		favorites = new ArrayList<Tweet>();
		followees = new ArrayList<UserAccount>();
		inferences = new ArrayList<UserInference>();		
		regularRecommendations = new ArrayList<RegularRecommendation>();
		semanticRecommendations = new ArrayList<SemanticRecommendation>();
		regularUnfollows = new ArrayList<UserAccount>();
		semanticUnfollows = new ArrayList<UserAccount>();
		retweets = new ArrayList<Tweet>();  
		lists = new ArrayList<persistence.entities.hibernate.List>();
		tweets = new ArrayList<Tweet>();	
		replies = new ArrayList<Tweet>();   
		
	}
	
	public UserAccount(String screenNanme){
		this.screenName = screenNanme;
		
		favorites = new ArrayList<Tweet>();
		followees = new ArrayList<UserAccount>();
		inferences = new ArrayList<UserInference>();		
		regularRecommendations = new ArrayList<RegularRecommendation>();
		semanticRecommendations = new ArrayList<SemanticRecommendation>();
		regularUnfollows = new ArrayList<UserAccount>();
		semanticUnfollows = new ArrayList<UserAccount>();
		retweets = new ArrayList<Tweet>();  
		lists = new ArrayList<persistence.entities.hibernate.List>();
		tweets = new ArrayList<Tweet>();	
		replies = new ArrayList<Tweet>();   
	}
	
	public UserAccount(long idUser) {
		this.idUser = idUser;
		
		favorites = new ArrayList<Tweet>();
		followees = new ArrayList<UserAccount>();
		inferences = new ArrayList<UserInference>();		
		regularRecommendations = new ArrayList<RegularRecommendation>();
		semanticRecommendations = new ArrayList<SemanticRecommendation>();
		regularUnfollows = new ArrayList<UserAccount>();
		semanticUnfollows = new ArrayList<UserAccount>();
		retweets = new ArrayList<Tweet>();  
		lists = new ArrayList<persistence.entities.hibernate.List>();
		tweets = new ArrayList<Tweet>();	
		replies = new ArrayList<Tweet>();   
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
	
	public List<Tweet> getReplies() {
		return replies;
	}
	
	public void setReplies(List<Tweet> replies){
		this.replies = replies;
	}
		
	public List<persistence.entities.hibernate.List> getLists(){
		return lists;
	}
	
	public void addList(persistence.entities.hibernate.List list){
		if(!lists.contains(list))
			lists.add(list);
	}
	
	public List<Tweet> getTweets(){
		return tweets;
	}
	
	public List<Tweet> getRetweets(){
		return retweets;
	}
		
	public List<Tweet> getFavorites(){
		return favorites;
	}
		
	public boolean isTargetUser() {
		return targetUser;
	}
	
	public void setIsTargetUser(boolean targetUser){
		this.targetUser = targetUser;
	}

	public List<UserAccount> getFollowees() {
		return followees;
	}
	
	public void addFollowee(UserAccount followee){
		if(!followees.contains(followee))
			followees.add(followee);
	}
	
	public List<UserInference> getInferences() {
		return inferences;
	}
	
	public void insertInference(UserInference inference){
		inferences.add(inference);
	}
	
	public List<UserAccount> getRegularUnfollows() {
		return regularUnfollows;
	}
	
	public List<UserAccount> getSemanticUnfollows() {
		return semanticUnfollows;
	}

	
	public List<SemanticRecommendation> getSemanticRecommendations() {
		return semanticRecommendations;
	}
	
	public List<RegularRecommendation> getRegularRecommendations() {
		return regularRecommendations;
	}
	
	public boolean tweetsCollected() {
		return tweetsCollected;
	}

	public void setTweetsCollected(boolean tweetsCollected) {
		this.tweetsCollected = tweetsCollected;
	}
	
	public boolean infered() {
		return infered;
	}

	public void setInfered(boolean infered) {
		this.infered = infered;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idUser ^ (idUser >>> 32));
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
		UserAccount other = (UserAccount) obj;
		if (idUser != other.idUser)
			return false;
		return true;
	}
	
		
	


}
