package william.course.summer.umeo.runway;

/**
 * A class for managing the actual list items in the history
 * basically only geters and a constructor.
 */
public class ListItem {
    private String date;
    private String altitude;
    private String speed;
    private String time;
    private String distance;

    public ListItem(String date, String time, String altitude, String speed, String distance) {
        this.date = date;
        this.altitude = altitude;
        this.speed = speed;
        this.time = time;
        this.distance = distance;
    }

    public String getSpeed() {
        return speed;
    }

    public String getTime() {
        return time;
    }

    public String distance() {
        return distance;
    }

    public String getDate() {
        return date;
    }

    public String getAltitude() {
        return altitude;
    }
}
