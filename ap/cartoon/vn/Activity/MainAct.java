package cartoon.youtube.vn.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.util.List;

import cartoon.youtube.vn.Cache.ImageCacheManager;
import cartoon.youtube.vn.Cartoon;
import cartoon.youtube.vn.CartoonDao;
import cartoon.youtube.vn.Constant;
import cartoon.youtube.vn.DaoMaster;
import cartoon.youtube.vn.DaoSession;
import cartoon.youtube.vn.DropBox.DownloadJson;
import cartoon.youtube.vn.Entity.CartoonEntity;
import cartoon.youtube.vn.MyApplication;
import cartoon.youtube.vn.R;
import cartoon.youtube.vn.Utils.Common;
import cartoon.youtube.vn.Utils.ULog;
import cartoon.youtube.vn.View.LoadMoreListView;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by huynhtran on 7/4/14.
 */
public class MainAct extends BaseAct {


    @Override
    public int getContentViewID() {
        return R.layout.list_cartoon;
    }

    @Override
    public void onAfterCreate(Bundle savedInstanceState) {

    }

}
