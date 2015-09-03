package nk.code.data;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;

public class EpochDatabase extends SQLiteAssetHelper {
	 private static final String DATABASE_NAME = "epoch.db";
	    private static final int DATABASE_VERSION = 1;

	    public EpochDatabase(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
}
