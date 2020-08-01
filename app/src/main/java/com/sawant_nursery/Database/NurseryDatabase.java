package com.sawant_nursery.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sawant_nursery.Model.ProductResponse;

import java.util.ArrayList;
import java.util.List;


public class NurseryDatabase extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "nursery_db";

    //Table Name
    public static final String TABLE_NAME = "adminStatus";
    public static final String PRODUCT_AMOUNT_TABLE_NAME = "productAmount";

    // Admin Status Table
    public static final String ADMIN_STATUS_ID_PK = "admin_status_id_pk";
    public static final String ADMIN_ID_PK = "admin_id_fk";
    public static final String ADMIN_RETAILER_STATUS = "admin_retailer_status";
    public static final String ADMIN_WHOLESALER_STATUS = "admin_wholesaler_status";

    // Product Amount Table
    public static final String AMOUNT_ID_PK = "amount_id_pk";
    public static final String ADMIN_ID_FK = "admin_id_fk";
    public static final String PRODUCT_ID_PK = "product_id_fk";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PLANT_SIZE_ID_PK = "plant_size_id_fk";
    public static final String PLANT_SIZE_NAME = "plant_size_name";
    public static final String BAG_SIZE_ID_PK = "bag_size_id_fk";
    public static final String BAG_SIZE_NAME = "bag_size_name";
    public static final String CGST = "cgst";
    public static final String SGST = "sgst";
    public static final String IGST = "igst";
    public static final String RETAILER_PRICE = "retailer_price";
    public static final String WHOLESALER_PRICE = "wholesaler_price";
    public static final String AMOUNT_STATUS = "amount_status";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private String CART_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            "(admin_status_id_pk INTEGER PRIMARY KEY AUTOINCREMENT, admin_id_fk VARCHAR, admin_retailer_status VARCHAR, admin_distributor_status VARCHAR)";


    private String PRODUCT_AMOUNT_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + PRODUCT_AMOUNT_TABLE_NAME +
            "(amount_id_pk INTEGER PRIMARY KEY AUTOINCREMENT, admin_id_fk VARCHAR, product_id_fk VARCHAR, product_name VARCHAR, plant_size_id_fk VARCHAR, plant_size_name VARCHAR, bag_size_id_fk VARCHAR," +
            " bag_size_name VARCHAR, cgst VARCHAR, sgst VARCAR, igst VARCHAR, retailer_price VARCHAR, wholesaler_price VARCHAR, amount_status VARCHAR)";


    public NurseryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CART_TABLE_QUERY);
        db.execSQL(PRODUCT_AMOUNT_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String amount_table_sql = "DROP TABLE IF EXISTS " + PRODUCT_AMOUNT_TABLE_NAME;

        db.execSQL(sql);
        db.execSQL(amount_table_sql);

    }

    public boolean insertStatus(String userId, String retailerStatus, String wholesalerStatus) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ADMIN_ID_PK, userId);
        contentValues.put(ADMIN_RETAILER_STATUS, retailerStatus);
        contentValues.put(ADMIN_WHOLESALER_STATUS, wholesalerStatus);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();

        return true;

    }

    public boolean insertAmount(String userId, String productId, String productName, String plantSizeId, String plantSizeName, String bagSizeId, String bagSizeName, String cgst, String sgst, String igst,
                                String retailerPrice, String wholesalerPrice) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ADMIN_ID_FK, userId);
        contentValues.put(PRODUCT_ID_PK, productId);
        contentValues.put(PRODUCT_NAME, productName);
        contentValues.put(PLANT_SIZE_ID_PK, plantSizeId);
        contentValues.put(PLANT_SIZE_NAME, plantSizeName);
        contentValues.put(BAG_SIZE_ID_PK, bagSizeId);
        contentValues.put(BAG_SIZE_NAME, bagSizeName);
        contentValues.put(CGST, cgst);
        contentValues.put(SGST, sgst);
        contentValues.put(IGST, igst);
        contentValues.put(RETAILER_PRICE, retailerPrice);
        contentValues.put(WHOLESALER_PRICE, wholesalerPrice);
        contentValues.put(AMOUNT_STATUS, "0");

        sqLiteDatabase.insert(PRODUCT_AMOUNT_TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();

        return true;

    }

    public List<ProductResponse> getAmountList(String userId) {

        String inCompleted = "0";

        List<ProductResponse> productResponseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PRODUCT_AMOUNT_TABLE_NAME + " WHERE admin_id_fk = '" + userId + "' AND amount_status = '" + inCompleted + "'", null);
        ProductResponse productResponse;
        while (cursor.moveToNext()) {

            productResponse = new ProductResponse();

            productResponse.setAmountId(cursor.getString(cursor.getColumnIndex(AMOUNT_ID_PK)));
            productResponse.setProductId(cursor.getString(cursor.getColumnIndex(PRODUCT_ID_PK)));
            productResponse.setProductName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
            productResponse.setProductSize(cursor.getString(cursor.getColumnIndex(PLANT_SIZE_ID_PK)));
            productResponse.setPlantSizeName(cursor.getString(cursor.getColumnIndex(PLANT_SIZE_NAME)));
            productResponse.setBagSize(cursor.getString(cursor.getColumnIndex(BAG_SIZE_ID_PK)));
            productResponse.setBagSizeName(cursor.getString(cursor.getColumnIndex(BAG_SIZE_NAME)));
            productResponse.setCgst(cursor.getString(cursor.getColumnIndex(CGST)));
            productResponse.setSgst(cursor.getString(cursor.getColumnIndex(SGST)));
            productResponse.setIgst(cursor.getString(cursor.getColumnIndex(IGST)));
            productResponse.setRetailPrice(cursor.getString(cursor.getColumnIndex(RETAILER_PRICE)));
            productResponse.setWholesalerPrice(cursor.getString(cursor.getColumnIndex(WHOLESALER_PRICE)));

            productResponseList.add(productResponse);

        }
        db.close();
        return productResponseList;

    }

    public List<ProductResponse> getProductList(String userId, String productId, String plantSizeId, String bagSizeId) {

        String inCompleted = "0";

        List<ProductResponse> productResponseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PRODUCT_AMOUNT_TABLE_NAME + " WHERE admin_id_fk = '" + userId + "' AND product_id_fk = '"+productId+"' AND plant_size_id_fk  = '"+plantSizeId+"' AND bag_size_id_fk = '"+bagSizeId+"' AND amount_status = '" + inCompleted + "'", null);
        ProductResponse productResponse;
        while (cursor.moveToNext()) {

            productResponse = new ProductResponse();

            productResponse.setAmountId(cursor.getString(cursor.getColumnIndex(AMOUNT_ID_PK)));
            productResponse.setProductId(cursor.getString(cursor.getColumnIndex(PRODUCT_ID_PK)));
            productResponse.setProductName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
            productResponse.setProductSize(cursor.getString(cursor.getColumnIndex(PLANT_SIZE_ID_PK)));
            productResponse.setPlantSizeName(cursor.getString(cursor.getColumnIndex(PLANT_SIZE_NAME)));
            productResponse.setBagSize(cursor.getString(cursor.getColumnIndex(BAG_SIZE_ID_PK)));
            productResponse.setBagSizeName(cursor.getString(cursor.getColumnIndex(BAG_SIZE_NAME)));
            productResponse.setCgst(cursor.getString(cursor.getColumnIndex(CGST)));
            productResponse.setSgst(cursor.getString(cursor.getColumnIndex(SGST)));
            productResponse.setIgst(cursor.getString(cursor.getColumnIndex(IGST)));
            productResponse.setRetailPrice(cursor.getString(cursor.getColumnIndex(RETAILER_PRICE)));
            productResponse.setWholesalerPrice(cursor.getString(cursor.getColumnIndex(WHOLESALER_PRICE)));

            productResponseList.add(productResponse);

        }
        db.close();
        return productResponseList;

    }

    public boolean updateStatus(String amountId, String status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(AMOUNT_STATUS, status);

        db.update(PRODUCT_AMOUNT_TABLE_NAME, contentValues, "amount_id_pk=?", new String[]{amountId});

        return true;

    }

    public List<ProductResponse> getProductAmountList(String userId, String productId) {

        String inCompleted = "0";

        List<ProductResponse> productResponseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PRODUCT_AMOUNT_TABLE_NAME + " WHERE admin_id_fk = '" + userId + "' AND product_id_fk = '" + productId + "' AND amount_status = '" + inCompleted + "'", null);
        ProductResponse productResponse;
        while (cursor.moveToNext()) {

            productResponse = new ProductResponse();

            productResponse.setAmountId(cursor.getString(cursor.getColumnIndex(AMOUNT_ID_PK)));
            productResponse.setProductId(cursor.getString(cursor.getColumnIndex(PRODUCT_ID_PK)));
            productResponse.setProductName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
            productResponse.setProductSize(cursor.getString(cursor.getColumnIndex(PLANT_SIZE_ID_PK)));
            productResponse.setPlantSizeName(cursor.getString(cursor.getColumnIndex(PLANT_SIZE_NAME)));
            productResponse.setBagSize(cursor.getString(cursor.getColumnIndex(BAG_SIZE_ID_PK)));
            productResponse.setBagSizeName(cursor.getString(cursor.getColumnIndex(BAG_SIZE_NAME)));
            productResponse.setCgst(cursor.getString(cursor.getColumnIndex(CGST)));
            productResponse.setSgst(cursor.getString(cursor.getColumnIndex(SGST)));
            productResponse.setIgst(cursor.getString(cursor.getColumnIndex(IGST)));
            productResponse.setRetailPrice(cursor.getString(cursor.getColumnIndex(RETAILER_PRICE)));
            productResponse.setWholesalerPrice(cursor.getString(cursor.getColumnIndex(WHOLESALER_PRICE)));

            productResponseList.add(productResponse);

        }
        db.close();
        return productResponseList;

    }
}
