package com.mygdx.frogger.Models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.XmlReader
import java.lang.Enum

class ReadObjects(level: FileHandle) {
    var level = level
    var scale: Float = Gdx.graphics.width.toFloat() / 720
    //cars
    var carAtlas: TextureAtlas
    val objectsList: com.badlogic.gdx.utils.Array<XmlReader.Element>?
    var objects: MutableList<xml> = arrayListOf()
    //animals
    var textureAtlas: TextureAtlas

    init {
        val xmlList = readXml()
        objectsList = xmlList
        carAtlas = TextureAtlas(Gdx.files.internal("gameassets/cars.atlas"))
        textureAtlas = TextureAtlas(Gdx.files.internal("gameassets/animals.atlas"))
    }
    private fun readXml(): com.badlogic.gdx.utils.Array<XmlReader.Element>? {
        //
        val reader = XmlReader()
        val xmlroot = reader.parse(level)
        val objects = xmlroot.getChildrenByName("object")
        return objects
    }
    @JvmName("getObjects1")
    fun getObjects(): Array<xml> {
        for (item in objectsList!!){
                val elem = item
                val atlas: TextureAtlas
                if(elem.getChildByName("type").text == "car")atlas = carAtlas
                else atlas = textureAtlas

                for (j in 0..elem.getChildByName("count").text.toInt()-1) {
                    val mMap = xml(SpriteBatch())
                    mMap.type = Enum.valueOf<type>(type::class.java, elem.getChildByName("type").text)
                    val textureRegion = atlas.findRegion(elem.getChildByName("model").text)
                    mMap.animation= Animation(elem.getChildByName("animation").text.toFloat(), textureRegion)
                    mMap.speed = elem.getChildByName("speed").text.toFloat()
                    val currentFrame = mMap.animation.getKeyFrame(0f, true)
                    if(mMap.speed>0){
                        mMap.pos = Vector2(j*(currentFrame!!.regionWidth*scale+48*scale*elem.getChildByName("space").text.toFloat()), elem.getChildByName("position").text.toInt()*48*scale)
                    }else{
                        val endOfScreen = 14*48*scale
                        mMap.pos = Vector2(endOfScreen-(currentFrame!!.regionWidth*scale+48*scale*elem.getChildByName("space").text.toFloat()), elem.getChildByName("position").text.toInt()*48*scale)
                    }
                    objects.add(mMap)
                }




            }
        return objects.toTypedArray()
        }
    }

