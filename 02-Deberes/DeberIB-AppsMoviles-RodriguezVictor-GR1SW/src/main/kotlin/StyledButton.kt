import javax.swing.*
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class StyledButton(text: String) : JButton(text) {
    init {
        isContentAreaFilled = false
        isFocusPainted = false
        border = RoundedBorder(20, Color.BLACK)
        foreground = Color.BLACK
        background = Color.WHITE
        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent) {
                foreground = Color.WHITE
                background = Color.BLUE
            }

            override fun mouseExited(e: MouseEvent) {
                foreground = Color.BLACK
                background = Color.WHITE
            }
        })
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        if (model.isArmed) {
            g2.color = background.darker()
        } else if (model.isRollover) {
            g2.color = background
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