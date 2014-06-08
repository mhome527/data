package puzzle.slider.vn;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * select image
 * 
 * @author huynhtran
 * 
 */
public class SliderImageActivity extends AbstractContentsActivity {
	String tag = "HuynhTD-" + SliderImageActivity.class.getSimpleName();
	private GridView myGrid;
	private ImageView imgBack;
	private ImageView imgStore;
	private LinearLayout lnProgressBar;
	private List<JigsawImageEntity> lstImage;
	public SliderImageAdapter gridSliderAdapter;
	public int width = 100, height = 100;
	boolean isClick = false;

	@Override
	protected int getViewLayoutId() {
		return R.layout.slider_select_image;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initView(final Bundle savedInstanceState) {
		int widthScr;
		LinearLayout.LayoutParams param;
		super.initView(savedInstanceState);
		try {

			myGrid = ViewHelper.findView(this, R.id.myGrid);
			imgBack = ViewHelper.findView(this, R.id.imgBack);
			imgStore = ViewHelper.findView(this, R.id.imgStore);

			lnProgressBar = ViewHelper.findView(this, R.id.lnProgressBar);

			widthScr = CustomSharedPreferences.getPreferences(Constant.WIDTH_SCREEN, 480);
			width = widthScr / 5;
//			switch (widthScr) {
//			case 1280:
//				width = widthScr / 5 + 25;
//				break;
//			default:
//				width = widthScr / 5;
//				break;
//			}

			// testData();
			// loadImage();

			ShowLog.showLogInfo(tag, "initView width screen: " + width);

			imgBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					try {
						if (isClick)
							return;
						isClick = true;
						
						Intent intent = new Intent(SliderImageActivity.this, StartupApp.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						SoundManager.playSound(Constant.SOUND_D, false);
						SoundManager.isLEFT =3;
						finish();
					}
					catch (Exception e) {
						ShowLog.showLogError(tag, "back error: " + e.getMessage());
					}
				}
			});

			imgStore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					if (isClick)
						return;
					isClick = true;
					Intent intent = new Intent(SliderImageActivity.this, PuzzlesStoreActivity.class);
					SoundManager.playSound(Constant.SOUND_C, false);
					startActivity(intent);
				}
			});

			param = new LinearLayout.LayoutParams((int) (width * 3.5), LinearLayout.LayoutParams.FILL_PARENT);
			myGrid.setColumnWidth(width + 50);
			
			myGrid.setLayoutParams(param);
			// gridSliderAdapter = new SliderImageAdapter(this, lstImage);
			//
			// myGrid.setAdapter(gridSliderAdapter);
			myGrid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					JigsawImageEntity item;
					try {
						if (isClick)
							return;
						isClick = true;
						
						item = lstImage.get(position);
						ShowLog.showLogInfo(tag, "click img position: " + position + "; gameId:" + item.getGameId() + "; pathName;" + item.getPathName());
						Intent intent = new Intent(SliderImageActivity.this, SliderMainActivity.class);
						intent.putExtra(Constant.GAME_ID, item.getGameId());
						intent.putExtra(Constant.PATH_NAME, item.getPathName());
						intent.putExtra(Constant.FINISH_GAME, item.isFinish());
						startActivity(intent);
						SoundManager.playSound(Constant.SOUND_B, false);
						// /
						finish();
					}
					catch (Exception e) {
						ShowLog.showLogError(tag, "item click error: " + e.getMessage());
					}
				}
			});

			new LoadData().execute();
			ShowLog.showLogInfo(tag, "initView");
		}
		catch (Exception e) {
			ShowLog.showLogError(tag, "initData error" + e);
		}
	}

	private void loadImage() {
		JigsawImageEntity entityImg;
		List<GameHistory> arrGame;
		GameHistoryDataHelper dataHelper;
		try {
			lstImage = new ArrayList<JigsawImageEntity>();
			dataHelper = new GameHistoryDataHelper(getApplicationContext());
			String[] whrepras = null;
			arrGame = dataHelper.find(" " + DatabaseConnection.GameHistorySchema.TYPE + "=1", null, whrepras);
			if (arrGame == null) {
				return;
			}

			for (GameHistory entity : arrGame) {
				entityImg = new JigsawImageEntity(entity.getIdGame(), entity.getFolderPath() + "/" + entity.getImageName(), entity.isFinish());
				lstImage.add(entityImg);
			}

//			// ///////test
//			for (GameHistory entity : arrGame) {
//				entityImg = new JigsawImageEntity(entity.getIdGame(), entity.getFolderPath() + "/" + entity.getImageName(), entity.isFinish());
//				lstImage.add(entityImg);
//			}
//			for (GameHistory entity : arrGame) {
//				entityImg = new JigsawImageEntity(entity.getIdGame(), entity.getFolderPath() + "/" + entity.getImageName(), entity.isFinish());
//				lstImage.add(entityImg);
//			}
//			for (GameHistory entity : arrGame) {
//				entityImg = new JigsawImageEntity(entity.getIdGame(), entity.getFolderPath() + "/" + entity.getImageName(), entity.isFinish());
//				lstImage.add(entityImg);
//			}
//			for (GameHistory entity : arrGame) {
//				entityImg = new JigsawImageEntity(entity.getIdGame(), entity.getFolderPath() + "/" + entity.getImageName(), entity.isFinish());
//				lstImage.add(entityImg);
//			}
//			for (GameHistory entity : arrGame) {
//				entityImg = new JigsawImageEntity(entity.getIdGame(), entity.getFolderPath() + "/" + entity.getImageName(), entity.isFinish());
//				lstImage.add(entityImg);
//			}
//			for (GameHistory entity : arrGame) {
//				entityImg = new JigsawImageEntity(entity.getIdGame(), entity.getFolderPath() + "/" + entity.getImageName(), entity.isFinish());
//				lstImage.add(entityImg);
//			}
//			// ////

		}
		catch (Exception e) {
			ShowLog.showLogError(tag, "LoadImage" + e.getMessage());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isClick = false;
	}
	
	private class LoadData extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			lnProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				ShowLog.showLogInfo(tag, "loaddata............");
				loadImage();
				System.gc();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			gridSliderAdapter = new SliderImageAdapter(SliderImageActivity.this, lstImage);
			myGrid.setAdapter(gridSliderAdapter);
			lnProgressBar.setVisibility(View.GONE);

		}
	}

	@Override
	public void onBackPressed() {
		try {
			SoundManager.playSound(Constant.SOUND_D, false);
			Intent intent = new Intent(SliderImageActivity.this, StartupApp.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			SoundManager.isLEFT =3;
			startActivity(intent);
			finish();
		}
		catch (Exception e) {
			ShowLog.showLogError(tag, "back error: " + e.getMessage());
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}