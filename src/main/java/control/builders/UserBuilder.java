package control.builders;

import com.fasterxml.uuid.Generators;
import entity.Location;

/**
 *
 * @author rttz159
 */
public abstract class UserBuilder<T extends UserBuilder<T>> {
    protected String userId;
    protected String username;
    protected String password;
    protected String contactNo;
    protected String email;
    protected Location location;

    public UserBuilder() {
        this.userId = generateUUIDv1();
    }

    private String generateUUIDv1() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public T username(String username) {
        this.username = username;
        return self();
    }

    public T password(String password) {
        this.password = password;
        return self();
    }

    public T contactNo(String contactNo) {
        this.contactNo = contactNo;
        return self();
    }

    public T email(String email) {
        this.email = email;
        return self();
    }

    public T location(Location location) {
        this.location = location;
        return self();
    }

    protected abstract T self();
}
