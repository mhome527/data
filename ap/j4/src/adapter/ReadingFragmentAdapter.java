package sjpn4.vn.adapter;

import java.util.List;
import sjpn4.vn.activity.ReadingContentFragment;
import sjpn4.vn.model.ReadingModel;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ReadingFragmentAdapter extends FragmentPagerAdapter {
	private List<ReadingModel> lstDay;
	Fragment fragment;
	public ReadingFragmentAdapter(FragmentManager fm, List<ReadingModel> lstDay) {
		super(fm);
		this.lstDay = lstDay;
	}

	@Override
	public Fragment getItem(int position) {
		return ReadingContentFragment.newInstance(lstDay.get(position));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lstDay.size();
	}


}
