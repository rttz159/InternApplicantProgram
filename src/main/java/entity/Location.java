package entity;

/**
 *
 * @author rttz159
 */
public class Location {

    private String state;
    private String fullAddress;

    public Location(String city, String fullAddress) {
        this.state = city;
        this.fullAddress = fullAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public enum MalaysianRegion {
        JOHOR,
        KEDAH,
        KELANTAN,
        MALACCA,
        NEGERI_SEMBILAN,
        PAHANG,
        PERAK,
        PERLIS,
        PENANG,
        SABAH,
        SARAWAK,
        SELANGOR,
        TERENGGANU,
        KUALA_LUMPUR,
        LABUAN,
        PUTRAJAYA;
    }

    public Location deepCopy() {
        return new Location(
                this.state,
                this.fullAddress
        );
    }

}
