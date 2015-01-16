package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.TextureLoader;

public class EnemySnail extends Enemy{
	
	public static final String name = "snail";

	public EnemySnail(float posX, float posY) {
		super(posX, posY);		
		setType(Type.snail);		
		setCustomName(name);
				
		setRegion(TextureLoader.snailNormal);
		setCollisionBounds(new Rectangle(getX()+5, getY(), getWidth()-10, 20));

	}

}
