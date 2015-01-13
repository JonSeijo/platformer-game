package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Enemy extends ActorJash{

	public Enemy(float posX, float posY) {
		super(posX, posY);
	}
	
	@Override
	public void drawDebug(ShapeRenderer shaper) {
		shaper.setColor(1, 0, 0, 1);
		
		Rectangle r = getCollisionBounds();
		shaper.rect(r.x, r.y, r.width, r.height);
	}

}
