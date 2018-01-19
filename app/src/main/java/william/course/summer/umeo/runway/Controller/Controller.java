package william.course.summer.umeo.runway.Controller;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;

import william.course.summer.umeo.runway.Fragment2;
import william.course.summer.umeo.runway.Model.MathOperations;
import william.course.summer.umeo.runway.Model.Run;
import william.course.summer.umeo.runway.Model.RunData;
import william.course.summer.umeo.runway.Model.StorageManager;
import william.course.summer.umeo.runway.Model.Timer;

/**
 * Controller class used to communicate between the Views and the model
 */
public class Controller {
    private Timer timer = new Timer();
    private Run run = new Run();
    private StorageManager storageManager = new StorageManager();
    private Fragment2 mFragment2;
    private MathOperations mMathOperations = new MathOperations();
    private Context context;


    public Run getRun() {
        return run;
    }

    public void setRun(Run run){
        this.run = run;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public void setTimer(Timer timer){
        this.timer = timer;
    }

    public Timer getTimer(){
        return this.timer;
    }
    /**
     * Adds a run to the xml file, that stores all runs.
     * Note that the run should be finished before calling this method.
     * @param rundata the run to be added
     */
    public void addCompletedRun(RunData rundata) {
        this.run.addRun(rundata);
    }

    /**
     * @return Returns the result of the real timer class
     */
    public long getTime() {
        return timer.getResult();
    }

    /**
     * Starts a new run and activates the inner timer
     */
    public void startRun() {
        run.setRun_active(true);
        timer.start();
    }

    /**
     * Pauses a run and pauses the inner timer
     */
    public void pauseRun() {
        run.setRun_paused(true);
        timer.pause();
    }

    /**
     * Resumes a run and resumes the inner timer
     */
    public void resumeRun() {
        run.setRun_paused(false);
        timer.resume();
    }

    /**
     * Stops a run and stops the timer
     */
    public void stopRun() {
        run.setRun_active(false);
        timer.stop();
    }

    /**
     * @return Check if a run is active
     */
    public boolean isRunActive() {
        return run.isRun_active();
    }

    /**
     * @return Check if a run is paused
     */
    public boolean isRunPaused() {
        return run.isRun_paused();
    }

    /**
     * Saves data to the xml repository
     * @param ctx the context where data came from
     * @throws Exception the file might now be found etc
     */
    public void saveData(Context ctx) throws Exception {
        storageManager.write(ctx, run);
    }

    /**
     * Called when one wants to update the history list
     * in fragment 2, fetches data from xml and shows in the
     * list in fragment 2.
     * @param context the context
     * @throws Exception file not found
     */
    public void updateHistory(Context context) throws Exception {
        this.run = storageManager.read(this.context);
        updateFragment2();
    }

    /**
     * A small workaround, instead of implementing the observer patter
     * Came up with this to late in the project.
     * @param fragment2 The history fragment
     */
    public void giveFragment2(Fragment2 fragment2) {
        this.mFragment2 = fragment2;
    }

    /**
     * Attempts to return the height traveled from the model
     * this seems to bug out
     * @param heightArray The array of height values
     * @return a double containing height gained
     */
    public double getHeightTraveled(ArrayList<Double> heightArray) {
        return mMathOperations.heightTraveled(heightArray);
    }

    /**
     * Returns your average speed during your run
     * @param speedArray array containing speed data
     * @return a float containing your average speed
     */
    public float getAverageSpeed(ArrayList<Float> speedArray) {
        return mMathOperations.averageSpeed(speedArray);
    }

    /**
     * An attempt to calculate distance traveled
     * @param timeInSeconds the time run
     * @param averageSpeed the average speed of the run
     * @return a double containing the distance
     */
    public double getLengthTraveled(long timeInSeconds, float averageSpeed) {
        return (double)mMathOperations.distanceTraveled(timeInSeconds, averageSpeed);
    }

    private void updateFragment2() {
            mFragment2.populateList();
    }

    public double[] getHighestLowest(ArrayList<Double> heightList){
        return mMathOperations.highestLowestHeight(heightList);
    }

}
