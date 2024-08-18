package com.example.a2024aswgr1d02vrra

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    private var allAuthors:ArrayList<CountryEntity> = arrayListOf()
    private var adapter:ArrayAdapter<CountryEntity>? = null
    private var selectedRegisterPosition = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataBase.tables = SqliteHelper(this)
        val authorsList = findViewById<ListView>(R.id.list_authors)
        allAuthors = DataBase.tables!!.getAllAuthors()
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            allAuthors
        )

        authorsList.adapter = adapter
        adapter!!.notifyDataSetChanged() // To update the UI

        val btnRedirectCreateAuthor = findViewById<Button>(R.id.btn_redirect_create_author)
        btnRedirectCreateAuthor.setOnClickListener{
            goToActivity(CrudCountry::class.java)
        }

        registerForContextMenu(authorsList)

    }



    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        // Set options for the context menu
        val inflater = menuInflater
        inflater.inflate(R.menu.country_menu, menu)

        // Get ID of the selected item of the list
        val register = menuInfo as AdapterView.AdapterContextMenuInfo
        selectedRegisterPosition = register.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_edit_author -> {
                goToActivity(CrudCountry::class.java, allAuthors[selectedRegisterPosition])
                return true
            }
            R.id.mi_delete_author -> {
                openDialog(allAuthors[selectedRegisterPosition].id)
                return true
            }
            R.id.mi_author_books -> {
                goToActivity(CitiesList::class.java, allAuthors[selectedRegisterPosition])
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun goToActivity(
        activityClass: Class<*>,
        dataToPass: CountryEntity? = null
    ) {
        val intent = Intent(this, activityClass)
        if(dataToPass != null) {
            intent.apply {
                putExtra("selectedAuthor", dataToPass)
            }
        }
        startActivity(intent)
    }

    private fun openDialog(registerIndex: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Está seguro de eliminar el país?")
        builder.setPositiveButton(
            "Eliminar"
        ) { _, _ ->
            DataBase.tables!!.deleteCountry(registerIndex)
            allAuthors.clear()
            allAuthors.addAll(DataBase.tables!!.getAllAuthors())
            adapter!!.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancelar", null)

        builder.create().show()
    }

}