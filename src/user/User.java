package user;
import utilities.Scan;
public class User {
    private String userID;
    private String password;
    private String email;
    private String name;

    public User(String userID, String email, String name, String password)
    {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public boolean login(String password) {
        if(password.equals(this.password))
        {
            return true;
        }
        else
        {
            System.out.println("Wrong Password");
            return false;
        }
    }

    public void changePassword() 
    {  
        Scan sc = Scan.getInstance();
        System.out.println("Input current password");
        String curpassword = sc.next();
        String newpassword;
        while (!this.login(curpassword))
            {
                System.out.println("Input current password");
                curpassword = sc.next();
            }
        System.out.println("Enter the new password");
        newpassword = sc.next();
        while(curpassword.equals(newpassword)) {
            System.out.println("New password is the same as the previous one. Try again.");
            System.out.println("Enter the new password");
            newpassword = sc.next();
        }
        System.out.println("Password changed successfully");
        this.password = newpassword;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getPassword() {
        return this.password;
    }
}
