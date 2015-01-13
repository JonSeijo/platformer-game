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
import com.jashlaviu.platformer.actors.CocoShoot;
import com.jashlaviu.platformer.actors.Player;
import com.jashlaviu.platformer.actors.Shoot;
import com.jashlaviu.platformer.screens.TestScreen;

public class GameLogic {	
	private Array<Rectangle> mapCollisionBounds;
	private TestScreen gameScreen;
	private TiledMap map;
	
	private Stage stage;		
	private Player player;	
	private Array<Shoot> shoots;
	
	private int level;
	
	public GameLogic(TestScreen gameScreen) {
		this.gameScreen = gameScreen;	
		
		mapCollisionBounds = new Array<Rectangle>();
		shoots = new Array<Shoot>();
		
		level = 1;		
		loadLevel(level);		
		
		stage = gameScreen.getStage();
		
		player = new Player(100,300);
		stage.addActor(player);		
	}
	
	public void update(float delta){
		handleInput(delta);
		
		playerLogic(delta);		
		shootsLogic(delta);
		
		//System.out.println("vel x: " + player.getVelocity().x);
		gameScreen.getCamera().position.x = MathUtils.round(10f * (player.getX()+20)) / 10f;
		gameScreen.getCamera().position.y = MathUtils.round(10f * (player.getY()+20)) / 10f;
		
	}
	
	private void shootsLogic(float delta){
		for(Shoot shoot : shoots){
			shoot.updateX(delta);
			shoot.updateY(delta);
		}
	}

	private void playerLogic(float delta) {
		Rectangle aBounds = player.getCollisionBounds();
		
		//Updates every actor X position
		player.updateX(delta);			
		handleXcollision(player, aBounds);	
		
		//Updates every actor Y position
		player.updateY(delta);			
		handleYcollision(player, aBounds);	
		
		player.updateRegion(delta);
	}

	private void handleYcollision(ActorJash player, Rectangle aBounds) {
		boolean air = true;
		for(Rectangle mapBounds : mapCollisionBounds){
			if(aBounds.overlaps(mapBounds)){
				if(player.getVelocity().y > 0){
					player.setY(mapBounds.y - aBounds.height - player.getBoundsDistanceY());
					player.getVelocity().y = 0;
				}
				
				if(player.getVelocity().y < 0){
					player.setY(mapBounds.y + mapBounds.height - player.getBoundsDistanceY() );
					player.getVelocity().y = 0;
					air = false;
					player.setState(State.WALKING);
					break;
				}				
			}				
		}
		
		if(air)
			player.setState(State.JUMPING);

	}

	private void handleXcollision(ActorJash player, Rectangle aBounds) {
		for(Rectangle mapBounds : mapCollisionBounds){
			if(aBounds.overlaps(mapBounds)){
				if(player.getVelocity().x > 0){
					player.setX(mapBounds.x - aBounds.width - player.getBoundsDistanceX());
					player.getVelocity().x = 0;
				}
				
				if(player.getVelocity().x < 0){
					player.setX(mapBounds.x + mapBounds.width - player.getBoundsDistanceX());
					player.getVelocity().x = 0;
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
		
		if(Gdx.input.isKeyJustPressed(Keys.Z)){
			player.jump();	
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.X)){
			shoot();
		}		
		
	}
	
	private void shoot(){
		System.out.println("SHOOTS FIRED LOLOLO");		
		
		CocoShoot shoot = new CocoShoot(player.getX() + 12, player.getY() + 15, player.isFacingRight());
		shoot.addVelocity(player.getVelocity().x,0);
		
		System.out.println(shoot.getVelocity().y);
		
		shoots.add(shoot);
		stage.addActor(shoot);
		
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
