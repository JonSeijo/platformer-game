package com.jashlaviu.platformer.actors.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.TextureLoader;
import com.jashlaviu.platformer.actors.enemy.Enemy.Type;

public class EnemySnake extends Enemy{	
	
	public static final String name = "snake";
	
	private Animation attackAnimation, dieAnimation;

	public EnemySnake(float posX, float posY) {
		super(posX, posY);
		
		setType(Type.snake);		
		setCustomName(name);
		
		setRegion(TextureLoader.snakeNormal);
		setCollisionBounds(new Rectangle(getX()+8, getY(), getWidth()-18, 26));	
		
		shootDelayAnimation = 0.3f; 		
		attackAnimation = new Animation(shootDelayAnimation/(float)TextureLoader.snakeAttack.size, TextureLoader.snakeAttack);
		dieAnimation = new Animation(dyingTime, TextureLoader.snakeDie);
	}
	
	@Override
	public void updateX(float delta) {
		super.updateX(delta);

	}
	
	@Override
	public void updateState(float delta){	
		shootTimer += delta;
		//System.out.println(shootTimer);
		if(!isShooting() && !isDying()){
			setRegion(TextureLoader.snakeNormal);
		}else if(isShooting() && !isDying()){
			//setregion shootanimation
			setRegion(attackAnimation.getKeyFrame(shootTimer));
			
			if(shootTimer > shootDelayAnimation){
				setShooting(false);
				setNeedShoot(true);
			}
		}else if(isDying()){
			if(dieAnimation.isAnimationFinished(animationTime)){
				setDead(true);
			}
			animationTime += delta;
			setRegion(dieAnimation.getKeyFrame(animationTime, true));
		
		}
		
		
	}

}
