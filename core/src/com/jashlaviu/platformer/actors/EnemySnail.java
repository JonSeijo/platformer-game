package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class EnemySnail extends Enemy{
	
	public static final String name = "snail";

	public EnemySnail(float posX, float posY) {
		super(posX, posY);		
		setType(Type.snail);		
		setCustomName(name);
		
		Texture snailTexture = new Texture(Gdx.files.internal("snail-1.png"));
		TextureRegion normalSnailRegion = new TextureRegion(snailTexture, 0, 0, 32, 32);
		
		setRegion(normalSnailRegion);
		setCollisionBounds(new Rectangle(getX()+5, getY(), getWidth()-10, 20));

	}

}
