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
        JOHOR(1.4927, 103.7414),
        KEDAH(6.1184, 100.3685),
        KELANTAN(6.1254, 102.2381),
        MALACCA(2.1896, 102.2501),
        NEGERI_SEMBILAN(2.7258, 101.9424),
        PAHANG(3.8126, 103.3256),
        PERAK(4.5975, 101.0901),
        PERLIS(6.4446, 100.1986),
        PENANG(5.4164, 100.3327),
        SABAH(5.9804, 116.0735),
        SARAWAK(1.5533, 110.3592),
        SELANGOR(3.0738, 101.5183),
        TERENGGANU(5.3117, 103.1324),
        KUALA_LUMPUR(3.1390, 101.6869),
        LABUAN(5.2831, 115.2308),
        PUTRAJAYA(2.9264, 101.6964);

        private final double latitude;
        private final double longitude;

        MalaysianRegion(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public static double distanceBetween(MalaysianRegion a, MalaysianRegion b) {
            final int R = 6371; 
            double latDiff = Math.toRadians(b.latitude - a.latitude);
            double lonDiff = Math.toRadians(b.longitude - a.longitude);

            double aCalc = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                    + Math.cos(Math.toRadians(a.latitude)) * Math.cos(Math.toRadians(b.latitude))
                    * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
            double c = 2 * Math.atan2(Math.sqrt(aCalc), Math.sqrt(1 - aCalc));

            return R * c;
        }
    }

    public Location deepCopy() {
        return new Location(
                this.state,
                this.fullAddress
        );
    }

}
