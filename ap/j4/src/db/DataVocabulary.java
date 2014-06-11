package sjpn4.vn.db;

import java.util.ArrayList;
import java.util.HashMap;

import sjpn4.vn.Util.ULog;
import sjpn4.vn.model.subModel.VocabularyList;
import android.content.Context;
import android.database.Cursor;

public class DataVocabulary extends BaseDatabase {

	private static DataVocabulary mInstance = null;

	@Override
	public String getTableName() {
		return TABLE_NAME_WORD;
	}

	private DataVocabulary(Context context) {
		super(context);
	}

	public static synchronized DataVocabulary getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DataVocabulary(context.getApplicationContext());
		}
		return mInstance;
	}

	public void insertWord(HashMap<String, String> queryValues) {
		insertData(queryValues);
	}

	// //////update
	public int updateWordById(HashMap<String, String> queryValues, String cond) {
		ULog.i(this, "updateWordById()...WORD_ID:" + cond);
		return updateData(queryValues, WORD_ID, cond);
	}

	public int updateWordLeand(String id, String value) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(WORD_LEANED, value);
		return updateData(map, WORD_ID, id);
	}

	// ///delete
	public void deleteWord(String cond) {
		deleteData(WORD_ID, cond);
	}

	// /select
	public ArrayList<VocabularyList> getAllWord() {
		VocabularyList wordList;
		ArrayList<VocabularyList> arrWord = new ArrayList<VocabularyList>();
		Cursor cursor;
		ULog.i(this, "getAllWord() ");
		cursor = getAllData();
		if (cursor == null)
			return null;
		if (cursor.moveToFirst()) {
			do {
				wordList = new VocabularyList();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					if (WORD_ID.equals(cursor.getColumnName(i)))
						wordList.ID = cursor.getString(i);
					else if (WORD_JP.equals(cursor.getColumnName(i)))
						wordList.wordJP = cursor.getString(i);
					else if (WORD_HIRAGANA.equals(cursor.getColumnName(i)))
						wordList.wordHiragana = cursor.getString(i);
					else if (WORD_EX.equals(cursor.getColumnName(i)))
						wordList.wordEX = cursor.getString(i);
					else if (WORD_EN.equals(cursor.getColumnName(i)))
						wordList.wordEN = cursor.getString(i);
					else if (WORD_VN.equals(cursor.getColumnName(i)))
						wordList.wordVN = cursor.getString(i);
					else if (WORD_LINK.equals(cursor.getColumnName(i)))
						wordList.wordLink = cursor.getString(i);
					else if (WORD_MEDIA.equals(cursor.getColumnName(i)))
						wordList.wordMedia = cursor.getString(i);
					else if (WORD_DAY.equals(cursor.getColumnName(i)))
						wordList.wordDay = cursor.getString(i);
					else if (WORD_LEANED.equals(cursor.getColumnName(i)))
						wordList.wordLean = cursor.getString(i);
					else if (WORD_VIEW.equals(cursor.getColumnName(i)))
						wordList.wordView = cursor.getString(i);
				}
				arrWord.add(wordList);
			} while (cursor.moveToNext());
		}

		return arrWord;
	}

	public ArrayList<VocabularyList> getWordById(String cond) {
		VocabularyList wordList;
		ArrayList<VocabularyList> arrWord = new ArrayList<VocabularyList>();
		Cursor cursor;
		ULog.i(this, "getWordById() ");
		cursor = getDataById(cond);
		if (cursor == null)
			return null;
		if (cursor.moveToFirst()) {
			do {
				wordList = new VocabularyList();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					if (WORD_ID.equals(cursor.getColumnName(i)))
						wordList.ID = cursor.getString(i);
					else if (WORD_JP.equals(cursor.getColumnName(i)))
						wordList.wordJP = cursor.getString(i);
					else if (WORD_HIRAGANA.equals(cursor.getColumnName(i)))
						wordList.wordHiragana = cursor.getString(i);
					else if (WORD_EX.equals(cursor.getColumnName(i)))
						wordList.wordEX = cursor.getString(i);
					else if (WORD_EN.equals(cursor.getColumnName(i)))
						wordList.wordEN = cursor.getString(i);
					else if (WORD_VN.equals(cursor.getColumnName(i)))
						wordList.wordVN = cursor.getString(i);
					else if (WORD_LINK.equals(cursor.getColumnName(i)))
						wordList.wordLink = cursor.getString(i);
					else if (WORD_MEDIA.equals(cursor.getColumnName(i)))
						wordList.wordMedia = cursor.getString(i);
					else if (WORD_DAY.equals(cursor.getColumnName(i)))
						wordList.wordDay = cursor.getString(i);
					else if (WORD_LEANED.equals(cursor.getColumnName(i)))
						wordList.wordLean = cursor.getString(i);
					else if (WORD_VIEW.equals(cursor.getColumnName(i)))
						wordList.wordView = cursor.getString(i);
				}
				arrWord.add(wordList);
			} while (cursor.moveToNext());
		}

		return arrWord;
	}

	private ArrayList<VocabularyList> getWordByCond(String[] whereClause, String[] whereArgs) {
		VocabularyList wordList;
		ArrayList<VocabularyList> arrWord = new ArrayList<VocabularyList>();
		Cursor cursor;
		// ULog.i(this, "getWordById() where:" + whereArgs);
		cursor = getDataByCond(whereClause, whereArgs);
		if (cursor == null) {
			ULog.e(this, "getWordByCond1 cursor is NULL");
			return null;
		}
		if (cursor.moveToFirst()) {
			ULog.i(DataVocabulary.this, "getWordByCond() get data....");

			do {
				wordList = new VocabularyList();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					if (WORD_ID.equals(cursor.getColumnName(i))) {
						wordList.ID = cursor.getString(i);
						// ULog.i(DataVocabulary.this,"getWordByCond() wordID:" + cursor.getString(i));
					} else if (WORD_JP.equals(cursor.getColumnName(i))) {
						wordList.wordJP = cursor.getString(i);
						// ULog.i(DataVocabulary.this,"getWordByCond() wordJP:" + cursor.getString(i));
					} else if (WORD_HIRAGANA.equals(cursor.getColumnName(i)))
						wordList.wordHiragana = cursor.getString(i);
					else if (WORD_EX.equals(cursor.getColumnName(i)))
						wordList.wordEX = cursor.getString(i);
					else if (WORD_EN.equals(cursor.getColumnName(i)))
						wordList.wordEN = cursor.getString(i);
					else if (WORD_VN.equals(cursor.getColumnName(i)))
						wordList.wordVN = cursor.getString(i);
					else if (WORD_VIET.equals(cursor.getColumnName(i)))
						wordList.wordViet = cursor.getString(i);
					else if (WORD_LINK.equals(cursor.getColumnName(i)))
						wordList.wordLink = cursor.getString(i);
					else if (WORD_MEDIA.equals(cursor.getColumnName(i)))
						wordList.wordMedia = cursor.getString(i);
					else if (WORD_DAY.equals(cursor.getColumnName(i)))
						wordList.wordDay = cursor.getString(i);
					else if (WORD_LEANED.equals(cursor.getColumnName(i)))
						wordList.wordLean = cursor.getString(i);
					else if (WORD_VIEW.equals(cursor.getColumnName(i))) {
						wordList.wordView = cursor.getString(i);
						// ULog.i(DataVocabulary.this,"getWordByCond() view:" + cursor.getString(i));
					}
				}
				arrWord.add(wordList);
			} while (cursor.moveToNext());
		}

		return arrWord;
	}

	public ArrayList<VocabularyList> getWordByLean1(String lean) {
		return getWordByCond(new String[] { WORD_LEANED }, new String[] { lean });
	}

	public ArrayList<VocabularyList> getWordByLeanDay(String lean, String day) {
		return getWordByCond(new String[] { WORD_LEANED, WORD_DAY }, new String[] { lean, day });
	}

}
