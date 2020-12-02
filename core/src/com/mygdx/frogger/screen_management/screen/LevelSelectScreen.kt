package com.mygdx.frogger.screen_management.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.frogger.Models.ReadObjects
import com.mygdx.frogger.config.GameConfig
import com.mygdx.frogger.screen_management.util.ScreenEnum
import com.mygdx.frogger.screen_management.util.UIFactory


class LevelSelectScreen() : AbstractScreen() {
    var tiledMap: TiledMap? = null
    var camera: OrthographicCamera? = null
    var tiledMapRenderer: OrthogonalTiledMapRenderer? = null
    private var txtrBg: Texture
    private var txtrBack: Texture
    private var txtrLevel1: Texture
    private var txtrLevel2: Texture
    private var txtrLevel3: Texture
    private var uiFactory: UIFactory
    val btnLevel1: ImageButton
    var score1Batch: SpriteBatch = SpriteBatch()
    val btnLevel2: ImageButton
    var score2Batch: SpriteBatch = SpriteBatch()
    val btnLevel3: ImageButton
    var score3Batch: SpriteBatch = SpriteBatch()

    val prefs: Preferences
    var font: BitmapFont

    var scale: Float = Gdx.graphics.width.toFloat() / 720
    var posCamera: Vector3 = Vector3(Gdx.graphics.width.toFloat() / 2, Gdx.graphics.height.toFloat() / 2, 0f)

    var gameConfig: GameConfig
    val readObjects: ReadObjects
    private var stage: Stage

    init {
        font = BitmapFont()
        font.data.scale(scale*2)
        prefs = Gdx.app.getPreferences("game preferences")
        stage = Stage(ScreenViewport())
        gameConfig = GameConfig()
        uiFactory = UIFactory()
        camera = OrthographicCamera()
        val path = Gdx.files.internal("maps/menuconf.xml")
        readObjects = ReadObjects(path)
        tiledMap = TmxMapLoader().load("maps/menu.tmx")

        txtrBg = Texture(Gdx.files.internal("gui/selectlevel.png"))
        txtrBack = Texture(Gdx.files.internal("gui/back.png"))
        txtrLevel1 = Texture(Gdx.files.internal("gui/level1.png"))
        txtrLevel2 = Texture(Gdx.files.internal("gui/level2.png"))
        txtrLevel3 = Texture(Gdx.files.internal("gui/level3.png"))

        btnLevel1 = uiFactory.createButton(txtrLevel1)
        btnLevel2 = uiFactory.createButton(txtrLevel2)
        btnLevel3 = uiFactory.createButton(txtrLevel3)
    }

    private fun readHS(level: Int): Int {
        if (prefs.getInteger("highscore" + level.toString())>0)
            return prefs.getInteger("highscore" + level.toString())
        else
            return 0
    }

    override fun show() {
        Gdx.input.setInputProcessor(stage)
    }

    override fun buildStage() {
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        camera!!.setToOrtho(false, w, h)
        camera!!.update()
        tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap, scale)

        // Adding actors
        val bg = Image(txtrBg)
        bg.scaleX = scale
        bg.scaleY = scale
        bg.setY((Gdx.graphics.height * 2 / 3).toFloat())
        bg.setX((Gdx.graphics.width / 2f - bg.width))
        stage!!.addActor(bg)
        val btnBack: ImageButton = uiFactory.createButton(txtrBack)
        btnBack.setBounds(0f,0f,txtrBack.width*scale, txtrBack.height*scale)
        btnBack.imageCell.expand().fill()
        stage!!.addActor(btnBack)
        btnLevel1.setBounds(60f*scale,Gdx.graphics.height*1/2f,btnLevel1.width*scale, btnLevel1.height*scale)
        btnLevel1.imageCell.expand().fill()
        stage!!.addActor(btnLevel1)
        //val btnLevel2: ImageButton = uiFactory.createButton(txtrLevel2)
        btnLevel2.setBounds(280*scale,Gdx.graphics.height*1/2f,btnLevel2.width*scale, btnLevel2.height*scale)
        btnLevel2.imageCell.expand().fill()
        stage!!.addActor(btnLevel2)
        // btnLevel3: ImageButton = uiFactory.createButton(txtrLevel3)
        btnLevel3.setBounds(500*scale,Gdx.graphics.height*1/2f,btnLevel3.width*scale, btnLevel3.height*scale)
        btnLevel3.imageCell.expand().fill()
        stage!!.addActor(btnLevel3)
        btnBack.addListener(uiFactory.createListener(ScreenEnum.MAIN_MENU, 0))
        btnLevel1.addListener(uiFactory.createListener(ScreenEnum.GAME, 1))
        btnLevel2.addListener(uiFactory.createListener(ScreenEnum.GAME, 2))
        btnLevel3.addListener(uiFactory.createListener(ScreenEnum.GAME, 3))
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
        score1Batch.begin()
        font.draw(score1Batch, readHS(1).toString(),btnLevel1.x+btnLevel1.width/2, btnLevel1.y - 10*scale)
        score1Batch.end()

        score2Batch.begin()
        font.draw(score2Batch, readHS(2).toString(),btnLevel2.x+btnLevel2.width/2, btnLevel2.y - 10*scale)
        score2Batch.end()

        score3Batch.begin()
        font.draw(score3Batch, readHS(3).toString(),btnLevel3.x+btnLevel3.width/2, btnLevel3.y - 10*scale)
        score3Batch.end()
        stage!!.act()
        stage!!.draw()
    }

    override fun dispose() {
        super.dispose()
        stage.dispose()
        txtrBg.dispose()
        txtrBack.dispose()
        txtrLevel1.dispose()
        txtrLevel2.dispose()
        txtrLevel3.dispose()
    }

}