package com.pompushka.collapso;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	
	public static Texture background1;
	public static Texture background2;
	public static Texture heroTex;
	public static Texture bulletTex;
	public static Texture bulletRedTex;
	public static Texture enemyTex[] = new Texture[4];
	public static Texture enemyExplosionTex;
	
	public static TextureRegion bkgnd1;
	public static TextureRegion hero;
	public static TextureRegion bullet[] = new TextureRegion[2];
	public static TextureRegion bulletRed;
	public static TextureRegion missile;
	public static TextureRegion enemyExplosionFrames[] = new TextureRegion[13];
	public static Animation enemyAnim;
	public static Animation enemyblows;
	
	public static BitmapFont font;
	
	public static Sound shotSound;
	public static Sound bangSound;
	public static Sound hitSound;
	
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file),true);
	}
	
	public static void load () {
		background1 = loadTexture("bkgnd1.png");
		background2 = loadTexture("bkgnd2.png");
		heroTex = loadTexture("textures/hero.png");
		bulletTex = loadTexture("bullet_basic.png");
		bulletRedTex = loadTexture("bullet_red.png");
		enemyTex[0] = loadTexture("enemy_basic_red.png");
		enemyTex[1] = loadTexture("enemy_basic_red_1.png");
		enemyTex[2] = loadTexture("enemy_basic_explo_1.png");
		enemyTex[3] = loadTexture("enemy_basic_explo_2.png");
		enemyExplosionTex = loadTexture("textures/explosion.png");
		
		
		background1.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		background2.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		//bkgnd1 = new TextureRegion(background1,100,100,1100,800);
		hero = new TextureRegion(heroTex, 0,0,150,120);
		//hero = new TextureRegion(enemyExplosionTex, 0,36,36,36);
		bullet[0] = new TextureRegion(bulletTex, 0,0,10,20);
		bullet[1] = new TextureRegion(bulletRedTex, 0,0,10,20);
		missile = new TextureRegion(bulletTex, 0,0,10,20);
		
		for (int i=0;i<6;i++)
			enemyExplosionFrames[i] = new TextureRegion(enemyExplosionTex, i*36,4,36,36);
		for (int i=0;i<6;i++)
			enemyExplosionFrames[5+i] = new TextureRegion(enemyExplosionTex, i*36,40,36,36);
		for (int i=0;i<2;i++)
			enemyExplosionFrames[11+i] = new TextureRegion(enemyExplosionTex, i*36,2*40,36,36);
		
		enemyTex[0].setFilter(TextureFilter.MipMap, TextureFilter.Nearest);
		enemyTex[1].setFilter(TextureFilter.MipMap, TextureFilter.Nearest);
		
		enemyAnim = new Animation(0.5f, new TextureRegion(enemyTex[0], 0,0,140,110), new TextureRegion(enemyTex[1], 0,0,140,110));
		//enemyblows = new Animation(0.5f, new TextureRegion(enemyTex[2], 0,0,140,110), new TextureRegion(enemyTex[3], 0,0,140,110));
		enemyblows = new Animation(0.025f, enemyExplosionFrames);
		
		//font = new BitmapFont(Gdx.files.internal("fonts/font1.fnt"));
		font = new BitmapFont(Gdx.files.internal("fonts/scores_font_pixel_millenium.fnt"));
		//font = new BitmapFont(Gdx.files.internal("fonts/scores_font.fnt"));
		//font = new BitmapFont(Gdx.files.internal("fonts/scores_font_color_basic.fnt"));
		
		shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));
		bangSound = Gdx.audio.newSound(Gdx.files.internal("bang.wav"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
	}
	
	public static void playSound (Sound sound) {
		if (Settings.soundOn) sound.play(1);
	}
}
