package entity;

/**
 *
 * @author rttz159
 */
public class Location {
    private String city;
    private String fullAddress;

    public Location(String city, String fullAddress) {
        this.city = city;
        this.fullAddress = fullAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
