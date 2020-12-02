package com.mygdx.frogger.screen_management.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.frogger.Models.CollisionDetector
import com.mygdx.frogger.Models.PlayerDirection
import com.mygdx.frogger.Models.ReadObjects
import com.mygdx.frogger.config.GameConfig
import com.mygdx.frogger.screen_management.util.GUI
import com.mygdx.frogger.screen_management.util.PlayerController
import com.mygdx.frogger.screen_management.util.ScreenEnum
import com.mygdx.frogger.screen_management.util.UIFactory

class GameScreen(level: Int) : AbstractScreen(), GestureDetector.GestureListener {

    val level = level
    var tiledMap: TiledMap
    var camera: OrthographicCamera
    var tiledMapRenderer: OrthogonalTiledMapRenderer
    var GUI: GUI
    var posCamera: Vector3
    var scale: Float = Gdx.graphics.width.toFloat() / 720
    var settingtex: Texture
    var uiFactory: UIFactory
    var gestureDetector: GestureDetector

    var gameConfig: GameConfig
    val readObjects: ReadObjects
    var collisionDetector: CollisionDetector
    private var stage: Stage
    val setting: ImageButton
    var finish = arrayOfNulls<String>(5)
    var finishTime = arrayOfNulls<Float>(5)

    //touch
    var start_pos = Vector3(0f, 0f, 0f)
    var player: PlayerController


    init {
        GUI = GUI(level)
        gameConfig = GameConfig()
        uiFactory = UIFactory()
        stage = Stage(ScreenViewport())
        camera = OrthographicCamera()
        settingtex = Texture(Gdx.files.internal("gui/setting.png"))
        setting = uiFactory.createButton(settingtex)
        setting.addListener(uiFactory.createListener(ScreenEnum.MAIN_MENU, 0))
        tiledMap = TmxMapLoader().load("maps/" + level + ".tmx")
        tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap, scale)
        val prop = tiledMap.properties
        val mapHeight: Int = prop.get("height", Int::class.java)
        val tilePixelHeight: Int = prop.get("tileheight", Int::class.java)
        val gap = Gdx.graphics.height - mapHeight * tilePixelHeight * scale
        posCamera = Vector3(Gdx.graphics.width.toFloat() / 2, Gdx.graphics.height.toFloat() / 2 - gap + 104 * scale, 0f)
        val path = Gdx.files.internal("maps/"+level+"conf.xml")
        readObjects = ReadObjects(path)
        readObjects.readGame()

        gestureDetector = GestureDetector(this)
        player = PlayerController(GUI)
        collisionDetector = CollisionDetector(scale, readObjects.readWater(), readObjects.readBorder(), readObjects.readFinish())
        collisionDetector.readCollision(readObjects.getObjects(), player, readObjects.stateTime)
    }

    override fun show() {
        Gdx.input.inputProcessor = gestureDetector

    }

    override fun buildStage() {
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat() - 48 * scale
        camera.setToOrtho(false, w, h)
        camera.update()
        setting.setBounds(0f, Gdx.graphics.height - settingtex.height * scale / 2f, settingtex.width * scale / 2, settingtex.height * scale / 2)
        setting.imageCell.expand().fill()
        stage.addActor(setting)


    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
        camera.position.lerp(posCamera, 0.1f)
        camera.update()

        tiledMapRenderer.setView(camera)
        tiledMapRenderer.render()
        collisionDetector.readCollision(readObjects.getObjectList(), player, readObjects.stateTime)
        readObjects.finishObj(finish, finishTime)
        collisionDetector.readFinish(readObjects.readFinish(), player, finish, GUI, readObjects.stateTime)
        readObjects.drawback(camera)
        player.watermove()
        player.draw(camera, collisionDetector)
        readObjects.drawfront(camera)
        readObjects.drawWater(camera)
        readObjects.drawFinish(camera, finish)
        GUI.draw()
        readObjects.endReader(player, finish, GUI)
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

    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        start_pos = Vector3(x,y,0f)
        return true
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        return false
    }

    override fun longPress(x: Float, y: Float): Boolean {
        return false
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        return false
    }

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        return false
    }

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        val touch_delta = Vector3(start_pos.x - x, start_pos.y - y, 0f)

        if (touch_delta.x > 250 * scale && touch_delta.y < 150 * scale && touch_delta.y > -150* scale) {
            if (!player.frogDead) {
                player.jump(PlayerDirection.left)
                player.moveint = 0f
            }
        }
        if (touch_delta.x < -250 * scale && touch_delta.y < 150 * scale && touch_delta.y > -150* scale) {
            if (!player.frogDead) {player.jump(PlayerDirection.right)
            player.moveint = 0f}
        }
        if (touch_delta.x < 150 * scale && touch_delta.x > -150* scale && touch_delta.y > 250 * scale) {
            if (!player.frogDead) {player.jump(PlayerDirection.up)
            player.moveint = 0f}
        }
        if (touch_delta.x < 150 * scale && touch_delta.x > -150* scale && touch_delta.y < -250 * scale) {
            if (!player.frogDead) {player.jump(PlayerDirection.down)
            player.moveint = 0f}
        }
        return true
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        return false
    }

    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
        return false
    }

    override fun pinchStop() {
    }
}