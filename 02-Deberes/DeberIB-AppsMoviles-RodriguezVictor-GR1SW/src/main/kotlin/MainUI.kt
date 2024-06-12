import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.border.EmptyBorder
import com.formdev.flatlaf.FlatIntelliJLaf

class MainUI : JFrame(), ActionListener {

    private val cardPanel: JPanel = JPanel(CardLayout())
    private val paisPanel: PaisPanelUI = PaisPanelUI()
    private val ciudadPanel: CiudadPanelUI = CiudadPanelUI()

    private val paisButton: JButton = RoundedButton("Gestión de Países")
    private val ciudadButton: JButton = RoundedButton("Gestión de Ciudades")

    init {
        title = "Victor Rodriguez - Deber Kotlin - GR1SW"
        setSize(800, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()
        contentPane.background = Color.WHITE

        val buttonPanel = JPanel()
        buttonPanel.layout = GridBagLayout()
        buttonPanel.background = Color.WHITE
        val gbc = GridBagConstraints()
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.insets = Insets(10, 20, 10, 20)
        buttonPanel.add(
            Box.Filler(Dimension(0, 0), Dimension(0, Int.MAX_VALUE), Dimension(0, Short.MAX_VALUE / 2)),
            gbc
        )

        configureButton(paisButton, "PAIS")
        configureButton(ciudadButton, "CIUDAD")

        paisButton.background = Color.BLUE
        paisButton.foreground = Color.WHITE

        gbc.gridx = 0
        buttonPanel.add(paisButton, gbc)
        gbc.gridx = 1
        buttonPanel.add(ciudadButton, gbc)

        add(buttonPanel, BorderLayout.NORTH)
        add(cardPanel, BorderLayout.CENTER)

        cardPanel.add(paisPanel, "PAIS")
        cardPanel.add(ciudadPanel, "CIUDAD")

        (cardPanel.layout as CardLayout).show(cardPanel, "PAIS")

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val x = (screenSize.width - width) / 2
        val y = (screenSize.height - height) / 2
        setLocation(x, y)
        isVisible = true
    }

    private fun configureButton(button: JButton, actionCommand: String) {
        button.isFocusPainted = false
        button.font = Font("Arial", Font.PLAIN, 14)
        button.addActionListener(this)
        button.actionCommand = actionCommand
        button.border = RoundedBorder(20)
        button.preferredSize = Dimension(200, 50)
        button.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) // Cambiar el cursor a una mano
        button.addActionListener {
            updateButtonStates(button)
        }
        if (actionCommand == "CIUDAD") {
            button.background = Color.WHITE
            button.foreground = Color.BLACK
            button.border = RoundedBorder(20, Color.BLACK)
        }
    }

    private fun updateButtonStates(activeButton: JButton) {
        val buttons = listOf(paisButton, ciudadButton)
        for (button in buttons) {
            if (button == activeButton) {
                button.background = Color.BLUE
                button.foreground = Color.WHITE
            } else {
                button.background = Color.WHITE
                button.foreground = Color.BLACK
                button.border = RoundedBorder(20, Color.BLACK)
            }
        }
    }

    override fun actionPerformed(e: ActionEvent) {
        val cardLayout = cardPanel.layout as CardLayout
        when (e.actionCommand) {
            "PAIS" -> cardLayout.show(cardPanel, "PAIS")
            "CIUDAD" -> cardLayout.show(cardPanel, "CIUDAD")
        }
    }
}

class RoundedButton(text: String) : JButton(text) {
    init {
        isContentAreaFilled = false
        isFocusPainted = false
        border = RoundedBorder(20)
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        if (model.isArmed) {
            g2.color = background.darker()
        } else if (model.isRollover) {
            g2.color = background.brighter()
        } else {
            g2.color = background
        }

        g2.fillRoundRect(0, 0, width, height, 20, 20)
        super.paintComponent(g)
    }

    override fun paintBorder(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = foreground
        g2.drawRoundRect(0, 0, width - 1, height - 1, 20, 20)
    }
}

class RoundedBorder(private val radius: Int, private val borderColor: Color = Color.GRAY) :
    javax.swing.border.AbstractBorder() {
    override fun paintBorder(c: Component, g: Graphics, x: Int, y: Int, width: Int, height: Int) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = borderColor
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius)
    }

    override fun getBorderInsets(c: Component): Insets {
        return Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius)
    }

    override fun getBorderInsets(c: Component, insets: Insets): Insets {
        insets.left = this.radius + 1
        insets.top = this.radius + 1
        insets.right = this.radius + 1
        insets.bottom = this.radius + 2
        return insets
    }
}


fun main() {
    FlatIntelliJLaf.install()
    MainUI()
}
