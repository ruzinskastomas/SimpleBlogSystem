package Business;

public class BlogEntry implements Comparable<BlogEntry>
{
    private int entryId;
    private String username;
    private String title;
    private String content;

    public BlogEntry(String username, String title, String content) {
        this.username = username;
        this.title = title;
        this.content = content;
    }

    public BlogEntry(int entryId, String username, String title, String content) {
        this.entryId = entryId;
        this.username = username;
        this.title = title;
        this.content = content;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BlogEntry{" + "entryId=" + entryId + ", username=" + username + ", title=" + title + ", content=" + content + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.entryId;
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
        final BlogEntry other = (BlogEntry) obj;
        if (this.entryId != other.entryId) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(BlogEntry o)
    {
        int result = this.entryId - o.getEntryId();
        return result * -1;
    }
}
