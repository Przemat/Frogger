package com.mygdx.frogger.Models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Intersector.intersectRectangles
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.mygdx.frogger.screen_management.util.GUI
import com.mygdx.frogger.screen_management.util.PlayerController

class CollisionDetector {

    var scale: Float
    var water: Array<Rectangle>
    var border: Rectangle
    var finish: Array<Rectangle>
    var intersector = Rectangle(0f, 0f, 0f, 0f)

    constructor(scale: Float, water: Array<Rectangle>, border: Rectangle, finish: Array<Rectangle>) {
        this.scale = scale
        this.water = water
        this.border = border
        this.finish = finish
    }

    fun readCollision(others: Array<xml>, player: PlayerController, stateTime: Float?) {
        val playerCollision = Rectangle(player.frogPosition.x, player.frogPosition.y, 48 * scale, 48 * scale)
        var inWater = false
        if (!player.frogJump) {
            for (i in water) {
                if (intersectRectangles(playerCollision, i, intersector)) {
                    inWater = true
                }
            }
            var waterway = 0f;
            for (i in 0..others.size - 1) {


                    if (others[i].type == type.car) {
                        val otherCollision = Rectangle(others[i].pos.x+10*scale, others[i].pos.y, others[i].animation.getKeyFrame(0f)!!.regionWidth * scale-10*scale, 48 * scale)
                        if (intersectRectangles(playerCollision, otherCollision, intersector)) {
                        player.frogDead = true
                        player.deadTime = stateTime?.plus(2f)!!
                        }
                    }
                    else if (others[i].type == type.enemy) {
                        val otherCollision = Rectangle(others[i].pos.x, others[i].pos.y, others[i].animation.getKeyFrame(0f)!!.regionWidth * scale, 48 * scale)
                        if (intersectRectangles(playerCollision, otherCollision, intersector)) {
                            player.frogDead = true
                            player.deadTime = stateTime?.plus(2f)!!
                        }
                    }
                    else if (others[i].type == type.waterway) {
                        val otherCollision = Rectangle(others[i].pos.x, others[i].pos.y, others[i].animation.getKeyFrame(0f)!!.regionWidth * scale, 48 * scale)
                        if (intersectRectangles(playerCollision, otherCollision, intersector)) {
                        if (!player.frogDrown) {
                            inWater = false
                            waterway = others[i].speed
                        }}
                    } else if (others[i].type == type.bonus) {

                    }

            }
            if (inWater && !player.frogDrown) {
                player.frogDrown = true
                player.deadTime = stateTime?.plus(2f)!!
            } else {
                player.waterway = waterway
            }
        }

    }

    fun readBorder(playerpos: Vector3, dir: PlayerDirection): Boolean {
        var x = playerpos.x
        var y = playerpos.y
        if (dir == PlayerDirection.up) {
            y += 48f * scale
        } else if (dir == PlayerDirection.down) {
            y += -48f * scale
        } else if (dir == PlayerDirection.right) {
            x += 48f * scale
        } else if (dir == PlayerDirection.left) {
            x += -48f * scale
        }
        val playerCollision = Rectangle(x, y, 48 * scale, 48 * scale)

        return intersectRectangles(playerCollision, border, intersector)
    }

    fun readFinish(readFinish: Array<Rectangle>, player: PlayerController, array: Array<String?>, gui: GUI, stateTime: Float?) {
        var x = player.frogPosition.x
        var y = player.frogPosition.y
        val playerCollision = Rectangle(x, y, 48 * scale, 48 * scale)
        for (i in 0..readFinish.size - 1) {
            if (intersectRectangles(readFinish[i], playerCollision, intersector)) {
                if (array[i] == null) {
                    player.frogJump = false
                    player.frogPosition = Vector3(7 * 48 * scale, 18 * 48 * scale, 0f)
                    gui.time = 40f
                    array[i] = "frog"
                    player.frogDirection = PlayerDirection.up
                    player.frogDrown = false
                    gui.addScore(50 + (gui.time / 0.5).toInt() * 10)
                } else if (array[i] == "frog") {
                    player.frogDrown = true
                    player.deadTime = stateTime?.plus(2f)!!
                } else if (array[i] == "fly") {
                    player.frogJump = false
                    player.frogPosition = Vector3(7 * 48 * scale, 18 * 48 * scale, 0f)
                    gui.time = 40f
                    array[i] = "frog"
                    player.frogDirection = PlayerDirection.up
                    player.frogDrown = false
                    gui.addScore(200 + 50 + (gui.time / 0.5).toInt() * 10)
                } else if (array[i] == "alligator") {
                    player.frogDead = true
                    player.deadTime = stateTime?.plus(2f)!!
                }


            }
        }
        if (gui.time<=0 && !player.frogDead){
            player.frogDead = true
            player.deadTime = stateTime?.plus(2f)!!
        }

    }

    fun readWater(target: bfrog, others: Array<xml>) {
        var objectCollision = Rectangle(target.pos.x - 48 * scale, target.pos.y, 48 * scale, 48 * scale)
        if (target.speed > 0)
            objectCollision = Rectangle(target.pos.x + 48 * scale, target.pos.y, 48 * scale, 48 * scale)


        for (i in 0..others.size - 1) {
            if (others[i].type == type.waterway) {
                val otherCollision = Rectangle(others[i].pos.x, others[i].pos.y, 48 * scale, 48 * scale)
                if (!intersectRectangles(objectCollision, otherCollision, intersector)) {
                    target.speed = -target.speed
                }
            }

        }
    }

}