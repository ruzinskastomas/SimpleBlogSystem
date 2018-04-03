package DAO;

import Business.User;

/**
 *
 * @author d00186050
 */
public interface UserDaoInterface 
{
    // Find User based on username and password
    public User findUserByUsernamePassword(String uname, String pword);
    
    // Find first user with that username
    public User findUserByUsername(String uname);
    
    // Add a user to the database
    // This will return true if the user was added to the database
    // and false if the user couldn't be added.
    public boolean addUser(User u);
    
    // Remove user
    // This will return true if the user could be removed from the database
    // and false if the user couldn't be removed.
    public boolean removeUser(User u);
}
