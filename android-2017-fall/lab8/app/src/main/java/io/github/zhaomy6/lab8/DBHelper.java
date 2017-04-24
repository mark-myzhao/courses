package io.github.zhaomy6.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;

public class DBHelper extends SQLiteOpenHelper {
    private final String DB_NAME = "Lab8DB";
    private final String TABLE_NAME = "BirthdayGiftList";
    private final int VERSION = 1;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE if not exists " + this.TABLE_NAME +
                " (id INTEGER PRIMARY KEY autoincrement, name TEXT, birth TEXT, gift TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    //  insert item into db
    public void insertDBItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", item.getName());
        cv.put("birth", item.getBirthday());
        cv.put("gift", item.getGift());
        db.insert(this.TABLE_NAME, null, cv);
        Log.d("DBHelper", "insert success");
        db.close();
    }

    //  delete the item whose name == name
    public void deleteDBItem(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {name};
        String whereClause = "name = ?";
        db.delete(this.TABLE_NAME, whereClause, args);
        db.close();
    }

    public void updateDBItem(String name, Item newItem) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] arg = {name};
        ContentValues cv = new ContentValues();
        cv.put("name", newItem.getName());
        cv.put("birth", newItem.getBirthday());
        cv.put("gift", newItem.getGift());
        db.update(this.TABLE_NAME, cv, whereClause, arg);
        db.close();
    }

    public LinkedList<Item> queryDBItem(String[] name) {
        SQLiteDatabase db = getWritableDatabase();
        LinkedList<Item> list = new LinkedList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM BirthdayGiftList WHERE name = ?", name);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex("name");
            String n = cursor.getString(index);
            index = cursor.getColumnIndex("birth");
            String birthday = cursor.getString(index);
            index = cursor.getColumnIndex("gift");
            String gift = cursor.getString(index);
            list.add(new Item(n, birthday, gift));
        }
        cursor.close();
        db.close();
        return list;
    }

    public LinkedList<Item> getAll() {
        LinkedList<Item> list = new LinkedList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM BirthdayGiftList", null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex("name");
            String name = cursor.getString(index);
            index = cursor.getColumnIndex("birth");
            String birthday = cursor.getString(index);
            index = cursor.getColumnIndex("gift");
            String gift = cursor.getString(index);
            list.add(new Item(name, birthday, gift));
        }
        cursor.close();
        db.close();
        return list;
    }
}
