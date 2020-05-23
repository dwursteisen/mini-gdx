package com.github.dwursteisen.minigdx.entity.text

import com.github.dwursteisen.minigdx.entity.CanCopy
import com.github.dwursteisen.minigdx.entity.CanDraw
import com.github.dwursteisen.minigdx.entity.CanMove
import com.github.dwursteisen.minigdx.entity.delegate.Movable
import com.github.dwursteisen.minigdx.entity.primitives.Texture
import com.github.dwursteisen.minigdx.log
import com.github.dwursteisen.minigdx.shaders.ShaderProgram

class Text(
    var text: String = "",
    private val angelCode: AngelCode,
    private val fontSprite: Texture
) : CanMove by Movable(), CanDraw, CanCopy<Text> {

    private var xOffset = 0f
    private var yOffset = 0f

    private val scaleW: Float
        get() {
            return scale.x
        }
    private val scaleH: Float
        get() {
            return scale.y
        }

    override fun draw(shader: ShaderProgram) {
        xOffset = 0f
        yOffset = 0f
        text.forEach { char ->
            drawCharacter(shader, char)
        }
    }

    // TODO: convert font size from the world to size from the font?
    private fun drawCharacter(shader: ShaderProgram, char: Char) {
        if (char == '\n') {
            xOffset = 0f
            yOffset += angelCode.info.lineHeight * scaleH
            return
        } else if (char == ' ') {
            xOffset += angelCode.info.base * scaleW
        }

        val code = angelCode.characters[char]
            ?: return log.warn("TEXT") { "Character '$char' missing from the font '${angelCode.info.fontFile}'" }

        fontSprite.draw(
            shader,
            position.x + xOffset, position.y + yOffset,
            angelCode.info.size * scaleW, angelCode.info.size * scaleH,
            code.x, code.y,
            code.width, code.height
        )

        xOffset += (code.xadvance + code.xoffset) * scaleW
    }

    override fun copy(): Text {
        return Text(
            text = text,
            fontSprite = fontSprite,
            angelCode = angelCode
        )
    }
}
