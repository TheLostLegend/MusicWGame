package com.serwylo.beatgame.screens

import com.badlogic.gdx.*
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.serwylo.beatgame.BeatFeetGame
import com.serwylo.beatgame.levels.Level
import com.serwylo.beatgame.levels.Levels
import com.serwylo.beatgame.levels.achievements.loadAllAchievements
import com.serwylo.beatgame.levels.loadHighScore
import com.serwylo.beatgame.ui.UI_SPACE
import com.serwylo.beatgame.ui.makeHeading
import com.serwylo.beatgame.ui.makeIcon
import com.serwylo.beatgame.ui.makeStage
import games.spooky.gdx.nativefilechooser.NativeFileChooser
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration
import java.io.File
import java.io.FilenameFilter


class LevelSelectScreen(private val game: BeatFeetGame, private val fileChooser: NativeFileChooser): ScreenAdapter() {

    private val stage = makeStage()

    private val sprites = game.assets.getSprites()
    private val styles = game.assets.getStyles()
    private val skin = game.assets.getSkin()
    private val strings = game.assets.getStrings()

    private val distanceTexture = sprites.right_sign
    private val scoreTexture = sprites.score

    private val achievements = loadAllAchievements()

    init {
        setupStage()
    }

    private fun setupStage() {
        // Later on, do some proper responsive sizing. However my first attempts struggled with
        // density independent pixel calculations (even though the math is simple, it didn't
        // seem to set proper breakpoints, perhaps because of the arbitrary math in calcDensityScaleFactor()
        // from before it occurred we could use DIPs).
        val levelsPerRow = 1
        val width = (stage.width - UI_SPACE * 2) / levelsPerRow
        val height = width * 2/7

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
            makeHeading(strings["level-select.title"], sprites.logo, styles, strings) {
                game.showMenu()
            }
        )

        val table = Table().apply {
            pad(UI_SPACE)
        }

        container.addActor(table)

        Levels.all.forEachIndexed { i, level ->

            if (i % levelsPerRow == 0) {
                table.row()
                y ++
                x = 0
            }

            table.add(makeButton(level)).width(width).height(height)

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

    private fun makeButton(level: Level): WidgetGroup {

        val buttonStyle = "default"
        val textColor = Color.WHITE

        val button = Button(skin, buttonStyle).apply {
            setFillParent(true)
            addListener(object: ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    onLevelSelected(level)
                }
            })
        }

        val labelString = strings[level.labelId]

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

        val highScore = loadHighScore(level)

        if (highScore.exists()) {

            val distanceLabel = Label(highScore.distancePercentString(), styles.label.large)
            val scoreLabel = Label(highScore.points.toString(), styles.label.large)

            val distanceIcon = makeIcon(distanceTexture)
            val scoreIcon = makeIcon(scoreTexture)

            val iconSize = Value.percentWidth(1.0f)
            val iconSpace = Value.percentWidth(0.2f)

            table.row()
            table.add(distanceIcon).spaceRight(iconSpace).size(iconSize)
            table.add(distanceLabel)
            table.add(scoreIcon).spaceLeft(iconSpace).spaceRight(iconSpace).size(iconSize)
            table.add(scoreLabel).expandX().left()

        }

        return WidgetGroup(button, table)

    }

    fun onLevelSelected(level: Level): Boolean {
        if (level.mp3Name == "custom.mp3") {
            val conf=NativeFileChooserConfiguration()
            conf.directory=Gdx.files.absolute(System.getProperty("user.home"))
            conf.mimeFilter="audio/*"
            conf.nameFilter = FilenameFilter { dir , name -> name.endsWith("mp3") }
            conf.title="Choose audio file"

            fileChooser.chooseFile(conf , object : NativeFileChooserCallback {
                override fun onFileChosen(file: FileHandle) {
                    // Do stuff with file, yay!
                }

                override fun onCancellation() {
                    // Warn user how rude it can be to cancel developer's effort
                }

                override fun onError(exception: Exception) {
                    // Handle error (hint: use exception type)
                }
            })




        } else {
            game.changeTrack("songs${File.separator}mp3${File.separator}${level.mp3Name}", strings[level.labelId])
        }

        return true
    }

}

