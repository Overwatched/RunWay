package william.course.summer.umeo.runway.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class uses the simple xml serializer in order to make this
 * object xml-serializable.
 * This class holds data related to a specific run.
 * Bascially only getters and setters.
 */
@Root(name="run")
public class RunData implements Parcelable {

    @ElementList
    private ArrayList<Double> longitude = new ArrayList<>();
    @ElementList
    private ArrayList<Double> latitude = new ArrayList<>();;
    @Attribute
    private Date date = new Date();
    @ElementList
    private ArrayList<Float> speed = new ArrayList<>();;
    @ElementList
    private ArrayList<Double> altitude = new ArrayList<>();;
    @Element
    private long time = 0;
    @Element
    private float distanceTraveled;

    public long getTime() {
        return time;
    }

    public void setDistanceTraveled(float distance){
        this.distanceTraveled = distance;
    }
    public float getDistanceTraveled(){
        return this.distanceTraveled;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<Double> getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude.add(longitude);
    }

    public ArrayList<Double> getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude.add(latitude);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Float> getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed.add(speed);
    }

    public ArrayList<Double> getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude.add(altitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.longitude);
        dest.writeList(this.latitude);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeList(this.speed);
        dest.writeList(this.altitude);
        dest.writeLong(this.time);
        dest.writeFloat(this.distanceTraveled);
    }

    public RunData() {
    }

    protected RunData(Parcel in) {
        this.longitude = new ArrayList<Double>();
        in.readList(this.longitude, Double.class.getClassLoader());
        this.latitude = new ArrayList<Double>();
        in.readList(this.latitude, Double.class.getClassLoader());
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.speed = new ArrayList<Float>();
        in.readList(this.speed, Float.class.getClassLoader());
        this.altitude = new ArrayList<Double>();
        in.readList(this.altitude, Double.class.getClassLoader());
        this.time = in.readLong();
        this.distanceTraveled = in.readFloat();
    }

    public static final Parcelable.Creator<RunData> CREATOR = new Parcelable.Creator<RunData>() {
        @Override
        public RunData createFromParcel(Parcel source) {
            return new RunData(source);
        }

        @Override
        public RunData[] newArray(int size) {
            return new RunData[size];
        }
    };
}
