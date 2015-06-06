package com.blockman.game;

import java.io.IOException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

public class MainMenu extends SimpleBaseGameActivity {


    private static final int CAMERA_WIDTH = 1270;
    private static final int CAMERA_HEIGHT = 800;

    private BitmapTextureAtlas mBitmapTextureAtlas;
    private TiledTextureRegion mPlayerTextureRegion;

    private BitmapTextureAtlas myBackgroundTexture;

    private BitmapTextureAtlas level1;
    private ITextureRegion level1_texture;
    
    private BitmapTextureAtlas level2;
    private ITextureRegion level2_texture;
    
    private BitmapTextureAtlas level3;
    private ITextureRegion level3_texture;
    
    private BitmapTextureAtlas level4;
    private ITextureRegion level4_texture;
    
    private BitmapTextureAtlas level5;
    private ITextureRegion level5_texture;
    
    private BitmapTextureAtlas level6;
    private ITextureRegion level6_texture;
    
    private BitmapTextureAtlas quit_bmp;
    private ITextureRegion quit_texture;

    private int tap = 0;

    private ITextureRegion myLayerMid;
    private ITextureRegion myLayerFront;
    
    private Font myFont;
    private Font title_font;
    private Font level_font;
    
    private Text txt;
    private Text title;
    private Text pick_level;

    private ButtonSprite lv1;
    private ButtonSprite lv2;
    private ButtonSprite lv3;
    private ButtonSprite lv4;
    private ButtonSprite lv5;
    private ButtonSprite lv6;
    
    private ButtonSprite quit;
    
    //--------
  	//MUSIC
  	private Music musicPlayer;
  	private BitmapTextureAtlas play_btn_bmp;
	private ITextureRegion play_btn_texture;
	private ButtonSprite play_btn;
  	//--------
    
    private Scene scene;

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
		options.getAudioOptions().setNeedsMusic(true);
		options.getAudioOptions().setNeedsSound(true);
        return options;
    }

    @Override
    public Scene onCreateScene() {

        this.mEngine.registerUpdateHandler(new FPSLogger());
        scene = new Scene();
        final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-10.0f, new Sprite(0, CAMERA_HEIGHT - this.myLayerFront.getHeight(), this.myLayerFront, vertexBufferObjectManager)));
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-5.0f, new Sprite(0, 0, this.myLayerMid, vertexBufferObjectManager)));
        scene.setBackground(autoParallaxBackground);


        //Adicionar texto

        final LoopEntityModifier blinkModifier = new LoopEntityModifier(
                new SequenceEntityModifier(new FadeOutModifier(0.50f), new FadeInModifier(0.50f)));
        
        //Adicionar player
        final float playerX = CAMERA_WIDTH / 2 - 30;
        final float playerY = 7 * CAMERA_HEIGHT / 9 - 68;
        final AnimatedSprite player = new AnimatedSprite(playerX, playerY , this.mPlayerTextureRegion, vertexBufferObjectManager);
        player.setScaleCenterY(this.mPlayerTextureRegion.getHeight());
        player.animate(new long[]{40, 40, 40, 40, 40,
        							40, 40, 40, 40, 40,
        							40, 40, 40, 40, 40,
        							40}, /*1*/32, /*16*/47, true);
        player.setScale((float)0.7);

        scene.attachChild(player);
        
        play_btn = new ButtonSprite(1050, 50, play_btn_texture, vertexBufferObjectManager){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
						if (musicPlayer.isPlaying())
						{
							play_btn.setAlpha((float) 0.3);
							SharedPreferences settings = getSharedPreferences("data", 0);
							SharedPreferences.Editor editor = settings.edit();
							editor.putBoolean("sound", false);
							editor.commit();

							//play_btn_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.play_btn_bmp, this, "stop.png", 0, 0);
							musicPlayer.pause();
						}
						else
						{
							play_btn.setAlpha(1);
							SharedPreferences settings = getSharedPreferences("data", 0);
							SharedPreferences.Editor editor = settings.edit();
							editor.putBoolean("sound", true);
							editor.commit();
							//play_btn_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.play_btn_bmp, this, "play.png", 0, 0);
							musicPlayer.resume();
						}
					}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}};

        //quit animation

        final SequenceEntityModifier click = new SequenceEntityModifier(new FadeOutModifier(0.10f), new FadeInModifier(0.10f){
            @Override
            protected void onModifierStarted(IEntity pItem)
            {
                super.onModifierStarted(pItem);
                // Your action after starting modifier
            }

            @Override
            protected void onModifierFinished(IEntity pItem)
            {
                super.onModifierFinished(pItem);
                SharedPreferences settings = getSharedPreferences("data", 0);
                int level = settings.getInt("currLevel", -1);
                
                if(pItem == quit) {
                    finish();
                    System.exit(1);
                }else if(pItem == lv1){
                    Intent game = new Intent(getBaseContext(), Game.class);
                    game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    game.putExtra("level", "1");
                    startActivity(game);
                    finish();
                }
                else if(pItem == lv2){
                	if(level < 2){
                		return;
                	}
                    Intent game = new Intent(getBaseContext(), Game.class);
                    game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    game.putExtra("level", "2");
                    startActivity(game);
                    finish();
                }
                else if(pItem == lv3){
                    Intent game = new Intent(getBaseContext(), Game.class);
                    game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    game.putExtra("level", "3");
                    startActivity(game);
                    finish();
                }
                else if(pItem == lv4){
                    Intent game = new Intent(getBaseContext(), Game.class);
                    game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    game.putExtra("level", "4");
                    startActivity(game);
                    finish();
                }
                else if(pItem == lv5){
                    Intent game = new Intent(getBaseContext(), Game.class);
                    game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    game.putExtra("level", "5");
                    startActivity(game);
                    finish();
                }
                else if(pItem == lv6){
                    Intent game = new Intent(getBaseContext(), Game.class);
                    game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    game.putExtra("level", "6");
                    startActivity(game);
                    finish();
                }
            }
        });

        lv1 =  new ButtonSprite(200, 300, level1_texture,  vertexBufferObjectManager)
        {
        	@Override
        	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
        			float pTouchAreaLocalX, float pTouchAreaLocalY) {
        		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
        			lv1.registerEntityModifier(click);
        		}
        		return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        	}};

        	lv2 =  new ButtonSprite(400, 300, level2_texture,  vertexBufferObjectManager)
        	{
        		@Override
        		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
        				float pTouchAreaLocalX, float pTouchAreaLocalY) {
        			if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
        				lv2.registerEntityModifier(click);
        			}
        			return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        		}};

        		lv3 =  new ButtonSprite(600, 300, level3_texture,  vertexBufferObjectManager)
        		{
        			@Override
        			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
        					float pTouchAreaLocalX, float pTouchAreaLocalY) {
        				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
        					lv3.registerEntityModifier(click);
        				}
        				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        			}};

        			lv4 =  new ButtonSprite(800, 300, level4_texture,  vertexBufferObjectManager)
        			{
        				@Override
        				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
        						float pTouchAreaLocalX, float pTouchAreaLocalY) {
        					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
        						lv4.registerEntityModifier(click);
        					}
        					return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        				}};

        				lv5 =  new ButtonSprite(1000, 300, level5_texture,  vertexBufferObjectManager)
        				{
        					@Override
        					public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
        							float pTouchAreaLocalX, float pTouchAreaLocalY) {
        						if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
        							lv5.registerEntityModifier(click);
        						}
        						return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        					}};

        					lv6 =  new ButtonSprite(400, 400, level6_texture,  vertexBufferObjectManager)
        					{
        						@Override
        						public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
        								float pTouchAreaLocalX, float pTouchAreaLocalY) {
        							if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
        								lv6.registerEntityModifier(click);
        							}
        							return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        						}};


        quit =  new ButtonSprite(CAMERA_WIDTH - 200, CAMERA_HEIGHT - 200 , quit_texture,  vertexBufferObjectManager)
             {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    quit.registerEntityModifier(click);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        }};

        quit.setScale((float)0.8);
         scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.isActionDown()) {
                    if(tap == 0) {
						SharedPreferences settings = getSharedPreferences("data", 0);
                        int silent = settings.getInt("currLevel", -1);
                        if(silent == -1){
                        	Intent tut = new Intent(getBaseContext(), Tutorial.class);
                        	tut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        	startActivity(tut);
                        	MainMenu.this.finish();
                        	return true; 
                        }else{
                        	scene.detachChild(txt);
                        	scene.detachChild(title);
                        	attach_levels();
                        }
                    }
                    tap++;
                    return true;
                }
                return false;
            }

        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String data = (String)extras.getString("isBack");
            if(data == null){
            	Log.d("BlockMan", "intent data retrieves null");
           	 	txt.registerEntityModifier(blinkModifier);
                scene.attachChild(txt);
                scene.attachChild(title);
            }else if(data.equals("back")){
            	Log.d("BlockMan", "retrieves 'back', going to pick level scene");
            	attach_levels();
            	tap++;
            }
        }else{
        	Log.d("BlockMan", "intent data retrieves null");
        	 txt.registerEntityModifier(blinkModifier);
             scene.attachChild(txt);
             scene.attachChild(title);
        }
        return scene;
    }


	@Override
    public void onCreateResources() {

		
        //Background
        this.myBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 3000, 1500);
        this.myLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.myBackgroundTexture, this, "background.jpg", 0, 0);
        this.myLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.myBackgroundTexture, this, "clouds.png", 0, 188);
        this.myBackgroundTexture.load();
        Log.d("BlockMan", "Starting to load player");
        long current_time = System.currentTimeMillis();
        this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 2048, 4000, TextureOptions.BILINEAR);
        this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "mustache_man_3.png", 0, 0, 16, 31);
        this.mBitmapTextureAtlas.load();
        
        Log.d("BlockMan", "Player loaded, it tooked " +  (System.currentTimeMillis() - current_time) + " ms");

        this.level1 = new BitmapTextureAtlas(this.getTextureManager(), 144, 144, TextureOptions.BILINEAR);
        this.level1_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.level1, this, "level1.png", 0, 0);
        this.level1.load();
        
        this.level2 = new BitmapTextureAtlas(this.getTextureManager(), 144, 144, TextureOptions.BILINEAR);
        this.level2_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.level2, this, "level2.png", 0, 0);
        this.level2.load();
        
        this.level3 = new BitmapTextureAtlas(this.getTextureManager(), 144, 144, TextureOptions.BILINEAR);
        this.level3_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.level3, this, "level3.png", 0, 0);
        this.level3.load();
        
        this.level4 = new BitmapTextureAtlas(this.getTextureManager(), 144, 144, TextureOptions.BILINEAR);
        this.level4_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.level4, this, "level4.png", 0, 0);
        this.level4.load();
        
        this.level5 = new BitmapTextureAtlas(this.getTextureManager(), 144, 144, TextureOptions.BILINEAR);
        this.level5_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.level5, this, "level5.png", 0, 0);
        this.level5.load();
        
        this.level6 = new BitmapTextureAtlas(this.getTextureManager(), 144, 144, TextureOptions.BILINEAR);
        this.level6_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.level6, this, "level6.png", 0, 0);
        this.level6.load();
        
        this.quit_bmp = new BitmapTextureAtlas(this.getTextureManager(), 144, 144, TextureOptions.BILINEAR);
        this.quit_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.quit_bmp, this, "quit.png", 0, 0);
        this.quit_bmp.load();
        
		this.play_btn_bmp = new BitmapTextureAtlas(this.getTextureManager(), 144, 144, TextureOptions.BILINEAR);
		this.play_btn_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.play_btn_bmp, this, "play.png", 0, 0);
		this.play_btn_bmp.load();

        myFont = FontFactory.createFromAsset(mEngine.getFontManager(),
                mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
                this.getAssets(), "fonts/3Dumb.ttf", 60f, true,
                Color.BLACK_ABGR_PACKED_INT);
        myFont.load();

        title_font = FontFactory.createFromAsset(mEngine.getFontManager(),
                mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
                this.getAssets(), "fonts/3Dumb.ttf", 90f, true,
                Color.BLACK_ABGR_PACKED_INT);
        title_font.load();
        
        level_font = FontFactory.createFromAsset(mEngine.getFontManager(),
                mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
                this.getAssets(), "fonts/3Dumb.ttf", 75f, true,
                Color.BLACK_ABGR_PACKED_INT);
        level_font.load();
        
        
        try {
        	if(musicPlayer != null){
        		musicPlayer.release();
        	}
			this.musicPlayer = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this,"music.mp3");
			this.musicPlayer.setLooping(true);
			Log.d("BlockMan", "Leu musica");
		} catch (IllegalStateException e) {
			Log.d("BlockMan", "Erro a ler musica");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("BlockMan", "Erro a ler musica");
			e.printStackTrace();
		}	
        
        this.txt = new Text(CAMERA_WIDTH / 2 - 160, CAMERA_HEIGHT / 2, myFont, "Tap to play",getVertexBufferObjectManager());
        this.title = new Text(CAMERA_WIDTH / 2 - 270, CAMERA_HEIGHT / 4, title_font, "BLOCK MAN",getVertexBufferObjectManager());
        this.pick_level = new Text(CAMERA_WIDTH / 2 - 245, CAMERA_HEIGHT / 4 - 130, level_font, "PICK A LEVEL",getVertexBufferObjectManager());
    }


	void attach_levels(){
		SharedPreferences settings = getSharedPreferences("data", 0);
        int level = settings.getInt("currLevel", -1);
        
        
        scene.attachChild(play_btn);
		scene.registerTouchArea(play_btn);
        boolean sound = settings.getBoolean("sound", true);
        if(sound){
        	musicPlayer.play();
        	musicPlayer.resume();
        	play_btn.setAlpha(1f);
        }else{
        	play_btn.setAlpha(0.4f);
        }

    	scene.attachChild(pick_level);
    	scene.attachChild(lv1);
    	scene.attachChild(lv2);
    	scene.attachChild(lv3);
    	scene.attachChild(lv4);
    	scene.attachChild(lv5);
    	//scene.attachChild(lv6);
    	scene.attachChild(quit);
    	scene.registerTouchArea(quit);
    	scene.registerTouchArea(lv1);
    	
    	if(level < 2) lv2.setAlpha(0.4f);
    	else scene.registerTouchArea(lv2);
    	
    	if(level < 3) lv3.setAlpha(0.4f);
    	else scene.registerTouchArea(lv3);
    	
    	if(level < 4) lv4.setAlpha(0.4f);
    	else scene.registerTouchArea(lv4);
    	
    	if(level < 5) lv5.setAlpha(0.4f);
    	else scene.registerTouchArea(lv5);
	}


}