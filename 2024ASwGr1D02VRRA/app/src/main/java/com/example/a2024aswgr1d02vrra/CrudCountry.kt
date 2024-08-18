package com.example.a2024aswgr1d02vrra

/*
class CrudCountry : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crud_country)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}*/
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class CrudCountry : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_country)

        val name = findViewById<EditText>(R.id.input_author_name)
        val age = findViewById<EditText>(R.id.input_author_age)
        val literaryGenre = findViewById<EditText>(R.id.input_author_literary_genre)
        val latitude = findViewById<EditText>(R.id.latitude)
        val longitude = findViewById<EditText>(R.id.longitude)
        val selectedAuthor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("selectedAuthor", CountryEntity::class.java)
        } else {
            intent.getParcelableExtra<CountryEntity>("selectedAuthor")
        }

        if(selectedAuthor == null) {
            // Create an author
            val btnCreateUpdateAuthor = findViewById<Button>(R.id.btn_create_update_author)
            btnCreateUpdateAuthor.setOnClickListener{
                DataBase.tables!!.createCountry(
                    name.text.toString(),
                    age.text.toString().toInt(),
                    literaryGenre.text.toString(),
                    latitude.text.toString(),
                    longitude.text.toString()
                )
                goToActivity(MainActivity::class.java)
            }
        } else {
            name.setText(selectedAuthor.name)
            age.setText(selectedAuthor.population.toString())
            literaryGenre.setText(selectedAuthor.description)
            latitude.setText(selectedAuthor.latitude)
            longitude.setText(selectedAuthor.longitude)

            // Update an author
            val btnCreateUpdateAuthor = findViewById<Button>(R.id.btn_create_update_author)
            btnCreateUpdateAuthor.setOnClickListener{
                DataBase.tables!!.updateAuthor(
                    selectedAuthor.id,
                    name.text.toString(),
                    age.text.toString().toInt(),
                    literaryGenre.text.toString(),
                    latitude.text.toString(),
                    longitude.text.toString()
                )
                goToActivity(MainActivity::class.java)
            }
        }

    }

    private fun goToActivity(
        activityClass: Class<*>
    ) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}