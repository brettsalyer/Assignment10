package userAccount;
 import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

public class Driver implements Serializable {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Facebook facebook = new Facebook();
        // read facebook.users from file and add to accounts
        File file = new File("FacebookUsers.dat");
        if (file.exists()) {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            try {
                facebook = (Facebook) ois.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("Could not find " + "'" + file + "'");
            } catch (IOException e) {
                System.out.println("There was an IOException error.");
            }
            ois.close();
        }

        else {
            facebook = new Facebook();
        }
        Scanner input = new Scanner(System.in);
        boolean valid = false;
        int option = 0;
        boolean quit = false;
        do {
            do {
                valid = true;
                // display menu options
                System.out.println("Menu:");
                System.out.println("1. List users alphabetically");
                System.out.println("2. List users by number of friends");
                System.out.println("3. Add a user");
                System.out.println("4. Delete a user");
                System.out.println("5. Get password hint");
                System.out.println("6. Add friends");
                System.out.println("7. Remove friends");
                System.out.println("8. List friends");
                System.out.println("9. Recommended friends");
                System.out.println("10. Like");
                System.out.println("11. List likes");
                System.out.println("12. Undo");
                System.out.println("13. Quit");
                System.out.print("Please choose one of the options: ");
                // read in option
                option = input.nextInt();
                // invalid check
                if (option <= 13 && option >= 1) {
                    valid = true;
                } else {
                    System.out.println("\tPlease select a valid option.");
                    System.out.println("");
                    valid = false;
                }
            } while (!valid);

            switch (option) {
            case 1:// List users alphabetically
                facebook.getUsers(0);
                break;
            case 2:// List users by number of friends
                facebook.getUsers(1);
                break;
            case 3:// Add a user
                facebook.addUser();
                break;
            case 4:// Delete a user
                facebook.deleteUser();
                break;
            case 5:// Get password hint
                facebook.getUsersPasswordHelp();
                break;
            case 6:// Add friends
                facebook.friend();
                break;
            case 7:// Remove friends
                facebook.defriend();
                break;
            case 8:// List friends
                facebook.listFriends();
                break;
            case 9:// Recommended friends
                facebook.getRecommendedFriends();
                break;
            case 10://Like
                facebook.like();
                break;
            case 11://List likes
                facebook.listLikes();
                break;
            case 12:
                facebook.undoAction();
                break;
            case 13:// Quit
                System.out.println("Goodbye.");
                quit = true;
                break;
            }
            System.out.println("");
        } while (quit == false);
        input.close();
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Users.dat")));
        try {
            oos.writeObject(facebook);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find " + file + ".");
        } catch (IOException e) {
            System.out.println("There was an IOException error.");
            e.printStackTrace();
        }
        oos.close();
    }
}
