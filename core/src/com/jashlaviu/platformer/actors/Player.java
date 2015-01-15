package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player extends ActorJash {
	
	private Rectangle bounds;
	private Checkpoint checkpoint;
	
	public enum State{
		WALKING, JUMPING, LANDING
	}
	protected State state;	
	protected Animation walkAnimation, jumpAnimation, fallAnimation;
	protected Animation shootNormalAnimation, shootWalkAnimation, shootJumpAnimation, shootFallAnimation;
	
	protected float FRICTION, MOVESPEED, MAX_VEL_X, MAX_VEL_Y;
	protected boolean movingLeft, movingRight;	
	
	protected boolean isShooting;

	public Player(Checkpoint checkpoint) {
		super(checkpoint.getX(), checkpoint.getY());
		this.checkpoint = checkpoint; 
		
		MAX_VEL_X = 150f;	
		FRICTION = 15f;
		
		MOVESPEED = 1500f;
		
		walkAnimation = new Animation(100f, getRegion());
		jumpAnimation = new Animation(100f, getRegion());
		fallAnimation = new Animation(100f, getRegion());
		
		shootNormalAnimation = new Animation(100f, getRegion());
		shootWalkAnimation = new Animation(100f, getRegion());
		shootJumpAnimation = new Animation(100f, getRegion());
		shootFallAnimation = new Animation(100f, getRegion());
		
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
		
		TextureRegion shootNormal1 = new TextureRegion(walkSheet, 0, 128, 32, 32);		
		TextureRegion shootNormal2 = new TextureRegion(walkSheet, 32, 128, 32, 32);
		TextureRegion shootNormal3 = new TextureRegion(walkSheet, 64, 128, 32, 32);
		TextureRegion shootNormal4 = new TextureRegion(walkSheet, 96, 128, 32, 32);
		TextureRegion shootNormal5 = new TextureRegion(walkSheet, 128, 128, 32, 32);
		
		TextureRegion shootFall1 = new TextureRegion(walkSheet, 0, 160, 32, 32);		
		TextureRegion shootFall2 = new TextureRegion(walkSheet, 32, 160, 32, 32);
		TextureRegion shootFall3 = new TextureRegion(walkSheet, 64, 160, 32, 32);
		TextureRegion shootFall4 = new TextureRegion(walkSheet, 96, 160, 32, 32);
		TextureRegion shootFall5 = new TextureRegion(walkSheet, 128, 160, 32, 32);
		
		TextureRegion shootJump1 = new TextureRegion(walkSheet, 0, 192, 32, 32);		
		TextureRegion shootJump2 = new TextureRegion(walkSheet, 32, 192, 32, 32);
		TextureRegion shootJump3 = new TextureRegion(walkSheet, 64, 192, 32, 32);
		TextureRegion shootJump4 = new TextureRegion(walkSheet, 96, 192, 32, 32);
		TextureRegion shootJump5 = new TextureRegion(walkSheet, 128, 192, 32, 32);
		
		
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
		
		Array<TextureRegion> shootNormalRegions = new Array<TextureRegion>();
		shootNormalRegions.add(shootNormal1);
		shootNormalRegions.add(shootNormal2);
		shootNormalRegions.add(shootNormal3);
		shootNormalRegions.add(shootNormal4);
		shootNormalRegions.add(shootNormal5);
		
		Array<TextureRegion> shootFallRegions = new Array<TextureRegion>();
		shootFallRegions.add(shootFall1);
		shootFallRegions.add(shootFall2);
		shootFallRegions.add(shootFall3);
		shootFallRegions.add(shootFall4);
		shootFallRegions.add(shootFall5);		
		
		Array<TextureRegion> shootJumpRegions = new Array<TextureRegion>();
		shootJumpRegions.add(shootJump1);
		shootJumpRegions.add(shootJump2);
		shootJumpRegions.add(shootJump3);
		shootJumpRegions.add(shootJump4);
		shootJumpRegions.add(shootJump5);	
		
		
		setNormalRegion(walk0);		
		
		setWalkAnimation(0.08f, walkRegions);		
		setJumpAnimation(.10f, jumpRegions);
		setFallAnimation(.1f, fallRegions);
		
		setShootNormalAnimation(0.08f, shootNormalRegions);
		setShootWalkAnimation(0.08f, shootNormalRegions);
		setShootFalllAnimation(0.08f, shootFallRegions);
		setShootJumpAnimation(0.08f, shootJumpRegions);
		
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
		if(isShooting){
			if(shootNormalAnimation.isAnimationFinished(animationTime))
				setShooting(false);
			
			if(shootWalkAnimation.isAnimationFinished(animationTime))
				setShooting(false);
			
			if(shootJumpAnimation.isAnimationFinished(animationTime))
				setShooting(false);
			
			if(shootFallAnimation.isAnimationFinished(animationTime))
				setShooting(false);
		}
		
		if(state == State.WALKING){					
			if(velocity.x == 0){
				if(isShooting){  //Is shooting and is standing
					setRegion(shootNormalAnimation.getKeyFrame(animationTime, true));
					animationTime += delta;
				}else{      //NOT shooting and is standing
					animationTime = 0;
					setRegion(normalRegion);
				}
			}else{
				if(isShooting){ //Is shooting and is moving
					setRegion(shootWalkAnimation.getKeyFrame(animationTime, true));
					animationTime += delta;
				}else{    //NOT shooting and is moving
					setRegion(walkAnimation.getKeyFrame(animationTime, true));
					animationTime += delta;
				}
			}
			
		}
		else if(state == State.JUMPING){
			if(velocity.y == 0){				
				setRegion(normalRegion);				
			}else if(velocity.y > 0){
				if(isShooting){ //Is shooting and is jumping
					animationTime += delta;
					setRegion(shootJumpAnimation.getKeyFrame(animationTime, true));
				}else{    //NOT shooting and is jumping
					animationTime += delta;
					setRegion(jumpAnimation.getKeyFrame(animationTime, true));
				}
			}else{
				if(isShooting){ //Is shooting and is falling
					setRegion(shootFallAnimation.getKeyFrame(animationTime, true));
					animationTime += delta;
				}else{   //NOT shooting and is falling
					setRegion(fallAnimation.getKeyFrame(0, false));
				}
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
	
	public void setShootNormalAnimation(float duration, Array<TextureRegion> regions){
		shootNormalAnimation = new Animation(duration, regions);
		animationTime = 0;
	}
	
	public void setShootWalkAnimation(float duration, Array<TextureRegion> regions){
		shootWalkAnimation = new Animation(duration, regions);
		animationTime = 0;
	}
	
	public void setShootJumpAnimation(float duration, Array<TextureRegion> regions){
		shootJumpAnimation = new Animation(duration, regions);
		animationTime = 0;
	}
	
	public void setShootFalllAnimation(float duration, Array<TextureRegion> regions){
		shootFallAnimation = new Animation(duration, regions);
		animationTime = 0;
	}
	
	public void setShooting(boolean shooting){
		this.isShooting = shooting;
		animationTime = 0;
	}
	
	/**
	 * Resets player position to the stored checkpoint
	 */
	public void respawn(){
		setPosition(checkpoint.getX(), checkpoint.getY());
	}
	
	public void die(){
		// funcion de perder vida
		respawn();
	}
	
	public void setCheckpoint(Checkpoint checkpoint){
		this.checkpoint = checkpoint;
	}
	
	public boolean isShooting(){
		return isShooting;
	}

}
