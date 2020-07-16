package com.github.dwursteisen.minigdx.ecs.components

import com.github.dwursteisen.minigdx.buffer.Buffer
import com.github.dwursteisen.minigdx.entity.primitives.Texture
import com.github.dwursteisen.minigdx.entity.text.Font
import com.github.dwursteisen.minigdx.render.sprites.RenderStrategy
import com.github.dwursteisen.minigdx.shaders.TextureReference

class SpritePrimitive(
    val texture: Texture,
    val x: Int = 0,
    val y: Int = 0,
    val width: Int = texture.width,
    val height: Int = texture.height,
    val renderStrategy: RenderStrategy,
    // -- GL specific data -- //
    var isCompiled: Boolean = false,
    var verticesBuffer: Buffer? = null,
    var uvBuffer: Buffer? = null,
    var verticesOrderBuffer: Buffer? = null,
    var textureReference: TextureReference? = null,
    var numberOfIndices: Int = 0
) : Component

class Text(
    var text: String,
    val font: Font
) : Component {

    var previousText: String = ""
}