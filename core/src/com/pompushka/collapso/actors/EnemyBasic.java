package com.pompushka.collapso.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.math.Interpolation.*;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.pompushka.collapso.Assets;
import com.pompushka.collapso.Core;


public class EnemyBasic extends Actor implements Poolable, Telegraph{
	
	//private TextureRegion tRegion1 = new TextureRegion(new Texture("enemy_basic_red.png"), 0,0,140,110);
	//private TextureRegion tRegion11 = new TextureRegion(new Texture("enemy_basic_red_1.png"), 0,0,140,110);
	//private TextureRegion tRegion2 = new TextureRegion(new Texture("enemy_basic_explo_1.png"), 0,0,140,110);
	//private TextureRegion tRegion3 = new TextureRegion(new Texture("enemy_basic_explo_2.png"), 0,0,140,110);
	
	private TextureRegion currentFrame;
	private Animation currentAnimation;
	private Animation routineAnimation;
	private Animation explodeAnimation;
	
	private float elapsedTime;
	private float explosionDuration;
	
	private Rectangle bounds;
	private int health = 50;
	private int score = 50;
	
	private Color basicColor;
	private Color hitColor;
	
	private float velX;
	private float velY;
	
	private boolean alive = false;
	//private CurrentState state;
	
	private void initShoting(){
    	Timer.schedule(new Task(){
            @Override
            public void run() {
            	//shot(activeEnemies.get(random.nextInt(activeEnemies.size)));
            	shot();
            }
        	}, (new Random().nextFloat())*3        //    (delay)
        	 , 2+(new Random().nextFloat())*5     //    (seconds)
    	);
    	stopShoting();
	}
	
	private void startShoting(){
		Timer.instance().start();
	}
	
	private void stopShoting(){
		Timer.instance().stop();
	}
	
	Random random = new Random();
	
	public EnemyBasic(){
		bounds = new Rectangle();
		routineAnimation = Assets.enemyAnim;
		explodeAnimation = Assets.enemyblows;
		routineAnimation.setPlayMode(PlayMode.LOOP);
		explosionDuration = explodeAnimation.getAnimationDuration();
		initShoting();
	}
	
	public void init(float X, float Y){
		this.setBounds(X, Y, 1.5f,1);
		setOrigin(getWidth()/2, getHeight()/2);
		bounds.set(X, Y, 1.5f,1);
		health = 50;
		alive = true;
		
		basicColor = getColor();
		hitColor = new Color(1,0,0,1);
		this.setColor(basicColor.r, basicColor.g, basicColor.b, 1.0f);
		currentAnimation = routineAnimation;
		this.addAction(forever(sequence(Actions.rotateTo(10f, 0.8f),Actions.rotateTo(-10,0.8f))));

		startShoting();
	}
	
	public boolean hit(int damage){
		return applyDamage(damage) ? true:false;
	}
	
	private boolean applyDamage(int damage){
		health -= damage;
		if  (health > 0) 
			this.addAction(sequence(color(hitColor, 0.1f),color(basicColor, 0.1f)));
		else 
			return true;
		return false;
	}
	
	@Override
	public void act (float delta){
		super.act(delta);
		bounds.setPosition(getX(), getY());
		if (getY() < -10) 
			this.setPosition(getX(), Core.applicationHeight);
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		currentFrame = currentAnimation.getKeyFrame(elapsedTime);
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		//batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight(), );
		batch.draw(currentFrame, getX(), getY(),  getOriginX(), getOriginY(), getWidth(), getHeight(),getScaleX(), getScaleY(), getRotation());
		batch.setColor(color.r, color.g, color.b, color.a);
		
		if (!alive && elapsedTime>=explosionDuration)
			Core.game.msgDispatcher.dispatchMessage(this, Core.Messages.ENEMY_FREE, this);
	}

	@Override
	public void reset() {
		if (this.getParent()!=null)	this.getParent().removeActor(this);
		alive = false;
	}
	
	public boolean getState(){
		return alive;
	}
	
	public void explode(){
		//state = CurrentState.EXPLODING;
		currentAnimation = explodeAnimation;
		elapsedTime = 0;
		this.clearActions();
		stopShoting();
		alive = false;
	}
	
	public Rectangle getBounds() {
        return bounds;
    }
	
	public int getScore(){
		return score;
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void shot(){
		Core.game.msgDispatcher.dispatchMessage(this, Core.Messages.MISSILE_SHOT, this);
	}
}
