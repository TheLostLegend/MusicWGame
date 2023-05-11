package com.serwylo.beatgame.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.serwylo.beatgame.Assets
import com.serwylo.beatgame.graphics.LayeredTiledSprite
import com.serwylo.beatgame.graphics.TiledSprite
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.sqrt

object ObstacleBuilder {

    const val TILE_SIZE = TiledSprite.TILE_SIZE

    fun makeObstacle(rect: Rectangle, sprites: Assets.Sprites, bonus:Boolean): Obstacle {
        return makeShortObstacle(sprites,rect.x, rect.y, rect.width, bonus)

    }

    private fun sizeToTileCount(size: Float): Int = ceil(size / TILE_SIZE).toInt()

    class WallSprites(val left: RegionFetcher, val inner: Array<RegionFetcher>, val right: RegionFetcher) {

        constructor(left: RegionFetcher, inner: RegionFetcher, right: RegionFetcher):
                this(left, arrayOf(inner), right)

        companion object {
            private val all = arrayOf(
                WallSprites({ it.wall_a_left }, { it.wall_a_inner }, { it.wall_a_right }),
                WallSprites({ it.wall_b_left }, { it.wall_b_inner }, { it.wall_b_right })
//                WallSprites({ it.wall_c_left }, { it.wall_c_inner }, { it.wall_c_right })
            )

            fun select(bonus: Boolean): WallSprites {
                return if (bonus) all[0]
                else all[1]
            }
        }
    }

    private fun makeShortObstacle(sprites: Assets.Sprites,x:Float, y: Float, width: Float, bonus: Boolean): Obstacle {
        // Fence or row of cars or seats, etc.
        val tilesWide = sizeToTileCount(width)
        val boundingBox = Rectangle(x, y, tilesWide * TILE_SIZE, TILE_SIZE)
        val wallSprites = WallSprites.select(bonus)
        val baseLayerSprites = Array<TextureRegion?>(tilesWide) {
            when (it) {
                0 -> wallSprites.left(sprites)
                (tilesWide - 1) -> wallSprites.right(sprites)
                else -> wallSprites.inner.random()(sprites)
            }
        }

        val position = Vector2(x, y)

        val layers = LayeredTiledSprite(arrayOf(
                TiledSprite(position, arrayOf(baseLayerSprites))
        ))

        return Obstacle(boundingBox, layers)
    }

    fun makeGround(sprites: Assets.Sprites): Ground {
        return Ground(GroundSprite.random().getSprite(sprites))
    }

    class GroundSprite(private val sprite: RegionFetcher) {

        fun getSprite(sprites: Assets.Sprites): TextureRegion = sprite(sprites)

        companion object {

            private val all = arrayOf(
                    GroundSprite { it.ground_a },
                    GroundSprite { it.ground_b },
                    GroundSprite { it.ground_c },
                    GroundSprite { it.ground_d },
                    GroundSprite { it.ground_e },
                    GroundSprite { it.ground_f }
            )

            fun random(): GroundSprite {
                return all.random()
            }
        }
    }

}

typealias RegionFetcher = (sprites: Assets.Sprites) -> TextureRegion