package com.serwylo.beatgame.screens

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.serwylo.beatgame.BeatFeetGame
import com.serwylo.beatgame.audio.customMp3
import com.serwylo.beatgame.levels.Level
import com.serwylo.beatgame.levels.achievements.loadAllAchievements
import com.serwylo.beatgame.levels.loadHighScore
import com.serwylo.beatgame.ships.Ship
import com.serwylo.beatgame.ships.Ships
import com.serwylo.beatgame.ui.UI_SPACE
import com.serwylo.beatgame.ui.makeHeading
import com.serwylo.beatgame.ui.makeIcon
import com.serwylo.beatgame.ui.makeStage
import java.io.File

class ShipSelectScreen(private val game: BeatFeetGame): ScreenAdapter() {

    private val stage = makeStage()

    private val sprites = game.assets.getSprites()
    private val styles = game.assets.getStyles()
    private val skin = game.assets.getSkin()
    private val strings = game.assets.getStrings()

    private val heart = sprites.heart
    private val shield = sprites.shield_full
    private val scoreTexture = sprites.score

    private val achievements = loadAllAchievements()

    init {
        setupStage()
    }

    private fun setupStage() {
        val levelsPerRow = 1
        val width = (stage.width - UI_SPACE * 2) / levelsPerRow
        val height = width * 1/7

        var x = 0
        var y = 0

        val container = VerticalGroup().apply {
            space(UI_SPACE)
            padTop(UI_SPACE * 2)
        }

        val scrollPane = ScrollPane(container, skin).apply {
            setFillParent(true)
            setScrollingDisabled(true, false)
            setupOverscroll(width / 4, 30f, 200f)
        }

        stage.addActor(scrollPane)

        container.addActor(
            makeHeading(strings["ship-select.title"], sprites.logo, styles, strings) {
                game.showMenu()
            }
        )

        val table = Table().apply {
            pad(UI_SPACE)
        }

        container.addActor(table)

        Ships.all.forEachIndexed { i , ship ->

            if (i % levelsPerRow == 0) {
                table.row()
                y ++
                x = 0
            }

            table.add(makeButton(ship)).width(width).height(height)

            x ++

        }

    }

    override fun show() {

        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        Gdx.input.inputProcessor = InputMultiplexer(stage, object : InputAdapter() {

            override fun keyDown(keycode: Int): Boolean {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
                    game.showMenu()
                    return true
                }

                return false
            }

        })

    }

    override fun hide() {
        Gdx.input.inputProcessor = null
        Gdx.input.setCatchKey(Input.Keys.BACK, false)
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        stage.clear()
        setupStage()
    }

    override fun render(delta: Float) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act(delta)
        stage.draw()

    }

    override fun dispose() {
        stage.dispose()
    }

    private fun makeButton(ship: Ship): WidgetGroup {

        val buttonStyle = "default"
        val textColor = Color.WHITE

        val button = Button(skin, buttonStyle).apply {
            setFillParent(true)
            addListener(object: ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    onShipSelected(ship)
                }
            })
        }

        val labelString = strings[ship.labelId]

        val levelLabel = Label(labelString, styles.label.large).apply {
            wrap = true
            color = textColor
            setAlignment(Align.topLeft)
        }

        val table = Table().apply {
            setFillParent(true)
            touchable = Touchable.disabled // Let the button in the background do the interactivity.
            pad(Value.percentWidth(0.125f))

            add(levelLabel).expand().fill().colspan(4)
        }

        val healthLabel = Label(ship.health.toString(), styles.label.large)
        val armorLabel = Label(ship.armor.toString(), styles.label.large)
        val scoreLabel = Label(ship.boost.toString() + "%", styles.label.large)

        val heartIcon = makeIcon(heart)
        val shieldIcon = makeIcon(shield)
        val scoreIcon= makeIcon(scoreTexture)

        val iconSize = Value.percentWidth(1.0f)
        val iconSpace = Value.percentWidth(0.2f)

        table.row()
        table.add(heartIcon).spaceRight(iconSpace).size(iconSize)
        table.add(healthLabel)
        table.add(shieldIcon).spaceLeft(iconSpace).spaceRight(iconSpace).size(iconSize)
        table.add(armorLabel).expandX().left()
        table.add(scoreIcon).spaceLeft(iconSpace).spaceRight(iconSpace).size(iconSize)
        table.add(scoreLabel).expandX().left()

        return WidgetGroup(button, table)

    }

    fun onShipSelected(ship: Ship): Boolean {
        game.changeShip(ship.labelId)
        return true
    }

}

