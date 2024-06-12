import java.util.Date

data class Pais(
    val id: String,
    var nombre: String,
    var diaFestivo: Date,
    var poblacion: Int,
    var area: Double,
    var ciudades: MutableList<Ciudad> = mutableListOf()
)
