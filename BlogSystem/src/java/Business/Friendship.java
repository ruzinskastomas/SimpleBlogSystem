package Business;

import java.util.Objects;
public class Friendship 
{
    private User user1;
    private User user2;

    public Friendship(User user1, User user2) {
        User tmp;
        // If they're in the wrong order coming in, swap them.
        if(user1.compareTo(user2) > 0)
        {
            tmp = user2;
            this.user2 = user1;
            this.user1 = tmp;
        }
        else
        {
            this.user1 = user1;
            this.user2 = user2;
        }
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User u1) {
       this.user1 = u1;
       correctFriendOrder();
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User u2) {
       this.user2 = u2;
       correctFriendOrder();
    }
    
    public void correctFriendOrder()
    {
        User tmp;
        // If they're in the wrong order, swap them.
        if(this.user1.compareTo(this.user2) > 0)
        {
            tmp = this.user2;
            this.user2 = this.user1;
            this.user1 = tmp;
        }
    }

    @Override
    public String toString() {
        return "Friendship{" + "user 1: " + user1 + ", user 2: " + user2 + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        correctFriendOrder();
        hash = 59 * hash + Objects.hashCode(this.user1);
        hash = 59 * hash + Objects.hashCode(this.user2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Friendship other = (Friendship) obj;

        this.correctFriendOrder();
        other.correctFriendOrder();
        
        if (!Objects.equals(this.user1, other.user1)) {
            return false;
        }
        if (!Objects.equals(this.user2, other.user2)) {
            return false;
        }
        return true;
    }
    
    public static void main(String [] args)
    {
        User u1 = new User("Charlie", "pass", "Charles", "Young");
        User u2 = new User("Zoey", "pass", "Zoey", "Bartlett");
        
        Friendship firstFriends = new Friendship(u1, u2);
        Friendship testEqualsFriends = new Friendship(u2, u1);
        System.out.println("Details for firstFriends: " + firstFriends);
        
        System.out.println("Details for testEquals friendship: " + testEqualsFriends);
        
        if(firstFriends.equals(testEqualsFriends))
        {
            System.out.println("The two friendships are equal!");
        }
        else
        {
            System.out.println("The two friendships are NOT equal");
        }
    }
}
