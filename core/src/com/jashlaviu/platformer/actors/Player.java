package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player extends ActorJash {
	
	private Rectangle bounds;

	public Player(float posX, float posY) {
		super(posX, posY);
		
		//Load texture
//		setRegion(new TextureRegion(new Texture(Gdx.files.internal("player_2.png"))));
		
		Texture walkSheet = new Texture(Gdx.files.internal("player2-2_walking.png"));
		TextureRegion walk0 = new TextureRegion(walkSheet, 0, 0, 32, 32);
		TextureRegion walk1 = new TextureRegion(walkSheet, 32, 0, 32, 32);
		TextureRegion walk2 = new TextureRegion(walkSheet, 64, 0, 32, 32);
		TextureRegion walk3 = new TextureRegion(walkSheet, 96, 0, 32, 32);
		TextureRegion walk4 = new TextureRegion(walkSheet, 128, 0, 32, 32);
		TextureRegion walk5 = new TextureRegion(walkSheet, 160, 0, 32, 32);
		TextureRegion walk6 = new TextureRegion(walkSheet, 192, 0, 32, 32);
		TextureRegion walk7 = new TextureRegion(walkSheet, 224, 0, 32, 32);
		TextureRegion walk8 = new TextureRegion(walkSheet, 256, 0, 32, 32);
		
		TextureRegion fall1 = new TextureRegion(walkSheet, 32, 32, 32, 32);
		
		TextureRegion jump1 = new TextureRegion(walkSheet, 64, 32, 32, 32);
		TextureRegion jump2 = new TextureRegion(walkSheet, 96, 32, 32, 32);
		TextureRegion jump3 = new TextureRegion(walkSheet, 128, 32, 32, 32);
		
		
		Array<TextureRegion> walkRegions = new Array<TextureRegion>();
		walkRegions.add(walk1);
		walkRegions.add(walk2);		
		walkRegions.add(walk3);		
		walkRegions.add(walk4);	
		walkRegions.add(walk5);
		walkRegions.add(walk6);
		walkRegions.add(walk7);
		walkRegions.add(walk8);
		
		
		Array<TextureRegion> jumpRegions = new Array<TextureRegion>();
		jumpRegions.add(jump1);
		jumpRegions.add(jump2);
		jumpRegions.add(jump3);
		
		Array<TextureRegion> fallRegions = new Array<TextureRegion>();
		fallRegions.add(fall1);		
		
		setNormalRegion(walk0);		
		
		setWalkAnimation(0.08f, walkRegions);		
		setJumpAnimation(.10f, jumpRegions);
		setFallAnimation(.1f, fallRegions);
		
		bounds = new Rectangle(getX()+12, getY()+1, 8, 27);
		setCollisionBounds(bounds);
		
			
	}

}
