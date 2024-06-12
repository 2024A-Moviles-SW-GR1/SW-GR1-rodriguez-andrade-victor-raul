import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PaisRepository {
    private val filePath = "src/data/paises.json"
    private val gson = Gson()

    private fun loadPaises(): List<Pais> {
        val file = File(filePath)
        return if (file.exists()) {
            val json = file.readText()
            gson.fromJson(json, object : TypeToken<List<Pais>>() {}.type)
        } else {
            emptyList()
        }
    }

    private fun savePaises(paises: List<Pais>) {
        val json = gson.toJson(paises)
        File(filePath).writeText(json)
    }

    fun createPais(pais: Pais): Boolean {
        if (paisExists(pais.id)) {
            return false
        }
        val paises = loadPaises().toMutableList()
        paises.add(pais)
        savePaises(paises)
        return true
    }

    fun addCiudadToPais(paisId: String, ciudadId: String, ciudadRepo: CiudadRepository) {
        val paises = loadPaises()
        val pais = paises.find { it.id == paisId }
        val ciudad = ciudadRepo.readCiudad(ciudadId)
        if (pais != null && ciudad != null) {
            if (pais.ciudades.any { it.esCapital }) {
                pais.ciudades.find { it.esCapital }?.esCapital = false
                ciudad.esCapital = true
                pais.ciudades.add(ciudad)
                syncUpdateCiudad(ciudad)
                savePaises(paises)
            } else {
                if (!pais.ciudades.any { it.id == ciudadId }) {
                    pais.ciudades.add(ciudad)
                    ciudad.esCapital = true
                    syncUpdateCiudad(ciudad)
                    savePaises(paises)
                }
            }
        }
    }

    fun readPais(id: String): Pais? {
        return loadPaises().find { it.id == id }
    }

    fun updatePais(pais: Pais) {
        val paises = loadPaises().toMutableList()
        val index = paises.indexOfFirst { it.id == pais.id }
        if (index != -1) {
            paises[index] = pais
            savePaises(paises)
        }
    }

    fun deletePais(id: String) {
        val paises = loadPaises().toMutableList()
        val paisToRemove = paises.find { it.id == id }
        if (paisToRemove != null) {
            paises.remove(paisToRemove)
            savePaises(paises)
        }
    }

    fun listPaises(): List<Pais> {
        return loadPaises()
    }

    fun syncRemoveCiudad(ciudadId: String) {
        val paises = loadPaises().toMutableList()
        paises.forEach { pais ->
            pais.ciudades.removeIf { it.id == ciudadId }
        }
        savePaises(paises)
    }

    fun syncUpdateCiudad(ciudad: Ciudad) {
        val paises = loadPaises().toMutableList()
        paises.forEach { pais ->
            val index = pais.ciudades.indexOfFirst { it.id == ciudad.id }
            if (index != -1) {
                pais.ciudades[index] = ciudad
            }
        }
        savePaises(paises)
    }


    fun paisExists(id: String): Boolean {
        return listPaises().any { it.id == id }
    }


}

