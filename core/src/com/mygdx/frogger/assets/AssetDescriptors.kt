package com.mygdx.frogger.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin

object AssetDescriptors {
    val UI_FONT: AssetDescriptor<BitmapFont> = AssetDescriptor<BitmapFont>(AssetPaths.UI_FONT, BitmapFont::class.java)
    val UI_SKIN: AssetDescriptor<Skin> = AssetDescriptor<Skin>(AssetPaths.UI_SKIN, Skin::class.java)
    val GAMEPLAY: AssetDescriptor<TextureAtlas> = AssetDescriptor<TextureAtlas>(AssetPaths.GAMEPLAY, TextureAtlas::class.java)
}

