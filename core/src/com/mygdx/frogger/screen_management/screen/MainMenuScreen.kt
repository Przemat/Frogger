package com.mygdx.frogger.screen_management.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.frogger.Models.ReadObjects
import com.mygdx.frogger.config.GameConfig
import com.mygdx.frogger.screen_management.util.ScreenEnum
import com.mygdx.frogger.screen_management.util.UIFactory


class MainMenuScreen : AbstractScreen() {
    var tiledMap: TiledMap? = null
    var camera: OrthographicCamera? = null
    var tiledMapRenderer: OrthogonalTiledMapRenderer? = null
    var posCamera: Vector3 = Vector3(Gdx.graphics.width.toFloat() / 2, Gdx.graphics.height.toFloat() / 2, 0f)
    var scale: Float = Gdx.graphics.width.toFloat() / 720
    var logo: Texture
    var exittex: Texture
    var playtex: Texture
    var gameConfig: GameConfig
    var uiFactory: UIFactory

    var music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.wav"))

    val readObjects: ReadObjects
    private var stage: Stage? = null

    init {
        music.stop()
        stage = Stage(ScreenViewport())
        gameConfig = GameConfig()
        uiFactory = UIFactory()
        camera = OrthographicCamera()
        val path = Gdx.files.internal("maps/menuconf.xml")
        readObjects = ReadObjects(path)
        tiledMap = TmxMapLoader().load("maps/menu.tmx")
        logo = Texture(Gdx.files.internal("gui/logo.png"))
        exittex = Texture(Gdx.files.internal("gui/exit.png"))
        playtex = Texture(Gdx.files.internal("gui/play.png"))
        music.isLooping = true
        music.volume = 0.5f
        music.play()
    }
    override fun buildStage() {
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        camera!!.setToOrtho(false, w, h)
        camera!!.update()

        tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap, scale)

        val title = Image(logo)
        title.scaleX = scale
        title.scaleY = scale
        title.setY((Gdx.graphics.height * 2 / 3).toFloat())
        title.setX((Gdx.graphics.width / 2f - title.width))
        stage!!.addActor(title)
        val playButton: ImageButton = uiFactory.createButton(playtex)
        playButton.image.setFillParent(true)
        playButton.setSize(playtex.width * scale, playtex.height * scale)
        playButton.setPosition(0f, Gdx.graphics.height.toFloat() / 2 - playButton.height)
        playButton.addListener(uiFactory.createListener(ScreenEnum.LEVEL_SELECT, music))
        stage!!.addActor(playButton)
        val exitButton: ImageButton = uiFactory.createButton(exittex)
        exitButton.image.setFillParent(true)
        exitButton.setSize(exittex.width * scale, exittex.height * scale)
        exitButton.setPosition(0f, Gdx.graphics.height.toFloat() / 3 - playButton.height)
        exitButton.addListener(
                object : InputListener() {
                    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        Gdx.app.exit()
                        return false
                    }
                })
        stage!!.addActor(exitButton)
    }

    override fun show() {
        Gdx.input.setInputProcessor(stage)
    }

    private fun cameraPoc() {
        if (posCamera.y >= Gdx.graphics.height.toFloat() / 2 + scale * 14 * 48 + 10) {
            posCamera.y = (Gdx.graphics.height.toFloat() / 2) + scale * 48
            camera!!.position.set(posCamera)
        }
        posCamera.y += 100f * Gdx.graphics.deltaTime
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
        cameraPoc()
        camera!!.position.lerp(posCamera, 0.1f)
        camera!!.update()

        tiledMapRenderer!!.setView(camera)
        tiledMapRenderer!!.render()

        readObjects.drawback(camera!!)
        readObjects.drawfront(camera!!)
        stage!!.act()
        stage!!.draw()
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        stage!!.dispose()
        tiledMapRenderer!!.dispose()
        tiledMap!!.dispose()
        logo.dispose()
    }

}
