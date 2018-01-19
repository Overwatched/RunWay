package william.course.summer.umeo.runway;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import william.course.summer.umeo.runway.Controller.Controller;

/**
 * Fragment 1 should in theory hold login options for a user.
 * This however haven't yet been implemented, therefore this lovely ImageButton is used
 * temporarily as a cover.
 */

public class Fragment1 extends Fragment {
    private static final String TAG = "Fragment1";
    private Controller controller;
    private Button mstartstopbtn;

    /**
     * Inflates the first fragment of the application.
     * @param inflater Inflater to inflate
     * @param container Viewgroup container
     * @param savedInstanceState Used for restoring data on event
     * @return Returns a view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment1_layout, container, false);

        controller = ((MainActivity)getActivity()).getController();
        mstartstopbtn = (Button) view.findViewById(R.id.startstopbtn);
        mstartstopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(1);
            }
        });

        return view;
    }
}
