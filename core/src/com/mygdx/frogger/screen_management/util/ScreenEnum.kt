package com.mygdx.frogger.screen_management.util

import com.mygdx.frogger.screen_management.screen.AbstractScreen
import com.mygdx.frogger.screen_management.screen.GameScreen
import com.mygdx.frogger.screen_management.screen.LevelSelectScreen
import com.mygdx.frogger.screen_management.screen.MainMenuScreen

enum class ScreenEnum {
    MAIN_MENU{
        override fun<T> getScreen(vararg params: T): AbstractScreen {
            return MainMenuScreen()
        }
    },
    LEVEL_SELECT {
        override fun<T> getScreen(vararg params: T): AbstractScreen {
            return LevelSelectScreen()
        }
    },
    GAME {
        override fun<T> getScreen(vararg params: T): AbstractScreen {
            return GameScreen(params[0] as Int)
        }
    };

    abstract fun<T> getScreen(vararg params: T): AbstractScreen
}