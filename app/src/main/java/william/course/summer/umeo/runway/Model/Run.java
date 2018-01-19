package william.course.summer.umeo.runway.Model;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.ArrayList;

/**
 * This class uses simple xml serializer in order to serialize
 * the class into xml objects.
 * This class holds all runs, and their data.
 */
@Root
public class Run implements Parcelable {

    private boolean run_active = false;
    private boolean run_paused = false;

    @ElementList
    private ArrayList<RunData> runs = new ArrayList<>();

    public Run() {
    }

    public ArrayList<RunData> getRuns() {
        return runs;
    }

    public void addRun(RunData runs) {
        this.runs.add(runs);
    }

    public boolean isRun_paused() {
        return run_paused;
    }

    public void setRun_paused(boolean run_paused) {
        this.run_paused = run_paused;
    }

    public boolean isRun_active() {
        return run_active;
    }

    public void setRun_active(boolean run_active) {
        this.run_active = run_active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.run_active ? (byte) 1 : (byte) 0);
        dest.writeByte(this.run_paused ? (byte) 1 : (byte) 0);
        dest.writeList(this.runs);
    }

    protected Run(Parcel in) {
        this.run_active = in.readByte() != 0;
        this.run_paused = in.readByte() != 0;
        this.runs = new ArrayList<RunData>();
        in.readList(this.runs, RunData.class.getClassLoader());
    }

    public static final Creator<Run> CREATOR = new Creator<Run>() {
        @Override
        public Run createFromParcel(Parcel source) {
            return new Run(source);
        }

        @Override
        public Run[] newArray(int size) {
            return new Run[size];
        }
    };
}
