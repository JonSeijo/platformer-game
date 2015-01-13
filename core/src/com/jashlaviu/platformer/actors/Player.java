package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.jashlaviu.platformer.actors.ActorJash.Facing;

public class Player extends ActorJash {
	
	private Rectangle bounds;
	private Checkpoint checkpoint;
	
	public enum State{
		WALKING, JUMPING, LANDING
	}
	protected State state;	
	protected Animation walkAnimation, jumpAnimation, fallAnimation;
	
	protected float FRICTION, MOVESPEED, MAX_VEL_X, MAX_VEL_Y;
	protected boolean movingLeft, movingRight;
	

	public Player(Checkpoint checkpoint) {
		super(checkpoint.getX(), checkpoint.getY());
		this.checkpoint = checkpoint; 
		
		MAX_VEL_X = 150f;	
		FRICTION = 15f;
		
		MOVESPEED = 1500f;

		
		walkAnimation = new Animation(100f, getRegion());
		jumpAnimation = new Animation(100f, getRegion());
		fallAnimation = new Animation(100f, getRegion());
		
		state = State.JUMPING;
		
		//Load textures		
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
		velocity.y -= GRAVITY * delta;		
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
	
	
	
	/**
	 * Resets player position to the stored checkpoint
	 */
	public void respawn(){
		setPosition(checkpoint.getX(), checkpoint.getY());
	}
	
	public void setCheckpoint(Checkpoint checkpoint){
		this.checkpoint = checkpoint;
	}

}
