package cartoon.youtube.vn.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cartoon.youtube.vn.Constant;
import cartoon.youtube.vn.DaoMaster;

/**
 * Created by huynhtran on 7/8/14.
 */
public class DBOpenHelper extends DaoMaster.OpenHelper  {

    public  DBOpenHelper(Context context) {
        super(context, Constant.DB_NAME, null);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}
