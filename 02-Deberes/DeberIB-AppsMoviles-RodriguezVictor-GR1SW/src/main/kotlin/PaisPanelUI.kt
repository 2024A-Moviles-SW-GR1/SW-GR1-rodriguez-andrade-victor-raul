import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.text.SimpleDateFormat
import java.util.Date
import com.toedter.calendar.JDateChooser

class PaisPanelUI : JPanel(), ActionListener {
    private val paisRepository = PaisRepository()
    private val ciudadRepository = CiudadRepository()
    private val outputPanel = JPanel()

    // Define las variables de los campos de texto aquí
    private val idField = RoundedTextField(20)
    private val nombreField = RoundedTextField(20)
    private val diaFestivoField = JDateChooser()
    private val poblacionField = RoundedTextField(20)
    private val areaField = RoundedTextField(20)

    init {
        layout = BorderLayout()

        val options = arrayOf("Crear País", "Leer País", "Actualizar País", "Eliminar País", "Listar Países")
        val list = JList(options)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.addListSelectionListener { e ->
            if (!e.valueIsAdjusting) {
                handleSelection(list.selectedValue)
            }
        }

        // Aumentar el tamaño de la letra de la lista
        list.font = Font("Arial", Font.PLAIN, 16)

        // Aumentar el espacio entre las opciones de la lista
        list.fixedCellHeight = 28

        // Cambiar el cursor a una mano cuando el mouse está sobre la lista
        list.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

        // Establecer un ListCellRenderer personalizado para la lista
        list.cellRenderer = object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(list: JList<*>?, value: Any?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
                val component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JComponent
                component.font = if (isSelected) component.font.deriveFont(Font.BOLD, 18f) else component.font.deriveFont(Font.PLAIN, 16f)
                if (cellHasFocus) {
                    component.border = BorderFactory.createLoweredBevelBorder()
                } else {
                    component.border = BorderFactory.createEmptyBorder()
                }
                return component
            }
        }

        val scrollPane = JScrollPane(list)
        scrollPane.preferredSize = Dimension(150, 0)

        // Establecer el color del borde del JScrollPane a blanco
        scrollPane.border = BorderFactory.createLineBorder(Color.WHITE)

        val optionsPanel = JPanel()
        optionsPanel.layout = BoxLayout(optionsPanel, BoxLayout.Y_AXIS)
        optionsPanel.add(Box.createVerticalGlue())
        optionsPanel.add(scrollPane)
        optionsPanel.add(Box.createVerticalGlue())

        // Establecer el color de fondo del panel de opciones a blanco
        optionsPanel.background = Color.WHITE

        // Establecer el color del borde del panel de opciones a blanco
        optionsPanel.border = BorderFactory.createLineBorder(Color.WHITE)

        add(optionsPanel, BorderLayout.WEST)
        add(outputPanel, BorderLayout.CENTER)

        // Establecer el color de fondo del panel de salida a blanco
        outputPanel.background = Color.WHITE
    }

    private fun handleSelection(option: String) {
        outputPanel.removeAll()
        when (option) {
            "Crear País" -> showPaisForm("Crear País", ::createPais)
            "Leer País" -> readPais()
            "Actualizar País" -> updatePais()
            "Eliminar País" -> deletePais()
            "Listar Países" -> listPaises()
        }
        outputPanel.revalidate()
        outputPanel.repaint()
    }

    private fun showPaisForm(action: String, submitAction: (String, String, Date, Int, Double) -> Unit) {
        outputPanel.removeAll()
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)

        val titleLabel = JLabel(action)
        titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 20f)
        outputPanel.add(titleLabel)

        outputPanel.add(JLabel(" "))

        idField.text = ""
        nombreField.text = ""
        diaFestivoField.date = null
        poblacionField.text = ""
        areaField.text = ""

        outputPanel.add(JLabel("ID:"))
        outputPanel.add(idField)
        outputPanel.add(Box.createVerticalStrut(10)) // Añade un espacio en blanco de 10 pixels
        outputPanel.add(JLabel("Nombre:"))
        outputPanel.add(nombreField)
        outputPanel.add(Box.createVerticalStrut(10)) // Añade un espacio en blanco de 10 pixels
        outputPanel.add(JLabel("Día festivo:"))
        outputPanel.add(diaFestivoField)
        outputPanel.add(Box.createVerticalStrut(10)) // Añade un espacio en blanco de 10 pixels
        outputPanel.add(JLabel("Población:"))
        outputPanel.add(poblacionField)
        outputPanel.add(Box.createVerticalStrut(10)) // Añade un espacio en blanco de 10 pixels
        outputPanel.add(JLabel("Área (km²):"))
        outputPanel.add(areaField)
        outputPanel.add(Box.createVerticalStrut(10)) // Añade un espacio en blanco de 10 pixels

        val submitButton = StyledButton("Enviar")
        submitButton.isContentAreaFilled = false
        submitButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) // Cambiar el cursor a una mano

        submitButton.addActionListener {
            val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
            val diaFestivo = diaFestivoField.date
            submitAction(
                idField.text,
                nombreField.text,
                diaFestivo,
                poblacionField.text.toInt(),
                areaField.text.toDouble()
            )
            //JOptionPane.showMessageDialog(this, "Actualización completada para: ${nombreField.text}")
            listPaises()
        }
        outputPanel.add(submitButton)

        outputPanel.revalidate()
        outputPanel.repaint()
    }

    private fun createPais(id: String, nombre: String, diaFestivo: Date, poblacion: Int, area: Double) {
        val pais = Pais(id, nombre, diaFestivo, poblacion, area)
        if (paisRepository.createPais(pais)) {
            JOptionPane.showMessageDialog(this, "Has creado exitosamente a $nombre")
        } else {
            val option = JOptionPane.showConfirmDialog(
                this,
                "Ya existe un país con el ID $id. ¿Desea actualizar la información?",
                "País existente",
                JOptionPane.YES_NO_OPTION
            )
            if (option == JOptionPane.YES_OPTION) {
                idField.isEditable = true // Asegurarse de que el campo ID sea editable
                showPaisForm("Actualizar", ::submitUpdatePais, pais)
            }
        }
        listPaises()
    }

    private fun readPais() {
        outputPanel.removeAll()
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)

        val paises = paisRepository.listPaises()
        if (paises.isNotEmpty()) {
            val titleLabel = JLabel("Seleccione un país")
            titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 16f)
            outputPanel.add(titleLabel)

            for (pais in paises) {
                val panel = JPanel()
                panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
                panel.border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
                panel.add(JLabel(pais.nombre))
                panel.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        displayPais(pais)
                    }
                })
                outputPanel.add(panel)
            }
        } else {
            outputPanel.add(JLabel("No hay países registrados"))
        }

        outputPanel.revalidate()
        outputPanel.repaint()
    }

    private fun displayPais(pais: Pais) {
        outputPanel.removeAll()
        val detailPanel = JPanel()
        detailPanel.layout = BoxLayout(detailPanel, BoxLayout.Y_AXIS)
        detailPanel.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        )
        detailPanel.add(JLabel("Nombre: ${pais.nombre}"))
        detailPanel.add(JLabel("ID: ${pais.id}"))
        detailPanel.add(JLabel("Día festivo: ${pais.diaFestivo}"))
        detailPanel.add(JLabel("Población: ${pais.poblacion}"))
        detailPanel.add(JLabel("Área: ${pais.area} km²"))

        if (pais.ciudades.isNotEmpty()) {
            detailPanel.add(JLabel("Ciudades:"))
            for (ciudad in pais.ciudades) {
                val ciudadPanel = JPanel()
                ciudadPanel.layout = BoxLayout(ciudadPanel, BoxLayout.Y_AXIS)
                ciudadPanel.border = BorderFactory.createEmptyBorder(5, 20, 5, 20)
                ciudadPanel.add(JLabel("ID: ${ciudad.id}"))
                ciudadPanel.add(JLabel("Nombre: ${ciudad.nombre}"))
                ciudadPanel.add(JLabel("Población: ${ciudad.poblacion}"))
                ciudadPanel.add(JLabel("Es capital: ${if (ciudad.esCapital) "Sí" else "No"}"))
                ciudadPanel.add(JLabel("Superficie: ${ciudad.superficie} km²"))
                detailPanel.add(ciudadPanel)
            }
        }

        val backButton = StyledButton("Regresar")
        backButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) // Cambiar el cursor a una mano

        backButton.addActionListener {
            readPais()
        }
        detailPanel.add(backButton)

        outputPanel.add(detailPanel)
        outputPanel.revalidate()
        outputPanel.repaint()
    }



    private fun updatePais() {
        outputPanel.removeAll()
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)

        val paises = paisRepository.listPaises()
        if (paises.isNotEmpty()) {
            for (pais in paises) {
                val panel = JPanel()
                panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
                panel.border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
                panel.add(JLabel(pais.nombre))
                panel.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        showPaisForm("Actualizar", ::submitUpdatePais, pais)
                    }
                })
                outputPanel.add(panel)
                outputPanel.add(Box.createRigidArea(Dimension(0, 10))) // Añade un espacio en blanco de 10 pixels
            }
        } else {
            outputPanel.add(JLabel("No hay países registrados"))
        }

        outputPanel.revalidate()
        outputPanel.repaint()
        //listPaises()
    }

    private fun submitUpdatePais(id: String, nombre: String, diaFestivo: Date, poblacion: Int, area: Double) {
        val pais = paisRepository.readPais(id)
        if (pais != null) {
            pais.nombre = nombre
            pais.diaFestivo = diaFestivo
            pais.poblacion = poblacion
            pais.area = area
            paisRepository.updatePais(pais)
            JOptionPane.showMessageDialog(this, "Actualización completada para: $nombre")
            updatePais()
        } else {
            JOptionPane.showMessageDialog(this, "$nombre no encontrado")
        }
    }

    private fun showPaisForm(action: String, submitAction: (String, String, Date, Int, Double) -> Unit, pais: Pais? = null) {
        outputPanel.removeAll()
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)

        val titleLabel = JLabel("$action País")
        titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 20f)
        outputPanel.add(titleLabel)

        // Llenar los campos del formulario con los datos del país seleccionado
        if (pais != null) {
            idField.text = pais.id
            nombreField.text = pais.nombre
            //diaFestivoField.text = pais.diaFestivo
            //val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
            val diaFestivo = diaFestivoField.date
            poblacionField.text = pais.poblacion.toString()
            areaField.text = pais.area.toString()
            idField.isEditable = false // Hacer el campo ID no editable
        } else {
            idField.text = ""
            nombreField.text = ""
            diaFestivoField.date = null
            poblacionField.text = ""
            areaField.text = ""
            idField.isEditable = true // Hacer el campo ID editable
        }
        outputPanel.add(Box.createVerticalStrut(10))
        outputPanel.add(JLabel("ID:"))
        outputPanel.add(idField)
        outputPanel.add(Box.createVerticalStrut(10)) // Añade un espacio en blanco de 10 pixels
        outputPanel.add(JLabel("Nombre:"))
        outputPanel.add(nombreField)
        outputPanel.add(Box.createVerticalStrut(10)) // Añade un espacio en blanco de 10 pixels
        outputPanel.add(JLabel("Día festivo:"))
        outputPanel.add(diaFestivoField)
        outputPanel.add(Box.createVerticalStrut(10)) // Añade un espacio en blanco de 10 pixels
        outputPanel.add(JLabel("Población:"))
        outputPanel.add(poblacionField)
        outputPanel.add(Box.createVerticalStrut(10)) // Añade un espacio en blanco de 10 pixels
        outputPanel.add(JLabel("Área (km²):"))
        outputPanel.add(areaField)
        outputPanel.add(Box.createVerticalStrut(10)) // Añade un espacio en blanco de 10 pixels

        val submitButton = StyledButton("Actualizar")
        submitButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) // Cambiar el cursor a una mano

        submitButton.addActionListener {
            //val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
            val diaFestivo = diaFestivoField.date
            submitAction(
                idField.text,
                nombreField.text,
                diaFestivo,
                poblacionField.text.toInt(),
                areaField.text.toDouble()
            )
            updatePais()
        }
        outputPanel.add(submitButton)

        outputPanel.revalidate()
        outputPanel.repaint()
    }


    private fun deletePais() {
        outputPanel.removeAll()
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)

        val titleLabel = JLabel("Eliminar País")
        titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 20f)
        outputPanel.add(titleLabel)

        val paises = paisRepository.listPaises()
        if (paises.isNotEmpty()) {
            for (pais in paises) {
                val panel = JPanel()
                panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
                panel.border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
                panel.add(JLabel(pais.nombre))
                panel.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        val option = JOptionPane.showConfirmDialog(
                            this@PaisPanelUI,
                            "¿Está seguro de eliminar a ${pais.nombre}?",
                            "Confirmación de eliminación",
                            JOptionPane.YES_NO_OPTION
                        )
                        if (option == JOptionPane.YES_OPTION) {
                            paisRepository.deletePais(pais.id)
                            JOptionPane.showMessageDialog(this@PaisPanelUI, "${pais.nombre} ha sido eliminado")
                            deletePais()
                        }
                    }
                })
                outputPanel.add(panel)
            }
        } else {
            outputPanel.add(JLabel("No hay países registrados"))
        }

        outputPanel.revalidate()
        outputPanel.repaint()
    }

    private fun listPaises() {
        outputPanel.removeAll()
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)
        val paises = paisRepository.listPaises()
        val titleLabel = JLabel("Lista de Países")
        titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 16f)
        titleLabel.alignmentX = Component.CENTER_ALIGNMENT // Centrar el título
        outputPanel.add(titleLabel)

        // Agregar una línea en blanco debajo del título
        outputPanel.add(JLabel(" "))

        for (pais in paises) {
            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            panel.border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
            panel.alignmentX = Component.CENTER_ALIGNMENT // Centrar las tarjetas de detalle

            // Agregar detalles al panel
            panel.add(JLabel("ID: ${pais.id}"))
            panel.add(JLabel("Nombre: ${pais.nombre}"))
            panel.add(JLabel("Día festivo: ${pais.diaFestivo}"))
            panel.add(JLabel("Población: ${pais.poblacion}"))
            panel.add(JLabel("Área (km²): ${pais.area}"))

            outputPanel.add(panel)
        }

        outputPanel.revalidate()
        outputPanel.repaint()
    }

    override fun actionPerformed(e: ActionEvent) {
        // No actions needed here since we're using list selection listener
    }

}
