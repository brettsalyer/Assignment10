package userAccount;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class FacebookUser extends UserAccount implements Comparable<FacebookUser>, Cloneable, Serializable {

    private static final long serialVersionUID = -3401368044041692793L;

    private String passwordHint;
    ArrayList<FacebookUser> friends = new ArrayList<FacebookUser>();
    ArrayList<String> likes = new ArrayList<String>();

    public FacebookUser(String username, String password, String passwordHint) {
        super(username, password);
        this.passwordHint = passwordHint;
    }

    void setPasswordHint(String hint) {
        this.passwordHint = hint;
    }
    
    boolean like(String newlike)
    {
        if(likes.size() == 0)
        {
            likes.add(newlike);
            System.out.println("\t You liked "+ newlike+".");
            return true;
        }
        else
        {
            int j = 0;
            int likesSearched = 0;
            boolean endLikeSearch = false;
            do {// friends do-while
                if (newlike.equals(likes.get(j))) {// already your friend
                    System.out.println("\t" + newlike.toString() + " you already liked.");
                    return false;
                } else {// searched user was not friends.get(j)
                    likesSearched++;// add to the total # of friends searched
                    j++;// increase j to search next friend
                }
                if (likesSearched == likes.size()) {// searched through all
                                                                // friends and the user
                                                                // was not in friends
                                                                // list
                    likes.add(newlike);// so we add to friends list
                    System.out.println("\t You liked "+ newlike+".");
                    return true;
                }
            } while (endLikeSearch == false);// will end when endFriendSearch
            return false;    
        }
    }

    boolean friend(FacebookUser newFriend) {
        if (friends.size() == 0) {// no friends
            friends.add(newFriend);
            System.out.println("\t" + newFriend.toString() + " is now your friend.");// add
                                                                                        // the
                                                                                        // friend
                                                                                        // to
                                                                                        // newUser's
                                                                                        // friend
            return true;                                                                            // list
        } else {
            int j = 0;
            int friendsSearched = 0;
            boolean endFriendSearch = false;
            do {// friends do-while
                if (newFriend.equals(friends.get(j))) {// already your friend
                    System.out.println("\t" + newFriend.toString() + " is already your friend.");
                    return false;
                } else {// searched user was not friends.get(j)
                    friendsSearched++;// add to the total # of friends searched
                    j++;// increase j to search next friend
                }
                if (friendsSearched == friends.size()) {// searched through all
                                                        // friends and the user
                                                        // was not in friends
                                                        // list
                    friends.add(newFriend);// so we add to friends list
                    System.out.println("\t" + newFriend.toString() + " is now your friend.");
                    return true;
                }
            } while (j < friends.size());
            
            // will end when endFriendSearch            
            // is true
            return false;
        }

    }

    boolean defriend(FacebookUser formerFriend) {
        if (friends.size() == 0) {// no friends
            System.out.println("\tYou have no friends.");
            return false;
        } else {
            int j = 0;
            int friendsSearched = 0;
            boolean endFriendSearch = false;
            do {// friends do-while
                if (formerFriend.equals(friends.get(j))) {// already your friend
                    friends.remove(formerFriend);// removes friends.get(j) from
                                                    // newUser's friend list
                    System.out.println("\t" + formerFriend.toString() + " has been removed from your friends.");
                    return true;
                } else {// searched user was not friends.get(j)
                    friendsSearched++;// add to the total # of friends searched
					j++;// increase j to search next friend
				}
				if (friendsSearched == friends.size()) {// searched through all
														// friends and the user
														// was not in friends
														// list
					System.out.println("\t" + formerFriend.toString() + " is not your friend.");
					return false;
				}
			} while (j < friends.size());// will end when endFriendSearch
												// is true
												
			return false;
		}
	}

	ArrayList<FacebookUser> getFriends() {
		if (friends.size() == 0) {
			return null;
		} else {
			Collections.sort(friends);
			ArrayList<FacebookUser> displayFriends = new ArrayList<FacebookUser>();
			;
			for (FacebookUser friend : friends) {
				displayFriends.add(friend);
			}
			return displayFriends;
		}
	}
	
	public void removeLike(String s){
	    likes.remove(s);
	}

	@Override
	public int compareTo(FacebookUser o) {
		if (toString().compareToIgnoreCase(o.toString()) != 0) {
			return toString().compareToIgnoreCase(o.toString());
		}
		return 0;
	}

	@Override
	public void getPasswordHelp() {
		System.out.println("\tHint: " + passwordHint);

	}

}
