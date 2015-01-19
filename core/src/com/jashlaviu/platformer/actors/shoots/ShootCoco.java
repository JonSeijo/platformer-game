package com.jashlaviu.platformer.actors.shoots;

import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.TextureLoader;
import com.jashlaviu.platformer.actors.ActorJash;

public class ShootCoco extends Shoot {

	public ShootCoco(float x, float y, ActorJash actorOrigin) {
		super(x, y, actorOrigin);	
		
		type = Type.COCO;
		
		//Set velocity in both directions
		setVelocity(150f, 150f);	
		
		setNormalRegion(TextureLoader.shootCocoNormal);
		
		setCollisionBounds(new Rectangle(getX(), getY(), 8, 8));	
		setDestroyAnim(0.03f, TextureLoader.shootCocoDestroy);				
	}
	
}
