package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Shoot extends Actor{
	
	protected TextureRegion region;
	
	protected Rectangle collisionBounds;
	protected float boundsDistanceX, boundsDistanceY;
	
	protected enum Facing{LEFT, RIGHT};
	protected Facing facing;
	
	protected Animation destroyAnim, destroyAnimLeft, destroyAnimRight, destroyAnimDown;
	protected float animationTime;	
	protected boolean destroying, destroyed;
	
	protected Vector2 velocity;
	protected final float GRAVITY;
	
	public Shoot(float x, float y, boolean rightDirection){	
		setRegion(new TextureRegion(new Texture(Gdx.files.internal("notexture.png"))));
		
		collisionBounds = new Rectangle();
		setCollisionBounds(new Rectangle(getX(), getY(), getWidth(), getHeight()));	
		
		setPosition(x, y);
		
		destroyAnim = new Animation(1f, getRegion());
		
		if(rightDirection){
			facing = Facing.RIGHT;
			velocity = new Vector2(150f, 150f);
		}else{
			facing = Facing.LEFT;
			velocity = new Vector2(-150f, 150f);	
		}
		
		GRAVITY = 700f;
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
	
	public void update(float delta){		
		updateX(delta);
		updateY(delta);
		
		if(isDestroying()){			
			if(destroyAnim.isAnimationFinished(animationTime)){
				destroyed = true;
			}
			
			region = destroyAnim.getKeyFrame(animationTime, false);
			animationTime += delta;
		}
	}
	
	public void updateX(float delta){	
		setX(getX() + velocity.x  * delta );		
	}
	
	public void updateY(float delta){	
		if(!isDestroying())
			velocity.y -= GRAVITY * delta;
		
		setY(getY() + velocity.y * delta);
	}	
	
	@Override
	public void drawDebug(ShapeRenderer shaper) {		
		shaper.setColor(0, 0, 0, 1);
		
		Rectangle r = getCollisionBounds();
		shaper.rect(r.x, r.y, r.width, r.height);	
	}
	
	public void setDestroyAnim(float duration, Array<TextureRegion> regions){
		destroyAnim = new Animation(duration, regions);
		animationTime = 0;
	}
	
	public void setRegion(TextureRegion newRegion){
		region = newRegion;
		setSize(region.getRegionWidth(), region.getRegionHeight());
	}
	
	public TextureRegion getRegion(){
		return region;
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
	
	public Vector2 getVelocity(){
		return velocity;
	}
	
	public void addVelocity(float x, float y){
		velocity.add(x, y);
	}
	
	public void destroy(){
		destroying = true;
		setVelocity(getVelocity().x * 0.05f, getVelocity().y * 0.05f);
	}
	
	public boolean isDestroying(){
		return destroying;
	}
	
	public boolean isDestroyed(){
		return destroyed;
	}
	
	public void setVelocity(float x, float y){
		velocity.set(x, y);		
	}
	
	
	
	

}
