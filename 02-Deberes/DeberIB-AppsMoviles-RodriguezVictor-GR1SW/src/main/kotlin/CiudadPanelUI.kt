import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class CiudadPanelUI : JPanel(), ActionListener {
    private val ciudadRepository = CiudadRepository()
    private val paisRepository = PaisRepository()
    private val outputPanel = JPanel()
    private val idField = JTextField(20)

    init {
        layout = BorderLayout()

        val options = arrayOf("Crear Ciudad", "Leer Ciudad", "Actualizar Ciudad", "Eliminar Ciudad", "Listar Ciudades", "Añadir Ciudad a País")
        val list = JList(options)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.addListSelectionListener { e ->
            if (!e.valueIsAdjusting) {
                handleSelection(list.selectedValue)
            }
        }

        // Aumentar el tamaño de la letra de la lista a 18
        list.font = Font("Arial", Font.PLAIN, 14)

        // Aumentar el espacio entre las opciones de la lista
        list.fixedCellHeight = 25

        list.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)


        // Establecer un ListCellRenderer personalizado para la lista
        list.cellRenderer = object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(list: JList<*>?, value: Any?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
                val component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                component.font = if (isSelected) component.font.deriveFont(Font.BOLD) else component.font.deriveFont(Font.PLAIN)
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
            "Crear Ciudad" -> {
                idField.isEditable = true
                showCiudadForm("Crear Ciudad", ::createCiudad)
            }
            "Leer Ciudad" -> readCiudad()
            "Actualizar Ciudad" -> {
                idField.isEditable = false
                listCiudadesForUpdate()
            }
            "Eliminar Ciudad" -> deleteCiudad()
            "Listar Ciudades" -> listCiudades()
            "Añadir Ciudad a País" -> addCiudadToPais()
        }
        outputPanel.revalidate()
        outputPanel.repaint()
    }

    private fun listCiudadesForUpdate() {
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)
        val ciudades = ciudadRepository.listCiudades()
        if (ciudades.isNotEmpty()) {
            for (ciudad in ciudades) {
                val panel = JPanel()
                panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
                panel.border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
                panel.add(JLabel(ciudad.nombre))
                panel.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        outputPanel.removeAll()
                        showCiudadForm("Actualizar Ciudad", ::updateCiudad, ciudad)
                        outputPanel.revalidate()
                        outputPanel.repaint()
                    }
                })
                outputPanel.add(panel)
            }
        } else {
            outputPanel.add(JLabel("No hay ciudades registradas"))
        }

        outputPanel.revalidate()
        outputPanel.repaint()
    }

    private fun showCiudadForm(action: String, submitAction: (String, String, Int, Boolean, Double) -> Unit, ciudad: Ciudad? = null) {
        outputPanel.removeAll()
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)
        outputPanel.background = Color.WHITE // Set the background color to white

        val titleLabel = JLabel("$action Ciudad")
        titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 16f)
        outputPanel.add(titleLabel)

        outputPanel.add(JLabel(" "))

        val idField = RoundedTextField(20)
        idField.preferredSize = Dimension(idField.preferredSize.width, 20) // Set the preferred height to 20
        idField.background = Color.WHITE // Set the background color to white
        val nombreField = RoundedTextField(20)
        val poblacionField = RoundedTextField(20)
        val superficieField = RoundedTextField(20)
        val isCapitalYes = StyledRadioButton("Sí")
        val isCapitalNo = StyledRadioButton("No")

        val isCapitalGroup = ButtonGroup()
        isCapitalGroup.add(isCapitalYes)
        isCapitalGroup.add(isCapitalNo)

        // If a city is provided, fill in the fields with its data
        if (ciudad != null) {
            idField.text = ciudad.id
            idField.isEditable = false // Make the ID field non-editable
            nombreField.text = ciudad.nombre
            poblacionField.text = ciudad.poblacion.toString()
            superficieField.text = ciudad.superficie.toString()
            if (ciudad.esCapital) {
                isCapitalYes.isSelected = true
            } else {
                isCapitalNo.isSelected = true
            }
        } else {
            idField.isEditable = true // Allow editing of the ID field when creating a new city
        }

        outputPanel.add(Box.createVerticalStrut(10))
        outputPanel.add(JLabel("ID:"))
        outputPanel.add(idField)
        outputPanel.add(Box.createVerticalStrut(10))
        outputPanel.add(JLabel("Nombre:"))
        outputPanel.add(nombreField)
        outputPanel.add(Box.createVerticalStrut(10))
        outputPanel.add(JLabel("Población:"))
        outputPanel.add(poblacionField)
        outputPanel.add(Box.createVerticalStrut(10))
        outputPanel.add(JLabel("Es capital:"))
        outputPanel.add(isCapitalYes)
        outputPanel.add(isCapitalNo)
        outputPanel.add(Box.createVerticalStrut(10))
        outputPanel.add(JLabel("Superficie:"))
        outputPanel.add(superficieField)
        outputPanel.add(Box.createVerticalStrut(10))

        val submitButton = StyledButton("Enviar")
        submitButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) // Cambiar el cursor a una mano

        submitButton.addActionListener {
            val isCapital = isCapitalYes.isSelected
            submitAction(
                idField.text,
                nombreField.text,
                poblacionField.text.toInt(),
                isCapital,
                superficieField.text.toDouble()
            )
            JOptionPane.showMessageDialog(this, "$action completada para: ${nombreField.text}")
        }
        outputPanel.add(submitButton)

        outputPanel.revalidate()
        outputPanel.repaint()
    }

    private fun createCiudad(id: String, nombre: String, poblacion: Int, esCapital: Boolean, superficie: Double) {
        val ciudad = Ciudad(id, nombre, poblacion, esCapital, superficie)
        if (ciudadRepository.createCiudad(ciudad)) {
            JOptionPane.showMessageDialog(this, "Ciudad creada exitosamente: $nombre")
        } else {
            val options = arrayOf("Actualizar", "Cambiar ID")
            val option = JOptionPane.showOptionDialog(
                this,
                "Ya existe una ciudad con el ID $id.",
                "Ciudad existente",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            )
            if (option == 0) {
                updateCiudad(id, nombre, poblacion, esCapital, superficie)
            } else if (option == 1) {
                // Aquí puedes mostrar un cuadro de diálogo para que el usuario ingrese un nuevo ID
                // y luego intentar crear la ciudad con el nuevo ID
            }
        }
        listCiudades()
    }

    private fun readCiudad() {
        outputPanel.removeAll()
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)
        val ciudades = ciudadRepository.listCiudades()
        val titleLabel = JLabel("Leer Ciudad")
        titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 16f)
        outputPanel.add(titleLabel)

        for (ciudad in ciudades) {
            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            panel.border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
            panel.add(JLabel(ciudad.nombre))
            panel.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    displayCiudad(ciudad)
                }
            })

            val box = Box.createHorizontalBox()
            box.add(Box.createHorizontalGlue())
            box.add(panel)
            box.add(Box.createHorizontalGlue())
            outputPanel.add(box)
        }

        outputPanel.revalidate()
        outputPanel.repaint()
    }


    private fun updateCiudad(id: String, nombre: String, poblacion: Int, esCapital: Boolean, superficie: Double) {
        val ciudad = ciudadRepository.readCiudad(id)
        if (ciudad != null) {
            ciudad.nombre = nombre
            ciudad.poblacion = poblacion
            ciudad.esCapital = esCapital
            ciudad.superficie = superficie
            ciudadRepository.updateCiudad(ciudad)
        } else {
            JOptionPane.showMessageDialog(this, "Ciudad no encontrada")
        }
    }

    private fun deleteCiudad() {
        val id = JOptionPane.showInputDialog("Ingrese el ID de la ciudad:")
        ciudadRepository.deleteCiudad(id)
        JOptionPane.showMessageDialog(this, "Ciudad eliminada")
    }

    private fun listCiudades() {
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)
        val ciudades = ciudadRepository.listCiudades()
        val titleLabel = JLabel("Lista de Ciudades")
        titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 16f)
        outputPanel.add(titleLabel)

        for (ciudad in ciudades) {
            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            panel.border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
            panel.add(JLabel("Nombre: ${ciudad.nombre}"))
            panel.add(JLabel("ID: ${ciudad.id}"))
            panel.add(JLabel("Población: ${ciudad.poblacion}"))
            panel.add(JLabel("Es capital: ${ciudad.esCapital}"))
            panel.add(JLabel("Superficie: ${ciudad.superficie} km²"))

            val box = Box.createHorizontalBox()
            box.add(Box.createHorizontalGlue())
            box.add(panel)
            box.add(Box.createHorizontalGlue())
            outputPanel.add(box)
        }
    }

    private fun displayCiudad(ciudad: Ciudad) {
        outputPanel.removeAll()
        val detailPanel = JPanel()
        detailPanel.layout = BoxLayout(detailPanel, BoxLayout.Y_AXIS)
        detailPanel.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        )
        detailPanel.add(JLabel("Nombre: ${ciudad.nombre}"))
        detailPanel.add(JLabel("ID: ${ciudad.id}"))
        detailPanel.add(JLabel("Población: ${ciudad.poblacion}"))
        detailPanel.add(JLabel("Es capital: ${if (ciudad.esCapital) "Sí" else "No"}"))
        detailPanel.add(JLabel("Superficie: ${ciudad.superficie} km²"))

        val backButton = StyledButton("Regresar")
        backButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) // Cambiar el cursor a una mano

        backButton.addActionListener {
            readCiudad()
        }
        detailPanel.add(backButton)

        outputPanel.add(detailPanel)
        outputPanel.revalidate()
        outputPanel.repaint()
    }

    private fun addCiudadToPais() {
        outputPanel.layout = BoxLayout(outputPanel, BoxLayout.Y_AXIS)

        val titleLabel = JLabel("Añadir Ciudad a País")
        titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 16f)
        outputPanel.add(titleLabel)

        val paisIdField = JTextField(20)
        val ciudadIdField = JTextField(20)

        outputPanel.add(JLabel("ID del País:"))
        outputPanel.add(paisIdField)
        outputPanel.add(JLabel("ID de la Ciudad:"))
        outputPanel.add(ciudadIdField)

        val submitButton = StyledButton("Agregar")
        submitButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) // Cambiar el cursor a una mano

        submitButton.addActionListener {
            paisRepository.addCiudadToPais(paisIdField.text, ciudadIdField.text, ciudadRepository)
            JOptionPane.showMessageDialog(this, "Ciudad añadida al país")
        }
        outputPanel.add(submitButton)
    }

    override fun actionPerformed(e: ActionEvent) {
        // No actions needed here since we're using list selection listener
    }
}
