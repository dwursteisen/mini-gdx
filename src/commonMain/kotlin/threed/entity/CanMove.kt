package threed.entity

import com.curiouscreature.kotlin.math.Mat4
import threed.math.Vector3

interface CanMove {

    var modelMatrix: Mat4

    fun rotate(x: Number, y: Number, z: Number): CanMove
    fun rotate(angles: Vector3): CanMove

    fun rotateX(angle: Float): CanMove
    fun rotateY(angle: Float): CanMove
    fun rotateZ(angle: Float): CanMove

    fun setRotationX(angle: Float): CanMove

    fun translate(move: Vector3): CanMove = translate(move.x, move.y, move.z)
    fun translate(x: Number, y: Number, z: Number): CanMove
}
