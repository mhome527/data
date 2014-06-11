package sjpn4.vn.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import sjpn4.vn.Util.Common;
import sjpn4.vn.Util.ULog;
import sjpn4.vn.model.VocabularyModel;
import sjpn4.vn.model.subModel.VocabularyList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseDatabase extends SQLiteOpenHelper {

	private static String DATABASE_NAME = "SjpN4.db";
	private static final int DATABASE_VERSION = 1;
	public String TABLE_NAME_WORD = "Vocabulary";
	public String WORD_ID = "id";
	public String WORD_JP = "jp";
	public String WORD_HIRAGANA = "hiragana";
	public String WORD_EN = "en";
	public String WORD_EX = "ex";
	public String WORD_VN = "vn";
	public String WORD_VIET = "viet";
	public String WORD_MEDIA = "media";
	public String WORD_LINK = "link";
	public String WORD_DAY = "day";
	public String WORD_LEANED = "leaned";
	public String WORD_VIEW = "view";

	private String table_name;
	private Context context;

	private SQLiteDatabase database;

	protected abstract String getTableName();

	protected BaseDatabase(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
//		ULog.i(this, "BaseDatabase() 2");
	}

	protected BaseDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		table_name = getTableName();
		this.context = context;
		database = this.getWritableDatabase();
//		ULog.i(this, "BaseDatabase() ");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		ULog.i(BaseDatabase.class, "onCreate() create database ");
		db.execSQL("CREATE TABLE " + TABLE_NAME_WORD + " (" + WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ WORD_JP + " TEXT," + WORD_HIRAGANA + " TEXT," + WORD_EX + " TEXT," 
				+ WORD_EN + " TEXT," + WORD_VN + " TEXT,"
				+ WORD_VIEW + "  INTEGER DEFAULT 0," + WORD_VIET + " TEXT," 
				+ WORD_MEDIA + " TEXT," + WORD_LINK + " TEXT, " + WORD_DAY + " INTEGER," + WORD_LEANED + " INTEGER DEFAULT 0);");

		initInsertData(db);
		closeDB();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String query = "DROP TABLE IF EXISTS " + TABLE_NAME_WORD;
		db.execSQL(query);
		onCreate(db);

	}

	protected void closeDB() {
		try {
			if (database != null && database.isOpen())
				database.close();
		} catch (Exception e) {
			ULog.e(this, "closeDB() " + e.getMessage());
		}
	}

	protected void initInsertData(SQLiteDatabase db) {
//		ULog.i(BaseDatabase.class, "initInsertData() ");
		HashMap<String, String> queryValues = new HashMap<String, String>();
		VocabularyModel model = (VocabularyModel) Common.getObjectJson(context);
		if(model==null){
			ULog.e(BaseDatabase.class, "Read Json error!!!!!");
			return;
		}
		List<VocabularyList> list = model.vocabularyList;
		for (VocabularyList result : list) {
			if (result != null) {
				queryValues.put(WORD_JP, result.wordJP);
				queryValues.put(WORD_HIRAGANA, result.wordHiragana);
				queryValues.put(WORD_EX, result.wordEX);
				queryValues.put(WORD_EN, result.wordEN);
				queryValues.put(WORD_VN, result.wordVN);
				queryValues.put(WORD_VIET, result.wordViet);
				queryValues.put(WORD_LINK, result.wordLink);
				queryValues.put(WORD_MEDIA, result.wordMedia);
				queryValues.put(WORD_DAY, result.wordDay);
				queryValues.put(WORD_LEANED, "0");
				queryValues.put(WORD_VIEW, "0");
				insertData(db, queryValues);
			}
		}
		ULog.i(BaseDatabase.class, "initInsertData() inserted ALL");
	}

	protected void insertData(SQLiteDatabase db, HashMap<String, String> queryValues) {
		String key;
		ContentValues values = new ContentValues();
		ULog.i(BaseDatabase.class, "insertData() ");
		Iterator<String> myIterator = queryValues.keySet().iterator();
		while (myIterator.hasNext()) {
			key = (String) myIterator.next();
			values.put(key, queryValues.get(key));
		}

		db.insert(table_name, null, values);
	}

	protected void insertData(HashMap<String, String> queryValues) {
		String key;
		ContentValues values = new ContentValues();
//		ULog.i(BaseDatabase.class, "insertData() ");
		Iterator<String> myIterator = queryValues.keySet().iterator();
		while (myIterator.hasNext()) {
			key = (String) myIterator.next();
			values.put(key, queryValues.get(key));
		}

		database.insert(table_name, null, values);
	}

	protected void deleteData(String key, String cond) {
		ULog.i(BaseDatabase.class, "deleteData() ");
		String deleteQuery = "DELETE FROM " + table_name + " WHERE " + key + "='" + cond + "'";
		database.execSQL(deleteQuery);
	}

	protected int updateData(HashMap<String, String> queryValues, String key, String value) {
		String nameColumn;
		ContentValues values = new ContentValues();
		ULog.i(BaseDatabase.class, "updateData() key:" + key +"; value:" + value);
		Iterator<String> myIterator = queryValues.keySet().iterator();
		while (myIterator.hasNext()) {
			nameColumn = (String) myIterator.next();
			values.put(nameColumn, queryValues.get(nameColumn));
		}

		return database.update(table_name, values, key + " = ?", new String[] { value });
	}

	protected Cursor getAllData() {
		Cursor cursor = null;
		String selectQuery = "SELECT  * FROM " + table_name;
		cursor = database.rawQuery(selectQuery, null);
		return cursor;
	}

	protected Cursor getDataById(String cond) {
		Cursor cursor = null;
		String selectQuery = "SELECT  * FROM " + table_name + " WHERE id='" + cond + "'";
		cursor = database.rawQuery(selectQuery, null);
		return cursor;

	}

	protected Cursor getDataByCond(String[] whereClause, String[] whereArgs) {
		Cursor cursor = null;
		String selectQuery = "SELECT  * FROM " + table_name + " WHERE ";
		for (int i = 0; i < whereClause.length; i++) {
			selectQuery += whereClause[i] + "=? ";
			if (i < whereClause.length - 1)
				selectQuery += " AND ";
		}
		ULog.i(this, "getDataByCond() select: " + selectQuery);
		// String selectQuery = "SELECT  * FROM " + table_name + " WHERE id='" + cond + "'";

		cursor = database.rawQuery(selectQuery, whereArgs);
		return cursor;

	}
}
