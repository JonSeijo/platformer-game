package com.jashlaviu.platformer;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.jashlaviu.platformer.actors.Checkpoint;
import com.jashlaviu.platformer.actors.CocoShoot;
import com.jashlaviu.platformer.actors.Enemy;
import com.jashlaviu.platformer.actors.EnemyCrab;
import com.jashlaviu.platformer.actors.Player;
import com.jashlaviu.platformer.actors.Player.State;
import com.jashlaviu.platformer.actors.Shoot;
import com.jashlaviu.platformer.screens.TestScreen;

public class GameLogic {	
	private Array<Rectangle> mapCollisionBounds;
	private TestScreen gameScreen;
	private TiledMap map;
	
	private Stage stage;		
	private Player player;	
	private Array<Shoot> shoots;
	private Array<Enemy> enemies;
	private Array<Checkpoint> checkpoints;

	private int level;
	
	public GameLogic(TestScreen gameScreen) {
		this.gameScreen = gameScreen;	
		
		mapCollisionBounds = new Array<Rectangle>();
		checkpoints = new Array<Checkpoint>();
		enemies = new Array<Enemy>();
		shoots = new Array<Shoot>();
		
		checkpoints.add(new Checkpoint(100, 200));
		stage = gameScreen.getStage();
		
		level = 1;		
		loadLevel(level);		
		

		
		player = new Player(checkpoints.get(0));
		stage.addActor(player);		
	}
	
	public void update(float delta){
		handleInput(delta);
		
		playerLogic(delta);	
		enemiesLogic(delta);
		shootsLogic(delta);		

		
		//System.out.println("vel x: " + player.getVelocity().x);
		gameScreen.getCamera().position.x = MathUtils.round(10f * (player.getX()+20)) / 10f;
		gameScreen.getCamera().position.y = MathUtils.round(10f * (player.getY()+20)) / 10f;
		
	}
	
	private void enemiesLogic(float delta){
		Iterator iter = enemies.iterator();
		while(iter.hasNext()){
			Enemy enemy = (Enemy)iter.next();
			
			enemy.updateX(delta);
			enemy.updateY(delta);
			
			Rectangle eBounds = enemy.getCollisionBounds();
			
			for(Rectangle mapBounds : mapCollisionBounds){
				if(eBounds.overlaps(mapBounds)){
					enemy.setY(mapBounds.y + mapBounds.height - enemy.getBoundsDistanceY());
					enemy.getVelocity().y = 0;
				}
			}
			
		}
	}
	
	private void shootsLogic(float delta){		
		Iterator iter = shoots.iterator();
		while(iter.hasNext()){
			Shoot shoot = (Shoot)iter.next();
			
			shoot.update(delta);
			
			if(shoot.isDestroyed()){
				shoot.remove();
				iter.remove();
			}
			
			for(Rectangle mapBounds : mapCollisionBounds){
				if(shoot.getCollisionBounds().overlaps(mapBounds)){
					if(!shoot.isDestroying()){	
						shoot.update(delta);
						shoot.destroy();
					}
				}
			}
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

	private void handleYcollision(Player player, Rectangle aBounds) {
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

	private void handleXcollision(Player player, Rectangle aBounds) {
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
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
			player.respawn();
		}
		
	}
	
	private void shoot(){		
		CocoShoot shoot = new CocoShoot(player.getX() + 12, player.getY() + 15, player.isFacingRight());
		shoot.addVelocity(player.getVelocity().x,0);
		
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
		
		loadMapBounds(map.getLayers().get("CollisionLayer1"));
		loadEnemies(map.getLayers().get("EnemyLayer1"));
		
		
	}
	
	private void loadEnemies(MapLayer enemyLayer){
		MapObjects mapObjects = enemyLayer.getObjects();
		
		for(RectangleMapObject object : 
			mapObjects.getByType(RectangleMapObject.class)){
			
			if(object.getName().equals(EnemyCrab.name)){

				EnemyCrab enemyCrab = new EnemyCrab(object.getRectangle().x, object.getRectangle().y);
				stage.addActor(enemyCrab);
				enemies.add(enemyCrab);			
			}
		}
	}
	
	private void loadMapBounds(MapLayer collisionLayer){
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
