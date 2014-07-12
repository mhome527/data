package cartoon.youtube.vn.DropBox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cartoon.youtube.vn.BuildConfig;
import cartoon.youtube.vn.Cartoon;
import cartoon.youtube.vn.CartoonDao;
import cartoon.youtube.vn.Constant;
import cartoon.youtube.vn.DaoMaster;
import cartoon.youtube.vn.DaoSession;
import cartoon.youtube.vn.Entity.CartoonEntity;
import cartoon.youtube.vn.MyApplication;
import cartoon.youtube.vn.Utils.Common;
import cartoon.youtube.vn.Utils.ULog;


/**
 * Created by huynhtran on 7/2/14.
 */

//https://www.dropbox.com/developers/core/start/android

public class DownloadJson extends AsyncTask<Void, Long, Boolean> {
    private Context mContext;
    //    private final ProgressDialog mDialog;
//    private String mPath;
    private DropboxAPI<?> mApi;

    private FileOutputStream mFos;

    private boolean mCanceled;
    private Long mFileLen;
    private String mErrorMsg;


    public DownloadJson(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String cachePath;
        try {
            ULog.i(DownloadJson.class, "doInBackground..");
            AndroidAuthSession session = buildSession();
            if (session == null) {
                ULog.e(this, "initData can't create session");
                return false;
            }
            mApi = new DropboxAPI<AndroidAuthSession>(session);


            // Get the metadata for a directory
            DropboxAPI.Entry dirent = mApi.metadata(Constant.FOLDER_NAME, 1000, null, true, null);

            ULog.i(DownloadJson.class, "doInBackground  Get the metadata");

            if (!dirent.isDir || dirent.contents == null) {
                // It's not a directory, or there's nothing in it
                mErrorMsg = "File or empty directory " + Constant.FOLDER_NAME;
                ULog.e(DownloadJson.class, "doInBackground " + mErrorMsg);
                return false;
            }

            // Make a list of everything in it that we can get a thumbnail for
            ArrayList<DropboxAPI.Entry> thumbs = new ArrayList<DropboxAPI.Entry>();
            for (DropboxAPI.Entry ent : dirent.contents) {
                ULog.i(DownloadJson.class, "File server thumb: " + ent.modified + "; file name: " + ent.fileName());

                if (!ent.thumbExists) {
                    // Add it to the list of thumbs we can choose from
                    thumbs.add(ent);
                }
            }

            if (thumbs.size() == 0) {
                // No thumbs in that directory
                mErrorMsg = "No File in that directory";
                ULog.e(DownloadJson.class, "doInBackground " + mErrorMsg);
                return false;
            }

            for( DropboxAPI.Entry ent : thumbs){
//                ULog.i(DownloadJson.class, "doInBackground file server size:" + ent.bytes + "; path: " + ent.path);
                cachePath = mContext.getCacheDir().getAbsolutePath() + "/" + ent.fileName();
                ULog.i(DownloadJson.class, "doInBackground file cache:" + cachePath);

                ///////////////
                try {
                    FileOutputStream outputStream = new FileOutputStream(cachePath);
                    DropboxAPI.DropboxFileInfo info = mApi.getFile(ent.path, null, outputStream, null);
                    ULog.i(DownloadJson.class, "The file's file name: " + info.getMetadata().fileName() + "; modify: " + info.getMetadata());
                } catch (Exception e) {
                    ULog.e(DownloadJson.class, "Exception download Error: " + e.getMessage());
                    if(BuildConfig.DEBUG)
                        e.printStackTrace();
                    return false;
                }
            }

        } catch (DropboxServerException e) {
            // Server-side exception.  These are examples of what could happen, but we don't do anything special with them here.
            ULog.e(DownloadJson.class, "DropboxServerException Error: " + e.getMessage());
            // This gets the Dropbox error, translated into the user's language
            mErrorMsg = e.body.userError;
            if (mErrorMsg == null) {
                mErrorMsg = e.body.error;
            }
        } catch (Exception e) {
            ULog.e(DownloadJson.class, "Exception Error: " + e.getMessage());
        }
        return true;
    }

//    @Override
//    protected void onPostExecute(Boolean result) {
//        if (result) {
//            ULog.i(DownloadJson.class, "Download finish");
//            getCartoonEntity();
//        } else {
//            // Couldn't download it
//            ULog.i(DownloadJson.class, "Download fail: " + mErrorMsg);
//        }
//    }

//    private void getCartoonEntity() {
//        CartoonEntity entity = (CartoonEntity) Common.getObjectJsonPath(mContext, CartoonEntity.class, mContext.getCacheDir().getAbsolutePath() + "/" + Constant.JSON_FILE_NAME);
//        if (entity != null && entity.list != null) {
//            ULog.i(DownloadJson.class, "list size:" + entity.list.size());
//            DaoMaster daoMaster;
//            CartoonDao dao;
//            ULog.i(this, "Load DB");
//
//            daoMaster = ((MyApplication) mContext.getApplicationContext()).daoMaster;
//            dao = daoMaster.newSession().getCartoonDao();
//
//            DaoSession mDaoSession = daoMaster.newSession();
//
//            for (Cartoon cartoon : entity.list) {
////                mDaoSession.insertOrReplace(cartoon);
////                dao.insertOrReplace(cartoon);
//
//            }
//        }
//        else
//            ULog.e(DownloadJson.class, "Read json error; can't download file json");
//
//    }

    private void insertData( ) {
        DaoMaster daoMaster;
        CartoonDao dao;
        ULog.i(this, "Load DB");

        daoMaster = ((MyApplication) mContext.getApplicationContext()).daoMaster;

        DaoSession mDaoSession = daoMaster.newSession();
        CartoonEntity entity = (CartoonEntity) Common.getObjectJson(mContext, CartoonEntity.class, Constant.JSON_FILE_NAME);
        if (entity == null) {
            ULog.e(MyApplication.class, "Can't load Json");
            return;
        }

        ULog.i(this, "===== Insert data :" + entity.list.size());
        for (Cartoon cartoon : entity.list) {
            mDaoSession.insert(cartoon);
        }
        ///

    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(Constant.APP_KEY, Constant.APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(Constant.APP_TOCKEN);
        return session;
    }


}
