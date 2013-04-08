package dk.jagdos.notification;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import dk.jagdos.notification.InFragment.PickCallback;

public class ClockAdapter extends FragmentPagerAdapter  {

	PickCallback pickCallback;
	
	 private static final String[] TITLES = new String[] { 
         "Now", 
         "In", 
         "At" 
     };
	 
	 private static final int PAGESIZE = 3;
	
	public ClockAdapter(FragmentManager fm, PickCallback pickCallback) {
		super(fm);
		this.pickCallback = pickCallback;
	}

	@Override
	public Fragment getItem(int pos) {
		// TODO Auto-generated method stub
		switch (pos) {
		case 0:
			return new NowFragment();

		case 1:
			return InFragment.newInstance();
			
		case 2:
			return AtFragment.newInstance();
			
		default:
			break;
		}
		return null;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGESIZE;
	}
	
	  @Override
    public String getPageTitle(int position) {
        return TITLES[position].toUpperCase();
    }

}
