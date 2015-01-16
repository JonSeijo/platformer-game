package com.jashlaviu.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TextureLoader {
	
	private Texture noTexture, playerTexture, shootTexture, crabTexture, snailTexture;
	
	public static TextureRegion noRegion, playerNormal, snailNormal, crabNormal;
	public static TextureRegion shootCocoNormal, shootCocoEmpty;
	
	public static Array<TextureRegion> playerWalk, playerJump, playerFall;
	public static Array<TextureRegion> playerShootNormal, playerShootFall, playerShootJump, playerShootWalk;
	public static Array<TextureRegion> shootCocoDestroy;
	
	public TextureLoader(){	
		
		playerTexture = new Texture(Gdx.files.internal("player2-2_walking.png"));
		noTexture = new Texture(Gdx.files.internal("notexture.png"));			
		snailTexture = new Texture(Gdx.files.internal("snail-1.png"));
		crabTexture =  new Texture(Gdx.files.internal("crab-1.png"));
		shootTexture = new Texture(Gdx.files.internal("shoot1.png"));
		
		noRegion = getRegion(noTexture, 0, 0, noTexture.getWidth(), noTexture.getHeight());		
		playerNormal = getRegion(playerTexture, 0, 0, 32, 32);	
		
		//                              texure, startX, y, width, height, amount of frames
		playerWalk = getRegions(playerTexture, 1, 0, 32, 32, 8);
		playerFall = getRegions(playerTexture, 1, 1, 32, 32, 1); 
		playerJump = getRegions(playerTexture, 2, 1, 32, 32, 3); 
				
		playerShootNormal = getRegions(playerTexture, 0, 4, 32, 32, 5);
		playerShootFall = getRegions(playerTexture, 0, 5, 32, 32, 5);
		playerShootJump = getRegions(playerTexture, 0, 6, 32, 32, 5);
		playerShootWalk = getRegions(playerTexture, 0, 7, 32, 32, 5);
		
		snailNormal = getRegion(snailTexture, 0, 0, 32, 32);
		crabNormal = getRegion(crabTexture, 0, 5, 32, 16);  
		
		shootCocoNormal = getRegion(shootTexture, 0, 0, 8, 8);
		shootCocoEmpty = getRegion(shootTexture, 1, 0, 8, 8);
		shootCocoDestroy = getRegions(shootTexture, 0, 1, 8, 8, 6);
		
		 									
		
	}
	
	private TextureRegion getRegion(Texture texture, int x, int y, int width, int height){
		return new TextureRegion(texture, x*width, y*height, width, height);
	}
	
	private Array<TextureRegion> getRegions(Texture texture, int startX, int y, int width, int height, int amount){
		Array<TextureRegion> arrayLoader = new Array<TextureRegion>();
		int addedFrames = 0;
		for(int x = startX; addedFrames < amount; x++){
			addedFrames++;
			arrayLoader.add(getRegion(texture, x, y, width, height));
		}
		
		return arrayLoader;
	}

	
	public void dispose(){
		playerTexture.dispose();
		shootTexture.dispose();
		crabTexture.dispose();
		snailTexture.dispose();
		
	}
}
