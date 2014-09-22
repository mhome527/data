package app.infobus.dropbox;

import android.content.Context;
import android.os.AsyncTask;
import app.infobus.BaseActivity;
import app.infobus.BuildConfig;
import app.infobus.utils.Constant;
import app.infobus.utils.Prefs;
import app.infobus.utils.ULog;

import com.dropbox.client2.DropboxAPI;
//import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.session.AppKeyPair;
//import com.dropbox.client2.session.Session.AccessType;

//import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by huynhtran on 7/2/14.
 */

// https://www.dropbox.com/developers/core/start/android

public class DownloadJson extends AsyncTask<Void, Long, Boolean[]> {
	private Context mContext;
	private DropboxAPI<?> mApi;
	private String mErrorMsg;

	// //
	// private DropboxAPI<AndroidAuthSession> mDBApi;
	// final static private AccessType ACCESS_TYPE = AccessType.DROPBOX;

	public DownloadJson(Context context) {
		mContext = context.getApplicationContext();

	}

	@Override
	protected Boolean[] doInBackground(Void... voids) {
		String cachePath;
		String dateAD;
		String dateDataHCM;
		String dateDataHN;
		Boolean[] a = new Boolean[3];
		a[0] = false;
		a[1] = false;
		a[2] = false;
		FileOutputStream outputStream;
		DropboxAPI.DropboxFileInfo info;
		try {
			ULog.i(DownloadJson.class, "doInBackground..");

			AndroidAuthSession session = buildSession();
			if (session == null) {
				ULog.e(this, "initData can't create session");
				return a;
			}

			//
			if (BaseActivity.pref == null)
				BaseActivity.pref = new Prefs(mContext);
			dateAD = BaseActivity.pref.getStringValue("", Constant.JSON_AD);
			dateDataHCM = BaseActivity.pref.getStringValue("", Constant.JSON_BUS_HCM);
			dateDataHN = BaseActivity.pref.getStringValue("", Constant.JSON_BUS_HN);
			mApi = new DropboxAPI<AndroidAuthSession>(session);

			ULog.i(DownloadJson.class, "doInBackground..metadata, Constant.FOLDER_NAME=" + Constant.FOLDER_NAME);

			// Get the metadata for a directory
			DropboxAPI.Entry dirent = mApi.metadata(Constant.FOLDER_NAME, 10000, null, true, null);

			ULog.i(DownloadJson.class, "doInBackground  Get the metadata");

			if (!dirent.isDir || dirent.contents == null) {
				// It's not a directory, or there's nothing in it
				mErrorMsg = "File or empty directory " + Constant.FOLDER_NAME;
				ULog.e(DownloadJson.class, "doInBackground " + mErrorMsg);
				return a;
			}

			// Make a list of everything in it that we can get a thumbnail for
			ArrayList<DropboxAPI.Entry> thumbs = new ArrayList<DropboxAPI.Entry>();
			for (DropboxAPI.Entry ent : dirent.contents) {
				// ULog.i(DownloadJson.class, "File server thumb: " + ent.modified + "; file name: " + ent.fileName());

				if (!ent.thumbExists) {
					// Add it to the list of thumbs we can choose from
					thumbs.add(ent);
				}
			}

			if (thumbs.size() == 0) {
				// No thumbs in that directory
				mErrorMsg = "No File in that directory";
				ULog.e(DownloadJson.class, "doInBackground " + mErrorMsg);
				return a;
			}

			for (DropboxAPI.Entry ent : thumbs) {

				// ULog.i(DownloadJson.class, "doInBackground server file name: " + ent.fileName() + "; ==== modify: " + ent.modified
				// + "; old: " + dateAD);

				if (ent.fileName().equals(Constant.JSON_AD) && (!dateAD.equalsIgnoreCase(ent.modified) || dateAD.equals(""))) {

					// save date
					BaseActivity.pref.putStringValue(ent.modified, Constant.JSON_AD);

					cachePath = mContext.getCacheDir().getAbsolutePath() + "/" + ent.fileName();
					ULog.i(DownloadJson.class, "doInBackground file cache:" + cachePath);

					// /////////////
					try {
						outputStream = new FileOutputStream(cachePath);
						info = mApi.getFile(ent.path, null, outputStream, null);
						ULog.i(DownloadJson.class,
								"The file's file name: " + info.getMetadata().fileName() + "; modify: " + info.getMetadata());
					} catch (Exception e) {
						ULog.e(DownloadJson.class, "Exception download Error: " + e.getMessage());
						if (BuildConfig.DEBUG)
							e.printStackTrace();
					}
				} else if (ent.fileName().equals(Constant.JSON_BUS_HCM)
						&& (!dateDataHCM.equalsIgnoreCase(ent.modified) || dateDataHCM.equals(""))) {
					try {
						BaseActivity.pref.putStringValue(ent.modified, Constant.JSON_BUS_HCM);

						cachePath = mContext.getCacheDir().getAbsolutePath() + "/" + ent.fileName();
						outputStream = new FileOutputStream(cachePath);
						info = mApi.getFile(ent.path, null, outputStream, null);
						ULog.i(DownloadJson.class,
								"The file's file name: " + info.getMetadata().fileName() + "; modify: " + info.getMetadata());
						a[1] = true;
					} catch (Exception e) {
						ULog.e(DownloadJson.class, "Exception download Error: " + e.getMessage());
						if (BuildConfig.DEBUG)
							e.printStackTrace();
					}
				} else if (ent.fileName().equals(Constant.JSON_BUS_HN)
						&& (!dateDataHN.equalsIgnoreCase(ent.modified) || dateDataHN.equals(""))) {
					try {
						BaseActivity.pref.putStringValue(ent.modified, Constant.JSON_BUS_HN);

						cachePath = mContext.getCacheDir().getAbsolutePath() + "/" + ent.fileName();
						outputStream = new FileOutputStream(cachePath);
						info = mApi.getFile(ent.path, null, outputStream, null);
						ULog.i(DownloadJson.class,
								"The file's file name: " + info.getMetadata().fileName() + "; modify: " + info.getMetadata());
						a[2] = true;
					} catch (Exception e) {
						ULog.e(DownloadJson.class, "Exception download Error: " + e.getMessage());
						if (BuildConfig.DEBUG)
							e.printStackTrace();
					}

				}
			}

		} catch (DropboxServerException e) {
			// Server-side exception. These are examples of what could happen, but we don't do anything special with them here.
			ULog.e(DownloadJson.class, "DropboxServerException Error: " + e.getMessage());
			// This gets the Dropbox error, translated into the user's language
			mErrorMsg = e.body.userError;
			if (mErrorMsg == null) {
				mErrorMsg = e.body.error;
			}
			return a;

		} catch (Exception e) {
			ULog.e(DownloadJson.class, "Exception Error: " + e.getMessage());
			return a;
		}

		return a;
	}

	private AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(Constant.APP_KEY, Constant.APP_SECRET);
		AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
		session.setOAuth2AccessToken(Constant.APP_TOCKEN);
		return session;
	}

}
