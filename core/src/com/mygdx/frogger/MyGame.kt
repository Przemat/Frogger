package com.mygdx.frogger

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.mygdx.frogger.screen_management.util.ScreenEnum
import com.mygdx.frogger.screen_management.util.ScreenManager


public class MyGame : Game() {



    override fun create() {

        ScreenManager.instance.initialize(this)
        ScreenManager.instance.showScreen(ScreenEnum.MAIN_MENU, 1)

        //setScreen(MenuScreen(this))
    }
}
