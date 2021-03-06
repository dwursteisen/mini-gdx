package com.github.dwursteisen.minigdx.render

import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.lookAt
import com.curiouscreature.kotlin.math.rotation
import com.curiouscreature.kotlin.math.translation
import com.github.dwursteisen.minigdx.GL
import com.github.dwursteisen.minigdx.Seconds
import com.github.dwursteisen.minigdx.ecs.components.Camera
import com.github.dwursteisen.minigdx.ecs.components.LightComponent
import com.github.dwursteisen.minigdx.ecs.entities.Entity
import com.github.dwursteisen.minigdx.ecs.entities.position
import com.github.dwursteisen.minigdx.ecs.systems.EntityQuery
import com.github.dwursteisen.minigdx.ecs.systems.System
import com.github.dwursteisen.minigdx.graphics.GLResourceClient
import com.github.dwursteisen.minigdx.shaders.ShaderProgram
import com.github.dwursteisen.minigdx.shaders.ShaderUtils
import com.github.dwursteisen.minigdx.shaders.fragment.FragmentShader
import com.github.dwursteisen.minigdx.shaders.vertex.VertexShader

data class RenderOptions(
    val renderName: String,
    var renderOnDisk: Boolean
)

interface Stage

abstract class RenderStage<V : VertexShader, F : FragmentShader>(
    protected val gl: GL,
    val compiler: GLResourceClient,
    val vertex: V,
    val fragment: F,
    query: EntityQuery,
    val cameraQuery: EntityQuery = EntityQuery(
        Camera::class
    ),
    lightsQuery: EntityQuery = EntityQuery(
        LightComponent::class
    ),
    val renderOption: RenderOptions = RenderOptions("undefined", renderOnDisk = false)
) : Stage, System(query) {

    private val lights by interested(lightsQuery)

    private val cameras by interested(cameraQuery)

    val camera: Entity?
        get() {
            return cameras.firstOrNull()
        }

    val light: Entity?
        get() {
            return lights.firstOrNull()
        }

    lateinit var program: ShaderProgram

    open val combinedMatrix: Mat4
        get() {
            return camera?.let {
                val eye = it.position.transformation.translation
                val cameraRotation = rotation(it.position.transformation)
                val direction = (cameraRotation * translation(FRONT)).translation
                val target = eye + direction

                val view = lookAt(
                    eye,
                    target,
                    UP
                )
                val projection = it.get(Camera::class).projection
                projection * view
            } ?: Mat4.identity()
        }

    open fun compileShaders() {
        program = ShaderUtils.createShaderProgram(gl, vertex.toString(), fragment.toString()).apply {
            vertex.parameters.forEach {
                it.create(this)
            }
            fragment.parameters.forEach {
                it.create(this)
            }
        }
    }

    open fun uniforms() = Unit

    open fun uniforms(entity: Entity) = Unit

    open fun attributes(entity: Entity) = Unit

    override fun update(delta: Seconds) {
        // TODO: if renderInMemory -> FBO
        gl.useProgram(program)
        uniforms()
        super.update(delta)
    }

    abstract override fun update(delta: Seconds, entity: Entity)

    companion object {
        private val FRONT = Float3(0f, 0f, -1f)
        private val UP = Float3(0f, 1f, 0f)
    }
}

class FrameBufferRegistry
