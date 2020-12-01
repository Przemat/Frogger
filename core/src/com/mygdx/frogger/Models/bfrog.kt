package com.mygdx.frogger.Models

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import kotlin.properties.Delegates

class bfrog (val spriteBatch: SpriteBatch) {
    var sb: SpriteBatch = spriteBatch
    lateinit var type: type
    lateinit var pos: Vector2
    var speed by Delegates.notNull<Float>()
}