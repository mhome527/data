package app.infobus;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import app.infobus.utils.Constant;

public class TestClass extends BaseActivity{
//FragmentActivity {
//	 @Override
//	 protected void onCreate(Bundle arg0) {
//	 // TODO Auto-generated method stub
//	 super.onCreate(arg0);
//	 setHeaderMenu();
//	 }

	private void setHeaderMenu() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.header_menu, null);

		ImageButton imgMenu = (ImageButton) mCustomView.findViewById(R.id.imgMenu);
		imgMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
			}
		});

		ImageButton imgSearch = (ImageButton) mCustomView.findViewById(R.id.imgSearch);
		imgSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

			}
		});

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHeaderMenu();
	}
}
