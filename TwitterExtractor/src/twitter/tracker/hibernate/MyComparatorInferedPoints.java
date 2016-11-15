package twitter.tracker.hibernate;
import java.util.Comparator;

import persistence.entities.hibernate.UserInference;


public class MyComparatorInferedPoints implements Comparator<UserInference> {

	@Override
	public int compare(UserInference inf1, UserInference inf2) {
		// TODO Auto-generated method stub
		return inf1.getInferedPoints() - inf2.getInferedPoints();
	}

}
