package com.serwylo.beatgame.screens

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.example.restapiidemo.network.RetrofitClient
import com.music.waves.model.entity.LeaderboardALTER
import com.serwylo.beatgame.BeatFeetGame
import com.serwylo.beatgame.levels.Level
import com.serwylo.beatgame.levels.achievements.loadAllAchievements
import com.serwylo.beatgame.ui.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LeaderboardScreen(private val game: BeatFeetGame): ScreenAdapter() {

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
            setWidth(width)

        }

        val scrollPane = ScrollPane(container, skin).apply {
            setFillParent(true)
            setScrollingDisabled(true, false)
            setupOverscroll(width / 4, 30f, 200f)
        }
        val table = Table().apply {
            pad(UI_SPACE)
        }
        val searchBar = makeTextField(strings["leaderboard-menu.text.search"], game.assets.getSkin(), width)
        val searchButton = makeButton(strings["leaderboard-menu.text.searchBT"], styles) { fill(searchBar.text.toString(), table, width, height) }

        stage.addActor(scrollPane)

        container.addActor(
            makeHeading(strings["leaderboard.title"], sprites.logo, styles, strings) {
                game.showMenu()
            }
        )
        //container.addActor(searchBar)
        //stage.addActor(searchButton)



        val table2 = Table().apply {
            pad(UI_SPACE)
        }
        table2.add(searchBar).width(width*5/6)
        table2.add(searchButton).width(width*1/6)


        container.addActor(table2)
        fill(searchBar.text.toString(), table, width, height)
        container.addActor(table)

    }

    fun fill(string: String, table: Table, width:Float, height:Float){
        table.clear()
        RetrofitClient.instance?.getMyApi()?.getRecords(string)?.enqueue(object : Callback<List<LeaderboardALTER?>> {
            override fun onResponse(
                call: Call<List<LeaderboardALTER?>> ,
                response: Response<List<LeaderboardALTER?>>
            ) {
                val list: List<LeaderboardALTER?>?=response.body()
                (list as ArrayList<LeaderboardALTER>?)?.forEachIndexed { i , level ->
                    table.row()
                    table.add(makeButton(level.playerLogin + "   " + level.trackName + "   " +level.score, game.assets.getStyles()){}).width(width)
                }
            }
            override fun onFailure(call: Call<List<LeaderboardALTER?>> , t: Throwable) {
                call.cancel()
            }
        })

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


        return WidgetGroup(button, table)

    }



}

