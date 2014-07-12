package cartoon.youtube.vn.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cartoon.youtube.vn.DaoMaster;


/**
 * Created by huynhtran on 7/8/14.
 */
public class DbController {
    public static SQLiteDatabase init(Context context, boolean writable) {
        SQLiteDatabase database;
        DaoMaster.OpenHelper mOpenHelper;

        mOpenHelper = new DBOpenHelper(context);
        if (writable) {
            database = mOpenHelper.getWritableDatabase();
        } else {
            database = mOpenHelper.getReadableDatabase();
        }
        return database;
    }
}
