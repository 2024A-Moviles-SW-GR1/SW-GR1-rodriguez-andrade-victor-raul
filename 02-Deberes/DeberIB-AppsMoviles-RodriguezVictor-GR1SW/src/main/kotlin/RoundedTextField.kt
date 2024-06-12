import javax.swing.JTextField
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.RoundRectangle2D
import java.awt.Color

class RoundedTextField(columns: Int) : JTextField(columns) {
    init {
        isOpaque = false
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = background
        g2.fill(RoundRectangle2D.Float(0f, 0f, width.toFloat(), height.toFloat(), 15f, 15f))
        super.paintComponent(g)
    }

    override fun paintBorder(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = Color.BLACK // Cambia esto al color que desees para el borde
        g2.draw(RoundRectangle2D.Float(0f, 0f, (width - 1).toFloat(), (height - 1).toFloat(), 15f, 15f))
    }
}