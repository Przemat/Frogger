package com.mygdx.frogger.Screen.Loading

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.frogger.Models.ReadObjects
import com.mygdx.frogger.MyGame


class MenuScreen//myGame.setScreen(GameScreen(myGame))
(myGame: MyGame) : Screen {
    var tiledMap: TiledMap? = null
    var camera: OrthographicCamera? = null
    var tiledMapRenderer: OrthogonalTiledMapRenderer? = null
    var posCamera: Vector3 = Vector3(Gdx.graphics.width.toFloat() / 2, Gdx.graphics.height.toFloat() / 2, 0f)
    var scale: Float = Gdx.graphics.width.toFloat() / 720



    val readObjects: ReadObjects

    private var stage: Stage? = null

    init {
        stage = Stage(ScreenViewport())
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        camera = OrthographicCamera()
        camera!!.setToOrtho(false, w, h)
        camera!!.update()

        val path = Gdx.files.internal("maps/menuconf.xml")
        readObjects = ReadObjects(path)

        tiledMap = TmxMapLoader().load("maps/menu.tmx")
        tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap, scale)
        val title = Label("Frogger", myGame.gameSkin)
        title.setAlignment(Align.center)
        title.setY((Gdx.graphics.height * 2 / 3).toFloat())
        title.width = Gdx.graphics.width.toFloat()
        title.setFontScale(Gdx.graphics.width.toFloat() / 100)
        stage!!.addActor(title)
        val playButton = TextButton("Play!", myGame.gameSkin)
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

    /* private fun readXml(): Document {
         val xmlFile = File("maps/menuconf.xml")
         val dbFactory = DocumentBuilderFactory.newInstance()
         val dBuilder = dbFactory.newDocumentBuilder()
         val xmlInput = InputSource(StringReader(xmlFile.readText()))
         val doc = dBuilder.parse(xmlInput)
         doc.documentElement.normalize()
         return doc
     }*/

    override fun hide() {
        TODO("Not yet implemented")
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

        readObjects.draw(camera!!)
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
