package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class CocoShoot extends Shoot {

	public CocoShoot(float x, float y, boolean rightDirection) {
		super(x, y, rightDirection);		
		
		Texture shootTexture = new Texture(Gdx.files.internal("shoot1.png"));		
		TextureRegion normalRegion = new TextureRegion(shootTexture, 0, 0, 8, 8); 
		
		Array<TextureRegion> destroyRegions = new Array<TextureRegion>();
		
		setRegion(normalRegion);				
		setCollisionBounds(new Rectangle(getX()-2, getY()-2, getWidth()+6, getHeight()+6));
		
		
	}

}
