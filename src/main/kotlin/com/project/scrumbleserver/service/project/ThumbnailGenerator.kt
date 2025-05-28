package com.project.scrumbleserver.service.project

import org.springframework.stereotype.Component
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Component
class ThumbnailGenerator {
    companion object {
        const val THUMBNAIL_WIDTH = 512
        const val THUMBNAIL_HEIGHT = 512

        const val FONT_NAME = "Pretendard"

        val COLOR_SET = setOf(
            Triple(255, 209, 220), // #FFD1DC
            Triple(255, 250, 205), // #FFFACD
            Triple(209, 255, 242), // #D1FFF2
            Triple(230, 230, 250), // #E6E6FA
            Triple(214, 240, 255), // #D6F0FF
            Triple(255, 229, 180), // #FFE5B4
            Triple(232, 204, 255), // #E8CCFF
            Triple(198, 241, 255), // #C6F1FF
            Triple(250, 214, 214), // #FAD6D6
            Triple(240, 255, 240), // #F0FFF0
            Triple(212, 205, 216), // #D4CDD8
            Triple(255, 194, 154), // #FFC29A
            Triple(232, 218, 239), // #E8DAEF
            Triple(229, 192, 192), // #E5C0C0
            Triple(198, 222, 222), // #C6DEDE
            Triple(231, 216, 201), // #E7D8C9
        )
    }

    fun generate(letter: Char): ByteArray {
        val image = BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_ARGB)
        val g2d = image.createGraphics()

        val backgroundColor = COLOR_SET.random()
        g2d.color = Color(backgroundColor.first, backgroundColor.second, backgroundColor.third)
        g2d.fillRect(0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

        g2d.color = Color(0, 0, 0)
        g2d.font = Font(FONT_NAME, Font.BOLD, 256)

        val fontMetrics = g2d.fontMetrics
        val x = (THUMBNAIL_WIDTH - fontMetrics.stringWidth(letter.toString())) / 2
        val y = (THUMBNAIL_HEIGHT - fontMetrics.height) / 2 + fontMetrics.ascent

        g2d.drawString(letter.toString(), x, y)
        g2d.dispose()

        val output = ByteArrayOutputStream()
        ImageIO.write(image, "png", output)
        return output.toByteArray()
    }
}