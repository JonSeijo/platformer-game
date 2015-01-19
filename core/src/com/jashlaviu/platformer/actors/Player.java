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
	
	protected boolean isShooting, needShoot;
	private float shootTimer;
	private float shootDelay;
	private float shootDelayAnimation;
	
	private float hunger;

	public Player(Checkpoint checkpoint) {
		super(checkpoint.getX(), checkpoint.getY());
		this.checkpoint = checkpoint; 
		
		MAX_VEL_X = 150f;	
		FRICTION = 15f;		
		MOVESPEED = 1500f;
		state = State.JUMPING;	
		
		shootTimer = 0;
		//shootDelay = 0.08f;
		shootDelay = 0.4f;
		shootDelayAnimation = 0.4f;
		
		initializeAnimations();		
		setNormalRegion(TextureLoader.playerNormal);
		
		bounds = new Rectangle(getX()+12, getY()+1, 8, 27);
		setCollisionBounds(bounds);	
	}
	
	public void updateHunger(float delta){
		hunger += delta;
		
		if(hunger >= 100){
			hunger = 100;
			System.out.println("DIE FOR HUNGER");
		}
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
	
	public void shoot(){
		if(shootTimer > shootDelay){
			shootTimer = 0;
			setShooting(true);
		}
	}
	
	public void updateState(float delta){			
		animationTime += delta;		

		if(!isShooting()){
			if(state == State.WALKING){					
				if(velocity.x == 0){	//Not moving				
					animationTime = 0;
					setRegion(normalRegion);
				}
				else{ //Is moving
					setRegion(walkAnimation.getKeyFrame(animationTime, true));
					//animationTime += delta;
				}			
			}
			else if(state == State.JUMPING){
				if(velocity.y == 0){				
					setRegion(normalRegion);				
				}else if(velocity.y > 0){ //NOT shooting and is jumping
			//		animationTime += delta;
					setRegion(jumpAnimation.getKeyFrame(animationTime, true));
					
				}else{  //NOT shooting and is falling
					setRegion(fallAnimation.getKeyFrame(0, false));			
				}
			}
		}
		
		shootTimer += delta;
		
		if(isShooting()){			
			if(state == State.WALKING){					
				if(velocity.x == 0){//is standing
					setRegion(shootNormalAnimation.getKeyFrame(shootTimer, true));
				}else{				//is moving
					setRegion(shootWalkAnimation.getKeyFrame(shootTimer, true));			
				}				
			}
			else if(state == State.JUMPING){
				if(velocity.y == 0){
					setRegion(normalRegion);
				}
				else if(velocity.y > 0){	//is jumping
					setRegion(shootJumpAnimation.getKeyFrame(shootTimer, true));					
				}else{				//is falling
					setRegion(shootFallAnimation.getKeyFrame(shootTimer, true));					
				}
			}
			/*
			if(shootTimer > shootDelayAnimation){
				setNeedShoot(true);
				setShooting(false);
			}*/
			
			if(shootNormalAnimation.isAnimationFinished(animationTime)){
				setShooting(false);
				setNeedShoot(true);
			}
			if(shootWalkAnimation.isAnimationFinished(animationTime)){
				setShooting(false);
				setNeedShoot(true);
			}
			if(shootJumpAnimation.isAnimationFinished(animationTime)){
				setShooting(false);
				setNeedShoot(true);
			}
			if(shootFallAnimation.isAnimationFinished(animationTime)){
				setShooting(false);
				setNeedShoot(true);
			}
		}
	}
	
	public void eat(float hungerSatisfaction){
		//Sonido de comer maybe?
		hunger -= hungerSatisfaction;
		
		if(hunger < 0)
			hunger = 0;
		
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
	
	public void setNeedShoot(boolean needShoot){
		this.needShoot = needShoot;
	}
	
	public boolean needShoot(){
		return needShoot;
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
		
		setShootNormalAnimation(shootDelayAnimation/ (float)TextureLoader.playerShootNormal.size, TextureLoader.playerShootNormal);		
		setShootFalllAnimation(shootDelayAnimation/(float)TextureLoader.playerShootFall.size, TextureLoader.playerShootFall);
		setShootJumpAnimation(shootDelayAnimation/(float)TextureLoader.playerShootJump.size, TextureLoader.playerShootJump);		
		setShootWalkAnimation(shootDelayAnimation/(float)TextureLoader.playerShootWalk.size, TextureLoader.playerShootWalk);
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
		hunger = 0;
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
	
	public float getHunger(){
		return hunger;
	}

}
