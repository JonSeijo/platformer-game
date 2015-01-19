package com.jashlaviu.platformer;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.jashlaviu.platformer.actors.ActorJash;
import com.jashlaviu.platformer.actors.ActorJash.Facing;
import com.jashlaviu.platformer.actors.Checkpoint;
import com.jashlaviu.platformer.actors.Player;
import com.jashlaviu.platformer.actors.Player.State;
import com.jashlaviu.platformer.actors.enemy.Enemy;
import com.jashlaviu.platformer.actors.enemy.EnemyCrab;
import com.jashlaviu.platformer.actors.enemy.EnemySnail;
import com.jashlaviu.platformer.actors.enemy.EnemySnake;
import com.jashlaviu.platformer.actors.food.Food;
import com.jashlaviu.platformer.actors.food.FoodChicken;
import com.jashlaviu.platformer.actors.shoots.Shoot;
import com.jashlaviu.platformer.actors.shoots.ShootCoco;
import com.jashlaviu.platformer.actors.shoots.ShootVenom;
import com.jashlaviu.platformer.screens.TestScreen;

public class GameLogic {	
	private Array<Rectangle> mapCollisionBounds;
	private TestScreen gameScreen;
	private TiledMap map;
	
	private Stage stage;		
	private Player player;	
	private Array<Food> food;
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
		food = new Array<Food>();		
		checkpoints.add(new Checkpoint(100, 200));
		
		stage = gameScreen.getStage();
		
		level = 1;		
		loadLevel(level);	
		
		player = new Player(checkpoints.get(0));
		stage.addActor(player);		
	}
	
	public void update(float delta){
		handleInput(delta);
		
		playerLogic(delta);  //Update, map collision, enemy collision		
		enemiesLogic(delta); //Update, map collision		
		shootsLogic(delta);	 //Update, map collision and enemy collision	
		foodLogic(delta);
		
		//System.out.println("vel x: " + player.getVelocity().x);
		gameScreen.getCamera().position.x = MathUtils.round(10f * (player.getX()+20)) / 10f;
		gameScreen.getCamera().position.y = MathUtils.round(10f * (player.getY()+20)) / 10f;		
	}
	
	private void foodLogic(float delta){
		Iterator<Food> foodIter = food.iterator();
		while(foodIter.hasNext()){
			Food foodSingle = (Food)foodIter.next();			
			Rectangle foodBounds = foodSingle.getCollisionBounds();
			
			foodSingle.update(delta);			

			for(Rectangle mapBounds : mapCollisionBounds){
				if(foodBounds.overlaps(mapBounds)){
					foodSingle.setY(mapBounds.y + mapBounds.height - foodSingle.getBoundsDistanceY());
					foodSingle.getVelocity().y = 0;
				}
			}			
			
		}
	}
	
	private void enemiesLogic(float delta){
		Iterator<Enemy> eIter = enemies.iterator();
		while(eIter.hasNext()){
			Enemy enemy = (Enemy)eIter.next();
			
			if(enemy.isDying()){
				if(enemy.isDead()){
					enemy.remove();
					eIter.remove();
				}				
			} //Snails don't move, so look to the player position
			else if(enemy.getType() == Enemy.Type.snail){
					enemy.setFacing( (player.getX() > enemy.getX()) ? 
							ActorJash.Facing.RIGHT : ActorJash.Facing.LEFT);
			}
			else if(enemy.getType() == Enemy.Type.snake){
				enemy.setFacing( (player.getX() > enemy.getX()) ? 
						ActorJash.Facing.RIGHT : ActorJash.Facing.LEFT);
				
				//reemplazar con enemy.isOnRange(player)
				if(player.getX() > enemy.getX() -100 && player.getX() < enemy.getX() + 100
						&& player.getY() > enemy.getY() -100 && player.getY() < enemy.getY() + 100){
					
					enemy.shot();					
				}
			}			
			
			if(enemy.needShoot()){
				enemy.setNeedShoot(false);			
				
				if(enemy.getType() == Enemy.Type.snake){
					ShootVenom shoot = new ShootVenom(enemy.getX() + 10, enemy.getY()+20, enemy);	
					if(enemy.getFacing() == Enemy.Facing.LEFT) shoot.setVelocity(-shoot.getVelocity().x, shoot.getVelocity().y);
					shoots.add(shoot);
					stage.addActor(shoot);
				}
				

				
				
			}
			
			Rectangle eBounds = enemy.getCollisionBounds();			
			
			enemy.updateY(delta);		
			enemy.updateX(delta);	
			enemy.updateState(delta);
			
			for(Rectangle mapBounds : mapCollisionBounds){
				if(eBounds.overlaps(mapBounds)){
					enemy.setY(mapBounds.y + mapBounds.height - enemy.getBoundsDistanceY());
					enemy.getVelocity().y = 0;
					
					if(eBounds.getX() < mapBounds.getX()){
						eBounds.x = mapBounds.getX();
						enemy.flipDirectionX();
						enemy.updateX(delta);
					}else if(eBounds.getX()+eBounds.getWidth() > mapBounds.getX()+mapBounds.getWidth()){
						eBounds.x = mapBounds.getX() + mapBounds.getWidth() - eBounds.width;
						enemy.flipDirectionX();
						enemy.updateX(delta);
					}
					break;
				}				
			}
			

			
		}
	}
	
	private void shootsLogic(float delta){		
		Iterator<Shoot> shootIter = shoots.iterator();
		while(shootIter.hasNext()){
			Shoot shoot = (Shoot)shootIter.next();
			
			if(!shoot.isThrowed()){
				if(shoot.getActorOrigin() == player){
					if(shoot.isInDelay()){						
						if(player.getFacing() == Facing.RIGHT){
							shoot.setPosition(player.getX() + 16, player.getY() + 12);	
							shoot.setFacing(Shoot.Facing.RIGHT);						
						}else{
							shoot.setPosition(player.getX() + 10, player.getY() + 12);
							shoot.setFacing(Shoot.Facing.LEFT);
						}					
					}else{  //If not throwed and not on delay
						shoot.setThrowed(true);	
						shoot.addVelocity(player.getVelocity().x, 0);
					}
				}
			}
			
			shoot.update(delta);
			Rectangle sBounds = shoot.getCollisionBounds();
			
			if(shoot.isDestroyed()){
				shoot.remove();
				shootIter.remove();
			}
			
			for(Rectangle mapBounds : mapCollisionBounds){
				if(sBounds.overlaps(mapBounds)){
					if(!shoot.isDestroying()){	
						//shoot.update(delta);
						shoot.destroy();
					}
				}
			}
			if(shoot.getActorOrigin() == player){
				Iterator<Enemy> enemyIter = enemies.iterator();
				while(enemyIter.hasNext()){
					Enemy enemy = (Enemy)enemyIter.next();
					
					if(sBounds.overlaps(enemy.getCollisionBounds())){
						if(!shoot.isDestroying()){	
							
							shoot.destroy();
							
							enemy.setDying(true);
						}
					}				
					
				}
			}
		}
	}	

	private void playerLogic(float delta) {
		boolean needRestart = false;
		
		Rectangle pBounds = player.getCollisionBounds();
		
		//Updates every actor X position
		player.updateX(delta);			
		handleXmapCollision(player, pBounds);	
		
		//Updates every actor Y position
		player.updateY(delta);			
		handleYmapCollision(player, pBounds);
		
		player.updateHunger(delta);
		
		for(Enemy enemy : enemies){
			if(pBounds.overlaps(enemy.getCollisionBounds())){
				//If the enemy isn't dying
				if(!enemy.isDying()){
					player.die();
					needRestart = true;										
				}
			}
		}	
		
		if(needRestart){
			restartLevel();
		}
		
		
		Iterator<Food> foodIter = food.iterator();
		while(foodIter.hasNext()){
			Food foodSingle = (Food)foodIter.next();	
			
			if(pBounds.overlaps(foodSingle.getCollisionBounds())){
				player.eat(foodSingle.getHungerSatisfaction());
				foodSingle.remove();
				foodIter.remove();
			}
			
		}
			
	
		player.updateRegion(delta);
	}

	private void handleYmapCollision(Player player, Rectangle aBounds) {
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

	private void handleXmapCollision(Player player, Rectangle aBounds) {
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
		//	player.respawn();
		}
		
	}
	
	private void shoot(){
		if(!player.isShooting()){
			ShootCoco shoot;		
			if(player.getFacing() == Facing.RIGHT){
				shoot = new ShootCoco(player.getX() + 16, player.getY() + 12, player);
			}else{
				shoot = new ShootCoco(player.getX() + 10, player.getY() + 12, player);
			}
			
			player.setShooting(true);
			
			shoots.add(shoot);
			stage.addActor(shoot);
		}
		
	}
	
	public void loadLevel(int level){	
		for(Food foodS : food) 
			foodS.remove();
		food.clear();
		
		for(Enemy enemy : enemies) 
			enemy.remove();		
		enemies.clear();
		
		for(Shoot shoot : shoots)
			shoot.remove();		
		shoots.clear();
		
		mapCollisionBounds.clear();
		
		TmxMapLoader mapLoader = new TmxMapLoader();
		TmxMapLoader.Parameters param = new TmxMapLoader.Parameters();
		
		param.textureMinFilter = TextureFilter.Nearest;
		param.textureMagFilter = TextureFilter.Nearest;
		
		map = mapLoader.load("map/map" + level + ".tmx", param);
		
		loadMapBounds(map.getLayers().get("CollisionLayer1"));
		loadEnemies(map.getLayers().get("EnemyLayer1"));
		loadFood(map.getLayers().get("FoodLayer"));		
	}
	
	private void loadFood(MapLayer foodLayer){
		MapObjects mapObjects = foodLayer.getObjects();
		
		for(RectangleMapObject object : 
			mapObjects.getByType(RectangleMapObject.class)){
			
			if(object.getName().equals(FoodChicken.name)){
				FoodChicken foodChicken = new FoodChicken(object.getRectangle().x, object.getRectangle().y);
				stage.addActor(foodChicken);
				food.add(foodChicken);	
			}
		}
	}
	
	private void loadEnemies(MapLayer enemyLayer){
		MapObjects mapObjects = enemyLayer.getObjects();
		
		for(RectangleMapObject object : 
			mapObjects.getByType(RectangleMapObject.class)){
			
			if(object.getName().equals(EnemySnail.name)){
				EnemySnail enemySnail = new EnemySnail(object.getRectangle().x, object.getRectangle().y);
				stage.addActor(enemySnail);
				enemies.add(enemySnail);			
			}else if(object.getName().equals(EnemyCrab.name)){				
				EnemyCrab enemyCrab = new EnemyCrab(object.getRectangle().x, object.getRectangle().y);
				stage.addActor(enemyCrab);
				enemies.add(enemyCrab);	
			}else if(object.getName().equals(EnemySnake.name)){				
				EnemySnake enemySnake = new EnemySnake(object.getRectangle().x, object.getRectangle().y);
				stage.addActor(enemySnake);
				enemies.add(enemySnake);	
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
	
	private void restartLevel(){
		loadLevel(level);
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
	
	public Player getPlayer(){
		return player;
	}
	


	
	

}
