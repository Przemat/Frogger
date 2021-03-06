package com.mygdx.frogger.screen_management.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align


class GUI(level: Int) {
    var time: Float
    var scale: Float = Gdx.graphics.width.toFloat() / 720
    var score: Int
    var lifes: Int
    var highScore: Int
    val prefs: Preferences
    var font: BitmapFont
    var level: Int

    var scoreBatch: SpriteBatch
    var scoreLayout: GlyphLayout


    var highscoreLayout: GlyphLayout


    var timeLayout: GlyphLayout
    var lifeLayout: GlyphLayout

    init {
        time = 40f
        score = 0
        lifes = 3
        this.level = level
        font = BitmapFont()
        font.data.scale(scale*2)
        prefs = Gdx.app.getPreferences("game preferences")
        highScore = readHS(level)
        scoreBatch = SpriteBatch()
        scoreLayout = GlyphLayout()
        highscoreLayout = GlyphLayout()
        timeLayout = GlyphLayout()
        lifeLayout = GlyphLayout()
    }

    private fun readHS(level: Int): Int {
        if (prefs.getInteger("highscore" + level.toString())>0)
            return prefs.getInteger("highscore" + level.toString())
        else
            return 0
    }

    fun addScore(add: Int){
        score+=add
        if (score>highScore){
            highScore = score
            prefs.putInteger("highscore"+level.toString(), highScore)
            prefs.flush()
            Gdx.app.log("score", prefs.getInteger("highscore" + level.toString()).toString())
        }
    }

    fun draw(){
        time-=Gdx.graphics.deltaTime
        scoreBatch.begin();
        scoreLayout.setText(font, "Wynik\n"+score, Color.WHITE, 0f, Align.center, true)
        font.draw(scoreBatch, scoreLayout, 160f*scale , Gdx.graphics.height.toFloat())

        highscoreLayout.setText(font, "Rekord\n"+score, Color.WHITE, 0f, Align.center, true)
            font.draw(scoreBatch, highscoreLayout, 320f*scale , Gdx.graphics.height.toFloat())

        timeLayout.setText(font, "Czas\n"+time.toInt(), Color.WHITE, 0f, Align.center, true)
        font.draw(scoreBatch, timeLayout, 480f*scale , Gdx.graphics.height.toFloat())

        timeLayout.setText(font, "Zycia\n"+lifes, Color.WHITE, 0f, Align.center, true)
        font.draw(scoreBatch, timeLayout, 640f*scale , Gdx.graphics.height.toFloat())

        scoreBatch.end();


    }


}