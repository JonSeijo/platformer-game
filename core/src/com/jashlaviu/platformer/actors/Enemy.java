package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Enemy extends ActorJash{
	
	public static enum Type{
		notype, snail, crab
	}
	private Type type;
	
	private String customName;
	
	public Enemy(float posX, float posY) {
		super(posX, posY);	
		type = Type.notype;
		customName = "noname";
	}
	
	@Override
	public void drawDebug(ShapeRenderer shaper) {
		shaper.setColor(1, 0, 0, 1);
		
		Rectangle r = getCollisionBounds();
		shaper.rect(r.x, r.y, r.width, r.height);
	}
	
	protected void setType(Type type){
		this.type = type;
	}
	
	public Type getType(){
		if(type == Type.notype)
			System.out.println("NO TYPE ASSIGNED");
		
		return type;
	}
	
	public void flipDirectionX(){
		velocity.x = -velocity.x;
		setFacing((getFacing() == Facing.RIGHT) ? Facing.LEFT : Facing.RIGHT);
	}
	
	protected void setCustomName(String name){
		this.customName = name;
	}
	
	public String getCustomName(){
		return customName;
	}
	
	

}
