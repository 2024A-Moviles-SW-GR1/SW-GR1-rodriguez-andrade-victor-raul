package com.example.a2024aswgr1d02vrra

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
/*
class CrudCity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crud_city)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}*/

import android.content.Intent
import android.os.Build
import android.widget.Button
import android.widget.EditText


class CrudCity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_city)

        val title = findViewById<EditText>(R.id.input_book_title)
        val description = findViewById<EditText>(R.id.input_book_description)
        val authorId = findViewById<EditText>(R.id.input_book_author_id)
        val selectedBook = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("selectedBook", CityEntity::class.java)
        } else {
            intent.getParcelableExtra<CityEntity>("selectedBook")
        }
        val selectedAuthor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("selectedAuthor", CountryEntity::class.java)
        } else {
            intent.getParcelableExtra<CountryEntity>("selectedAuthor")
        }
        val create = intent.getBooleanExtra("create", true)

        if(create) {
            authorId.setText(selectedAuthor!!.id.toString())

            // Create a book
            val btnCreateUpdateBook = findViewById<Button>(R.id.btn_create_update_book)
            btnCreateUpdateBook.setOnClickListener{
                DataBase.tables!!.createCity(
                    title.text.toString(),
                    description.text.toString(),
                    authorId.text.toString().toInt()
                )
                goToActivity(CitiesList::class.java, selectedAuthor)
            }
        } else {
            title.setText(selectedBook!!.cityName)
            description.setText(selectedBook.description)
            authorId.setText(selectedBook.countryId.toString())

            // Update a book
            val btnCreateUpdateBook = findViewById<Button>(R.id.btn_create_update_book)
            btnCreateUpdateBook.setOnClickListener{
                DataBase.tables!!.updateBook(
                    selectedBook.id,
                    title.text.toString(),
                    description.text.toString(),
                    authorId.text.toString().toInt()
                )
                goToActivity(CitiesList::class.java, selectedAuthor!!)
            }
        }
    }

    private fun goToActivity(
        activityClass: Class<*>,
        dataToPass: CountryEntity
    ) {
        val intent = Intent(this, activityClass)
        intent.apply {
            putExtra("selectedAuthor", dataToPass)
        }
        startActivity(intent)
    }

}