package userAccount;
 

import java.util.Comparator;

public class FacebookUserComparatorFriends implements Comparator<FacebookUser> {

	public int compare(FacebookUser u1, FacebookUser u2) {
		// TODO Auto-generated method stub
		int a, b;
		a = 0;
		b = 0;
		if (u1.getFriends() != null) {
			a = u1.getFriends().size();
		}
		if (u2.getFriends() != null) {
			b = u2.getFriends().size();
		}
		return b - a;
	}
}
