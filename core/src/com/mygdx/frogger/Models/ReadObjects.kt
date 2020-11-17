package com.mygdx.frogger.Models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.XmlReader
import java.lang.Enum

class ReadObjects(level: FileHandle) {
    var stateTime: Float? = null

    var level = level
    var scale: Float = Gdx.graphics.width.toFloat() / 720

    //cars
    var carAtlas: TextureAtlas
    val readList: com.badlogic.gdx.utils.Array<XmlReader.Element>?
    var objectList: Array<xml>

    //animals
    var textureAtlas: TextureAtlas

    init {
        stateTime = 0f
        readList = readXml()
        carAtlas = TextureAtlas(Gdx.files.internal("gameassets/cars.atlas"))
        textureAtlas = TextureAtlas(Gdx.files.internal("gameassets/animals.atlas"))
        objectList = getObjects()
    }

    private fun readXml(): com.badlogic.gdx.utils.Array<XmlReader.Element>? {
        //
        val reader = XmlReader()
        val xmlroot = reader.parse(level)
        val objects = xmlroot.getChildrenByName("position")
        return objects
    }
    fun readEnv(): Array<Rectangle> {
        val reader = XmlReader()
        val xmlroot = reader.parse(level)
        val objects = xmlroot.getChildrenByName("environment")
        var envArray = mutableListOf<Rectangle>()
        for (item in objects) {
            val elem = item
            Gdx.app.log("size", elem.childCount.toString())
            for (j in 0..elem.childCount - 1) {
                val curobj = elem.getChild(j)
                var rectangle: Rectangle
                rectangle = Rectangle(0f,curobj.getIntAttribute("down").toFloat()*48*scale, Gdx.graphics.width.toFloat(),
                        curobj.getIntAttribute("height").toFloat()*48*scale)
                    envArray.add(rectangle)
                }
        }
        return envArray.toTypedArray()
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

    @JvmName("getObjectList1")
    fun getObjectList(): Array<xml> {
        return objectList
    }
}

