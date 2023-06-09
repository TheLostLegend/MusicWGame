package com.serwylo.beatgame.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.serwylo.beatgame.graphics.ParallaxCamera
import com.serwylo.beatgame.graphics.SpriteRenderer

class Obstacle(val rect: Rectangle, private val sprite: SpriteRenderer) : Entity {

    override fun update(delta: Float) {
    }

    override fun render(batch: SpriteBatch, camera: ParallaxCamera, isPaused: Boolean) {

        sprite.render(batch)

        /*val r = Globals.shapeRenderer
        r.projectionMatrix = camera.combined
        r.color = Color.WHITE
        r.begin(ShapeRenderer.ShapeType.Line)
        r.rect(rect.x, rect.y, rect.width, rect.height)
        r.end()*/

    }

    companion object {

        const val STRENGTH_TO_WIDTH = 3f

        const val MIN_WIDTH = 0.1f
    }

}