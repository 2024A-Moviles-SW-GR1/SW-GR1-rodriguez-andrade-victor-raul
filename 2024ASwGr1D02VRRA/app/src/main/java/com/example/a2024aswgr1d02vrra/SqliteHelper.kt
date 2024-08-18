package com.example.a2024aswgr1d02vrra

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(
    context: Context? /* this */
) : SQLiteOpenHelper(context, "AndroidApp", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createCountryTable = """
            CREATE TABLE COUNTRY(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name VARCHAR(100),
                population INTEGER,
                description VARCHAR(50),
                latitude VARCHAR(50),
                longitude VARCHAR(50)
            );
        """.trimIndent()

        val createCityTable = """
            CREATE TABLE CITY(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title VARCHAR(100),
                description VARCHAR(200),
                countryId INTEGER,
                FOREIGN KEY (countryId) REFERENCES COUNTRY(id) ON DELETE CASCADE
            );
        """.trimIndent()

        db?.execSQL(createCountryTable)
        db?.execSQL(createCityTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun getAllAuthors():ArrayList<CountryEntity> {
        val lectureDB =readableDatabase
        val queryScript ="""
            SELECT * FROM COUNTRY
        """.trimIndent()
        val queryResult = lectureDB.rawQuery(
            queryScript,
            emptyArray()
        )
        val response = arrayListOf<CountryEntity>()

        if(queryResult.moveToFirst()) {
            do {
                response.add(
                    CountryEntity(
                        queryResult.getInt(0),
                        queryResult.getString(1),
                        queryResult.getInt(2),
                        queryResult.getString(3),
                        queryResult.getString(4),
                        queryResult.getString(5)
                    )
                )
            } while(queryResult.moveToNext())
        }
        queryResult.close()
        lectureDB.close()

        return response
    }

    fun getBooksByAuthor(countryId: Int):ArrayList<CityEntity> {
        val lectureDB =readableDatabase
        val queryScript ="""
            SELECT * FROM CITY WHERE countryId=?
        """.trimIndent()
        val queryResult = lectureDB.rawQuery(
            queryScript,
            arrayOf(countryId.toString())
        )
        val response = arrayListOf<CityEntity>()

        if(queryResult.moveToFirst()) {
            do {
                response.add(
                    CityEntity(
                        queryResult.getInt(0),
                        queryResult.getString(1),
                        queryResult.getString(2),
                        queryResult.getInt(3)
                    )
                )
            } while(queryResult.moveToNext())
        }
        queryResult.close()
        lectureDB.close()

        return response
    }

    fun createCountry(
        name: String,
        population: Int,
        description: String,
        latitude: String,
        longitude: String
    ): Boolean {
        val writeDB = writableDatabase
        val valuesToStore = ContentValues()
        valuesToStore.put("name", name)
        valuesToStore.put("population", population)
        valuesToStore.put("description", description)
        valuesToStore.put("latitude", latitude)
        valuesToStore.put("longitude", longitude)

        val storeResult = writeDB.insert(
            "COUNTRY", // Table name
            null,
            valuesToStore // Values to insert
        )
        writeDB.close()

        return storeResult.toInt() != -1
    }

    fun createCity(
        title: String,
        description: String,
        countryId: Int
    ): Boolean {
        val writeDB = writableDatabase
        val valuesToStore = ContentValues()
        valuesToStore.put("title", title)
        valuesToStore.put("description", description)
        valuesToStore.put("countryId", countryId)

        val storeResult = writeDB.insert(
            "CITY", // Table name
            null,
            valuesToStore // Values to insert
        )
        writeDB.close()

        return storeResult.toInt() != -1
    }

    fun updateAuthor(
        id: Int,
        name: String,
        population: Int,
        description: String,
        latitude: String,
        longitude: String
    ): Boolean {
        val writeDB = writableDatabase
        val valuesToUpdate = ContentValues()
        valuesToUpdate.put("name", name)
        valuesToUpdate.put("population", population)
        valuesToUpdate.put("description", description)
        valuesToUpdate.put("latitude", latitude)
        valuesToUpdate.put("longitude", longitude)

        val parametersUpdateQuery = arrayOf(id.toString())
        val updateResult = writeDB.update(
            "COUNTRY", // Table name
            valuesToUpdate,
            "id=?",
            parametersUpdateQuery
        )
        writeDB.close()

        return updateResult != -1
    }

    fun updateBook(
        id: Int,
        title: String,
        description: String,
        countryId: Int
    ): Boolean {
        val writeDB = writableDatabase
        val valuesToUpdate = ContentValues()
        valuesToUpdate.put("title", title)
        valuesToUpdate.put("description", description)
        valuesToUpdate.put("countryId", countryId)

        val parametersUpdateQuery = arrayOf(id.toString())
        val updateResult = writeDB.update(
            "CITY", // Table name
            valuesToUpdate,
            "id=?",
            parametersUpdateQuery
        )
        writeDB.close()

        return updateResult != -1
    }

    fun deleteCountry(id:Int): Boolean {
        val writeDB = writableDatabase
        // SQL query example: where .... ID=? AND NAME=? [?=1, ?=2]
        val parametersDeleteQuery = arrayOf(id.toString())
        val deleteResult = writeDB.delete(
            "COUNTRY",
            "id=?",
            parametersDeleteQuery
        )
        writeDB.close()

        return deleteResult != -1
    }

    fun deleteCity(id:Int): Boolean {
        val writeDB = writableDatabase
        // SQL query example: where .... ID=? AND NAME=? [?=1, ?=2]
        val parametersDeleteQuery = arrayOf(id.toString())
        val deleteResult = writeDB.delete(
            "CITY",
            "id=?",
            parametersDeleteQuery
        )
        writeDB.close()

        return deleteResult != -1
    }

}