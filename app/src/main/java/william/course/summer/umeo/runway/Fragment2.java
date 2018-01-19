package william.course.summer.umeo.runway;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import william.course.summer.umeo.runway.Controller.Controller;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * This Fragment holds the history view elements.
 * Note how this fragment does not make use of
 * SavedInstanceState in order to keep it's content available.
 */
public class Fragment2 extends Fragment {
    private static final String TAG = "Fragment2";
    private static final String KEY_RELOAD_DATA = "reload";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<ListItem> mlistItems;
    private Controller mController;

    /**
     * OnCreate is called whenever the Fragment is
     * started/infalted or restored.
     * @param inflater The object to inflate
     * @param container The ViewGroup container
     * @param savedInstanceState Object used in order to restored data
     * @return Returns a view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment2_layout, container, false);
        mController = ((MainActivity) getActivity()).getController();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mlistItems = new ArrayList<>();

        populateList();
        mController.giveFragment2(this);

        mAdapter = new HistoryAdapter(mlistItems, getContext());
        mRecyclerView.setAdapter(mAdapter);

        VerticalRecyclerViewFastScroller fastScroller = view.findViewById(R.id.fast_scroller);
        fastScroller.setRecyclerView(mRecyclerView);
        mRecyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        return view;
    }

    /**
     * Function used in order to populate the RecyclerView with items
     * This function can also be used to update the RecyclerView when new items
     * have been added.
     */
    public void populateList(){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        mlistItems.clear();

        if(!mController.getRun().getRuns().isEmpty()){
            Log.d(TAG, "populateList: "+mController.getRun().getRuns().size());
            for (int i = 0; i < mController.getRun().getRuns().size(); i++) {
                String date = df.format(mController.getRun().getRuns().get(i).getDate());
                float averageSpeed = mController.getAverageSpeed(mController.getRun().getRuns().get(i).getSpeed());
                double[] minMax = mController.getHighestLowest(mController.getRun().getRuns().get(i).getAltitude());
                double min = minMax[0];
                double max = minMax[1];
                long time = mController.getRun().getRuns().get(i).getTime();
                float distanceTraveled = mController.getRun().getRuns().get(i).getDistanceTraveled();
                ListItem listitem = new ListItem(
                        date,
                        String.format(Locale.ENGLISH,"%d", time),
                        String.format(Locale.ENGLISH,"%.2f %.2f", min, max),
                        String.format(Locale.ENGLISH,"%.2f", averageSpeed),
                        String.format(Locale.ENGLISH,"%.2f", distanceTraveled));
                mlistItems.add(listitem);
            }
        }
    }

}
