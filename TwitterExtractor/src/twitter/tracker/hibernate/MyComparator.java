package twitter.tracker.hibernate;
import java.util.Comparator;


public class MyComparator implements Comparator<TwitterAccount> {

	@Override
	public int compare(TwitterAccount account1, TwitterAccount account2) {
		// TODO Auto-generated method stub
		return account1.getInferedPoints() - account2.getInferedPoints();
	}

}
