package com.serwylo.beatgame.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.serwylo.beatgame.Assets
import com.serwylo.beatgame.Globals
import com.serwylo.beatgame.graphics.ParallaxCamera

class SuccessPlayer(
        sprites: Assets.Sprites
) : Entity {

    private val position = Vector2()
    private val spriteFront = sprites.character_a_walk[0]

    private var animation: Animation<TextureRegion> = Animation(
            0.2f,
            sprites.character_a_walk
    )

    var successTime = 0f

    fun setup(position: Vector2) {
        this.position.set(position)
        this.successTime = Globals.animationTimer
    }

    override fun render(batch: SpriteBatch, camera: ParallaxCamera, isPaused: Boolean) {
        position.y += 5f * Gdx.graphics.deltaTime
        val sprite = if (Globals.animationTimer - successTime < STAND_TIME) spriteFront else animation.getKeyFrame(Globals.animationTimer, true)

        batch.draw(sprite, position.x, position.y, Player.WIDTH, Player.HEIGHT)

    }

    override fun update(delta: Float) {

    }

    companion object {
        private const val STAND_TIME = 1f
    }

}
