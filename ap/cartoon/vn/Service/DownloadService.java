package cartoon.youtube.vn.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import cartoon.youtube.vn.BuildConfig;
import cartoon.youtube.vn.Cartoon;
import cartoon.youtube.vn.CartoonDao;
import cartoon.youtube.vn.Constant;
import cartoon.youtube.vn.DaoMaster;
import cartoon.youtube.vn.DropBox.DownloadJson;
import cartoon.youtube.vn.Entity.CartoonEntity;
import cartoon.youtube.vn.MyApplication;
import cartoon.youtube.vn.Utils.Common;
import cartoon.youtube.vn.Utils.ULog;

/**
 * Created by trandaohuynh on 7/12/14.
 */
public class DownloadService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        getCacheDir().getAbsoluteFile();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ULog.i(this, "onStartCommand startId:" + startId);

        //download json
        new DownloadJson(this){
            @Override
            protected void onPostExecute(Boolean result) {
                CartoonEntity entity;
                super.onPostExecute(result);
                try {
                    entity = (CartoonEntity) Common.getObjectJsonPath(DownloadService.this, CartoonEntity.class, getCacheDir().getAbsoluteFile() + "/" + Constant.JSON_FILE_NAME);
                    if (entity != null && entity.list != null) {
                        ULog.i(DownloadJson.class, "list size:" + entity.list.size());
                        DaoMaster daoMaster;
                        CartoonDao dao;
                        ULog.i(this, "Service Insert DB");

                        daoMaster = ((MyApplication) DownloadService.this.getApplicationContext()).daoMaster;
                        dao = daoMaster.newSession().getCartoonDao();

//                        DaoSession mDaoSession = daoMaster.newSession();

                        for (Cartoon cartoon : entity.list) {
//                mDaoSession.insertOrReplace(cartoon);
                            dao.insertOrReplace(cartoon);
                        }
                        publishResults(Constant.JSON_FILE_NAME);
                    } else
                        ULog.e(DownloadJson.class, "Read json error; can't download file json");
                }catch (Exception e){
                    ULog.e(DownloadJson.class, "Read json error; " + e.getMessage());
                    if(BuildConfig.DEBUG)
                        e.printStackTrace();
                }

            }

        }.execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void publishResults(String filename) {
        Intent intent = new Intent(Constant.BROADDCAST_RELOAD);
        intent.putExtra(Constant.INTENT_FILE, filename);
        sendBroadcast(intent);
    }
}
