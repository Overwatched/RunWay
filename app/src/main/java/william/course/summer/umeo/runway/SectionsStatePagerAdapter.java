package william.course.summer.umeo.runway;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Management class for the fragments used in this project.
 */
public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Adds a fragment to the adapter
     * @param fragment the fragment to add
     * @param title title of the fragment
     */
    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    /**
     * @param position element at position
     * @return Returns the position of a certain fragment
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    /**
     * @return Retrieve the number of elements in the adapter
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}