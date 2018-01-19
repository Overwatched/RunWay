package william.course.summer.umeo.runway.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains some math operations necessary
 */
public class MathOperations {
    /**
     * Attempts to calculate the distance traveled by using your time and avg speed
     * @param timeInSeconds time running
     * @param speed average speed
     * @return the distance traveled
     */
    public double distanceTraveled(long timeInSeconds, float speed) {
        float test = timeInSeconds;
        float speedtest = speed;
        if (test != 0) {
            return (test / 3600) * (speedtest / 3.6);
        }
        return 0;
    }

    /**
     * Attempts to calculate your average speed
     * @param speed array of speed elements
     * @return a float containing your average speed
     */
    public float averageSpeed(ArrayList<Float> speed) {
        float sum = 0;
        for (int i = 0; i < speed.size(); i++) {
            sum += speed.get(i);
        }
        return sum / speed.size();
    }

    /**
     * Attempts to calculate your height gained during
     * your run. This seems to be kind of sporadic and
     * unpredictable. Since the GPS might give you some
     * weird and choppy data. This is where the use of reliability
     * would come in handy.
     * @param heightArray Array of your altitude values
     * @return your altitude gained
     */
    public double heightTraveled(ArrayList<Double> heightArray) {
        double baseValue = 0;
        double heightTraveled = 0;
        int index = 0;
        for (int i = 0; i < heightArray.size(); i++) {
            if (heightArray.get(i) != 0) {
                baseValue = heightArray.get(i);
                index = i;
                break;
            }
        }
        if (baseValue != 0) {
            for (int i = index; i < heightArray.size(); i++) {
                if (baseValue > heightArray.get(i)) {
                    baseValue = heightArray.get(i);
                    index = i;
                } else if (baseValue < heightArray.get(i) && i != index) {
                    heightTraveled += heightArray.get(i) - baseValue;
                }
            }
        }
        return heightTraveled;
    }

    /**
     * In an attempt to return something else that doesn't bug out as much
     * @param heigtArray list of altitude elements
     * @return an array containing two elements, lowest, highest.
     */
    public double[] highestLowestHeight(ArrayList<Double> heigtArray){
        if (!heigtArray.isEmpty()) {
            while (heigtArray.contains(0.00)){
                heigtArray.remove(heigtArray.get(heigtArray.indexOf(0.00)));
            }
            Double min = heigtArray.get(heigtArray.indexOf(Collections.min(heigtArray)));
            Double max = heigtArray.get(heigtArray.indexOf(Collections.max(heigtArray)));
            return new double[]{min,max};
        }
        return new double[]{0,0};
    }
}
