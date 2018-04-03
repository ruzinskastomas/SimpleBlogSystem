package DAO;

import Business.BlogEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BlogEntryDao extends Dao implements BlogEntryDaoInterface
{
    // When using the BlogEntryDao you must supply the name of the database to access
    public BlogEntryDao(String dbName)
    {
        super(dbName);
    }
    
    /**
     * Add a new BlogEntry to the database.
     * @param username Username of the <code>User</code> creating the new entry.
     * @param title Title of the <code>BlogEntry</code> being added to the database.
     * @param content Content/text included in the <code>BlogEntry</code> being 
     * added to the database.
     * @return The id of the new <code>BlogEntry</code> in the database if it 
     * could be added successfully. Otherwise it will return -1.
     */
    public int addBlogEntry(String username, String title, String content) 
    {
        Connection con = null;
        PreparedStatement ps = null; 
        ResultSet generatedKeys = null;
        int newId = -1;
        try {
            con = this.getConnection();

            String query = "INSERT INTO blog_entries(username, title, content) VALUES (?, ?, ?)";
            // Need to get the id back, so have to tell the database to return the id it generates
            ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, username);
            ps.setString(2, title);
            ps.setString(3, content);
            ps.executeUpdate();
            
            // Find out what the id generated for this entry was
            generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next())
            {
                newId = generatedKeys.getInt(1);
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the addBlogEntry method:");
            System.err.println("\t"+e.getMessage());
            newId = -1;
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
                System.err.println("A problem occurred when closing down the addBlogEntry method:\n" + e.getMessage());
            }
        }
        return newId;
    }
    
    
    /**
     * Remove a <code>BlogEntry</code> from the database.
     * @param id The id of the <code>BlogEntry</code> to be removed.
     * @return 1 if the delete was successful, 0 otherwise.
     */
    public int removeBlogEntry(int id)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;
      
        try {
            con = this.getConnection();

            String query = "DELETE FROM blog_entries WHERE entryID = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, id);

            rowsAffected = ps.executeUpdate();
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the removeBlogEntry method:");
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
                System.err.println("A problem occured when closing down the removeBlogEntry method:\n" + e.getMessage());
            }
        }
        return rowsAffected;
    }    

    
    /**
     * Find all <code>BlogEntry</code> information written by a specific author
     * @param author The username of the <code>User</code> who wrote the <code>BlogEntries</code>
     * @return An <code>ArrayList</code> of all <code>BlogEntries</code> associated 
     * with the given username. If there were no <code>BlogEntries</code> found for 
     * that username, this list will be empty.
     */
    public ArrayList<BlogEntry> findBlogEntriesByAuthor(String author)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<BlogEntry> entries = new ArrayList<BlogEntry>();
        try 
        {
            con = this.getConnection();
            
            String query = "SELECT * FROM blog_entries WHERE username = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, author);
            
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                int entryID = rs.getInt("entryID");
                String username = rs.getString("username");
                String title = rs.getString("title");
                String content = rs.getString("content");
                BlogEntry b = new BlogEntry(entryID, username, title, content);
                entries.add(b);
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the findBlogEntriesByAuthor method:");
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
                System.err.println("A problem occurred when closing down the findBlogEntryByAuthor method:\n" + e.getMessage());
            }
        }
        return entries;     // entries may be empty 
    }

    
    /**
     * Find a specific <code>BlogEntry</code> based on its entryId information
     * @param id The entryId of the <code>BlogEntry</code> being searched for.
     * @return The <code>BlogEntry</code> matching the supplied entryId, otherwise null.
     */
    public BlogEntry findBlogEntryByID(int id)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BlogEntry b = null;
        try 
        {
            con = this.getConnection();
            
            String query = "SELECT * FROM blog_entries WHERE entryID = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            
            rs = ps.executeQuery();
            if (rs.next()) 
            {
                int entryID = rs.getInt("entryID");
                String username = rs.getString("username");
                String title = rs.getString("title");
                String content = rs.getString("content");
                b = new BlogEntry(entryID, username, title, content);
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the findBlogEntryByID method:");
            System.err.println("\t"+e.getMessage());
            b = null;
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
                System.err.println("A problem occurred when closing down the findBlogEntryByID method:\n" + e.getMessage());
            }
        }
        return b;     // b may be null 
    }
    
    /**
     * Find the first <code>BlogEntry</code> matching a specified title.
     * @param searchTitle The title to search for.
     * @return The <code>BlogEntry</code> matching the specified title. If 
     * more than one <code>BlogEntry</code> match is found, the first <code>BlogEntry</code>
     * is returned.
     */
    public BlogEntry findBlogEntryByTitle(String searchTitle)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BlogEntry b = null;
        try 
        {
            con = this.getConnection();
            
            String query = "SELECT * FROM blog_entries WHERE title = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, searchTitle);
            
            rs = ps.executeQuery();
            if (rs.next()) 
            {
                int entryID = rs.getInt("entryID");
                String username = rs.getString("username");
                String title = rs.getString("title");
                String content = rs.getString("content");
                b = new BlogEntry(entryID, username, title, content);
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("\tA problem occurred during the findBlogEntryByID method:");
            System.err.println("\t"+e.getMessage());
            b = null;
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
                System.err.println("A problem occurred when closing down the findBlogEntryByID method:\n" + e.getMessage());
            }
        }
        return b;     // b may be null 
    }
    
    /**
     * Retrieve all <code>BlogEntries</code> in the database
     * @return An <code>ArrayList</code> of all <code>BlogEntry</code> information
     * currently in the database.
     */
    public ArrayList<BlogEntry> findAllBlogEntries()
    {
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<BlogEntry> entries = new ArrayList<BlogEntry>();
        
        try 
        {
            //Get connection object using the methods in the super class (Dao.java)...
            con = this.getConnection();

            String query = "SELECT * FROM blog_entries";
            ps = con.prepareStatement(query);
            
            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                int entryID = rs.getInt("entryID");
                String username = rs.getString("username");
                String title = rs.getString("title");
                String content = rs.getString("content");
                BlogEntry b = new BlogEntry(entryID, username, title, content);
                entries.add(b);
            }
        } 
        catch (SQLException e) 
        {            
            System.err.println("\tA problem occurred during the findAllBlogEntries method:");
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
                System.err.println("A problem occurred when closing down the findAllBlogEntries() method:\n" + e.getMessage());
            }
        }
        return entries;     // may be empty
    }

    // Sample code showing these methods in use.
    public static void main(String [] args)
    {
        BlogEntryDao blogDAO = new BlogEntryDao("MyBlog");
        try{
            /*
            *   DEMONSTRATING SEARCH METHODS
                1) Searching by title
                2) Searching by author
                3) Searching by id
                4) Getting all entries in the database
            */
            // 1) Searching by title
            System.out.println("Checking database for blog entry titled \"My Test Entry\"...");
            BlogEntry b = blogDAO.findBlogEntryByTitle("My Test Entry");
            
            // Display results
            if(b != null)
            {
                System.out.println("Details found for \"My Test Entry\": "); 
                System.out.println("ID: \t\t" + b.getEntryId());
                System.out.println("Author:\t\t" + b.getUsername());
                System.out.println("Title: \t\t" + b.getTitle());
                System.out.println("Content: \t" + b.getContent());
            }
            else
            {
                System.out.println("Cannot find a blog with that title");
            }
            
            System.out.println("");
            System.out.println("Checking database for blog entry titled \"My Tester Entry\"...");
            b = blogDAO.findBlogEntryByTitle("My Tester Entry");
            // Display results
            if(b != null)
            {
                System.out.println("Details found for \"My Test Entry\": "); 
                System.out.println("ID: \t\t" + b.getEntryId());
                System.out.println("Author:\t\t" + b.getUsername());
                System.out.println("Title: \t\t" + b.getTitle());
                System.out.println("Content: \t" + b.getContent());
            }
            else
            {
                System.out.println("Cannot find a blog with that title");
            }
                        
            // 2) Searching by author
            System.out.println("\nChecking the blog entries by user with username \"Michelle\"...");
            ArrayList<BlogEntry> entries = blogDAO.findBlogEntriesByAuthor("Michelle");
            
            // Display results
            if(entries.size() > 0)
            {
                System.out.println("Success! The following entries were found for that username:");
                System.out.println("==================================================");
                int i = 1;
                for(BlogEntry entry: entries)
                {
                    System.out.println("Entry #" + i + ":");
                    System.out.println("\tID: \t\t" + entry.getEntryId());
                    System.out.println("\tAuthor:\t\t" + entry.getUsername());
                    System.out.println("\tTitle: \t\t" + entry.getTitle());
                    System.out.println("\tContent: \t" + entry.getContent());
                    System.out.println("==================================================");
                    i++;
                }
            }
            else
            {
                System.out.println("There were no entries found by that username.");
            }
            
            // 3) Searching by id
            System.out.println("Checking database for blog entry with id = 1.");
            b = blogDAO.findBlogEntryByID(1);
            
            // Display results
            if(b != null)
            {
                System.out.println("Details for entry: "); 
                System.out.println("ID: \t\t" + b.getEntryId());
                System.out.println("Author:\t\t" + b.getUsername());
                System.out.println("Title: \t\t" + b.getTitle());
                System.out.println("Content: \t" + b.getContent());
            }
            else
            {
                System.out.println("Cannot find a blog with that id");
            }
            
            // 4) Getting all entries in the database
            System.out.println("\nGetting all of the blog entries in the database...");
            entries = blogDAO.findAllBlogEntries();
            
            // Display results
            if(entries.size() > 0)
            {
                System.out.println("Success! The following entries were found:");
                System.out.println("==================================================");
                int i = 1;
                for(BlogEntry entry: entries)
                {
                    System.out.println("Entry #" + i + ":");
                    System.out.println("\tID: \t\t" + entry.getEntryId());
                    System.out.println("\tAuthor:\t\t" + entry.getUsername());
                    System.out.println("\tTitle: \t\t" + entry.getTitle());
                    System.out.println("\tContent: \t" + entry.getContent());
                    System.out.println("==================================================");
                    i++;
                }
            }
            else
            {
                System.out.println("There were no entries found in the database.");
            }
            
            /*
            *   DEMONSTRATING ADDING A BLOG_ENTRY
            */
            BlogEntry test = new BlogEntry("Charles", "Hey there", "Allo, Allo");
            System.out.println("Attempting to add a test entry to the database: " + test);
            
            int newEntryId = blogDAO.addBlogEntry("Charles", "Hey there", "Allo, Allo");
            if(newEntryId != -1)
            {
                System.out.println("The addition was successful, the new entry has been added to the database with an id of " + newEntryId);
            }
            
            /*
            *   DEMONSTRATING REMOVING A BLOG_ENTRY
            */
            System.out.println("\nAttempting to remove the test entry from the database: " + test);
            
            int verdict = blogDAO.removeBlogEntry(newEntryId);
            if(verdict != 0)
            {
                System.out.println("The removal was successful, the entry has been removed from the database");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
