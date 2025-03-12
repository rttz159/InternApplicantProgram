package entity;

/**
 *
 * @author rttz159
 */
public abstract class User {
    protected String userId;
    protected String username;
    protected String password;
    protected String contactno;
    protected String email;
    protected Location location;

    public User(String userid, String username, String password, String contactno, String email, Location location) {
        this.userId = userid;
        this.username = username;
        this.password = password;
        this.contactno = contactno;
        this.email = email;
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    public abstract User deepCopy();

}
