package com.jashlaviu.platformer.actors.enemy;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.jashlaviu.platformer.actors.ActorJash;

public class Enemy extends ActorJash{
	
	public static enum Type{
		notype, snail, crab
	}
	private Type type;
	
	private String customName;
	
	protected boolean dying, dead;
	protected float dyingTime;
	
	public Enemy(float posX, float posY) {
		super(posX, posY);	
		type = Type.notype;
		customName = "noname";
		
		dyingTime = 2f;
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
	
	public boolean isDying(){
		return dying;
	}
	
	public void setDying(boolean dying){
		this.dying = dying;
		if(dying){
			velocity.set(0, 0);
			animationTime = 0;		
			this.addAction(new ParallelAction(Actions.moveTo(getX(), getY() + 40, dyingTime), Actions.fadeOut(dyingTime)));
		}
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void setDead(boolean dead){
		this.dead = dead;		
	}
	
	public void setDyingTime(float time){
		dyingTime = time;
	}
	
	
	

}
