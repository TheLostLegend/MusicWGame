package com.serwylo.beatgame

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.serwylo.beatgame.audio.features.World
import com.serwylo.beatgame.levels.Levels
import com.serwylo.beatgame.screens.*
import games.spooky.gdx.nativefilechooser.NativeFileChooser
import java.io.File

open class BeatFeetGame(val platformListener: PlatformListener, private val verbose: Boolean, private val fileChooser: NativeFileChooser) : Game() {

    // Initialize this in the create() method so that we can access Gdx logging. Helps to diagnose
    // issues with asset loading if we can log meaningful messages.
    // See https://github.com/beat-feet/beat-feet/issues/97.
    lateinit var assets: Assets
    lateinit var prefs: Preferences

    @Suppress("LibGDXLogLevel") // Optional flag to make more verbose.
    override fun create() {
        prefs = Gdx.app.getPreferences("My Preferences")

        if (verbose) {
            Gdx.app.logLevel = Application.LOG_DEBUG
        }

        assets = Assets(Assets.getLocale())

        Globals.shapeRenderer = ShapeRenderer()
        Globals.spriteBatch = SpriteBatch()

        assets.initSync()
        val strings = assets.getStrings()
        if (prefs.getString("songName").equals("") or prefs.getString("musicFile").equals("")){
            prefs.putString("songName", strings[Levels.EyeTwitching.labelId])
            prefs.putString("musicFile", "songs${File.separator}mp3${File.separator}${Levels.EyeTwitching.mp3Name}")
            prefs.flush();
        }
        if (prefs.getString("selectedShip").equals("")){
            prefs.putString("selectedShip", "ships.wolf")
            prefs.flush();
        }
        if (prefs.getString("login").equals("")){
            prefs.putString("login", "0")
            prefs.flush()
        }
        setScreen(MainMenuScreen(this))
    }

    fun loadGame(musicFile: FileHandle, songName: String, selectedShip:String) {
        Gdx.app.postRunnable {
            setScreen(LoadingScreen(this, musicFile, songName, selectedShip))
        }
    }
    fun changeTrack(musicFile: String, songName: String) {
        prefs.putString("songName", songName)
        prefs.putString("musicFile", musicFile)
        prefs.flush();
        Gdx.app.postRunnable {
            setScreen(MainMenuScreen(this))
        }
    }

    fun changeShip(shipName: String){
        prefs.putString("selectedShip", shipName)
        prefs.flush();
        Gdx.app.postRunnable {
            setScreen(MainMenuScreen(this))
        }
    }

    fun startGame(world: World , selectedShip: String) {
        Gdx.app.postRunnable {
            setScreen(PlatformGameScreen(this, world, selectedShip))
        }
    }

    fun showMenu() {
        Gdx.app.postRunnable {
            setScreen(MainMenuScreen(this))
        }
    }

    fun showLoginMenu() {
        Gdx.app.postRunnable {
            setScreen(LoginScreen(this))
        }
    }

    fun showLevelSelectMenu() {
        Gdx.app.postRunnable {
            setScreen(LevelSelectScreen(this, fileChooser))
        }
    }

    fun showShipSelectMenu() {
        Gdx.app.postRunnable {
            setScreen(ShipSelectScreen(this))
        }
    }

    fun explainCustomSongs() {
        Gdx.app.postRunnable {
            setScreen(ExplainCustomSongsScreen(this))
        }
    }

    fun showAboutScreen() {
        Gdx.app.postRunnable {
            setScreen(AboutScreen(this))
        }
    }

    fun showAchievements() {
        Gdx.app.postRunnable {
            setScreen(AchievementsScreen(this))
        }
    }

}
