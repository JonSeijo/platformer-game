package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.TextureLoader;

public class EnemyCrab extends Enemy {
	
	public static final String name = "crab";
	private float MOVESPEED = 26;
	
	public EnemyCrab(float posX, float posY) {
		super(posX, posY);
		
		setType(Type.crab);		
		setCustomName(name);
		
		setRegion(TextureLoader.crabNormal);
		setCollisionBounds(new Rectangle(getX()+5, getY(), getWidth()-10, 12));
		
		velocity.set(MOVESPEED, 0);
	}

}
