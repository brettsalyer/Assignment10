package userAccount; 


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Stack;

public class Facebook implements Comparable<FacebookUser>, Cloneable, Serializable {

    private static final long serialVersionUID = 409677907447106034L;

    transient Scanner input;

    ArrayList<FacebookUser> users = new ArrayList<FacebookUser>();
    
    /*EXPLANATION: I've decided to use a stack to store the necessary
     * data for undoing actions. This data structure works because we
     * only ever need to see the most recent action, which happens to
     * be whatever is on top of the stack. Each element of the stack
     * is an instance of UndoData, a class that stores the user who
     * did the action, the object affected by the action and a code
     * to identify the action performed.
     */
    
    Stack<UndoData> undoData = new Stack<UndoData>();
    HashMap<String, Integer> likes = new HashMap<String, Integer>();
    
    ArrayList<FacebookUser> recommendedFriends;

   void getUsers(int option) {
        System.out.println("");
        if (users.size() == 0) {
            displayNoUsers();
        } else {
            try {
                if (option == 1) {
                    Collections.sort(users, new FacebookUserComparatorFriends());
                } else {
                    Collections.sort(users);
                }
                ArrayList<FacebookUser> displayUsers = new ArrayList<FacebookUser>();
                
                for (FacebookUser user : users) {
                    displayUsers.add(user);
                }
                System.out.println("\t" + displayUsers);
            } catch (Exception e) {
                System.out.println("\tThere was an error.");
            }
        }
    }

    void addUser() {
        input = new Scanner(System.in);
        String newUser;
        boolean breakLoop = false;
        boolean makeUser = false;
        do {
            System.out.println("");
            displayInstructions("Enter a username");
            System.out.print("\tUsername: ");
            newUser = input.nextLine();
            int newUserIndex = findUser(newUser);
            if (newUserIndex >= 0) {// username already exists
                System.out.println("\t" + newUser + " already exists.");
                breakLoop = false;
            } else if (newUserIndex == -1) {// username doesn't exist
                breakLoop = true;
                makeUser = true;
            } else if (newUserIndex == -2) {// quit
                displayQuit();
                breakLoop = true;
            } else if (newUserIndex == -3) {// no Facebook users
                makeUser = true;
                breakLoop = true;
            } else if (newUserIndex == -4) {// not valid username
                displayEnterAValidName();
                breakLoop = false;
            }
        } while (breakLoop == false);// exits when newUserName is a correct
                                        // username
        if (makeUser == true) {
            System.out.print("\tPassword: ");
            String PW = input.nextLine();
            System.out.print("\tPassword hint: ");
            String PWHint = input.nextLine();
            FacebookUser createdUser = new FacebookUser(newUser, PW, PWHint);// creates
                                                                                // the
                                                                                // user
            users.add(createdUser);// adds user to users list
            undoData.push(new UndoData(1,createdUser,null));
        }
    }

    void deleteUser() {
        if (users.size() == 0) {
            System.out.println("");
            displayNoUsers();
        } else {
            int login = login();
            if (login >= 0) {
                FacebookUser removed = users.get(login);
                users.remove(login);
                System.out.println("\tYour user has successfully been deleted.");
                undoData.push(new UndoData(2,removed,null));
            } else if (login == -2) {
                displayQuit();
            }
        }
    }

    void getUsersPasswordHelp() {
        if (users.size() == 0) {// no users have been created
            System.out.println("");
            displayNoUsers();
        } else {
            input = new Scanner(System.in);
            String helpUser;
            boolean breakLoop = false;
            do {
                System.out.println("");
                displayInstructions("Enter Username to get password help");
                System.out.print("\tUsername: ");
                helpUser = input.nextLine();
                int helpUserIndex = findUser(helpUser);
                if (helpUserIndex >= 0) {// username exists
                    users.get(helpUserIndex).getPasswordHelp();
                    breakLoop = true;
                } else if (helpUserIndex == -1) {// username doesn't exist
                    displayUserNotFound(helpUser);
                    breakLoop = false;
                } else if (helpUserIndex == -2) {// quit
                    displayQuit();
                    breakLoop = true;
                } else if (helpUserIndex == -4) {
                    displayEnterAValidName();
                }
            } while (breakLoop == false);// exits when helpUserName is a correct
                                            // username
        }
    }
    
    public void like()
    {
        if (users.size() == 0) {
            System.out.println("");
            displayNoUsers();
        } else {
            int userIndex = login();
            if (userIndex >= 0) {
                String newLike;
                System.out.println("");
                displayInstructions("Enter Like to be added");
                System.out.print("\tLike: ");
                newLike = input.nextLine();
                if(users.get(userIndex).like(newLike)){
                    addLike(newLike);
                    undoData.push(new UndoData(5,newLike,users.get(userIndex)));
                }
            }
        
        }
    }

    void addLike(String like)
    {
        Integer value = likes.get(like);
        if(value != null)
        {
            likes.put(like, likes.get(like) +1);
        }
        else
        {
            likes.put(like, 1);
        }
    }
    void listLikes()
    {
        
        /*
         * EXPLANATION: I have chosen a treemap. A treemap provides a means of storing key/value pairs in sorted order and 
         * allows rapid retrieval. 
         */
        Map<String, Integer> treeMap = new TreeMap<String,Integer>(likes);
        for(String key : treeMap.keySet()){
            System.out.println("Like: "+ key + " - "+likes.get(key));
        }
    }
    
    
    
    
    void friend() {
        if (users.size() == 0) {
            System.out.println("");
            displayNoUsers();
        } else {
            int userIndex = login();
            if (userIndex >= 0) {
                String newFriend;
                int newFriendUserIndex;
                boolean breakLoop = false;
                do {
                    System.out.println("");
                    displayInstructions("Enter friend to be added");
                    System.out.print("\tUsername: ");
                    newFriend = input.nextLine();
                    newFriendUserIndex = findUser(newFriend);
                    if (newFriendUserIndex >= 0) {// username exists
                        if(users.get(userIndex).friend(users.get(newFriendUserIndex))){
                            undoData.push(new UndoData(3,users.get(newFriendUserIndex),users.get(userIndex)));
                        }
                        breakLoop = true;
                    } else if (newFriendUserIndex == -1) {// username doesn't
                                                            // exist
                        displayUserNotFound(newFriend);
                        breakLoop = false;
                    } else if (newFriendUserIndex == -2) {// quit
                        displayQuit();
                        breakLoop = true;
                    } else if (newFriendUserIndex == -4) {
                        displayEnterAValidName();
                        breakLoop = false;
                    }
                } while (breakLoop == false);// exits when newUserName is a
                                                // correct username
            } else if (userIndex == -2) {
                displayQuit();
            }
        }
    }

    void defriend() {
        if (users.size() == 0) {
            System.out.println("");
            displayNoUsers();
        } else {
            int userIndex = login();
            if (userIndex >= 0) {
                String formerFriend;
                int formerFriendUserIndex;
                boolean breakLoop = false;
                do {
                    System.out.println("");
                    displayInstructions("Enter friend to be removed");
                    System.out.print("\tUsername: ");
                    formerFriend = input.nextLine();
                    formerFriendUserIndex = findUser(formerFriend);
                    if (formerFriendUserIndex >= 0) {// username exists
                        if(users.get(userIndex).defriend(users.get(formerFriendUserIndex))){
                            undoData.push(new UndoData(4,users.get(formerFriendUserIndex),users.get(userIndex)));
                        }
                        breakLoop = true;
                    } else if (formerFriendUserIndex == -1) {// username doesn't
                                                                // exist
                        displayUserNotFound(formerFriend);
                        breakLoop = false;
                    } else if (formerFriendUserIndex == -2) {// quit
                        displayQuit();
                        breakLoop = true;
                    } else if (formerFriendUserIndex == -4) {
                        displayEnterAValidName();
                        breakLoop = false;
                    }
                } while (breakLoop == false);// exits when newUserName is a
                                                // correct username
            } else if (userIndex == -2) {
                displayQuit();
            }
        }
    }

    void listFriends() {
        if (users.size() == 0) {
            System.out.println("");
            displayNoUsers();
        } else {
            input = new Scanner(System.in);
            int userIndex = login();
            if (userIndex >= 0) {// successful login
                ArrayList<FacebookUser> friends = users.get(userIndex).getFriends();
                if (friends != null) {// you do have friends
                    // System.out.println("\t" +
                    // users.get(userIndex).getFriends());
                    Collections.sort(users.get(userIndex).getFriends());
                    System.out.println("\t" + users.get(userIndex).getFriends());
                } else {// no friends
                    System.out.println("\tYou don't have any friends.");
                }
            } else if (userIndex == -2) {// quit
                displayQuit();
            }
        }
    }

    public int findUser(String username) {
        // returns i if user exists, -1 if user doesn't exist, -2 if quit was
        // chosen, -4 if ""
        int searchedUsers = 0;
        if (username.equals("q")) {
            return -2;
        } else if (username.equals("")) {
            return -4;
        }
        for (int i = 0; i < users.size(); i++) {
            if (username.equals(users.get(i).toString())) {
                return i;
            } else {
                searchedUsers++;
            }
            if (searchedUsers == users.size() && users.size() != 0) {
                return -1;
            }
        }
        return -3;
    }

    public int login() {
        // returns i if correct username & pw, -2 if quit
        input = new Scanner(System.in);
        String username;
        boolean breakLoop = false;
        if (users.size() == 0) {// no users have been created
            System.out.println("");
            displayNoUsers();
            return -2;
        } else {
            do {
                int searchedUsers = 0;
                System.out.println("");
                displayInstructions("Login");
                System.out.print("\tUsername: ");
                username = input.nextLine();

                if (username.equals("")) {// No name was entered
                    displayEnterAValidName();
                    breakLoop = false;
                } else if (username.equals("q")) {// quit option was chosen
                    return -2;
                } else {// there are existing users
                    for (int i = 0; i < users.size(); i++) {
                        if (username.equals(users.get(i).toString())) {// username
                                                                        // exists
                            System.out.print("\tPassword: ");
                            String password = input.nextLine();
                            if (users.get(i).checkPassword(password)) {
                                return i;
                            } else {
                                System.out.println("\tPassword incorrect.");
                                breakLoop = false;
                            }
                        } else {// username does not equal users.get(j)'s
                                // username
                            searchedUsers++;
                            breakLoop = false;
                        }
                        if (searchedUsers == users.size()) {
                            breakLoop = false;
                            System.out.println("\t" + username + " is not a Facebook user.");
                        }
                    }
                }
            } while (breakLoop == false);// exits when username is a user
        }
        return -1;// Should never get here
    }
    
    private FacebookUser loginForUser(){
        int userIndex = login();
        if(userIndex >= 0){
            return users.get(userIndex);
        }else{
            return null;
        }
    }

    public void getRecommendedFriends() {
        int userIndex = login();
        if (userIndex >= 0) {
            recommendedFriends = new ArrayList<FacebookUser>();
            System.out.println("");
            getRecommendedFriends(users.get(userIndex));
            if (recommendedFriends.size() == 0) {
                System.out.println("\t" + users.get(userIndex).toString() + " does not recommend any friends.");
            } else {
                Collections.sort(recommendedFriends, new FacebookUserComparatorFriends());
                // Collections.sort(recommendedFriends);
                ArrayList<FacebookUser> displayrecommendedFriends = new ArrayList<FacebookUser>();
                ;
                for (FacebookUser friend : recommendedFriends) {
                    displayrecommendedFriends.add(friend);
                }
                System.out.println("\t" + displayrecommendedFriends);
            }
        }
    }

    public void getRecommendedFriends(FacebookUser user) {
        if (user.friends.size() == 0) {
            return;
        } else {
            for (int i = 0; i < user.friends.size(); i++) {
                if (!recommendedFriends.contains(user.friends.get(i))) {
                    recommendedFriends.add(user.friends.get(i));

                    getRecommendedFriends(user.friends.get(i));
                }
            }
        }
    }
    
    
    void undoAction(){
        FacebookUser u;
        System.out.println();
        if(undoData.empty()){
            System.out.println("\tNo actions to undo");
            return;
        }
        UndoData data = undoData.peek();
        switch(data.getActionCode()){
        //Undo addUser
        case 1:
            users.remove(data.getActionValue());
            break;
        //Undo removeUser
        case 2:
            users.add((FacebookUser) data.getActionValue());
            break;
        //Undo friendUser
        case 3:
            System.out.println("Enter credentials for user " + data.getUser().toString());
            u = loginForUser();
            if(u == null || u != data.getUser()){
                System.out.println("\tUndo unsuccessful");
                return;
            }
            u.defriend((FacebookUser)data.getActionValue());
            break;
        //Undo defriend
        case 4:
            System.out.println("Enter credentials for user " + data.getUser().toString());
            u = loginForUser();
            if(u == null || u != data.getUser()){
                System.out.println("\tUndo unsuccessful");
                return;
            }
            u.friend((FacebookUser)data.getActionValue());
            break;
        //Undo like
        case 5:
            System.out.println("Enter credentials for user " + data.getUser().toString());
            u = loginForUser();
            if(u == null || u != data.getUser()){
                System.out.println("\tUndo unsuccessful");
                return;
            }
            u.removeLike((String)data.getActionValue());
            
            likes.put((String)data.getActionValue(),likes.get(data.getActionValue()) - 1);
            if(likes.get(data.getActionValue()) == 0){
                likes.remove(data.getActionValue());
            }
        }
        undoData.pop();
        System.out.println("\tLast action undone");
    }
    

    @Override
    public int compareTo(FacebookUser o) {
        if (toString().compareToIgnoreCase(o.toString()) != 0) {
            return toString().compareToIgnoreCase(o.toString());
        }
        return 0;
    }

    void displayQuit() {
        System.out.println("\tReturning to menu...");
    }

    void displayNoUsers() {
        System.out.println("\tThere are no Facebook users.");
    }

    void displayUserNotFound(String username) {
        System.out.println("\t" + username + " was not found.");
    }

    void displayEnterAValidName() {
        System.out.println("\tPlease enter a valid username.");
    }

    void displayInstructions(String prompt) {
        System.out.println("\t" + prompt + ", or enter 'q' to return to menu.");
    }
    
    void displayLiked(String username, String like)
    {
        System.out.println("\t" + username + " likes "+like+".");
    }

}
