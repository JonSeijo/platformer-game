package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.jashlaviu.platformer.TextureLoader;

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
		state = State.JUMPING;	
		
		initializeAnimations();		
		setNormalRegion(TextureLoader.playerNormal);				
		
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
	
	private void initializeAnimations(){
		walkAnimation = new Animation(100f, getRegion());
		jumpAnimation = new Animation(100f, getRegion());
		fallAnimation = new Animation(100f, getRegion());		
		
		shootNormalAnimation = new Animation(100f, getRegion());
		shootWalkAnimation = new Animation(100f, getRegion());
		shootJumpAnimation = new Animation(100f, getRegion());
		shootFallAnimation = new Animation(100f, getRegion());		
		
		setWalkAnimation(0.08f, TextureLoader.playerWalk);		
		setJumpAnimation(.10f, TextureLoader.playerJump);
		setFallAnimation(.1f, TextureLoader.playerFall);
		
		setShootNormalAnimation(0.08f, TextureLoader.playerShootNormal);		
		setShootFalllAnimation(0.08f, TextureLoader.playerShootFall);
		setShootJumpAnimation(0.08f, TextureLoader.playerShootJump);		
		setShootWalkAnimation(0.08f, TextureLoader.playerShootWalk);
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
