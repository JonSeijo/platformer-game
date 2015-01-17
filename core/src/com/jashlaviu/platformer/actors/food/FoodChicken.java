package com.jashlaviu.platformer.actors.food;

import com.jashlaviu.platformer.TextureLoader;

public class FoodChicken extends Food{

	public static String name = "chicken";
	
	public FoodChicken(float posX, float posY) {
		super(posX, posY);		
		setIdleAnimation(1f, TextureLoader.foodChicken);				
	}

}
