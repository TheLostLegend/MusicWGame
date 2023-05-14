package com.serwylo.beatgame.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3


fun calcDensityScaleFactor(): Float {
    return ((Gdx.graphics.density - 1) * 0.8f).coerceAtLeast(1f)
}

class ParallaxCamera(viewportWidth: Float, viewportHeight: Float) : OrthographicCamera(viewportWidth, viewportHeight) {

    var parallaxView = Matrix4()
    var parallaxCombined = Matrix4()
    var tmp = Vector3()
    var tmp2 = Vector3()

    fun calculateParallaxMatrix(parallaxX: Float, parallaxY: Float): Matrix4 {

        update()

        tmp.set(position)
        tmp.x *= parallaxX
        tmp.y *= parallaxY

        parallaxView.setToLookAt(tmp, tmp2.set(tmp).add(direction), up)
        parallaxCombined.set(projection)

        Matrix4.mul(parallaxCombined.`val`, parallaxView.`val`)

        return parallaxCombined

    }

}