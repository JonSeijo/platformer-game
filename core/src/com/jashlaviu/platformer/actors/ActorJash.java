package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ActorJash extends Actor{
	
	protected TextureRegion region, normalRegion;
	protected Vector2 velocity;
	
	protected enum Facing{LEFT, RIGHT};
	protected Facing facing;
	
	protected Rectangle collisionBounds;
	protected float boundsDistanceX, boundsDistanceY;
	protected float animationTime;
	
	protected final float GRAVITY = 700f;

	
	public ActorJash(float posX, float posY){
		setRegion(new TextureRegion(new Texture(Gdx.files.internal("notexture.png"))));
				
		collisionBounds = new Rectangle();
		setCollisionBounds(new Rectangle(getX(), getY(), getWidth(), getHeight()));
		
		setPosition(posX, posY);
		
		velocity = new Vector2(0, 0);		
		facing = Facing.RIGHT;	
		setDebug(false);
	}

	
	@Override
	public void draw(Batch batch, float parentAlpha) {	
		
		Color col = getColor();		
		batch.setColor(col.r, col.g, col.b, col.a * parentAlpha);
		
		if(facing == Facing.RIGHT){
			batch.draw(getRegion(), MathUtils.round(10f * (getX())) / 10f, MathUtils.round(10f * (getY())) / 10f, 
					getOriginX(), getOriginY(), getWidth(), getHeight(), 
					getScaleX(), getScaleY(), getRotation());
		}
		
		// drawing with -width flips the image.
		else if(facing == Facing.LEFT){
			batch.draw(getRegion(), MathUtils.round(10f * (getX()+ getWidth())) / 10f, MathUtils.round(10f * (getY())) / 10f, 
					getOriginX(), getOriginY(), -getWidth(), getHeight(), 
					getScaleX(), getScaleY(), getRotation());
		}
		
	}	
	
	@Override
	public void drawDebug(ShapeRenderer shaper) {
		shaper.setColor(0, 0, 0, 1);
		
		Rectangle r = getCollisionBounds();
		shaper.rect(r.x, r.y, r.width, r.height);	
	}
	

	
	public void setRegion(TextureRegion newRegion){
		region = newRegion;
		setSize(region.getRegionWidth(), region.getRegionHeight());
	}
	
	public void setCollisionBounds(Rectangle bounds){
		collisionBounds.set(bounds);
		boundsDistanceX = collisionBounds.x - getX();
		boundsDistanceY = collisionBounds.y - getY();
	}	
	
	public Rectangle getCollisionBounds(){
		return collisionBounds;
	}
	
	@Override
	protected void positionChanged() {
		updateCollisionBounds();
	}

	protected void updateCollisionBounds() {		
		collisionBounds.x = getX() + boundsDistanceX;
		collisionBounds.y = getY() + boundsDistanceY;			
	}
	
	protected void setFacing(Facing newFacing){
		facing = newFacing;		
	}
	
	public TextureRegion getRegion(){
		return region;
	}

	public Vector2 getVelocity(){
		return velocity;
	}
	
	public void updateX(float delta){

	}
	
	public void updateY(float delta){
		velocity.y -= GRAVITY * delta;
		setY(getY() + velocity.y * delta);
	}
	
	public void applyGravity(float delta){
		velocity.y -= GRAVITY * delta;		
	}
	
	public Facing getFacing(){
		return facing;
	}
	
	public boolean isFacingRight(){
		return facing == Facing.RIGHT;
	}
	
	public float getBoundsDistanceX(){
		return boundsDistanceX;
	}
	
	public float getBoundsDistanceY(){
		return boundsDistanceY;
	}

}
