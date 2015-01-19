package com.jashlaviu.platformer.actors.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.TextureLoader;
import com.jashlaviu.platformer.actors.enemy.Enemy.Type;

public class EnemySnake extends Enemy{	
	
	public static final String name = "snake";

	public EnemySnake(float posX, float posY) {
		super(posX, posY);
		
		setType(Type.snake);		
		setCustomName(name);
		
		setRegion(TextureLoader.snakeNormal);
		setCollisionBounds(new Rectangle(getX()+8, getY(), getWidth()-18, 26));		
	}
	
	@Override
	public void updateX(float delta) {
		super.updateX(delta);

	}
	
	@Override
	public void updateState(float delta){	
		shootTimer += delta;
		System.out.println(shootTimer);
		if(!isShooting()){
			setRegion(TextureLoader.snakeNormal);
		}else{
			//setregion shootanimation
			setRegion(TextureLoader.snakeNormal);
			
			if(shootTimer > shootDelayAnimation){
				System.out.println("RELEASE");
				setShooting(false);
				setNeedShoot(true);
			}
		}
		
		
		
		if(isDying()){
			setDead(true);
		}
	}

}
