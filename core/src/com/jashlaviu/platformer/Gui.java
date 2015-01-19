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
		hungerBar = TextureLoader.guiHungerBar;
		hungerText = TextureLoader.guiHungerText;
	}
	
	public void draw(){				
		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();		
		
		batch.draw(hungerText, 30, 610, 80, 20);
		batch.draw(hungerCapsule, 50, 200, hungerCapsule.getRegionWidth(), hungerCapsule.getRegionHeight());
		batch.draw(hungerBar, 50, 201, hungerBar.getRegionWidth(), 
				(hungerCapsule.getRegionHeight()-1-(gameLogic.getPlayer().getHunger()*4)));		
		
		batch.end();
	}
}
