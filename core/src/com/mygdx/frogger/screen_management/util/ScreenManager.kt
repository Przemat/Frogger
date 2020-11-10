package com.mygdx.frogger.screen_management.util

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen;
import com.mygdx.frogger.screen_management.screen.AbstractScreen;

class ScreenManager private constructor(){

    // Reference to game
    private var game: Game? = null

    // Singleton: retrieve instance
    private object HOLDER {
        val INSTANCE = ScreenManager()
    }

    companion object{
        val instance: ScreenManager by lazy { HOLDER.INSTANCE}
    }

    // Initialization with the game class
    fun initialize(game: Game?) {
        this.game = game
    }

    // Show in the game the screen which enum type is received
    open fun<T> showScreen(screenEnum: ScreenEnum, vararg params: T) {

        // Get current screen to dispose it
        val currentScreen = game!!.screen

        // Show new screen
        val newScreen: AbstractScreen? = screenEnum.getScreen(*params)
        newScreen!!.buildStage()
        game!!.screen = newScreen

        // Dispose previous screen
        currentScreen?.dispose()
    }
}