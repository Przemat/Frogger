package com.mygdx.frogger.screen_management.util

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.mygdx.frogger.screen_management.util.ScreenManager


class UIFactory {
    fun createButton(texture: Texture?): ImageButton {
        return ImageButton(
                TextureRegionDrawable(
                        TextureRegion(texture)))
    }

    fun<T> createListener(dstScreen: ScreenEnum?, vararg params: T?): InputListener? {
        return object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                ScreenManager.instance.showScreen(dstScreen!!, *params)
                return false
            }
        }
    }
}