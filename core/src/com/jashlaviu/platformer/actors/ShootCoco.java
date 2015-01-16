package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.TextureLoader;

public class ShootCoco extends Shoot {

	public ShootCoco(float x, float y) {
		super(x, y);	
		
		//Set velocity in both directions
		setVelocity(150f, 150f);	
		
		setNormalRegion(TextureLoader.shootCocoNormal);
		setEmptyRegion(TextureLoader.shootCocoEmpty);
		
		setCollisionBounds(new Rectangle(getX(), getY(), 8, 8));	
		setDestroyAnim(0.03f, TextureLoader.shootCocoDestroy);				
	}
	
}
