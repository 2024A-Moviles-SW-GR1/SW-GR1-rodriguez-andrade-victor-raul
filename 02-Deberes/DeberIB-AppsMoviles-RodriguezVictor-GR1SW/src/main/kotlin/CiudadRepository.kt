import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CiudadRepository {
    private val gson = Gson()
    private val file = File("src/data/ciudades.json")

    fun createCiudad(ciudad: Ciudad): Boolean {
        if (ciudadExists(ciudad.id)) {
            return false
        }
        val ciudades = listCiudades().toMutableList()
        ciudades.add(ciudad)
        saveCiudades(ciudades)
        return true
    }

    fun readCiudad(id: String): Ciudad? {
        val ciudades = listCiudades()
        return ciudades.find { it.id == id }
    }

    fun updateCiudad(ciudad: Ciudad) {
        val ciudades = listCiudades().toMutableList()
        val index = ciudades.indexOfFirst { it.id == ciudad.id }
        if (index != -1) {
            ciudades[index] = ciudad
            saveCiudades(ciudades)
        }
    }

    fun deleteCiudad(id: String) {
        val ciudades = listCiudades().toMutableList()
        val ciudad = ciudades.find { it.id == id }
        if (ciudad != null) {
            ciudades.remove(ciudad)
            saveCiudades(ciudades)
        }
    }

    fun listCiudades(): List<Ciudad> {
        return if (file.exists()) {
            val type = object : TypeToken<List<Ciudad>>() {}.type
            gson.fromJson(file.readText(), type)
        } else {
            emptyList()
        }
    }

    private fun saveCiudades(ciudades: List<Ciudad>) {
        file.writeText(gson.toJson(ciudades))
    }

    fun ciudadExists(id: String): Boolean {
        return listCiudades().any { it.id == id }
    }
}