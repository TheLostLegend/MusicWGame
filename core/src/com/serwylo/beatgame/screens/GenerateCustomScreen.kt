package com.serwylo.beatgame.screens

import com.badlogic.gdx.*
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling.fillX
import com.example.restapiidemo.network.RetrofitClient
import com.google.gson.Gson
import com.music.waves.model.entity.LeaderboardALTER
import com.serwylo.beatgame.BeatFeetGame
import com.serwylo.beatgame.audio.CachedWorldData
import com.serwylo.beatgame.audio.cacheWorld
import com.serwylo.beatgame.audio.features.World
import com.serwylo.beatgame.levels.Level
import com.serwylo.beatgame.levels.achievements.loadAllAchievements
import com.serwylo.beatgame.network.data.TrackModel
import com.serwylo.beatgame.ui.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GenerateCustomScreen(private val game: BeatFeetGame , file: FileHandle): ScreenAdapter() {

    private val stage = makeStage()

    private val sprites = game.assets.getSprites()
    private val styles = game.assets.getStyles()
    private val skin = game.assets.getSkin()
    private val strings = game.assets.getStrings()
    private var file = file

    init {
        setupStage()
    }

    private fun setupStage() {
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
        val someTime = Label("WARNING: May take a lot of time!", styles.label.medium).apply {
            setAlignment(Align.center)
        }
        val here = Label("Or you may find already generated map here:", styles.label.medium).apply {
            setAlignment(Align.center)
        }
        val genButton = makeLargeButton(strings["generate-custom.newGen"], styles) {
            game.changeTrack(file.toString(), "Your Song")
        }
        val searchBar = makeTextField(strings["generate-custom.search"], game.assets.getSkin(), width)
        val searchButton = makeButton(strings["generate-custom.searchBT"], styles) { fill(searchBar.text.toString(), table, width) }

        stage.addActor(scrollPane)

        container.addActor(
            makeHeading(strings["generate-custom.title"], sprites.logo, styles, strings) {
                game.showMenu()
            }
        )

        val table3 = Table().apply {
            pad(UI_SPACE)
            add(someTime).apply {
                fillX()
            }
            row()
            add(genButton).apply {
                fillX()
            }
            row()
            row()
            add(here).apply {
                fillX()
            }
            row()
        }


        val table2 = Table().apply {
            pad(UI_SPACE)
        }
        table2.add(searchBar).width(width*5/6)
        table2.add(searchButton).width(width*1/6)

        container.addActor(table3)
        container.addActor(table2)
        fill(searchBar.text.toString(), table, width)
        container.addActor(table)

    }

    fun fill(string: String, table: Table, width:Float){
        table.clear()
        RetrofitClient.instance?.getMyApi()?.getTracks(string)?.enqueue(object : Callback<List<TrackModel?>> {
            override fun onResponse(
                call: Call<List<TrackModel?>> ,
                response: Response<List<TrackModel?>>
            ) {
                table.clear()
                val list: List<TrackModel?>?=response.body()
                (list as ArrayList<TrackModel>?)?.forEachIndexed { i , track ->
                    table.row()
                    table.add(makeButton(track.title + "   " + track.duration, game.assets.getStyles()){
                        var json = track.levelMap
                        val data = Gson().fromJson(json, CachedWorldData::class.java)

                        if (data.version != CachedWorldData.currentVersion) {
                            error("Precompiled world data is version ${data.version}, whereas we only know how to handle version ${CachedWorldData.currentVersion} with certainty. Perhaps we need to compile again using :song-extract:processSongs?")
                        }
                        cacheWorld(file, World(file, data.duration, arrayOf(), data.featuresLow, data.featuresMid, data.featuresHigh))
                        game.showMenu()
                    }).width(width)
                }
            }
            override fun onFailure(call: Call<List<TrackModel?>> , t: Throwable) {
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



}

