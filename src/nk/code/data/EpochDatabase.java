package nk.code.data;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;

public class EpochDatabase extends SQLiteAssetHelper {
	 private static final String DATABASE_NAME = "epoch.db";
	    private static final int DATABASE_VERSION = 1;
	    public static final String U_TABLE = "epoch";
		public static final String U_ID = "id";
		public static final String U_EPOCH = "timeline";
		public static final String U_TYPE = "tip";
		public static final String U_NAME= "naziv";
		public static final String U_START = "start";
		public static final String U_END = "end";
		public static final String U_DESCRIPTION = "opis";
		public static final String U_SIZE = "size";
		public static final String U_STYLE= "stil";
		public static final String U_COLOR = "boja";
		public static final String U_VISIBILITY = "vidljivost";
		public static final String U_Y= "y";
		public static final String U_LOOK= "look";
		public static final String U_EXTRA1= "extra1";
		
	    public EpochDatabase(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }


}
