package il.ac.technion.socialcampus;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Used to create a PagerView so tabs could be switched by swipe. 
 */
public class PagerAdapter extends FragmentPagerAdapter {
  private final List<Fragment> fragments;
 
  
  /**
   * @param fm
   * @param fragments
   */
  public PagerAdapter(final FragmentManager fm, final List<Fragment> fragments) {
    super(fm);
    this.fragments = fragments;

  }
  
  @Override public Fragment getItem(final int position) {
    return fragments.get(position);
  }
  
   
  
  @Override public int getCount() {
    return fragments.size();
  }
  
}
