package com.mygdx.frogger.Models

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import kotlin.properties.Delegates

data class bfrog (val spriteBatch: SpriteBatch) {
    var sb: SpriteBatch = spriteBatch
    var type: type? = null
    lateinit var pos: Vector2
    lateinit var dir: PlayerDirection
    var stay by Delegates.notNull<Float>()
    var speed by Delegates.notNull<Float>()
}