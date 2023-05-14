package com.serwylo.beatgame

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.kotcrab.vis.ui.VisUI
import com.serwylo.beatgame.audio.features.World
import com.serwylo.beatgame.levels.Levels
import com.serwylo.beatgame.screens.*
import com.serwylo.beatgame.ui.CustomToast
import java.io.File
import java.util.*

open class BeatFeetGame(val platformListener: PlatformListener, private val verbose: Boolean) : Game() {

    // Initialize this in the create() method so that we can access Gdx logging. Helps to diagnose
    // issues with asset loading if we can log meaningful messages.
    // See https://github.com/beat-feet/beat-feet/issues/97.
    lateinit var assets: Assets
    lateinit var prefs: Preferences
    lateinit var toastFactory : CustomToast.ToastFactory
    private val toasts: LinkedList<CustomToast> = LinkedList<CustomToast>()
    lateinit var gl: GL20

    @Suppress("LibGDXLogLevel") // Optional flag to make more verbose.
    override fun create() {
        VisUI.load()
        gl = Gdx.graphics.gL20
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
        if (prefs.getString("fuel").equals("") or prefs.getString("time").equals("")){
            prefs.putString("fuel", "3")
            prefs.putString("time", "0")
            prefs.flush()
        }

        toastFactory = CustomToast.ToastFactory.Builder()
                .font(assets.getStyles().Labels().large.font)
                .build()

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
            setScreen(LevelSelectScreen(this))
        }
    }

    fun showShipSelectMenu() {
        Gdx.app.postRunnable {
            setScreen(ShipSelectScreen(this))
        }
    }

    fun showLeaderboards() {
        Gdx.app.postRunnable {
            setScreen(LeaderboardScreen(this))
        }
    }

    fun showChoice(file: FileHandle) {
        Gdx.app.postRunnable {
            setScreen(GenerateCustomScreen(this, file))
        }
    }

    open fun toastLong(text: String?) {
        toasts.add(toastFactory.create(text , CustomToast.Length.LONG))
    }
    open fun toastShort(text: String?) {
        toasts.add(toastFactory.create(text , CustomToast.Length.SHORT))
    }

    fun explainCustomSongs() {
        Gdx.app.postRunnable {
            setScreen(ExplainCustomSongsScreen(this))
        }
    }

    override fun render() {
        super.render()

        // handle toast queue and display
        val it: MutableIterator<CustomToast> = toasts.iterator()
        while (it.hasNext()) {
            val t=it.next()
            if (!t.render(Gdx.graphics.deltaTime)) {
                it.remove() // toast finished -> remove
            } else {
                break // first toast still active, break the loop
            }
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
