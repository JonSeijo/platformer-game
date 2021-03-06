package com.jashlaviu.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gui {
	
	private GameLogic gameLogic;
	private PlatformerGame game;
	private SpriteBatch batch;
	
	private OrthographicCamera guiCamera;
	
	private TextureRegion hungerCapsule, hungerBar, hungerText;
	
	public Gui(GameLogic gameLogic, PlatformerGame game){
		this.gameLogic = gameLogic;
		this.game = game;		
		this.batch = game.getBatch();
		
		guiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		guiCamera.translate(Gdx.graphics.getWidth()/2f, Gdx.graphics.getWidth()/2f);  //So (0, 0) is at the bottom left of the screen.
		guiCamera.update();
		
		hungerCapsule = TextureLoader.guiHungerCapsule;		
		hungerBar = TextureLoader.guiHungerBarGreen;
		hungerText = TextureLoader.guiHungerText;
	}
	
	public void draw(){	
		if(gameLogic.getPlayer().getHunger() >= 70){			
			hungerBar = TextureLoader.guiHungerBarRed;			
		}else if(gameLogic.getPlayer().getHunger() >= 40){
			hungerBar = TextureLoader.guiHungerBarYellow;
		}else if(gameLogic.getPlayer().getHunger() >= 0){			
			hungerBar = TextureLoader.guiHungerBarGreen;			
		} 
		
		float hungerHeight = (hungerCapsule.getRegionHeight()-(gameLogic.getPlayer().getHunger()*4)-8);
		if(hungerHeight < 0) hungerHeight = 1;	
		
		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();		
		
	//	batch.draw(hungerText, 30, 610, 80, 20);
		batch.draw(TextureLoader.foodChicken.get(0), 40, 610, 48, 64);
		batch.draw(hungerCapsule, 50, 200, hungerCapsule.getRegionWidth(), hungerCapsule.getRegionHeight());
		
		batch.draw(hungerBar, 50, 204, hungerBar.getRegionWidth(), hungerHeight);	
		
		batch.draw(TextureLoader.guiSkull, 34, 120, 64, 80);
		
		for(int i = 0; i < gameLogic.getPlayer().getShootsLeft(); i++){
			batch.draw(TextureLoader.shootCocoNormal, 550 + (i * 30), 750, 24, 30);			
		}
		
		batch.end();
	}
}
