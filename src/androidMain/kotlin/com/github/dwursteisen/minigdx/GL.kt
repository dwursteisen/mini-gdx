package com.github.dwursteisen.minigdx

import android.graphics.Point
import com.github.dwursteisen.minigdx.file.FileHandler
import com.github.dwursteisen.minigdx.graphics.FillViewportStrategy
import com.github.dwursteisen.minigdx.graphics.ViewportStrategy
import com.github.dwursteisen.minigdx.input.InputHandler
import com.github.dwursteisen.minigdx.input.Key
import com.github.dwursteisen.minigdx.input.TouchSignal
import com.github.dwursteisen.minigdx.internal.MiniGdxSurfaceView
import com.github.dwursteisen.minigdx.math.Vector2

actual class GLConfiguration(val activity: MiniGdxActivity)

actual class GLContext actual constructor(private val configuration: GLConfiguration) {
    internal actual fun createContext(): GL {
        val display = configuration.activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        // FIXME: should be the world size versus the screen size.
        return AndroidGL(Canvas(400, 400))
    }

    internal actual fun createFileHandler(): FileHandler {
        return FileHandler(configuration.activity)
    }


    internal actual fun createViewportStrategy(): ViewportStrategy {
        return FillViewportStrategy()
    }

    internal actual fun createInputHandler(): InputHandler {
        return object : InputHandler {
            override fun isKey(key: Key): Boolean = false

            override fun isKeyPressed(key: Key): Boolean = false

            override fun isTouched(signal: TouchSignal): Vector2? = null

            override fun isJustTouched(signal: TouchSignal): Vector2? = null
        }
    }

    actual fun run(gameFactory: () -> Game) {
        val surfaceView = MiniGdxSurfaceView(configuration.activity)
        configuration.activity.setContentView(surfaceView)
    }
}