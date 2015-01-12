package com.jashlaviu.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.jashlaviu.platformer.actors.ActorJash;
import com.jashlaviu.platformer.actors.ActorJash.State;
import com.jashlaviu.platformer.actors.Player;
import com.jashlaviu.platformer.screens.TestScreen;

public class GameLogic {	
	private Array<Rectangle> mapCollisionBounds;
	private TestScreen gameScreen;
	private TiledMap map;
	
	private Stage stage;		
	private Player player;	
	
	private int level;
	
	public GameLogic(TestScreen gameScreen) {
		this.gameScreen = gameScreen;	
		mapCollisionBounds = new Array<Rectangle>();
		level = 1;		
		loadLevel(level);		
		
		stage = gameScreen.getStage();
		
		player = new Player(100,300);
		stage.addActor(player);
	}
	
	public void update(float delta){
		handleInput(delta);
		
		for(Actor actor : stage.getActors()){
			ActorJash a = (ActorJash)actor;
			Rectangle aBounds = a.getCollisionBounds();
			
			//Updates every actor X position
			a.updateX(delta);			
			handleXcollision(a, aBounds);	
			
			//Updates every actor Y position
			a.updateY(delta);			
			handleYcollision(a, aBounds);	
			
			a.updateRegion(delta);
		}
		
		//System.out.println("vel x: " + player.getVelocity().x);
		gameScreen.getCamera().position.x = MathUtils.round(10f * (player.getX()+20)) / 10f;
		
	}

	private void handleYcollision(ActorJash a, Rectangle aBounds) {
		boolean air = true;
		for(Rectangle mapBounds : mapCollisionBounds){
			if(aBounds.overlaps(mapBounds)){
				if(a.getVelocity().y > 0){
					a.setY(mapBounds.y - aBounds.height - a.getBoundsDistanceY());
					a.getVelocity().y = 0;
				}
				
				if(a.getVelocity().y < 0){
					a.setY(mapBounds.y + mapBounds.height - a.getBoundsDistanceY() );
					a.getVelocity().y = 0;
					air = false;
					a.setState(State.WALKING);
				}				
			}				
		}

	}

	private void handleXcollision(ActorJash a, Rectangle aBounds) {
		for(Rectangle mapBounds : mapCollisionBounds){
			if(aBounds.overlaps(mapBounds)){
				if(a.getVelocity().x > 0){
					a.setX(mapBounds.x - aBounds.width - a.getBoundsDistanceX());
					a.getVelocity().x = 0;
				}
				
				if(a.getVelocity().x < 0){
					a.setX(mapBounds.x + mapBounds.width - a.getBoundsDistanceX());
					a.getVelocity().x = 0;
				}					
			}
		}
	}
	
	public void handleInput(float delta){
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
			player.moveLeft();		
		}else{
			player.setMovingLeft(false);
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			player.moveRight();
		}else{
			player.setMovingRight(false);
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
			player.jump();	
		}
		
		
	}
	
	public void loadLevel(int level){
		mapCollisionBounds.clear();
		TmxMapLoader mapLoader = new TmxMapLoader();
		TmxMapLoader.Parameters param = new TmxMapLoader.Parameters();
		
		param.textureMinFilter = param.textureMinFilter.Nearest;
		param.textureMagFilter = param.textureMagFilter.Nearest;
		
		map = mapLoader.load("map/map" + level + ".tmx", param);
		MapLayer collisionLayer = map.getLayers().get("CollisionLayer1");
		MapObjects mapObjects = collisionLayer.getObjects();
		
		for(RectangleMapObject object : 
			mapObjects.getByType(RectangleMapObject.class)){
			
			Rectangle rec = object.getRectangle();
			mapCollisionBounds.add(new Rectangle(rec));			
		}		
	}
	
	public void dispose(){
		map.dispose();
	}
	
	public TiledMap getMap(){
		return map;
	}
	
	public Array<Rectangle> getMapCollisionBounds(){
		return mapCollisionBounds;
	}
	


	
	

}
