package com.mygdx.frogger.Models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.XmlReader
import com.mygdx.frogger.screen_management.util.*
import java.lang.Enum
import kotlin.random.Random

class ReadObjects(level: FileHandle) {
    var stateTime: Float? = null

    var level = level
    var scale: Float = Gdx.graphics.width.toFloat() / 720

    //cars
    var carAtlas: TextureAtlas
    val readList: com.badlogic.gdx.utils.Array<XmlReader.Element>?
    var objectList: Array<xml>
    lateinit var waterobjList: Array<bfrog>
    lateinit var border: Rectangle

    //animals
    var textureAtlas: TextureAtlas
    lateinit var envitems: XmlReader.Element
    val finishSB: Array<SpriteBatch> = arrayOf(SpriteBatch(), SpriteBatch(), SpriteBatch(), SpriteBatch(), SpriteBatch())
    lateinit var finishPos: Array<Rectangle>

    init {
        stateTime = 0f
        readList = readXml()
        carAtlas = TextureAtlas(Gdx.files.internal("gameassets/cars.atlas"))
        textureAtlas = TextureAtlas(Gdx.files.internal("gameassets/animals.atlas"))
        objectList = getObjects()
    }

    fun readGame(){
        envitems = readEnv()
        waterobjList = readWobj()
        finishPos = readFinish()
        border = readBorder()
    }

    private fun readXml(): com.badlogic.gdx.utils.Array<XmlReader.Element>? {
        //
        val reader = XmlReader()
        val xmlroot = reader.parse(level)
        val objects = xmlroot.getChildrenByName("position")
        return objects
    }

    fun readEnv(): XmlReader.Element {
        val reader = XmlReader()
        val xmlroot = reader.parse(level)
        val objects = xmlroot.getChildByName("environment")
        return objects
    }

    fun readBorder(): Rectangle {
        val curobj = envitems.getChildByName("border")
        val rectangle = Rectangle(0f, curobj.getIntAttribute("down").toFloat() * 48 * scale, Gdx.graphics.width.toFloat(),
                curobj.getIntAttribute("height").toFloat() * 48 * scale)
        return rectangle
    }

    fun readWater(): Array<Rectangle> {
        val objects = envitems.getChildrenByName("water")
        var waterArray = mutableListOf<Rectangle>()
        for (item in objects) {
            val curobj = item
            val rectangle = Rectangle(-96 * scale, curobj.getIntAttribute("down").toFloat() * 48 * scale, Gdx.graphics.width.toFloat() + 48 * 4 * scale,
                    curobj.getIntAttribute("height").toFloat() * 48 * scale)
            waterArray.add(rectangle)
        }
        return waterArray.toTypedArray()

    }

    fun readFinish(): Array<Rectangle> {
        val objects = envitems.getChildByName("finish")
        var finishArray = mutableListOf<Rectangle>()
        for (i in 1..14 step 3) {
            val rectangle = Rectangle(i * 48 * scale, objects.getIntAttribute("position").toFloat() * 48 * scale, 48 * scale,
                    48 * scale)
            finishArray.add(rectangle)
        }
        return finishArray.toTypedArray()
    }

    fun readWobj(): Array<bfrog>{
        var objects = envitems.getChildrenByName("frog")
        var finishArray = mutableListOf<bfrog>()
        for (item in objects) {
            val curobj = item
            val mMap = bfrog(SpriteBatch())
            mMap.type = type.bonus
            mMap.pos = Vector2(scale * 48 * curobj.getFloatAttribute("x"), scale * 48 * curobj.getFloatAttribute("y"))
            mMap.speed = curobj.getFloatAttribute("waterspeed")
            finishArray.add(mMap)
        }
        objects = envitems.getChildrenByName("watersnake")
        for (item in objects) {
            val curobj = item
            val mMap = bfrog(SpriteBatch())
            mMap.type = type.enemy
            mMap.pos = Vector2(scale * 48 * curobj.getFloatAttribute("x"), scale * 48 * curobj.getFloatAttribute("y"))
            mMap.speed = curobj.getFloatAttribute("waterspeed")
            finishArray.add(mMap)
        }
        return finishArray.toTypedArray()
    }



    @JvmName("getObjects1")
    fun getObjects(): Array<xml> {
        val objects: MutableList<xml> = arrayListOf()
        for (item in readList!!) {
            val elem = item
            var atlas: TextureAtlas
            val pos = item.getFloatAttribute("id")
            for (j in 0..elem.childCount - 1) {
                val curobj = elem.getChild(j)
                if (curobj.getAttribute("type") == "car") {
                    atlas = carAtlas
                    val mMap = xml(SpriteBatch())
                    mMap.type = Enum.valueOf<type>(type::class.java, curobj.getAttribute("type"))
                    val textureRegion = atlas.findRegions(curobj.getAttribute("model"))
                    mMap.animation = Animation(curobj.getFloatAttribute("animation"), textureRegion)
                    mMap.speed = curobj.getFloatAttribute("speed")
                    mMap.pos = Vector2(scale * 48 * curobj.getFloatAttribute("position"), pos * 48 * scale)
                    objects.add(mMap)
                } else if (curobj.getAttribute("type") == "waterway") {
                    atlas = textureAtlas
                    val mMap = xml(SpriteBatch())
                    mMap.type = Enum.valueOf<type>(type::class.java, curobj.getAttribute("type"))
                    val textureRegion = atlas.findRegions(curobj.getAttribute("model"))
                    mMap.animation = Animation(curobj.getFloatAttribute("animation"), textureRegion)
                    mMap.speed = curobj.getFloatAttribute("speed")
                    mMap.pos = Vector2(scale * 48 * curobj.getFloatAttribute("position"), pos * 48 * scale)
                    objects.add(mMap)

                } else if (curobj.getAttribute("type") == "bonus") {
                    atlas = textureAtlas

                } else {
                    atlas = textureAtlas
                    val mMap = xml(SpriteBatch())
                    mMap.type = Enum.valueOf<type>(type::class.java, curobj.getAttribute("type"))
                    val textureRegion = atlas.findRegions(curobj.getAttribute("model"))
                    mMap.animation = Animation(curobj.getFloatAttribute("animation"), textureRegion)
                    mMap.speed = curobj.getFloatAttribute("speed")
                    mMap.pos = Vector2(scale * 48 * curobj.getFloatAttribute("position"), pos * 48 * scale)
                    objects.add(mMap)
                }
            }
        }
        return objects.toTypedArray()
    }

    fun drawback(camera: OrthographicCamera) {
        stateTime = stateTime?.plus(Gdx.graphics.deltaTime)

        for (i in 0..objectList.size - 1) {
            objectList[i].sb.projectionMatrix = camera!!.combined
            if (objectList[i].type === type.waterway) {
                val currentFrame = objectList[i].animation.getKeyFrame(stateTime!!, true)
                if (objectList[i].speed < 0f) {
                    if (objectList[i].pos.x >= -currentFrame!!.regionWidth * scale - 10)
                        objectList[i].pos.x += 100f * objectList[i].speed * Gdx.graphics.deltaTime
                    else
                        objectList[i].pos.x = 48 * 15 * scale + currentFrame!!.regionWidth * scale
                } else {
                    if (objectList[i].pos.x <= 15 * 48 * scale + currentFrame!!.regionWidth * scale + 10)
                        objectList[i].pos.x += 100f * objectList[i].speed * Gdx.graphics.deltaTime
                    else
                        objectList[i].pos.x = -currentFrame!!.regionWidth * scale
                }
                objectList[i].sb.begin()
                objectList[i].sb.draw(currentFrame, objectList[i].pos.x, objectList[i].pos.y, currentFrame!!.regionWidth * scale, currentFrame!!.regionHeight * scale)
                objectList[i].sb.end()
            } else if (objectList[i].type === type.bonus) {
            } else if (objectList[i].type === type.enemy) {
                var flip = false
                val currentFrame = objectList[i].animation.getKeyFrame(stateTime!!, true)
                if (objectList[i].speed < 0f) {
                    flip = true
                    if (objectList[i].pos.x >= -currentFrame!!.regionWidth * scale - 10)
                        objectList[i].pos.x += 100f * objectList[i].speed * Gdx.graphics.deltaTime
                    else
                        objectList[i].pos.x = 48 * 15 * scale + currentFrame!!.regionWidth * scale
                } else {
                    if (objectList[i].pos.x <= 15 * 48 * scale + currentFrame!!.regionWidth * scale + 10)
                        objectList[i].pos.x += 100f * objectList[i].speed * Gdx.graphics.deltaTime
                    else
                        objectList[i].pos.x = -currentFrame!!.regionWidth * scale
                }
                objectList[i].sb.begin()
                objectList[i].sb.draw(currentFrame!!.texture, objectList[i].pos.x, objectList[i].pos.y, currentFrame!!.regionWidth * scale, currentFrame!!.regionHeight * scale, currentFrame!!.regionX, currentFrame!!.regionY, currentFrame!!.regionWidth, currentFrame!!.regionHeight, flip, false)
                objectList[i].sb.end()
            }
        }
    }

    fun drawfront(camera: OrthographicCamera) {
        stateTime = stateTime?.plus(Gdx.graphics.deltaTime)

        for (i in 0..objectList.size - 1) {
            objectList[i].sb.projectionMatrix = camera.combined
            if (objectList[i].type == type.car) {
                var flip = false
                val currentFrame = objectList[i].animation.getKeyFrame(stateTime!!, true)

                if (objectList[i].speed < 0f) {
                    if (objectList[i].pos.x >= -currentFrame!!.regionWidth * scale - 10)
                        objectList[i].pos.x += 100f * objectList[i].speed * Gdx.graphics.deltaTime
                    else
                        objectList[i].pos.x = 48 * 15 * scale
                } else {
                    flip = true
                    if (objectList[i].pos.x <= 15 * 48 * scale + 10)
                        objectList[i].pos.x += 100f * objectList[i].speed * Gdx.graphics.deltaTime
                    else
                        objectList[i].pos.x = -currentFrame!!.regionWidth * scale
                }
                objectList[i].sb.begin()
                objectList[i].sb.draw(currentFrame!!.texture, objectList[i].pos.x, objectList[i].pos.y, currentFrame!!.regionWidth * scale, currentFrame!!.regionHeight * scale, currentFrame!!.regionX, currentFrame!!.regionY, currentFrame!!.regionWidth, currentFrame!!.regionHeight, flip, false)
                objectList[i].sb.end()
            }
        }
    }

    fun drawWater(camera: OrthographicCamera){
        stateTime = stateTime?.plus(Gdx.graphics.deltaTime)

        for (i in 0..waterobjList.size - 1) {
            waterobjList[i].sb.projectionMatrix = camera.combined
            if (waterobjList[i].type == type.car) {

                var flip = false
                val currentFrame = objectList[i].animation.getKeyFrame(stateTime!!, true)

                if (waterobjList[i].speed < 0f) {
                    if (waterobjList[i].pos.x >= -currentFrame!!.regionWidth * scale - 10)
                        waterobjList[i].pos.x += 100f * objectList[i].speed * Gdx.graphics.deltaTime
                    else
                        waterobjList[i].pos.x = 48 * 15 * scale
                } else {
                    flip = true
                    if (waterobjList[i].pos.x <= 15 * 48 * scale + 10)
                        waterobjList[i].pos.x += 100f * objectList[i].speed * Gdx.graphics.deltaTime
                    else
                        waterobjList[i].pos.x = -currentFrame!!.regionWidth * scale
                }
                waterobjList[i].sb.begin()
                waterobjList[i].sb.draw(currentFrame!!.texture, waterobjList[i].pos.x, waterobjList[i].pos.y, currentFrame!!.regionWidth * scale, currentFrame!!.regionHeight * scale, currentFrame!!.regionX, currentFrame!!.regionY, currentFrame!!.regionWidth, currentFrame!!.regionHeight, flip, false)
                waterobjList[i].sb.end()
            }
        }
    }


    @JvmName("getObjectList1")
    fun getObjectList(): Array<xml> {
        return objectList
    }

    fun drawFinish(camera: OrthographicCamera, finish: Array<String?>) {
        stateTime = stateTime?.plus(Gdx.graphics.deltaTime)

        for (i in 0..finishSB.size - 1) {
            finishSB[i].projectionMatrix = camera.combined
            if (finish[i] == "frog") {
                val texture = textureAtlas.findRegion("frog_finish")

                finishSB[i].begin()
                finishSB[i].draw(texture.texture, finishPos[i].x, finishPos[i].y, texture.regionWidth * scale, texture.regionHeight * scale, texture.regionX, texture.regionY, texture.regionWidth, texture.regionHeight, false, false)
                finishSB[i].end()
            }
            else if (finish[i] == "alligator"){
                val texture = textureAtlas.findRegion("Alligator_head")
                finishSB[i].begin()
                finishSB[i].draw(texture.texture, finishPos[i].x, finishPos[i].y, texture.regionWidth * scale, texture.regionHeight * scale, texture.regionX, texture.regionY, texture.regionWidth, texture.regionHeight, false, false)
                finishSB[i].end()
            }
            else if (finish[i] == "fly"){
                val texture = textureAtlas.findRegions("fly")
                val animation = Animation(0.5f, texture)
                var currentFrame = animation.getKeyFrame(stateTime!!, true)

                finishSB[i].begin()
                finishSB[i].draw(currentFrame!!.texture, finishPos[i].x, finishPos[i].y, currentFrame!!.regionWidth * scale, currentFrame!!.regionHeight * scale, currentFrame!!.regionX, currentFrame!!.regionY, currentFrame!!.regionWidth, currentFrame!!.regionHeight, false, false)
                finishSB[i].end()
            }
        }
    }

    fun finishObj(finish: Array<String?>, finishTime: Array<Float?>) {

        val place = Random.nextInt(0,5)
        val random = Random.nextInt(0,1000)

            if (finish[place] == null){
                if (random == 0 && "fly" !in finish){
                    finish[place] = "fly"
                    finishTime[place] = stateTime?.plus(100f)
                    Gdx.app.log("cur", stateTime.toString())
                    Gdx.app.log("end", finishTime[place].toString())
                }else if( random == 9 && "alligator" !in finish){
                    finish[place] = "alligator"
                    finishTime[place] = stateTime?.plus(10f)
                }

            }
        for (i in 0..finish.size -1){
            if (finishTime[i] != null)
            if (finishTime[i]!! <= stateTime!! && finish[i] != "frog"){
                finish[i] = null
            }
        }
    }

    fun endReader(player: PlayerController, finish: Array<String?>, gui: GUI) {
        if ((player.frogPosition.x> border.x+border.width || player.frogPosition.x < border.x)&& player.frogDead==false){
            player.frogDead = true
            player.deadTime = stateTime?.plus(2f)!!
        }
        if ((player.frogDead || player.frogDrown) && stateTime!! > player.deadTime){
            player.frogPosition = Vector3(7 * 48 * scale, 18 * 48 * scale, 0f)
            player.frogJump = false
            player.frogDead = false
            player.frogDrown = false
            gui.time = 40f
            player.frogDirection = PlayerDirection.up
            gui.lifes-=1
            if(gui.lifes<=0){
                ScreenManager.instance.showScreen(ScreenEnum.MAIN_MENU, 0)
            }
        }
        if (finish[0] == "frog" && finish[1] == "frog" && finish[2] == "frog" && finish[3] == "frog" && finish[4] == "frog" ){
            ScreenManager.instance.showScreen(ScreenEnum.GAME, 2)
        }
    }
}

