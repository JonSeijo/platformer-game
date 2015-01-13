package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class EnemyCrab extends Enemy{
	
	public final static String name = "crab";

	public EnemyCrab(float posX, float posY) {
		super(posX, posY);
		
		Texture crabTexture = new Texture(Gdx.files.internal("enemy-1.png"));
		TextureRegion normalCrabRegion = new TextureRegion(crabTexture, 0, 80, 32, 16);
		
		setRegion(normalCrabRegion);
		setCollisionBounds(new Rectangle(getX(), getY(), getWidth(), 16));
	}

}
