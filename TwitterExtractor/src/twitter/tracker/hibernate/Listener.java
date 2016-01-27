package twitter.tracker.hibernate;
import java.io.IOException;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


public class Listener {

	public static void main(String[] args) throws TwitterException, IOException{
	        StatusListener listener = new StatusListener(){
	     
			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStatus(Status status) {
				// TODO Auto-generated method stub
				System.out.println(status.getUser().getName() + " : " + status.getText());				
			}
			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub
				
			}
	    };
	    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	    twitterStream.addListener(listener);
	    FilterQuery query = new FilterQuery();
	    query.follow(new long[]{844652533, 1973235902});
	    twitterStream.filter(query);
	    	   
	}
}
