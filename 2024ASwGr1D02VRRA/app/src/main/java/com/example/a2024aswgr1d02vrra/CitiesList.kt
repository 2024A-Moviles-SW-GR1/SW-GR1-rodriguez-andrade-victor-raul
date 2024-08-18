package com.example.a2024aswgr1d02vrra

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
/*
class CitiesList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cities_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}*/


import android.content.Intent
import android.os.Build
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class CitiesList : AppCompatActivity() {

    private var allBooks:ArrayList<CityEntity> = arrayListOf()
    private var adapter:ArrayAdapter<CityEntity>? = null
    private var selectedRegisterPosition = -1
    private var selectedAuthor: CountryEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities_list)


        selectedAuthor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("selectedAuthor", CountryEntity::class.java)
        } else {
            intent.getParcelableExtra<CountryEntity>("selectedAuthor")
        }
        val booksAuthor = findViewById<TextView>(R.id.txt_view_author_name)
        booksAuthor.text = selectedAuthor!!.name

        DataBase.tables = SqliteHelper(this)
        val booksList = findViewById<ListView>(R.id.list_books)
        allBooks = DataBase.tables!!.getBooksByAuthor(selectedAuthor!!.id)
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            allBooks
        )

        booksList.adapter = adapter
        adapter!!.notifyDataSetChanged() // To update the UI

        val btnRedirectCreateBook = findViewById<Button>(R.id.btn_redirect_create_book)
        btnRedirectCreateBook.setOnClickListener{
            goToActivity(CrudCity::class.java, null, selectedAuthor!!)
        }
        val btnBackToMain = findViewById<Button>(R.id.btn_back_to_main)
        btnBackToMain.setOnClickListener{
            goToActivity(MainActivity::class.java)
        }

        registerForContextMenu(booksList)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        // Set options for the context menu
        val inflater = menuInflater
        inflater.inflate(R.menu.city_menu, menu)

        // Get ID of the selected item of the list
        val register = menuInfo as AdapterView.AdapterContextMenuInfo
        selectedRegisterPosition = register.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_edit_book -> {
                goToActivity(CrudCity::class.java, allBooks[selectedRegisterPosition], selectedAuthor!!, false)
                return true
            }
            R.id.mi_delete_book -> {
                openDialog(allBooks[selectedRegisterPosition].id)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun goToActivity(
        activityClass: Class<*>,
        dataToPass: CityEntity? = null,
        dataToPass2: CountryEntity? = null,
        create: Boolean = true
    ) {
        val intent = Intent(this, activityClass)
        if(dataToPass != null) {
            intent.apply {
                putExtra("selectedBook", dataToPass)
            }
        }
        if(dataToPass2 != null) {
            intent.apply {
                putExtra("selectedAuthor", dataToPass2)
            }
        }
        intent.apply {
            putExtra("create", create)
        }
        startActivity(intent)
    }

    private fun openDialog(registerIndex: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Está seguro de eliminar la ciudad?")
        builder.setPositiveButton(
            "Eliminar"
        ) { _, _ ->
            DataBase.tables!!.deleteCity(registerIndex)
            allBooks.clear()
            allBooks.addAll(DataBase.tables!!.getBooksByAuthor(selectedAuthor!!.id))
            adapter!!.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancelar", null)

        builder.create().show()
    }

}
