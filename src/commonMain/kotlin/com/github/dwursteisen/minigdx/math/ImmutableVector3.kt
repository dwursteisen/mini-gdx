package com.github.dwursteisen.minigdx.math

/**
 * Vector in which you can only <strong>read</strong> values.
 * Those values can be updated by another entity, that's why
 * you can only read as you might loose your modification otherwise.
 */
class ImmutableVector3(private val delegate: Vector3) {

    constructor(x: Float = 0f, y: Float = 0f, z: Float = 0f) : this(Vector3(x, y, z))

    val x: Float
        get() = delegate.x
    val y: Float
        get() = delegate.y
    val z: Float
        get() = delegate.z

    val width: Float
        get() = delegate.x
    val height: Float
        get() = delegate.y
    val deep: Float
        get() = delegate.z

    fun mutable(): Vector3 {
        return delegate.copy()
    }

    fun add(other: ImmutableVector3): Vector3 {
        return add(other.mutable())
    }

    fun add(other: Vector3): Vector3 {
        return mutable().add(other)
    }
}