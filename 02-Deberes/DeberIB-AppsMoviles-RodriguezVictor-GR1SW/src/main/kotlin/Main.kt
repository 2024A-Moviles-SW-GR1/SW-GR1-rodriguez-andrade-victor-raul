import java.util.*
import java.text.SimpleDateFormat

fun main() {
    val paisRepository = PaisRepository()
    val ciudadRepository = CiudadRepository()
    val scanner = Scanner(System.`in`)

    while (true) {
        println("\u001B[33mMenu:\u001B[0m") // Amarillo para el texto del menú
        println("\u001B[36m1. Crear País\u001B[0m") // Cyan para la opción
        println("\u001B[36m2. Leer País\u001B[0m")
        println("\u001B[36m3. Actualizar País\u001B[0m")
        println("\u001B[36m4. Eliminar País\u001B[0m")
        println("\u001B[36m5. Listar Países\u001B[0m")
        println("\u001B[32m6. Crear Ciudad\u001B[0m") // Verde para la opción
        println("\u001B[32m7. Leer Ciudad\u001B[0m")
        println("\u001B[32m8. Actualizar Ciudad\u001B[0m")
        println("\u001B[32m9. Eliminar Ciudad\u001B[0m")
        println("\u001B[32m10. Listar Ciudades\u001B[0m")
        println("\u001B[34m11. Añadir Ciudad a País\u001B[0m") // Azul para la opción
        println("\u001B[0m0. Salir") // Resetear los colores después del menú
        println("Seleccione una opción: ")

        when (scanner.nextLine().toInt()) {
            1 -> {
                println("Ingrese el ID del país: ")
                val id = scanner.nextLine()
                println("Ingrese el nombre del país: ")
                val nombre = scanner.nextLine()
                println("Ingrese un dia festivo del país: ")
                val diaFestivo = scanner.nextDate()
                println("Ingrese la población del país: ")
                val poblacion = scanner.nextLine().toInt()
                println("Ingrese el área del país (en km2): ")
                val area = scanner.nextLine().toDouble()
                val pais = Pais(id, nombre, diaFestivo, poblacion, area)
                paisRepository.createPais(pais)
            }

            2 -> {
                println("Ingrese el ID del país: ")
                val id = scanner.nextLine()
                val pais = paisRepository.readPais(id)
                println(pais?.toString() ?: "País no encontrado")
            }

            3 -> {
                println("Ingrese el ID del país: ")
                val id = scanner.nextLine()
                val pais = paisRepository.readPais(id)
                if (pais != null) {
                    println("Ingrese el nuevo nombre del país: ")
                    pais.nombre = scanner.nextLine()
                    println("Ingrese el nuevo día festivo del país: ")
                    pais.diaFestivo = scanner.nextDate()
                    println("Ingrese la nueva población del país: ")
                    pais.poblacion = scanner.nextLine().toInt()
                    println("Ingrese el nuevo área del país (en km2): ")
                    pais.area = scanner.nextLine().toDouble()
                    paisRepository.updatePais(pais)
                } else {
                    println("País no encontrado")
                }
            }

            4 -> {
                println("Ingrese el ID del país: ")
                val id = scanner.nextLine()
                paisRepository.deletePais(id)
            }

            5 -> {
                val paises = paisRepository.listPaises()
                paises.forEach { println(it) }
            }

            6 -> {
                println("Ingrese el ID de la ciudad: ")
                val id = scanner.nextLine()
                println("Ingrese el nombre de la ciudad: ")
                val nombre = scanner.nextLine()
                println("Ingrese la población de la ciudad: ")
                val poblacion = scanner.nextLine().toInt()
                println("Es la ciudad capital? (true/false): ")
                val esCapital = scanner.nextLine().toBoolean()
                println("Ingrese la superficie de la ciudad (en km2): ")
                val superficie = scanner.nextLine().toDouble()
                val ciudad = Ciudad(id, nombre, poblacion, esCapital, superficie)
                ciudadRepository.createCiudad(ciudad)
            }

            7 -> {
                println("Ingrese el ID de la ciudad: ")
                val id = scanner.nextLine()
                val ciudad = ciudadRepository.readCiudad(id)
                println(ciudad?.toString() ?: "Ciudad no encontrada")
            }

            8 -> {
                println("Ingrese el ID de la ciudad: ")
                val id = scanner.nextLine()
                val ciudad = ciudadRepository.readCiudad(id)
                if (ciudad != null) {
                    println("Ingrese el nuevo nombre de la ciudad: ")
                    ciudad.nombre = scanner.nextLine()
                    println("Ingrese la nueva población de la ciudad: ")
                    ciudad.poblacion = scanner.nextLine().toInt()
                    println("Es la ciudad capital? (true/false): ")
                    ciudad.esCapital = scanner.nextLine().toBoolean()
                    println("Ingrese la nueva superficie de la ciudad (en km2): ")
                    ciudad.superficie = scanner.nextLine().toDouble()
                    ciudadRepository.updateCiudad(ciudad)
                } else {
                    println("Ciudad no encontrada")
                }
            }

            9 -> {
                println("Ingrese el ID de la ciudad: ")
                val id = scanner.nextLine()
                ciudadRepository.deleteCiudad(id)
            }

            10 -> {
                val ciudades = ciudadRepository.listCiudades()
                ciudades.forEach { println(it) }
            }

            11 -> {
                println("Ingrese el ID del país: ")
                val paisId = scanner.nextLine()
                println("Ingrese el ID de la ciudad: ")
                val ciudadId = scanner.nextLine()
                paisRepository.addCiudadToPais(paisId, ciudadId, ciudadRepository)
            }

            0 -> return
        }
    }
}

fun Scanner.nextDate(): Date {
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    return sdf.parse(this.nextLine())
}
