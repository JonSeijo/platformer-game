package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class CocoShoot extends Shoot {

	public CocoShoot(float x, float y, boolean rightDirection) {
		super(x, y, rightDirection);		
		
		Texture shootTexture = new Texture(Gdx.files.internal("shoot1.png"));		
		TextureRegion normalRegion = new TextureRegion(shootTexture, 0, 0, 8, 8); 
		
		TextureRegion destroy1 = new TextureRegion(shootTexture, 0, 0, 8, 8); 
		TextureRegion destroy2 = new TextureRegion(shootTexture, 0, 8, 8, 8); 
		TextureRegion destroy3 = new TextureRegion(shootTexture, 8, 8, 8, 8); 
		TextureRegion destroy4 = new TextureRegion(shootTexture, 16, 8, 8, 8); 
		TextureRegion destroy5 = new TextureRegion(shootTexture, 24, 8, 8, 8); 
		TextureRegion destroy6 = new TextureRegion(shootTexture, 32, 8, 8, 8); 
		
		Array<TextureRegion> destroyRegions = new Array<TextureRegion>();
		destroyRegions.add(destroy1);
		destroyRegions.add(destroy2);
		destroyRegions.add(destroy3);
		destroyRegions.add(destroy4);		
		destroyRegions.add(destroy5);		
		destroyRegions.add(destroy6);
		
		setRegion(normalRegion);				
		setCollisionBounds(new Rectangle(getX()-2, getY()-2, getWidth()+6, getHeight()+6));
		setDestroyAnim(0.03f, destroyRegions);
		
		
	}

}
