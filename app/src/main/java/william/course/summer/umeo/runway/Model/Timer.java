package william.course.summer.umeo.runway.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is a timer class that implements Parcelable
 */
public class Timer implements Parcelable {

    private long startTime = 0;
    private long stopTime = 0;
    private long pauseTimeStart = 0;
    private long pauseTime = 0;

    /**
     * Makes a note when the user wants to pause the timer
     */
    public void pause(){
        pauseTimeStart = System.currentTimeMillis();
    }

    /**
     * Makes a note when the user has finished the pause
     * and calculates the pausetime
     */
    public void resume(){
        pauseTime += System.currentTimeMillis() - pauseTimeStart;
    }

    /**
     * Makes a note when the user wants to start a timer
     */
    public void start(){
        startTime = System.currentTimeMillis();
    }

    /**
     * Makes a note when the user wants to stop the timer
     */
    public void stop(){
        stopTime = System.currentTimeMillis();
    }

    /**
     * Calculates the timer measurement, excluding pausetime
     * @return a long containing the time in seconds
     */
    public long getResult(){
        if(stopTime != 0)return ((stopTime-startTime) - pauseTime)/1000;
        return ((System.currentTimeMillis()-startTime) - pauseTime)/1000;
    }

    /**
     * Used in order to make the class parcelable
     */

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Used in order to make the class parcelable
     */

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.startTime);
        dest.writeLong(this.stopTime);
        dest.writeLong(this.pauseTimeStart);
        dest.writeLong(this.pauseTime);
    }

    public Timer() {
    }

    /**
     * Used in order to make the class parcelable
     */

    protected Timer(Parcel in) {
        this.startTime = in.readLong();
        this.stopTime = in.readLong();
        this.pauseTimeStart = in.readLong();
        this.pauseTime = in.readLong();
    }

    /**
     * Used in order to make the class parcelable
     */

    public static final Parcelable.Creator<Timer> CREATOR = new Parcelable.Creator<Timer>() {
        @Override
        public Timer createFromParcel(Parcel source) {
            return new Timer(source);
        }

        @Override
        public Timer[] newArray(int size) {
            return new Timer[size];
        }
    };
}
