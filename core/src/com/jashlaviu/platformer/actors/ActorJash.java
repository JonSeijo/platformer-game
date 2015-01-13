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
	
	public enum State{
		WALKING, JUMPING, LANDING
	}
	protected State state;
	
	protected Animation walkAnimation, jumpAnimation, fallAnimation;	
	protected Rectangle collisionBounds;
	protected float boundsDistanceX, boundsDistanceY;
	protected float animationTime;
	
	protected final float GRAVITY;
	protected float FRICTION, MOVESPEED, MAX_VEL_X, MAX_VEL_Y;
	protected boolean movingLeft, movingRight;
	
	public ActorJash(float posX, float posY){
		setRegion(new TextureRegion(new Texture(Gdx.files.internal("notexture.png"))));
		setNormalRegion(getRegion());
		
		collisionBounds = new Rectangle();
		setCollisionBounds(new Rectangle(getX(), getY(), getWidth(), getHeight()));

		velocity = new Vector2(0, 0);
		
		setPosition(posX, posY);		
	
		MAX_VEL_X = 150f;
		//FRICTION = .20f;  * pow		
		FRICTION = 15f;
		
		//MOVESPEED = 1700f;
		MOVESPEED = 1500f;
		GRAVITY = 700f;
		
		walkAnimation = new Animation(100f, getRegion());
		jumpAnimation = new Animation(100f, getRegion());
		fallAnimation = new Animation(100f, getRegion());
		
		facing = Facing.RIGHT;	
		state = State.JUMPING;
		
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
		Rectangle r = getCollisionBounds();
		shaper.rect(r.x, r.y, r.width, r.height);	
	}
	
	public void moveRight(){
		setFacing(Facing.RIGHT);	
		setMovingRight(true);
	}
	
	public void moveLeft(){
		setFacing(Facing.LEFT);
		setMovingLeft(true);
	}
	
	public void setNormalRegion(TextureRegion newNormalRegion){
		normalRegion = newNormalRegion;
		setRegion(normalRegion);
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
	
	
	public void applyGravity(float delta){
		velocity.y -= GRAVITY * delta;		
	}
	
	public void updateX(float delta){		
		if(movingLeft) velocity.x -= MOVESPEED * delta;	
		if(movingRight)velocity.x += MOVESPEED * delta;		
		
		velocity.x *= Math.exp((double)FRICTION * -delta);  //Aplly friction. This fixes  direfent friction in different fps
		
		if(Math.abs(velocity.x) < 2f) velocity.x = 0;	
		
		if(velocity.x > MAX_VEL_X) velocity.x = MAX_VEL_X;
		else if(velocity.x < -MAX_VEL_X)  velocity.x = -MAX_VEL_X;
		
		setX(getX() + velocity.x  * delta );
		
	}
	
	public void updateY(float delta){	
	//	if(state == State.JUMPING){
			velocity.y -= GRAVITY * delta;		
		//}		
	
		setY(getY() + velocity.y * delta);
	}
	
	public void updateRegion(float delta){
		if(state == State.WALKING){
			if(velocity.x == 0){
				animationTime = 0;
				setRegion(normalRegion);				
			}else{
				animationTime += delta;			
				setRegion(walkAnimation.getKeyFrame(animationTime, true));
			}
		}
		else if(state == State.JUMPING){
			if(velocity.y == 0){
				setRegion(normalRegion);				
			}else if(velocity.y > 0){
				animationTime += delta;
				setRegion(jumpAnimation.getKeyFrame(animationTime, true));
			}else{
				setRegion(fallAnimation.getKeyFrame(0, false));
			}
		}
	}
	
	
	public void setMovingLeft(boolean moving){
		movingLeft = moving;
	}
	
	public void setMovingRight(boolean moving){
		movingRight = moving;
	}
	
	public void jump(){
		velocity.y = 250;
		setState(State.JUMPING);
	}
	
	public void setState(State state){
		this.state = state;			
	}
	
	public Facing getFacing(){
		return facing;
	}
	
	public boolean isFacingRight(){
		return facing == Facing.RIGHT;
	}
	
	public void setWalkAnimation(float duration, Array<TextureRegion> regions){
		walkAnimation = new Animation(duration, regions);
		animationTime = 0;
	}
	
	public void setJumpAnimation(float duration, Array<TextureRegion> regions){
		jumpAnimation = new Animation(duration, regions);
		animationTime = 0;
	}
	
	public void setFallAnimation(float duration, Array<TextureRegion> regions){
		fallAnimation = new Animation(duration, regions);
		animationTime = 0;
	}
	
	public float getBoundsDistanceX(){
		return boundsDistanceX;
	}
	
	public float getBoundsDistanceY(){
		return boundsDistanceY;
	}



}
