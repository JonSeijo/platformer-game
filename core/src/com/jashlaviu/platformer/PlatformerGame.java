package com.jashlaviu.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jashlaviu.platformer.screens.TestScreen;

public class PlatformerGame extends Game {
	private SpriteBatch batch;
	private ShapeRenderer shaper;
	private TextureLoader textureLoader;

	@Override
	public void create() {
		batch = new SpriteBatch();
		shaper = new ShapeRenderer();
		textureLoader = new TextureLoader();
		
		this.setScreen(new TestScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		shaper.dispose();
		textureLoader.dispose();
	}
	
	public SpriteBatch getBatch(){
		return batch;
	}
	
	public ShapeRenderer getShaper(){
		return shaper;
	}
}
