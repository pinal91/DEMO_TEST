package secure.com.giphydemo.offlinestorage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import secure.com.giphydemo.helper.Global

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    @Throws(SQLiteConstraintException::class)
    fun addProductToCart(product: OfflineDataModel): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TITLE, if (product.title == null) "" else product.title)
        values.put(COLUMN_DESCRIPTION, if (product.description == null) "" else product.description)
        values.put(COLUMN_IMAGE, if (product.image == null) "" else product.image)

        db.insert(TABLE_CART, null, values)
        db.close()
        close()
        return true
    }



    @Throws(SQLiteConstraintException::class)
    fun getAllCartProducts(): ArrayList<OfflineDataModel> {
        val arrListAllCartProducts = ArrayList<OfflineDataModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        cursor = try {
            db.rawQuery("select * from $TABLE_CART", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val title = Global.checkNull(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)))
                val description = Global.checkNull(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)))
                val image = Global.checkNull(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)))

                arrListAllCartProducts.add(OfflineDataModel(title,description,image))

                cursor.moveToNext()
            }
        }
        cursor.close()
        close()
        return arrListAllCartProducts
    }


    fun deleteTable(tableName: String) {
        val db = writableDatabase
        db.execSQL("delete from $tableName")
        close()
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Demo.db"
        const val TABLE_CART = "demo_list"

        private const val COLUMN_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE = "image"



        private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_CART + " (" +
                        COLUMN_ID + " TEXT PRIMARY KEY," +
                        COLUMN_TITLE + " TEXT," +
                        COLUMN_DESCRIPTION + " TEXT," +
                        COLUMN_IMAGE + " TEXT)"




        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_CART"
    }
}