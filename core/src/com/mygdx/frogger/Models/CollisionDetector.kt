package com.mygdx.frogger.Models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Intersector.intersectRectangles
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Rectangle
import com.mygdx.frogger.screen_management.util.PlayerController

class CollisionDetector {

    var scale: Float
    var envCollision: Array<Rectangle>
    var intersector = Rectangle(0f, 0f, 0f, 0f)

    constructor(scale: Float, envArray: Array<Rectangle>, camera: Camera) {
        this.scale = scale
        envCollision = envArray
    }

    fun readCollision(others: Array<xml>, player: PlayerController) {
        val playerCollision = Rectangle(player.frogPosition.x, player.frogPosition.y, 48 * scale, 48 * scale)
        for (i in 1..envCollision.size - 1) {
            if (intersectRectangles(playerCollision, envCollision[i], intersector)) {
                player.frogDrown = true
            }
        }
        for (i in 0..others.size - 1) {
            val otherCollision = Rectangle(others[i].pos.x, others[i].pos.y, others[i].animation.getKeyFrame(0f)!!.texture.width.toFloat(), others[i].animation.getKeyFrame(0f)!!.texture.height.toFloat())
            if (intersectRectangles(playerCollision, otherCollision, intersector)) {
                if (others[i].type == type.car) player.frogDead = true
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

        return intersectRectangles(playerCollision, envCollision[0], intersector)
    }

}