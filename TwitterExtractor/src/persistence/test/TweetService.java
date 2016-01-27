package persistence.test; 

import java.util.Date;

import persistence.dao.HashtagDao;
import persistence.dao.ListDao;
import persistence.dao.TweetDao;
import persistence.dao.URLDao;
import persistence.dao.UserDao;
import persistence.entities.Hashtag;
import persistence.entities.List;
import persistence.entities.Tweet;
import persistence.entities.URL;
import persistence.entities.UserAccount;


public class TweetService {

	public static void main(String args[]){
	
		
		UserAccount u = new UserAccount(616, new Date(System.currentTimeMillis()), 54, 45, 10, "english","bilbao", 85, "lola", "lol", "folle", "mercedes.benz", 5, true, false);
		//daoU.add(u);
		
//		daoL.addIntoList_UserTB(25, 613);		
//		daoL.addIntoList_UserTB(25, 614);
//		daoL.addIntoList_UserTB(26, 615);
		
		
		//User u2 = new User(611, new Date(System.currentTimeMillis()), 54, 45, 10, "english","bilbao", 85, "lola", "lol", "folle", "mercedes.benz", 5, true);
				
		
//		daoL.add(l);
//		
//		l = new List(26, "economia", "geopolitica mundial", u);
//		daoL.add(l);
//		
		List l = new List(28, "futebol", "brasileirao serie a/b", u);
		//daoL.add(l);
		
		//daoU.addIntoUser_ReplyTB(u, u2);
		Tweet t = new Tweet(06, new Date(System.currentTimeMillis()), "mahalo #ehnois", "funkês", 5, true, 0, 0, "", 41, u.getIDUser());
		//daoT.add(t);
		//daoU.addIntoUser_RetweetTB(u, t);
		//daoU.addIntoUser_FavoriteTB(u, t);
//	
//		daoT.add(t);
//		
//		daoT.addIntoTweet_UserTB(t, u);
//		
//		u = daoU.readUserByID(611);
	
//		
//		daoT.addIntoTweet_HashtagTB(t, h);
//		
//		h = new Hashtag(2, "peace");
//		daoH.add(h);
//		
//		daoT.addIntoTweet_HashtagTB(t, h);
		
//		URL url = new URL(4, "https://www.youtube.com/watch?v=Rb0UmrCXxVA");
//		daoURL.add(url);
//		
//		daoT.addIntoTweet_URLTB(t, url);
//		
//		url = new URL(2, "https://twitter.com/");
//		daoURL.add(url);
//		
//		daoT.addIntoTweet_URLTB(t, url);
		
//		u = daoU.getUserByID(612);
//		
//		for(Tweet fav: u.getRetweets()){
//			System.out.println("Tweet ID: " + fav.getTweetID());
//			System.out.println("Retweet publisher ID: " + fav.getTweetPublisher().getIDUser());
//		}
		
		//System.out.println("Replies Size: " + u.getReplies().size());
		
//		for(User m: l.getListMembers()){
//			System.out.println("Member ID: " + m.getIDUser());
//		
		
	}

}