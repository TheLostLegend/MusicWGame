package com.serwylo.beatgame.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.serwylo.beatgame.BeatFeetGame
import com.serwylo.beatgame.ui.*
import kotlin.properties.Delegates


class MainMenuScreen(private val game: BeatFeetGame): ScreenAdapter() {

    private val stage = makeStage()
    private val batch: SpriteBatch = SpriteBatch()
    private lateinit var testButton: Button
    var login by Delegates.notNull<Int>()
    var layout:GlyphLayout
    private val fuel = game.assets.getSprites().barrel_b
    private var amount = 3
    private var last:Long = 0
    var path=Gdx.files.localStoragePath

    init {
        val sprites = game.assets.getSprites()
        val styles = game.assets.getStyles()
        val strings = game.assets.getStrings()
        layout = GlyphLayout()

        amount = game.prefs.getString("fuel").toInt()
        last = game.prefs.getString("time").toLong()

        val container = VerticalGroup().apply {
            setFillParent(true)
            align(Align.center)
            space(UI_SPACE)
        }

        while (amount < 3 && System.currentTimeMillis() - 300000 >= last){
            var check = System.currentTimeMillis()
            amount++
            last += 300000
            game.prefs.putString("fuel", amount.toString())
            game.prefs.flush()
            game.prefs.putString("time", System.currentTimeMillis().toString())
            game.prefs.flush()
        }

        login = game.prefs.getString("login").toInt()

        container.addActor(
            makeHeading(strings["app.name"], sprites.logo, styles, strings)
        )



        val buttonTable = Table()
        val bottomTable = Table()
        bottomTable.setFillParent(true)
        bottomTable.bottom().padBottom(30f);

        val noFuel = Label("No Fuel?", styles.label.medium).apply {
            setAlignment(Align.center)
            color = color.cpy().apply { a = 0f }
        }

        val playButton = makeLargeButton(strings["main-menu.btn.play"], styles) {
            if (amount <= 0){
                //game.toastLong("Not enough fuel");
            }
            else {
                amount -=1
                game.prefs.putString("fuel", amount.toString())
                game.prefs.flush()
                if (amount == 2){
                    var check = System.currentTimeMillis().toString()
                    game.prefs.putString("time", check)
                    game.prefs.flush()
                }
                game.loadGame(Gdx.files.internal(game.prefs.getString("musicFile")), game.prefs.getString("songName"), game.prefs.getString("selectedShip"))
            }
            noFuel.clearActions()
            noFuel.addAction(
                Actions.sequence(
                    Actions.alpha(0f) ,
                    Actions.fadeIn(0.1f) ,
                    Actions.delay(1f) ,
                    Actions.fadeOut(0.8f)
                )
            )
        }
        val songsButton = makeLargeButton(strings["main-menu.btn.songs"], styles) { game.showLevelSelectMenu() }
        val shipButton = makeLargeButton(strings["main-menu.btn.ship"], styles) { game.showShipSelectMenu() }
        val lbButton = makeLargeButton(strings["main-menu.btn.leaderboards"], styles) { game.showLeaderboards() }
        val loginButton = makeLargeButton(strings["main-menu.btn.login"], styles) { game.showLoginMenu() }
        val logoutButton = makeLargeButton(strings["main-menu.btn.logout"], styles) {
            game.prefs.putString("login", "0")
            game.prefs.flush();
            game.showMenu()
        }

        bottomTable.apply {
            add(noFuel).center()
            row()
            add(playButton).apply {
                fillX()
            }
        }

        buttonTable.apply {
            pad(UI_SPACE)

            if (login!=0){
                row()
                add(logoutButton).apply {
                    fillX()
                }

                row()
                add(songsButton).apply {
                    fillX()
                }
                row()
                add(shipButton).apply {
                    fillX()
                }
            }
            else {
                row()
                add(loginButton).apply {
                    fillX()
                }
            }

            row()
            add(lbButton).apply {
                fillX()
            }

            //row()
//            add(achievementsButton).apply {
//                fillX()
//            }

//            row()
//            add(aboutButton).apply {
//                fillX()
//                padBottom(UI_SPACE * 2)
//            }
        }

        if (Gdx.app.type == Application.ApplicationType.Desktop) {
            val quitButton = makeButton(strings["main-menu.btn.quit"], styles) { Gdx.app.exit() }
            buttonTable.row()
            buttonTable.add(quitButton).fillX()
        }

        container.addActor(buttonTable)

        stage.addActor(container)
        if (login !=0){
            layout.width = Gdx.graphics.getWidth().toFloat();
            layout.setText(styles.label.large.font, game.prefs.getString("songName"))
            stage.addActor(bottomTable)
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
    }
    var x =0F// initial x position
    var speed = 50f; // adjust as needed

    override fun render(delta: Float) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)

        x -= speed * Gdx.graphics.getDeltaTime();

        if (x < -layout.width) {
            x = Gdx.graphics.getWidth().toFloat();
        }
        var styles = game.assets.getStyles()
        batch.begin();

        //makeIcon(fuel)

        amount = game.prefs.getString("fuel").toInt()
        last = game.prefs.getString("time").toLong()

        styles.label.large.font.draw(batch, (amount.toString() + " / 3"), 30f, Gdx.graphics.height.toFloat()-30) // выводим текст на экран в координатах (x,y)
        if (amount<3 && System.currentTimeMillis() - 300000 >= last){
            amount++
            game.prefs.putString("fuel", amount.toString())
            game.prefs.flush()
            game.prefs.putString("time", System.currentTimeMillis().toString())
            game.prefs.flush()
        }
        if (amount<3){
            var seconds = (System.currentTimeMillis() - 300000 - last )/1000 * -1
            var minutes = seconds/60
            var e = ""
            seconds%=60
            if (seconds<=9) e = "0"
            styles.label.large.font.draw(batch, "$minutes:$e$seconds" , 190f, Gdx.graphics.height.toFloat()-30)
        }
        styles.label.large.font.draw(batch, layout, x, 30F);
        batch.end();
        stage.draw()
    }

}