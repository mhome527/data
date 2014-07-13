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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.youtube.player.YouTubeIntents;

import java.util.List;

import cartoon.youtube.vn.Cache.ImageCacheManager;
import cartoon.youtube.vn.Cartoon;
import cartoon.youtube.vn.CartoonDao;
import cartoon.youtube.vn.Constant;
import cartoon.youtube.vn.DaoMaster;
import cartoon.youtube.vn.MyApplication;
import cartoon.youtube.vn.R;
import cartoon.youtube.vn.Utils.ULog;
import cartoon.youtube.vn.View.LoadMoreListView;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by huynhtran on 7/8/14.
 */
public class ListDetailAct extends BaseAct {

    private AdapterCartoon adapter;
    private LoadMoreListView lstCartoon;
    private static final String VIDEO_ID = "-Uwjt32NvVA";


    @Override
    public int getContentViewID() {
        return R.layout.list_cartoon;
    }

    @Override
    public void onAfterCreate(Bundle savedInstanceState) {
        lstCartoon = (LoadMoreListView) findViewById(R.id.lstCartoon);
        new LoadData().execute();


        // set a listener to be invoked when the list reaches the end
        lstCartoon.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            public void onLoadMore() {
                // Do the work to load more items at the end of list here
                new LoadDataMoreTask().execute();
            }
        });

        lstCartoon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent;
                Cartoon cartoon = (Cartoon)lstCartoon.getAdapter().getItem(position);
                ULog.i(ListDetailAct.class, "item: " + cartoon.getTitle() + "; youtube:" + cartoon.getYoutube());
//                intent = new Intent(ListDetailAct.this, DetailAct.class);
                intent = new Intent(ListDetailAct.this, WatchAct.class);
                intent.putExtra(Constant.INTENT_DATA, cartoon.getYoutube());
//                intent.putExtra(Constant.INTENT_DATA, cartoon.getYoutube());
//                startActivity(intent);
//                intent = YouTubeIntents.createPlayVideoIntentWithOptions(ListDetailAct.this, cartoon.getYoutube(), true, false);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(Constant.BROADDCAST_RELOAD));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String filename = bundle.getString(Constant.INTENT_FILE);
                ULog.i(MainAct.class, "Receiver filename:" + filename);
                if(filename.equals(Constant.JSON_FILE_NAME)){
                    new LoadData().execute();
                }
            }
        }
    };

    private class AdapterCartoon extends ArrayAdapter<Cartoon> {
        private Context context;
        private int layoutResourceId;

        public AdapterCartoon(Context context, int layoutResourceId, List<Cartoon> objects) {
            super(context, layoutResourceId, objects);
            this.context = context;
            this.layoutResourceId = layoutResourceId;
        }

        public AdapterCartoon(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            Cartoon cartoon;
            CartoonHolder cartoonHolder;
            String thumbnail;
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                cartoonHolder = new CartoonHolder();
                cartoonHolder.imgCartoon = (NetworkImageView) row.findViewById(R.id.imgCartoon);
                cartoonHolder.tvTitle = (TextView) row.findViewById(R.id.tvTitle);

                row.setTag(cartoonHolder);
            } else {
                row = convertView;
                cartoonHolder = (CartoonHolder)row.getTag();
            }
            cartoon = getItem(position);

            thumbnail = String.format(Constant.URL_THUMBNAIL, cartoon.getYoutube());
            ULog.i(this, "data: title :" + thumbnail +"; youtube: " + cartoon.getYoutube());

            cartoonHolder.imgCartoon.setImageUrl(thumbnail, ImageCacheManager.getInstance().getImageLoader());
            cartoonHolder.imgCartoon.setDefaultImageResId(R.drawable.conan_icon);
            cartoonHolder.tvTitle.setText(cartoon.getTitle());
            return row;
        }
    }

    static class  CartoonHolder{
        NetworkImageView imgCartoon;
        TextView tvTitle;

    }


    private class LoadData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lnProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            DaoMaster daoMaster;
            CartoonDao dao;
            ULog.i(this, "Load DB");

            daoMaster = ((MyApplication) getApplication()).daoMaster;
            dao = daoMaster.newSession().getCartoonDao();
            QueryBuilder<Cartoon> qb = dao.queryBuilder();

            qb.where(CartoonDao.Properties.Group.eq("1"));
            ULog.i(this, "===data db:" + qb.list().size());
            adapter = new AdapterCartoon(ListDetailAct.this, R.layout.item_cartoon, qb.list());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            ULog.i(this, "Finish load");

            lnProgress.setVisibility(View.GONE);
            lstCartoon.setAdapter(adapter);

        }
    }

    private class LoadDataMoreTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            if (isCancelled()) {
                return null;
            }

            // Simulates a background task
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

//                for (int i = 0; i < mNames.length; i++)
//                    mListItems.add(mNames[i]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//                mListItems.add("Added after load more"); ????????????
            ULog.i(this, "Set adapter");

            // We need notify the adapter that the data have been changed
            adapter.notifyDataSetChanged();

            // Call onLoadMoreComplete when the LoadMore task, has finished
            lstCartoon.onLoadMoreComplete();

            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            // Notify the loading more operation has finished
            ((LoadMoreListView) lstCartoon).onLoadMoreComplete();
        }
    }
}
