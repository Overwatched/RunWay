package william.course.summer.umeo.runway;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import william.course.summer.umeo.runway.Controller.Controller;

/**
 * MainActivity manages the different Fragments
 * instead of using a singleton class, the MainAcitivty
 * hands out the controller class necessary to
 * communicate with the model.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private ViewPager mViewPager;
    private Controller mController = new Controller();

    /**
     * OnCreate is ran whenever the application
     * is to be launced or restored
     * @param savedInstanceState Used for restoring and saving data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started.");
        mController.setContext(getBaseContext());
        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        //setup the pager
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_directions_run_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_history_black_24dp);
    }

    /**
     * Setting the viewpager items
     * @param fragmentNumber
     */
    public void setViewPager(int fragmentNumber) {
        mViewPager.setCurrentItem(fragmentNumber);
    }
    /**
     * Instead of using singleton, i allow the MainActivity to handle a single copy of the controller.
     * @return A controller class
     */
    public Controller getController() {
        return mController;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(), "Fragment1");
        adapter.addFragment(new Fragment3(), "Fragment3");
        adapter.addFragment(new Fragment2(), "Fragment2");
        viewPager.setAdapter(adapter);
    }

}