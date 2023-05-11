package com.serwylo.beatgame.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.Layout
import com.badlogic.gdx.utils.Align
import com.example.restapiidemo.network.RetrofitClient
//import com.kotcrab.vis.ui.widget.toast.Toast
import com.serwylo.beatgame.Assets
import com.serwylo.beatgame.BeatFeetGame
import com.serwylo.beatgame.network.data.Message
import com.serwylo.beatgame.ui.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginScreen(private val game: BeatFeetGame): ScreenAdapter() {

    private val stage = makeStage()

    init {
        val sprites = game.assets.getSprites()
        val styles = game.assets.getStyles()
        val strings = game.assets.getStrings()

        val container = VerticalGroup().apply {
            setFillParent(true)
            align(Align.center)
            space(UI_SPACE)
        }

        container.addActor(
            makeHeading(strings["login.title"], sprites.logo, styles, strings) {
                game.showMenu()
            }
        )


        val buttonTable = Table()
        val login = makeTextField(strings["login-menu.text.login"], game.assets.getSkin())
        val email = makeTextField(strings["login-menu.text.email"], game.assets.getSkin())
        val password = makeTextField(strings["login-menu.text.password"], game.assets.getSkin())
        var isReg = makecheck(strings["login-menu.check.isRegister"], game.assets.getSkin())
        val forgorButton = makeButton(strings["login-menu.btn.forgor"], styles) { game.showShipSelectMenu() }
        val loginButton = makeLargeButton("              " + strings["login-menu.btn.login"]+"              ", styles) {
            gettest(login.text.toString(), password.text.toString())}



        buttonTable.apply {
            pad(UI_SPACE*2)
            row()
            add(login).apply {
                fillX()
                pad(UI_SPACE)
            }
            row()
            add(email).apply {
                fillX()
                pad(UI_SPACE)
            }
            row()
            add(password).apply {
                fillX()
                pad(UI_SPACE)
            }
            row()
            add(isReg).apply {
                fillX()
                pad(UI_SPACE)
            }
            row()
            add(forgorButton).apply {
                fillX()
                pad(UI_SPACE)
            }

            row()
            add(loginButton).apply {
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

    override fun render(delta: Float) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    private fun gettest(login:String, password:String) {
        RetrofitClient.instance?.getMyApi()?.getTest2(login, password)
            ?.enqueue(object : Callback<Message?> {
                override fun onResponse(call: Call<Message?> , response: Response<Message?>) {
                    if (response.isSuccessful) {
                        game.prefs.putString("login",response.body()?.id.toString())
                        game.prefs.flush()
                        game.showMenu()
                    } else {
//                        Toast("Логин или пароль неверен")
////                        Toast.makeText(applicationContext, "Логин или пароль неверен", Toast.LENGTH_LONG)
////                            .show()
                    }
                }

                override fun onFailure(call: Call<Message?> , t: Throwable) {
                    var taa:Int = 0
                    taa+=1
//                    Toast.makeText(applicationContext, "An error has occured", Toast.LENGTH_LONG)
//                        .show()
                }
            })
    }

}