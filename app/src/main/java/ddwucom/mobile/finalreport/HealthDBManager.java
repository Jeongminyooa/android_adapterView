package ddwucom.mobile.finalreport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class HealthDBManager {
    HealthDBHelper dbHelper;

    public HealthDBManager(Context context) { dbHelper = new HealthDBHelper(context); }

    public ArrayList<Health> getAllFood() {
        ArrayList<Health> healthList = new ArrayList<Health>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(HealthDBHelper.TABLE_NAME, null, null, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            long _id = cursor.getLong(cursor.getColumnIndex(HealthDBHelper.COL_ID));
            String sex = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_SEX));
            String weight = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_WEIGHT));
            String height = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_HEIGHT));
            String age = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_AGE));
            String date = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_DATE));
            String exercise_Intensity = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_EXERCISE_INTENSITY));
            int picture = cursor.getInt(cursor.getColumnIndex(HealthDBHelper.COL_PICTURE));

            healthList.add(new Health(_id, sex, weight, height, age, date, exercise_Intensity, picture));
        }

        cursor.close();
        dbHelper.close();
        return healthList;
    }
    public boolean addNewHealth(Health newHealth) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues rows = new ContentValues();

        rows.put(HealthDBHelper.COL_SEX, newHealth.getSex());
        rows.put(HealthDBHelper.COL_WEIGHT, newHealth.getWeight());
        rows.put(HealthDBHelper.COL_HEIGHT, newHealth.getHeight());
        rows.put(HealthDBHelper.COL_AGE, newHealth.getAge());
        rows.put(HealthDBHelper.COL_DATE, newHealth.getDate());
        rows.put(HealthDBHelper.COL_EXERCISE_INTENSITY, newHealth.getExercise_intensity());
        rows.put(HealthDBHelper.COL_CALORIE, newHealth.getCalorie());
        rows.put(HealthDBHelper.COL_PICTURE, newHealth.getPicture());

        long count = db.insert(HealthDBHelper.TABLE_NAME, null, rows);

        dbHelper.close();
        if(count > 0) return true;
        else return false;
    }

    //    _id 를 기준으로 Health 의 모든 컬럼 변경
    public boolean modifyHealth(Health health) {
        String whereClause = HealthDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(health.get_id()) };

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues rows = new ContentValues();

        rows.put(HealthDBHelper.COL_SEX, health.getSex());
        rows.put(HealthDBHelper.COL_WEIGHT, health.getWeight());
        rows.put(HealthDBHelper.COL_HEIGHT, health.getHeight());
        rows.put(HealthDBHelper.COL_AGE, health.getAge());
        rows.put(HealthDBHelper.COL_DATE, health.getDate());
        rows.put(HealthDBHelper.COL_EXERCISE_INTENSITY, health.getExercise_intensity());
        rows.put(HealthDBHelper.COL_CALORIE, health.getCalorie());
        rows.put(HealthDBHelper.COL_PICTURE, health.getPicture());

        int result = db.update(HealthDBHelper.TABLE_NAME, rows, whereClause, whereArgs);

        dbHelper.close();
        if(result > 0) return true;
        return false;
    }

    //   Health 의 calorie를 계산한 값으로 변경
    public boolean modifyCalorie(Health health) {
        String whereClause = HealthDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(health.get_id()) };

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues rows = new ContentValues();

        rows.put(HealthDBHelper.COL_CALORIE, health.getCalorie());

        int result = db.update(HealthDBHelper.TABLE_NAME, rows, whereClause, whereArgs);

        dbHelper.close();
        if(result > 0) return true;
        return false;
    }
    //    _id 를 기준으로 DB에서 food 삭제
    public boolean removehealth(long id) {
        String whereClause = HealthDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int result = db.delete(HealthDBHelper.TABLE_NAME, whereClause, whereArgs);

        dbHelper.close();
        if(result > 0) return true;
        else return false;
    }

    //날짜를 통해 검색
    public Health getHealthByDate(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectClause = HealthDBHelper.COL_DATE + "=?";
        String[] selectArgs = new String[] {date};

        Cursor cursor = db.query(HealthDBHelper.TABLE_NAME, null, selectClause, selectArgs,
                null, null, null, null);

        int id = -1, picture = 0;
        String sex = "", weight = "", height = "", age = "", exercise = "";

        while(cursor.moveToNext()) {
             id = cursor.getInt(cursor.getColumnIndex(HealthDBHelper.COL_ID));
             sex = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_SEX));
             weight = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_WEIGHT));
             height = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_HEIGHT));
             age = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_AGE));
             exercise = cursor.getString(cursor.getColumnIndex(HealthDBHelper.COL_EXERCISE_INTENSITY));
             picture = cursor.getInt(cursor.getColumnIndex(HealthDBHelper.COL_PICTURE));
        }
        dbHelper.close();
        if(id != -1)
            return new Health(id, sex, weight, height, age, date, exercise, picture);
        else
            return null;
    }

}
