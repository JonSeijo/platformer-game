package com.jashlaviu.platformer.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.utils.Array;
import com.jashlaviu.platformer.TextureLoader;
import com.sun.corba.se.impl.presentation.rmi.DynamicAccessPermission;

public class Player extends ActorJash {
	
	private Rectangle bounds, boundsNormal, boundsCrouch;
	private Checkpoint checkpoint;
	
	public enum State{
		WALKING, JUMPING, CROUCHING
	}
	
	private State state;	
	private Animation walkAnimation, jumpAnimation, fallAnimation, crouchAnimation, crouchWalkAnimation, dieAnimation;
	private Animation shootNormalAnimation, shootWalkAnimation, shootJumpAnimation, shootFallAnimation;
	
	private float FRICTION, MOVESPEED, MAX_VEL_X, MAX_VEL_Y;
	private boolean movingLeft, movingRight;
	
	private boolean dying;
	private float dyingTimer;
	
	private boolean isShooting, needShoot;
	private int shootsLeft, jumpTimes;
	
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
		
		shootsLeft = 5;
		shootTimer = 0;
		shootDelay = 0.2f;
		shootDelayAnimation = 0.2f;
		
		initializeAnimations();		
		setNormalRegion(TextureLoader.playerNormal);
		
		bounds = new Rectangle(getX()+12, getY()+1, 8, 27);
		boundsNormal = new Rectangle(getX()+13, getY()+1, 6, 27);
		boundsCrouch = new Rectangle(getX()-10, getY(), getWidth()-10, 14);
		
		setCollisionBounds(boundsNormal);	
	}
	
	public void updateHunger(float delta){
		hunger += delta*2;
		
		if(hunger >= 100){
			hunger = 100;
			die();
		}
	}
	
	public void updateX(float delta){		
		if(movingLeft){
			if(isCrouching()) velocity.x -= MOVESPEED * 0.5 * delta;
			else velocity.x -= MOVESPEED * delta;			
		}
		
		if(movingRight){
			if (isCrouching()) velocity.x += MOVESPEED * 0.5 * delta;
			else velocity.x += MOVESPEED * delta;			
		}
		
		velocity.x *= Math.exp((double)FRICTION * -delta);  //Aplly friction. This fixes  direfent friction in different fps
		
		if(Math.abs(velocity.x) < 5f) velocity.x = 0;	
		
		if(velocity.x > MAX_VEL_X) velocity.x = MAX_VEL_X;
		else if(velocity.x < -MAX_VEL_X)  velocity.x = -MAX_VEL_X;
		
		setX(getX() + velocity.x  * delta );
		
	}
	
	public void updateY(float delta){		
		velocity.y -= GRAVITY * delta;		
		setY(getY() + velocity.y * delta);
	}
	
	public void shoot(){
		if(shootsLeft > 0 && shootTimer > shootDelay && state != State.CROUCHING){
			shootsLeft--;
			shootTimer = 0;
			setShooting(true);
		}
	}
	
	public void crouch(){
		if(state == State.WALKING){
			state = State.CROUCHING;			
		//	boundsCrouch = new Rectangle(getX()+2, getY(), getWidth()-4, 8);
			boundsCrouch = new Rectangle(getX()+4, getY()+1, getWidth()-10, 12);
			setCollisionBounds(boundsCrouch);			
			animationTime = 0;
		}
	}
	
	public void stand(){
		if(state == State.CROUCHING){
			state = State.WALKING;
			//boundsNormal = new Rectangle(getX(), getY(), getWidth(), 15);
			boundsNormal = new Rectangle(getX()+13, getY()+1, 6, 27);
			setCollisionBounds(boundsNormal);
			animationTime = 0;
		}
	}
	
	public void updateState(float delta){			
		animationTime += delta;	
		shootTimer += delta;

		if(!isShooting()){
			if(state == State.WALKING){					
				if(velocity.x == 0){	//Not moving				
					animationTime = 0;
					setRegion(normalRegion);
				}
				else{ //Is moving
					setRegion(walkAnimation.getKeyFrame(animationTime, true));
				}			
			}
			else if(state == State.JUMPING){
				if(velocity.y == 0){				
					setRegion(normalRegion);				
				}else if(velocity.y > 0){ //NOT shooting and is jumping
					setRegion(jumpAnimation.getKeyFrame(animationTime, true));
					
				}else{  //NOT shooting and is falling
					setRegion(fallAnimation.getKeyFrame(0, false));			
				}
			}else if(state == State.CROUCHING){				
				if(!crouchAnimation.isAnimationFinished(animationTime)){
					setRegion(crouchAnimation.getKeyFrame(animationTime, false));
				}else{
					if(velocity.x == 0){
						setRegion(crouchAnimation.getKeyFrame(animationTime, false));
					}
					else{  //Is moving
						setRegion(crouchWalkAnimation.getKeyFrame(animationTime, true));
					}	
				}
				
				
			}
		}		

		
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
			
			if(shootTimer > shootDelayAnimation){
				setNeedShoot(true);
				setShooting(false);
			}
		}
		
		if(isDying()){
			dyingTimer += delta;	
			setRegion(dieAnimation.getKeyFrame(dyingTimer, false));
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
		if(jumpTimes < 2){
			velocity.y = 250;
			jumpTimes++;
			setState(State.JUMPING);		
		}
	}
	
	public void setState(State state){
		this.state = state;	
		if(state == State.WALKING){
			jumpTimes = 0;
		}
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
		crouchAnimation = new Animation(100f, getRegion());
		
		shootNormalAnimation = new Animation(100f, getRegion());
		shootWalkAnimation = new Animation(100f, getRegion());
		shootJumpAnimation = new Animation(100f, getRegion());
		shootFallAnimation = new Animation(100f, getRegion());		
		
		// before:       0.08f
		setWalkAnimation(0.07f, TextureLoader.playerWalk);		
		setJumpAnimation(.10f, TextureLoader.playerJump);
		setFallAnimation(.1f, TextureLoader.playerFall);
		
		crouchAnimation = new Animation(0.10f, TextureLoader.playerCrouch);
		crouchWalkAnimation = new Animation(0.10f, TextureLoader.playerCrouchWalk);
		dieAnimation = new Animation(0.07f, TextureLoader.playerDie);
		
		setShootNormalAnimation(shootDelayAnimation/(float)TextureLoader.playerShootNormal.size, TextureLoader.playerShootNormal);		
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
		addAction(Actions.color(Color.WHITE));
		shootsLeft = 5;
		dyingTimer = 0;
		dying = false;
		hunger = 0;
		setPosition(checkpoint.getX(), checkpoint.getY());
	}
	
	public void die(){
		// funcion de perder vida
		if(!dying){
			dying = true;
			this.addAction(new ParallelAction(Actions.moveTo(getX(), getY() + 40, 2), Actions.fadeOut(2)));
		}
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
	
	public void setShootsLeft(int shoots){
		shootsLeft = shoots;
	}
	
	public boolean isCrouching(){
		return (state == State.CROUCHING);
	}
	
	public boolean isWalking(){
		return (state == State.WALKING);
	}
	
	public int getShootsLeft(){
		return shootsLeft;
	}
	
	public State getState(){
		return state;
	}
	
	public boolean isDying(){
		return dying;
	}
	
	public boolean isDead(){
		return dyingTimer >= 2;
	}

}
