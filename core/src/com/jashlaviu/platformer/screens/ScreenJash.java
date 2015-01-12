package com.jashlaviu.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jashlaviu.platformer.PlatformerGame;

public class ScreenJash extends ScreenAdapter{
	
	protected PlatformerGame game;
	protected SpriteBatch batch;
	protected ShapeRenderer shaper;
	protected OrthographicCamera camera;
	
	protected Stage stage;
	
	public ScreenJash(PlatformerGame game) {
		this.game = game;
		this.batch = game.getBatch();
		this.shaper = game.getShaper();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(147.f/255.f, 214.f/255.f, 250.f/255.f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

}
