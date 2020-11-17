package com.mygdx.frogger.screen_management.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector3
import com.mygdx.frogger.Models.*

class PlayerController {

    var stateTime: Float
    var scale: Float = Gdx.graphics.width.toFloat() / 720

    //animals
    var frogBatch: SpriteBatch
    var animap = mapOf<String, Animation<TextureRegion?>>()
    var frogDirection: PlayerDirection
    var frogJump: Boolean
    var frogDead: Boolean
    var frogDrown: Boolean
    var frogPosition: Vector3
    var moveint: Float


    init {
        stateTime = 0f
        frogBatch = SpriteBatch()
        readAnim()
        frogDirection = PlayerDirection.up
        frogJump = false
        frogDead = false
        frogDrown = false
        frogPosition = Vector3(7 * 48 * scale, 18 * 48 * scale, 0f)
        moveint = 0f
    }

    private fun readAnim() {
        val textureAtlas = TextureAtlas(Gdx.files.internal("gameassets/animals.atlas"))
        var textureRegion = textureAtlas.findRegions("bfrog_up_stay")

        var animation: Animation<TextureRegion?> = Animation(0.3f, textureRegion)
        animap += Pair("bfrog_up_stay", animation)

        textureRegion = textureAtlas.findRegions("bfrog_down_stay")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("bfrog_down_stay", animation)

        textureRegion = textureAtlas.findRegions("bfrog_left_stay")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("bfrog_left_stay", animation)

        textureRegion = textureAtlas.findRegions("bfrog_right_stay")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("bfrog_right_stay", animation)

        textureRegion = textureAtlas.findRegions("bfrog_up_jump")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("bfrog_up_jump", animation)

        textureRegion = textureAtlas.findRegions("bfrog_down_jump")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("bfrog_down_jump", animation)

        textureRegion = textureAtlas.findRegions("bfrog_left_jump")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("bfrog_left_jump", animation)

        textureRegion = textureAtlas.findRegions("bfrog_right_jump")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("bfrog_right_jump", animation)

        textureRegion = textureAtlas.findRegions("bfrog_down_dead")
        animation = Animation(0f, textureRegion)
        animap += Pair("bfrog_down_dead", animation)

        textureRegion = textureAtlas.findRegions("bfrog_left_dead")
        animation = Animation(0f, textureRegion)
        animap += Pair("bfrog_left_dead", animation)

        textureRegion = textureAtlas.findRegions("bfrog_right_dead")
        animation = Animation(0f, textureRegion)
        animap += Pair("bfrog_right_dead", animation)

        textureRegion = textureAtlas.findRegions("bfrog_up_dead")
        animation = Animation(0f, textureRegion)
        animap += Pair("bfrog_up_dead", animation)

        textureRegion = textureAtlas.findRegions("frog_down_stay")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("frog_down_stay", animation)

        textureRegion = textureAtlas.findRegions("frog_left_stay")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("frog_left_stay", animation)

        textureRegion = textureAtlas.findRegions("frog_right_stay")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("frog_right_stay", animation)

        textureRegion = textureAtlas.findRegions("frog_up_jump")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("frog_up_jump", animation)

        textureRegion = textureAtlas.findRegions("frog_down_jump")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("frog_down_jump", animation)

        textureRegion = textureAtlas.findRegions("frog_left_jump")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("frog_left_jump", animation)

        textureRegion = textureAtlas.findRegions("frog_right_jump")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("frog_right_jump", animation)

        textureRegion = textureAtlas.findRegions("frog_down_dead")
        animation = Animation(0f, textureRegion)
        animap += Pair("frog_down_dead", animation)

        textureRegion = textureAtlas.findRegions("frog_left_dead")
        animation = Animation(0f, textureRegion)
        animap += Pair("frog_left_dead", animation)

        textureRegion = textureAtlas.findRegions("frog_right_dead")
        animation = Animation(0f, textureRegion)
        animap += Pair("frog_right_dead", animation)

        textureRegion = textureAtlas.findRegions("frog_up_dead")
        animation = Animation(0f, textureRegion)
        animap += Pair("frog_right_dead", animation)

        textureRegion = textureAtlas.findRegions("frog_up_stay")
        animation = Animation(0.3f, textureRegion)
        animap += Pair("frog_up_stay", animation)
    }

    fun jump(dir: PlayerDirection) {
        frogDirection = dir
        frogJump = true
    }

    fun move() {
        if (frogDirection == PlayerDirection.up) {
            if (moveint < 48) {
                frogPosition.y += 6 * scale
            } else {
                frogJump = false
            }
            moveint += 6
        } else if (frogDirection == PlayerDirection.down) {
            if (moveint < 48) {
                frogPosition.y -= 6 * scale
            } else {
                frogJump = false
            }
            moveint += 6
        } else if (frogDirection == PlayerDirection.left) {
            if (moveint < 48) {
                frogPosition.x -= 6 * scale
            } else {
                frogJump = false
            }
            moveint += 6
        } else if (frogDirection == PlayerDirection.right) {
            if (moveint < 48) {
                frogPosition.x += 6 * scale
            } else {
                frogJump = false
            }
            moveint += 6
        }
    }

    fun draw(camera: OrthographicCamera, collisionDetector: CollisionDetector) {
        val collisionDetector = collisionDetector
        stateTime = stateTime.plus(Gdx.graphics.deltaTime)
        frogBatch.projectionMatrix = camera.combined
        var currentFrame: TextureRegion? = null
        if (frogDirection == PlayerDirection.up) {
            if (frogDead) {
                currentFrame = animap["frog_up_dead"]?.getKeyFrame(stateTime, true)
            } else if (frogDrown) {
                currentFrame = animap["bubble"]?.getKeyFrame(stateTime, true)
            } else {
                if (frogJump) {
                    currentFrame = animap["frog_up_jump"]?.getKeyFrame(stateTime, true)
                    if (collisionDetector.readBorder(frogPosition, frogDirection))
                        move()
                    else frogJump = false
                } else {
                    currentFrame = animap["frog_up_stay"]?.getKeyFrame(stateTime, true)
                }
            }
        } else if (frogDirection == PlayerDirection.down) {
            if (frogDead) {
                currentFrame = animap["frog_down_dead"]?.getKeyFrame(stateTime, true)
            } else if (frogDrown) {
                currentFrame = animap["bubble"]?.getKeyFrame(stateTime, true)
            } else {
                if (frogJump) {
                    currentFrame = animap["frog_down_jump"]?.getKeyFrame(stateTime, true)
                    if (collisionDetector.readBorder(frogPosition, frogDirection))
                        move()
                    else frogJump = false
                } else {
                    currentFrame = animap["frog_down_stay"]?.getKeyFrame(stateTime, true)
                }
            }
        } else if (frogDirection == PlayerDirection.left) {
            if (frogDead) {
                currentFrame = animap["frog_left_dead"]?.getKeyFrame(stateTime, true)
            } else if (frogDrown) {
                currentFrame = animap["bubble"]?.getKeyFrame(stateTime, true)
            } else {
                if (frogJump) {
                    currentFrame = animap["frog_left_jump"]?.getKeyFrame(stateTime, true)
                    if (collisionDetector.readBorder(frogPosition, frogDirection))
                        move()
                    else frogJump = false
                } else {
                    currentFrame = animap["frog_left_stay"]?.getKeyFrame(stateTime, true)
                }
            }
        } else if (frogDirection == PlayerDirection.right) {
            if (frogDead) {
                currentFrame = animap["frog_right_dead"]?.getKeyFrame(stateTime, true)
            } else if (frogDrown) {
                currentFrame = animap["bubble"]?.getKeyFrame(stateTime, true)
            } else {
                if (frogJump) {
                    currentFrame = animap["frog_right_jump"]?.getKeyFrame(stateTime, true)
                    if (collisionDetector.readBorder(frogPosition, frogDirection))
                        move()
                    else frogJump = false
                } else {
                    currentFrame = animap["frog_right_stay"]?.getKeyFrame(stateTime, true)
                }
            }
        }

        frogBatch.begin()
        frogBatch.draw(currentFrame!!.texture, frogPosition.x, frogPosition.y, currentFrame.regionWidth * scale, currentFrame.regionHeight * scale, currentFrame.regionX, currentFrame.regionY, currentFrame.regionWidth, currentFrame.regionHeight, false, false)
        frogBatch.end()

    }
}