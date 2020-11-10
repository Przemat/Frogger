package com.mygdx.frogger.screen_management.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.frogger.Models.ReadObjects
import com.mygdx.frogger.config.GameConfig
import com.mygdx.frogger.screen_management.util.UIFactory

class GameScreen(level: Int) : AbstractScreen() {

    val level = level
    var tiledMap: TiledMap
    var camera: OrthographicCamera
    var tiledMapRenderer: OrthogonalTiledMapRenderer
    var posCamera: Vector3
    var scale: Float = Gdx.graphics.width.toFloat() / 720
    var settingtex: Texture
    var uiFactory: UIFactory

    //val readObjects: ReadObjects
    var gameConfig: GameConfig
    val readObjects: ReadObjects
    private var stage: Stage

    init {
        gameConfig = GameConfig()
        uiFactory = UIFactory()
        stage = Stage(ScreenViewport())
        camera = OrthographicCamera()
        settingtex = Texture(Gdx.files.internal("gui/setting.png"))
        tiledMap = TmxMapLoader().load("maps/" + level + ".tmx")
        tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap, scale)
        var prop = tiledMap.properties
        val mapHeight: Int = prop.get("height", Int::class.java)
        val tilePixelHeight: Int = prop.get("tileheight", Int::class.java)
        val gap = Gdx.graphics.height-mapHeight*tilePixelHeight*scale
        posCamera = Vector3(Gdx.graphics.width.toFloat() / 2, Gdx.graphics.height.toFloat()/2 - gap+104*scale , 0f)
        val path = Gdx.files.internal("maps/1conf.xml")
        readObjects = ReadObjects(path)


    }

    override fun show() {
        Gdx.input.setInputProcessor(stage);
    }

    override fun buildStage() {
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat() - 48 * scale
        camera.setToOrtho(false, w, h)
        camera.update()
        val setting: ImageButton = uiFactory.createButton(settingtex)
        setting.setBounds(0f, Gdx.graphics.height-settingtex.height*scale/2f, settingtex.width * scale / 2, settingtex.height * scale / 2)
        setting.imageCell.expand().fill()
        stage.addActor(setting)


    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
        camera.position.lerp(posCamera, 0.1f)
        camera.update()
        tiledMapRenderer.setView(camera)
        tiledMapRenderer.render()
        //readObjects.draw(camera)
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun hide() {
    }

    override fun dispose() {
        super.dispose()
        stage.dispose()
        settingtex.dispose()
    }
}