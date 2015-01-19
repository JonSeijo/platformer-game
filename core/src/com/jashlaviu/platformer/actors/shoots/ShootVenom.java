package com.jashlaviu.platformer.actors.shoots;

import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.TextureLoader;
import com.jashlaviu.platformer.actors.ActorJash;


public class ShootVenom extends Shoot{
	
	public ShootVenom(float x, float y, ActorJash actorOrigin) {
		super(x, y, actorOrigin);	
		type = Type.VENOM;
		
		setVelocity(150f, 0);	
		
		setNormalRegion(TextureLoader.shootVenomNormal);
		
		setCollisionBounds(new Rectangle(getX(), getY()+2, 16, 4));	
		setDestroyAnim(0.03f, TextureLoader.shootCocoDestroy);
	}
	
	@Override
	public void updateY(float delta) {
		//No gravity
	}
}
