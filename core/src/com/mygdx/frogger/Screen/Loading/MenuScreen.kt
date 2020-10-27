package com.mygdx.frogger.Screen.Loading

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector3
import com.mygdx.frogger.MyGame
import java.io.File
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilderFactory


class MenuScreen : Screen {
    var tiledMap: TiledMap? = null
    var camera: OrthographicCamera? = null
    var tiledMapRenderer: TiledMapRenderer? = null
    var posCamera: Vector3 = Vector3(Gdx.graphics.width.toFloat() / 2, Gdx.graphics.height.toFloat() / 2, 0f)
    var scale: Float = Gdx.graphics.width.toFloat() / 720

    var batch: SpriteBatch? = null
    var animals: TextureAtlas? = null
    var vehicles: TextureAtlas? = null
    var assetsConf: Document? = null

    private var stage: Stage? = null

    private fun readObjcets(){
        var xlmFile = File("maps/menuconf.xml")
        assetsConf = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xlmFile)
        assetsConf!!.documentElement.normalize()

        
    }

    private fun moveObjcets(){

    }


    constructor(myGame: MyGame) {
        stage = Stage(ScreenViewport())

        var w = Gdx.graphics.width.toFloat()
        var h = Gdx.graphics.height.toFloat()

        camera = OrthographicCamera()
        camera!!.setToOrtho(false, w, h)
        camera!!.update()

        tiledMap = TmxMapLoader().load("maps/menu.tmx")
        tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap, scale)


        var title = Label("Frogger", myGame.gameSkin)
        title.setAlignment(Align.center)
        title.setY((Gdx.graphics.height * 2 / 3).toFloat())
        title.width = Gdx.graphics.width.toFloat()
        title.setFontScale(Gdx.graphics.width.toFloat() / 100)
        stage!!.addActor(title)

        var playButton = TextButton("Play!", myGame.gameSkin)
        playButton.setSize(Gdx.graphics.width.toFloat() / 2, Gdx.graphics.width.toFloat() / 4)
        playButton.setPosition(Gdx.graphics.width.toFloat() / 2 - playButton.width / 2, Gdx.graphics.height.toFloat() / 2 - playButton.height / 2)
        playButton.label.setFontScale(Gdx.graphics.width.toFloat() / 100)
        playButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                //myGame.setScreen(GameScreen(myGame))
                title.setText("Dziala")
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                title.setText("Frogger")
                return true
            }
        })
        stage!!.addActor(playButton)
    }

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun show() {
        Gdx.input.setInputProcessor(stage)
    }

    private fun cameraPoc() {
        Gdx.app.log("POSY", posCamera.y.toString())
        Gdx.app.log("Y", (Gdx.graphics.height.toFloat() / 2 + scale * 14 * 48).toString())
        if (posCamera.y >= Gdx.graphics.height.toFloat() / 2 + scale * 14 * 48+10){
            posCamera.y = (Gdx.graphics.height.toFloat() / 2) + scale * 48
            camera!!.position.set(posCamera)}
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
        stage!!.act()
        stage!!.draw()
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        stage!!.dispose()
    }

}
