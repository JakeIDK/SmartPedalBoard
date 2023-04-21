package com.example.smartpedalboard.ProfileClasses
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProfileClass(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object
    {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "profiles.db"
        private const val TBL_PROFILE = "tbl_profile"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EFFECT1 = "effect1"
        private const val EFFECT2 = "effect2"

    }

    override fun onCreate(db:SQLiteDatabase?) {
        val createTblProfile = ("CREATE TABLE " + TBL_PROFILE + " ("+ ID +" INTEGER PRIMARY KEY,"
                + NAME + " TEXT," + EFFECT1 + " TEXT,"+ EFFECT2 + " TEXT" + ");")
        db?.execSQL(createTblProfile)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    db!!.execSQL("DROP TABLE IF EXISTS $TBL_PROFILE")
        onCreate(db)
    }
    fun insertProfile(pf: ProfileModel): Long
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,pf.id)
        contentValues.put(NAME,pf.name)
        contentValues.put(EFFECT1,pf.effect1)
        contentValues.put(EFFECT2,pf.effect2)

        val success = db.insert(TBL_PROFILE,null,contentValues)
        db.close()
        return success
    }
    @SuppressLint("Range")
    fun getAllProfile(): ArrayList<ProfileModel>
    {
        val pfList: ArrayList<ProfileModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_PROFILE"
        val db = this.readableDatabase
        val cursor: Cursor
        try
        {
            cursor = db.rawQuery(selectQuery,null)
        }
        catch (e:Exception)
        {
            db.execSQL(selectQuery)
            e.printStackTrace()
            return ArrayList()
        }
        var id: Int
        var name: String
        var effect1: String
        var effect2: String

        if(cursor.moveToFirst())
        {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                effect1 = cursor.getString(cursor.getColumnIndex("effect1"))
                effect2 = cursor.getString(cursor.getColumnIndex("effect2"))
                val pf = ProfileModel(id = id, name = name, effect1 = effect1, effect2 = effect2)
                pfList.add(pf)
            } while (cursor.moveToNext())
        }
        return pfList
    }
    fun updateProfiles(pfd: ProfileModel):Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, pfd.id)
        contentValues.put(NAME, pfd.name)
        contentValues.put(EFFECT1, pfd.effect1)
        contentValues.put(EFFECT2, pfd.effect2)
        val success = db.update(TBL_PROFILE,contentValues,"id" +pfd.id,null )
        db.close()
        return success
    }
}