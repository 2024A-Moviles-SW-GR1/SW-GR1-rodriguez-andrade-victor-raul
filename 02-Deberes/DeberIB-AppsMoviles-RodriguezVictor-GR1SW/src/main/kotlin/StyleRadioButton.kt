import javax.swing.JRadioButton
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.Dimension

class StyledRadioButton(text: String) : JRadioButton(text) {
    init {
        isContentAreaFilled = false
        isFocusPainted = false
        foreground = Color.BLACK
        background = Color.WHITE
        preferredSize = Dimension(30, 30) // Make the radio button larger
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = Color.WHITE
        g2.fillOval(0, 0, size.width - 1, size.height - 1)
        super.paintComponent(g)
    }
}