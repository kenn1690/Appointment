package Classes;

/**
 * The main purpose of this class is to construct a user.
 * It also sets and gets user name.
 */
public class User  {
    private int userID;
    public static String userName;

    /**
     * Constructor method for User.
     * @param userID user's id
     * @param userName users name
     */
    public User (int userID, String userName){
        this.userID = userID;
        this.userName = userName;
    }

    /**
     * This allows the current user id to be set. It is used
     * in the User DB login portion. After a user enters their username and password correctly,
     * the id will be set and used for last modified and created by fields from DB
     * @param userID users id
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Allows fields to get the username and be used
     * @return username is returned as a string
     */
    public static String getUserName() {
        return userName;
    }
    /**
     * This allows the current user id to be set. It is used
     * in the User DB login portion. After a user enters their username and password correctly,
     * the name will be set and used for last modified and created by fields from DB
     * @param userName username gets set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**This makes sure a newly added appointment can not have the
     * same start or end time as another appointment for the customer.
     * @return user id to string
     * */
    @Override
    public String toString(){
        return Integer.toString(userID);
    }
}
