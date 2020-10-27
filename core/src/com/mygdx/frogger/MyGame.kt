package com.mygdx.frogger

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.mygdx.frogger.Screen.Loading.MenuScreen


public class MyGame : Game() {

    var gameSkin: Skin?? = null

    override fun create() {
        gameSkin = Skin(Gdx.files.internal("skin/uiskin.json"))
        setScreen(MenuScreen(this))
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
    }
}
