package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.TextureLoader;


public class ShootVenom extends Shoot{
	
	public ShootVenom(float x, float y, ActorJash actorOrigin) {
		super(x, y, actorOrigin);	
		type = Type.VENOM;
		
		setVelocity(150f, 0);	
		
		setNormalRegion(TextureLoader.shootCocoNormal);
		setEmptyRegion(TextureLoader.shootCocoEmpty);
		
		setCollisionBounds(new Rectangle(getX(), getY(), 8, 8));	
		setDestroyAnim(0.03f, TextureLoader.shootCocoDestroy);
	}
	
	@Override
	public void updateY(float delta) {
		//No gravity
	}
}
