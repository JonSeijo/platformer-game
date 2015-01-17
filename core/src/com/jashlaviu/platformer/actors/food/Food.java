package com.jashlaviu.platformer.actors.food;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.jashlaviu.platformer.TextureLoader;
import com.jashlaviu.platformer.actors.ActorJash;

public class Food extends ActorJash{
	
	public static String name;
	
	protected float hungerSatisfaction;
	protected Animation idleAnimation;
	
	public Food(float posX, float posY) {
		super(posX, posY);
		
		idleAnimation = new Animation(1f, TextureLoader.noRegion);		
		setCollisionBounds(new Rectangle(getX(), getY()-10, 16, 32));
		
		hungerSatisfaction = 20f;		
	}
		
	public void update(float delta){
		updateX(delta);
		updateY(delta);
		
		setRegion(idleAnimation.getKeyFrame(animationTime, true));
		animationTime += delta;
	}
	
	protected void setIdleAnimation(float framDuration, Array<TextureRegion> regions){
		idleAnimation = new Animation(framDuration, regions);
	}
	
	public float getHungerSatisfaction(){
		return hungerSatisfaction;
	}
	
	

}
