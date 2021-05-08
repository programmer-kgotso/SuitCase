package com.example.SuitCase.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.SuitCase.Model.ToPurchaseModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toPurchaseListDatabase";
    private static final String TOPURCHASE_TABLE = "topurchase";
    private static final String ID ="id";
    private static final String PRODUCT = "product";
    private static final String STATUS = "status";
    private static final String createTableUser = "CREATE TABLE if not exists 'user' ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT, 'username' TEXT," +
            " 'password' TEXT, 'email' TEXT, 'dob' TEXT, 'country' TEXT, 'gender' TEXT)";
    private static final String CREATE_TOPURCHASE_TABLE = "CREATE TABLE " + TOPURCHASE_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                    + PRODUCT + " TEXT, " + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context, NAME,null, VERSION);
        getWritableDatabase().execSQL(createTableUser);

    }

    public void insertUser(ContentValues contentValues){
        getWritableDatabase().insert("user", "", contentValues);
    }

    public boolean isLoginValid(String username, String password) {
        String sql = "Select count(*) from user where username='" + username + "' and password='" + password + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();

        if (l == 1) {
            return true;
        }else {
            return false;
        }

    }


    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TOPURCHASE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + TOPURCHASE_TABLE);
        //Create tables again
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertProduct(ToPurchaseModel product){
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT, product.getProduct());
        cv.put(STATUS, 0);
        db.insert(TOPURCHASE_TABLE, null, cv);
    }

    public List<ToPurchaseModel> getAllProducts(){
        List<ToPurchaseModel> productList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
            try{
                cur = db.query(TOPURCHASE_TABLE, null, null, null, null, null, null, null);
                if(cur != null){
                    if (cur.moveToFirst()){
                        do {
                            ToPurchaseModel product = new ToPurchaseModel();
                            product.setId(cur.getInt(cur.getColumnIndex(ID)));
                            product.setProduct(cur.getString(cur.getColumnIndex(PRODUCT)));
                            product.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                            productList.add(product);
                        }while(cur.moveToNext());
                    }
                }
            }
            finally {
                db.endTransaction();
                assert cur != null;
                cur.close();
            }
            return productList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TOPURCHASE_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateProduct(int id, String product) {
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT, product);
        db.update(TOPURCHASE_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteProduct(int id){
        db.delete(TOPURCHASE_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

}
