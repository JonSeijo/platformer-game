package com.jashlaviu.platformer.actors.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.TextureLoader;

public class EnemyCrab extends Enemy {
	
	public static final String name = "crab";	

	private Animation walkAnimation, dieAnimation;
	private float MOVESPEED = 26;
		
	public EnemyCrab(float posX, float posY) {
		super(posX, posY);	
		
		setType(Type.crab);		
		setCustomName(name);
		
		walkAnimation = new Animation(0.08f, TextureLoader.crabWalk);
		dieAnimation = new Animation(dyingTime, TextureLoader.crabDie);
		
		setRegion(TextureLoader.crabNormal);
		setCollisionBounds(new Rectangle(getX()+2, getY(), getWidth()-4, 14));
		
		velocity.set(MOVESPEED, 0);
	}
	
	@Override
	public void updateX(float delta) {
		super.updateX(delta);		
		
	}
	
	@Override
	public void updateState(float delta) {
		if(!isDying()){
			setRegion(walkAnimation.getKeyFrame(animationTime, true));
			animationTime += delta;
		}else{					
			if(dieAnimation.isAnimationFinished(animationTime)){
				setDead(true);
			}
			animationTime += delta;
			setRegion(dieAnimation.getKeyFrame(animationTime, true));
		
		}
	}

}
