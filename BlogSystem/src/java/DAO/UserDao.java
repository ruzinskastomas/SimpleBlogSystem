package DAO;

import java.sql.*;
import Business.*;

public class UserDao extends Dao implements UserDaoInterface
{
    // When using the UserDao you must supply the name of the database to access
    public UserDao(String dbName)
    {
        super(dbName);
    }
    
    /**
     * Find a specific <code>User</code> in the database matching a supplied 
     * username and password.
     * @param uname The username of the <code>User</code> being searched for
     * @param pword The password of the <code>User</code> being searched for
     * @return The <code>User</code> object matching the supplied information. If
     * no match is found for the supplied information, then the object will be null.
     */
    public User findUserByUsernamePassword(String uname, String pword)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User u = null;
        try 
        {
            con = this.getConnection();
            
            String query = "SELECT * FROM users WHERE USERNAME = ? AND PASSWORD = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, uname);
            ps.setString(2, pword);
            
            rs = ps.executeQuery();
            if (rs.next()) 
            {
                String firstname = rs.getString("firstName");
                String lastname = rs.getString("lastName");
                String username = rs.getString("username");
                String password = rs.getString("password");
                u = new User(username, password, firstname, lastname);
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the findUserByUsernamePassword method:");
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
                System.err.println("A problem occurred when closing down the findUserByUsernamePassword method:\n" + e.getMessage());
            }
        }
        return u;     // u may be null 
    }
    
    
    /**
     * Find the first <code>User</code> matching a specified username. If more than one 
     * <code>User</code> matching the username is found, only the first match will be returned.
     * @param uname The username of the <code>User</code> being searched for.
     * @return A <code>User</code> matching the specified username, otherwise null.
     */
    public User findUserByUsername(String uname)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User u = null;
        try {
            con = this.getConnection();
            
            String query = "SELECT * FROM users WHERE USERNAME = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, uname);
            
            rs = ps.executeQuery();
            if (rs.next()) 
            {
                String firstname = rs.getString("firstName");
                String lastname = rs.getString("lastName");
                String username = rs.getString("username");
                String password = rs.getString("password");
                u = new User(username, password, firstname, lastname);
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the findUserByUsername method:");
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
                System.err.println("A problem occurred when closing down the findUserByUsername method:\n" + e.getMessage());
            }
        }
        return u;     // u may be null 
    } 
    
    /**
     * Add a new <code>User</code> to the database.
     * @param u The <code>User</code> to be added to the database.
     * @return True if the <code>User</code> was successfully added to the database, false otherwise.
     */
    public boolean addUser(User u)
    {
        Connection con = null;
        PreparedStatement ps = null;
        if(findUserByUsername(u.getUsername()) == null)
        {
            try {
                con = this.getConnection();

                String query = "INSERT INTO users(username, password, firstName, lastName) VALUES (?, ?, ?, ?)";
                ps = con.prepareStatement(query);
                ps.setString(1, u.getUsername());
                ps.setString(2, u.getPassword());
                ps.setString(3, u.getFirstName());
                ps.setString(4, u.getLastName());

                ps.execute();
            } 
            catch (SQLException e) 
            {
                System.err.println("\tA problem occurred during the addUser method:");
                System.err.println("\t"+e.getMessage());
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
                    System.err.println("A problem occurred when closing down the addUser method:\n" + e.getMessage());
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Remove a <code>User</code> from the database.
     * @param u The <code>User</code> to be removed from the database.
     * @return True if the <code>User</code> could be removed, false otherwise.
     */
    public boolean removeUser(User u)
    {
        Connection con = null;
        PreparedStatement ps = null;
        boolean removed = false;
        try {
            con = this.getConnection();

            String query = "DELETE FROM users WHERE username = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, u.getUsername());

            int rowsAffected = ps.executeUpdate();
            if(rowsAffected != 0)
            {  
                removed = true;
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the removeUser method:");
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
                System.err.println("A problem occurred when closing down the removeUser method:\n" + e.getMessage());
            }
        }
        return removed;
    }
    
    // Sample code showing these methods in use.
    public static void main(String [] args)
    {
        UserDao uDAO = new UserDao("MyBlog");
        try{
            // Try to find an existing user
            User u = uDAO.findUserByUsernamePassword("Steph", "password");
            System.out.println(u); 
            
            // Try to find a user that doesn't exist
            u = uDAO.findUserByUsernamePassword("zzzzzzzzz", "password");
            System.out.println(u); 
            
            u = new User("alexis", "password", "Michelle", "Addison");
            uDAO.addUser(u);
            
            u = uDAO.findUserByUsername("alexis");
            System.out.println(u); 
            
            u = uDAO.findUserByUsername("Charles");
            System.out.println(u); 
            
            // Try to remove a user that doesn't exist
            User noUser = new User("Hannah", "pass", "Hannah", "Abbott");
            boolean removed = uDAO.removeUser(noUser);
            if(removed)
            {
                System.out.println("User " + noUser.getUsername() + " was removed successfully.");
            }
            else
            {
                System.out.println("User " + noUser.getUsername() + " could not be removed.");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
