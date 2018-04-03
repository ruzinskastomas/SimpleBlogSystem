package DAO;

import Business.Friendship;
import Business.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author d00186050
 */
public class FriendshipDao extends Dao implements FriendshipDaoInterface
{
    // When using the FriendshipDao you must supply the name of the database to access
    public FriendshipDao(String dbName)
    {
        super(dbName);
    }
    
    /**
     * Add a new <code>Friendship</code> to the database
     * @param username1 User1 in the <code>Friendship</code> (order is irrelevant)
     * @param username2 User2 in the <code>Friendship</code> (order is irrelevant)
     * @return 0 if the <code>Friendship</code> was not added to the database, 1 if the add was successful
     */
    public int addFriendship(String username1, String username2)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;
        
        try{
            con = this.getConnection();

            String query = "INSERT INTO friends(friend1, friend2) VALUES (?, ?)";
            ps = con.prepareStatement(query);
            ps.setString(1, username1);
            ps.setString(2, username2);

            rowsAffected = ps.executeUpdate();
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the addFriendship method:");
            System.err.println("\t"+e.getMessage());
            rowsAffected = 0;
        } 
        finally 
        {
            try 
            {
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                System.err.println("A problem occurred when closing down the addFriendship method:\n" + e.getMessage());
            }
        }
        return rowsAffected;
    }
    
    
    /**
     * Remove a <code>Friendship</code> from the database
     * @param username1 User1 in the <code>Friendship</code> (order is irrelevant)
     * @param username2 User2 in the <code>Friendship</code> (order is irrelevant)
     * @return true if the <code>Friendship</code> was removed successfully, false otherwise
     */
    public boolean removeFriendship(String username1, String username2)
    {
        Connection con = null;
        PreparedStatement ps = null;
        boolean removed = false;
      
        try {
            con = this.getConnection();

            String query = "DELETE FROM friends WHERE (friend1 = ? AND friend2 = ?) OR (friend1 = ? AND friend2 = ?)";
            ps = con.prepareStatement(query);
            ps.setString(1, username1);
            ps.setString(2, username2);
            ps.setString(3, username2);
            ps.setString(4, username1);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 0)
            {
                removed = true;
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the removeFriendship method:");
            System.err.println("\t"+e.getMessage());
            removed = false;
        } 
        finally 
        {
            try 
            {
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                System.err.println("A problem occurred during closing down the removeFriendship method:\n" + e.getMessage());
            }
        }
        return removed;
    }
    
    
    /**
     * Remove all <code>Friendships</code> for a specific user
     * @param username1 The name of the user to remove all <code>Friendships</code> for
     * @return True if at least one <code>Friendship</code> was successfully removed from the 
     * database, false otherwise
     */
    public boolean removeUserFriends(String username1)
    {
        Connection con = null;
        PreparedStatement ps = null;
        boolean removed = false;
      
        try {
            con = this.getConnection();

            String query = "DELETE FROM friends WHERE friend1 = ? OR friend2 = ? ";
            ps = con.prepareStatement(query);
            ps.setString(1, username1);
            ps.setString(2, username1);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 0)
            {
                removed = true;
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the removeUserFriends method:");
            System.err.println("\t"+e.getMessage());
            removed = false;
        } 
        finally 
        {
            try 
            {
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                System.err.println("A problem occurred in closing down the removeUserFriends method: \n" + e.getMessage());
            }
        }
        return removed;
    }
    
    
    /**
     * Retrieve all <code>Friendships</code> in the database for a specific User
     * @param username The name of the user whose <code>Friendships</code> are 
     * being retrieved
     * @return An <code>ArrayList</code> of <code>Friendships</code> attached to 
     * the supplied username. This will be empty if there were no <code>Friendships</code> 
     * found for the supplied username.
     */
    public ArrayList<Friendship> findFriendshipsByUsername(String username)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Friendship> friends = new ArrayList<Friendship>();
        try 
        {
            con = this.getConnection();
            
            // Get the member's details
            UserDao userDao = new UserDao("MyBlog");
            User user = userDao.findUserByUsername(username);
            
            String query = "SELECT * FROM friends WHERE friend1 = ? OR friend2 = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, username);
            
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                // Get the username of the friend
                // Need to make sure we're not looking at this user's username
                String friend = rs.getString("friend1");
                if(friend.equals(username))
                {
                    friend = rs.getString("friend2");
                }
                // Get the details for the friend of this user
                User userFriend = userDao.findUserByUsername(friend);
                
                // Make a friendship & add it to the list.
                Friendship f = new Friendship(user, userFriend);
                friends.add(f);
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the findFriendshipsByUsername method:");
            System.err.println("\t"+e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (rs != null) 
                {
                    rs.close();
                }
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                System.err.println("A problem occurred when closing down the findFriendshipsByUsername method:\n" + e.getMessage());
            }
        }
        return friends;
    }
    
    
    /**
     * Check for the existance of a <code>Friendship</code> between two users
     * @param username1 User1 in the <code>Friendship</code> (order is irrelevant)
     * @param username2 User1 in the <code>Friendship</code> (order is irrelevant)
     * @return The <code>Friendship</code> object for that pairing if a 
     * <code>Friendship</code> match exists in the database, otherwise null.
     */
    public Friendship checkFriendshipStatus(String username1, String username2)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Friendship friends = null;
        try 
        {
            con = this.getConnection();
                        
            String query = "SELECT * FROM friends WHERE (friend1 = ? AND friend2 = ?) OR (friend1 = ? AND friend2 = ?)";
            ps = con.prepareStatement(query);
            ps.setString(1, username1);
            ps.setString(2, username2);
            ps.setString(3, username2);
            ps.setString(4, username1);
            
            rs = ps.executeQuery();
            if (rs.next()) 
            {
                String uname1 = rs.getString("friend1");
                String uname2 = rs.getString("friend2");
                // Get the details of each User in the Friendship based on the usernames
                UserDao userDao = new UserDao("MyBlog");
                User friend1 = userDao.findUserByUsername(uname1);
                User friend2 = userDao.findUserByUsername(uname2);
                             
                friends = new Friendship(friend1, friend2);
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the checkFriendshipStatus method:");
            System.err.println("\t"+e.getMessage());
            friends = null;
        } 
        finally 
        {
            try 
            {
                if (rs != null) 
                {
                    rs.close();
                }
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                System.err.println("A problem occurred when closing down the checkFriendshipStatus method:\n" + e.getMessage());
            }
        }
        return friends;     // friends may be null 
    }

    public static void main (String [] args)
    {
        FriendshipDao friendsDao = new FriendshipDao("MyBlog");
        try{
            /*
            *   DEMONSTRATING SEARCH METHODS
                1) Search for a specific user's friendships
                2) Search for a specific friendship based on the usernames of the Users involved.
            */
            
            // 1) Searching for a specific User's friendships
            System.out.println("\n++++++++++++++Demonstrating searching for a specific user's friends++++++++++++++");
            User user1 = new User("Jedwards", "password", "James", "Edwards");
            ArrayList<Friendship> friendsList = friendsDao.findFriendshipsByUsername(user1.getUsername());
            
            // Display results
            if(friendsList.size() > 0)
            {
                System.out.println("Success! The following friends were found for that user:");
                System.out.println("==================================================");
                int i = 1;
                for(Friendship friendship: friendsList)
                {
                    // Make sure we print out the friends of the user, not the user itself
                    User f = friendship.getUser1();
                    if(f.equals(user1))
                    {
                        f = friendship.getUser2();
                    }
                    
                    System.out.println("Friendship #" + i + ":");
                    System.out.println("\tUsername:\t\t" + f.getUsername());
                    System.out.println("==================================================");
                    i++;
                }
            }
            else
            {
                System.out.println("There were no friends found for that User. (" + user1.getUsername() + ").");
            }
            
            System.out.println("\nTrying again with a different user.");
            User user2 = new User("Rick", "password", "Rick", "Riordan");
            friendsList = friendsDao.findFriendshipsByUsername(user2.getUsername());
            
            // Display results
            if(friendsList.size() > 0)
            {
                System.out.println("Success! The following frinds were found for that user:");
                System.out.println("==================================================");
                int i = 1;
                for(Friendship friendship: friendsList)
                {
                    // Make sure we print out the friends of the user, not the user itself
                    User f = friendship.getUser1();
                    if(f.equals(user2))
                    {
                        f = friendship.getUser2();
                    }
                    
                    System.out.println("Friendship #" + i + ":");
                    System.out.println("\tUsername:\t\t" + f.getUsername());
                    System.out.println("==================================================");
                    i++;
                }
            }
            else
            {
                System.out.println("There were no friends found for that User. (" + user2.getUsername() + ").");
            }
            
            // 2) Search for a specific friendship based on the usernames of the Users involved.
            System.out.println("\n++++++++++++++Demonstrating searching for a specific friendship++++++++++++++");
            System.out.println("\nChecking database to see if " + user2.getUsername() + " is friends with " + user1.getUsername() + "...");
            Friendship pairOfFriends = friendsDao.checkFriendshipStatus(user2.getUsername(), user1.getUsername());
            if(pairOfFriends == null)
            {
                System.out.println(user2.getUsername() + " is NOT friends with " + user1.getUsername() + ".");
            }else
            {
                System.out.println(user2.getUsername() + " IS friends with " + user1.getUsername() + ".");
            }
            
            System.out.println("\nTrying again with a different user...");
            
            User user3 = new User("Charles", "password", "Charles", "Dickens");
            pairOfFriends = friendsDao.checkFriendshipStatus(user2.getUsername(), user3.getUsername());
            if(pairOfFriends == null)
            {
                System.out.println(user2.getUsername() + " is NOT friends with " + user3.getUsername() + ".");
            }else
            {
                System.out.println(user2.getUsername() + " IS friends with " + user3.getUsername() + ".");
            }
            
            /*
            *
                Demonstrating ADDING a friendship to the database
            *
            */
            System.out.println("\n++++++++++++++Demonstrating adding a friendship++++++++++++++");
            // First add Jedwards to the database
            UserDao userDao = new UserDao("MyBlog");
            boolean verdict = userDao.addUser(user1);
            // Check if the add was successful
            if(verdict)
            {
                // If the user was added to the database, try to add a friendship
                int result = friendsDao.addFriendship(user1.getUsername(), user2.getUsername());
                if( result != 0 )
                {
                    System.out.println("The friendship was successfully added to the database - " + user1.getUsername() + " and " + user2.getUsername() + " are now friends");
                    System.out.println("\tDouble-checking by checking the database for a friendship between these users");
                    pairOfFriends = friendsDao.checkFriendshipStatus(user2.getUsername(), user1.getUsername());
                    if(pairOfFriends == null)
                    {
                        System.out.println("\t" + user2.getUsername() + " is NOT friends with " + user1.getUsername() + ".");
                    }else
                    {
                        System.out.println("\t" + user2.getUsername() + " IS friends with " + user1.getUsername() + ".");
                    }
                }
                else{
                    System.out.println("The friendship was not added to the database");
                }
            }else
            {
                System.out.println("User already in the database.");
            }
            System.out.println("");
            
            System.out.println("\n++++++++++++++Demonstrating adding a friendships for a user when the friend doesn't exist++++++++++++++");
            System.out.println("Trying to make a friendship between " + user1.getUsername() + " and \"Technician\".");
            int result = friendsDao.addFriendship(user1.getUsername(), "Technician");
            if( result != 0 )
            {
                System.out.println("The friendship was successfully added to the database - " + user1.getUsername() + " and " + user2.getUsername() + " are now friends");
                System.out.println("\tDouble-checking by checking the database for a friendship between these users");
                pairOfFriends = friendsDao.checkFriendshipStatus("Technician", user1.getUsername());
                if(pairOfFriends == null)
                {
                    System.out.println("\tTechnician is NOT friends with " + user1.getUsername() + ".");
                }else
                {
                    System.out.println("\tTechnician IS friends with " + user1.getUsername() + ".");
                }
            }
            else{
                System.out.println("The friendship was not added to the database");
            }
            
            /*
            *
                Demonstrating REMOVING a friendship between two users from the database
            *
            */
            System.out.println("\n++++++++++++++Demonstrating removing a friendship between two users++++++++++++++");
            
            verdict = friendsDao.removeFriendship(user1.getUsername(), user2.getUsername());
            if(verdict)
            {
                System.out.println("Friendship between " + user1.getUsername() + " and " + user2.getUsername() + " was successfully removed");
                System.out.println("\tDouble-checking by checking the database for a friendship between these users");
                pairOfFriends = friendsDao.checkFriendshipStatus(user1.getUsername(), user2.getUsername());
                if(pairOfFriends == null)
                {
                    System.out.println("\t" + user2.getUsername() + " is NOT friends with " + user1.getUsername() + ".");
                }else
                {
                    System.out.println("\t" + user2.getUsername() + " IS friends with " + user1.getUsername() + ".");
                }
            }
            else
            {
                System.out.println("Friendship between " + user1.getUsername() + " and " + user2.getUsername() + " was not removed.");
            }
            
            /*
            *
                Demonstrating REMOVING all friendship for a specific user
            *
            */
            System.out.println("\n++++++++++++++Demonstrating removing all friendships for a user++++++++++++++");
            verdict = friendsDao.removeUserFriends(user2.getUsername());
            
            if(verdict)
            {
                System.out.println("All friendships for " + user2.getUsername() + " were successfully removed");
                System.out.println("\tDouble-checking by checking the database for any friendship involving this user");
                friendsList = friendsDao.findFriendshipsByUsername(user2.getUsername());
                
                // Display results
                if(friendsList.size() > 0)
                {
                    System.out.println("Success! The following frinds were found for that user:");
                    System.out.println("==================================================");
                    int i = 1;
                    for(Friendship friendship: friendsList)
                    {
                        // Make sure we print out the friends of the user, not the user itself
                        User f = friendship.getUser1();
                        if(f.equals(user2))
                        {
                            f = friendship.getUser2();
                        }

                        System.out.println("Friendship #" + i + ":");
                        System.out.println("\tUsername:\t\t" + f.getUsername());
                        System.out.println("==================================================");
                        i++;
                    }
                }
                else
                {
                    System.out.println("\tThere were no friends found for that User. (" + user2.getUsername() + ").");
                }
            }
            else
            {
                System.out.println("No Friendships removed for " + user2.getUsername() + ".");
            }
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
