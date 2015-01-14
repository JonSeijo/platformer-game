package com.jashlaviu.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jashlaviu.platformer.GameLogic;
import com.jashlaviu.platformer.PlatformerGame;
import com.jashlaviu.platformer.actors.ActorJash;

public class TestScreen extends ScreenJash{
	
	private GameLogic gameLogic;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	public TestScreen(PlatformerGame game) {
		super(game);		
		gameLogic = new GameLogic(this);
		batch.setProjectionMatrix(camera.combined);
		
		mapRenderer = new OrthogonalTiledMapRenderer(gameLogic.getMap(), batch);
		mapRenderer.setView(camera);
		
		camera.zoom = 0.35f;
		camera.position.x = 190;
		camera.position.y = 130;
	
		camera.update();		
		stage.setDebugAll(false);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);		
		
		gameLogic.update(delta);		
		stage.act(delta);
		
		//Update camera position
		cameraInput();
		camera.update();		
		mapRenderer.setView(camera);
				
		//Draw map and actors
		batch.setProjectionMatrix(camera.combined);
		mapRenderer.render();			
		stage.draw();		
		
	//	drawDebug();
	}
	
	private void drawDebug(){
		shaper.begin(ShapeType.Line);
		shaper.setProjectionMatrix(batch.getProjectionMatrix());
		shaper.setColor(0, 0, 0, 1);
		
		for(Rectangle r : gameLogic.getMapCollisionBounds())
			shaper.rect(r.x, r.y, r.width, r.height);		
		
		shaper.end();
	}
	
	private void cameraInput(){		
		if(Gdx.input.isKeyPressed(Keys.D))
			camera.position.x += 5;
		
		if(Gdx.input.isKeyPressed(Keys.A))
			camera.position.x -= 5;
		
		if(Gdx.input.isKeyPressed(Keys.W))
			camera.position.y += 5;
		
		if(Gdx.input.isKeyPressed(Keys.S))
			camera.position.y -= 5;
		
		if(Gdx.input.isKeyPressed(Keys.E))
			camera.zoom -= .03f;
		
		if(Gdx.input.isKeyPressed(Keys.Q))
			camera.zoom += .03f;		
		
	}
	
	public void setMapRenderer(TiledMap map){
		mapRenderer.setMap(map);
	}	
	
	public void addActor(ActorJash actor){
		stage.addActor(actor);
	}
	
	@Override
	public void hide() {
		super.hide();
		dispose();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		mapRenderer.dispose();
		gameLogic.dispose();
	}
	
	public Stage getStage(){
		return stage;
	}
	
	public OrthographicCamera getCamera(){
		return camera;
	}
	

}
