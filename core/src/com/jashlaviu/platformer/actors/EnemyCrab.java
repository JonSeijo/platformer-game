package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.jashlaviu.platformer.actors.Enemy.Type;

public class EnemyCrab extends Enemy {
	
	public static final String name = "crab";
	private float MOVESPEED = 26;
	
	public EnemyCrab(float posX, float posY) {
		super(posX, posY);
		
		setType(Type.crab);		
		setCustomName(name);
		
		Texture crabTexture = new Texture(Gdx.files.internal("crab-1.png"));
		TextureRegion normalCrabRegion = new TextureRegion(crabTexture, 0, 80, 32, 16);
		
		setRegion(normalCrabRegion);
		setCollisionBounds(new Rectangle(getX()+5, getY(), getWidth()-10, 12));
		
		velocity.set(MOVESPEED, 0);
	}

}
