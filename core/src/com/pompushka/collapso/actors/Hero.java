package com.pompushka.collapso.actors;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.pompushka.collapso.Assets;
import com.pompushka.collapso.Core;
import com.pompushka.collapso.actors.bullets.Gun;
import com.pompushka.collapso.actors.bullets.Weapon_1;
import com.pompushka.collapso.actors.bullets.Weapon_2;

public class Hero extends Group implements Telegraph{
	
	private TextureRegion tRegion = Assets.hero;
	private int totalHealth = 450;
	private int health = totalHealth;
	private float velocity = Core.HERO_SPEED;
	private int direction = 0;
	private Color color;
	private Gun gun;
	private Rectangle bounds = new Rectangle();
	
	public Hero(){
		this.setBounds(Core.viewPortWidth*0.5f, 0, 1.5f, 1.2f);
		bounds.set(Core.viewPortWidth*0.5f, 0, 1, 1);
		gun = new Weapon_1(getWidth()*0.5f, getHeight());
		this.addActor(gun);
		this.addListener(new HeroTouchListener());
		Core.game.msgDispatcher.addListener(this, Core.Messages.PADS);
	}
	
	@Override
	public void act (float delta){
		super.act(delta);
		//gun.update(getX(), getY(), delta);
		if (direction!=0){
			float newPos = getX()+velocity*delta*direction;
			if (newPos > 0 && newPos < Core.viewPortWidth-getWidth())
				setX(newPos);
		}
		bounds.setPosition(getX(), getY());	
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(tRegion, getX(), getY(), getWidth(), getHeight());
		batch.setColor(color.r, color.g, color.b, color.a);
	}
	
	public int applyDamage(int damage){
		health -= damage;
		if (health<0) health = 0;
		Assets.playSound(Assets.hitSound);
		return (int) getHealthInPercentage();
	}
	
	public float getHealthInPercentage(){
		return health*100/totalHealth;
	}
	
	public void moveLeft(){
		direction = -1;
	}
	public void moveRight(){
		direction = 1;
	}
	public void moveStop(){
		direction = 0;
	}
	
	public Rectangle getBounds() {
        return bounds;
    }	
	
	public void setDirection(int dir){
		direction = dir;
	}
	
	public int getDirection(){
		return direction;
	}
	
	public Gun getGun(){
		return gun;
	}
	
	@Override
	public boolean handleMessage(Telegram msg) {
		if (msg.message == Core.Messages.PADS){
			int state = (Integer)msg.extraInfo;

			switch (state){
				case InvisiblePads.PadState.PAD_LEFT_DN:
					setDirection(getDirection()-1);break;
				case InvisiblePads.PadState.PAD_LEFT_UP:
					setDirection(getDirection()+1);break;
				case InvisiblePads.PadState.PAD_RIGHT_DN:
					setDirection(getDirection()+1);break;
				case InvisiblePads.PadState.PAD_RIGHT_UP:
					setDirection(getDirection()-1);break;
			}
		}
		return false;
	}
	
	public class HeroTouchListener extends InputListener{
	    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	        System.out.println("Hero down");
	        Core.isPaused = !Core.isPaused;
	        return true;
	    }

	    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        System.out.println("Hero up");
	    }
	}
}
