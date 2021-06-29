package ddwucom.mobile.finalreport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HealthDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "health_table";
    public static final String COL_ID = "_id";
    public static final String COL_SEX = "sex";
    public static final String COL_WEIGHT = "weight";
    public static final String COL_HEIGHT = "height";
    public static final String COL_AGE = "age";
    public static final String COL_DATE = "date";
    public static final String COL_EXERCISE_INTENSITY = "exercise_Intensity";
    public static final String COL_CALORIE = "calorie";
    public static final String COL_PICTURE = "picture";
    static final String DB_NAME = "health.db";

    public HealthDBHelper(@Nullable Context context) { super(context, DB_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement, " +
                COL_SEX + " TEXT, " + COL_HEIGHT + " TEXT, " + COL_WEIGHT + " TEXT, " + COL_AGE + " TEXT, " + COL_DATE + " TEXT, " +
                COL_EXERCISE_INTENSITY + " TEXT, " + COL_PICTURE + " integer, " + COL_CALORIE + " TEXT )";
        db.execSQL(sql);

        db.execSQL("insert into " + TABLE_NAME + " values (null, '여자', '163.4', '62.8', '21', '2020-12-31', '1'," + R.mipmap.sample1 +" , '0');");
        db.execSQL("insert into " + TABLE_NAME + " values (null, '여자', '163.4', '58.3', '22', '2021-2-17', '2'," + R.mipmap.sample2 +" , '0');");
        db.execSQL("insert into " + TABLE_NAME + " values (null, '여자', '163.4', '56.3', '22', '2021-3-1', '3'," + R.mipmap.sample3 +" , '0');");
        db.execSQL("insert into " + TABLE_NAME + " values (null, '여자', '163.4', '53.3', '22', '2021-4-12', '3'," + R.mipmap.sample4 +" , '0');");
        db.execSQL("insert into " + TABLE_NAME + " values (null, '여자', '163.4', '49.7', '22', '2021-5-18', '1'," + R.mipmap.sample5 +" , '0');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
