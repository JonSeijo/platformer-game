package com.jashlaviu.platformer.actors.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.TextureLoader;

public class EnemySnail extends Enemy{
	
	public static final String name = "snail";	
	private Animation dieAnimation;

	public EnemySnail(float posX, float posY) {
		super(posX, posY);		
		setType(Type.snail);		
		setCustomName(name);
		
		dieAnimation = new Animation(dyingTime, TextureLoader.snailDie);
				
		setRegion(TextureLoader.snailNormal);
		setCollisionBounds(new Rectangle(getX()+6, getY(), getWidth()-15, 20));
	}
	
	@Override
	public void updateX(float delta) {
		super.updateX(delta);
		
		if(isDying()){
			setRegion(dieAnimation.getKeyFrame(animationTime, true));
			
			if(dieAnimation.isAnimationFinished(animationTime)){
				setDead(true);
			}
			animationTime += delta;
		}
	}

}
