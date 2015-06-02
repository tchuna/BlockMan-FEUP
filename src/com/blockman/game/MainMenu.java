package com.blockman.game;

import android.content.Intent;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;

public class MainMenu extends SimpleBaseGameActivity {


    private static final int CAMERA_WIDTH = 1270;
    private static final int CAMERA_HEIGHT = 800;


    private BitmapTextureAtlas mBitmapTextureAtlas;
    private TiledTextureRegion mPlayerTextureRegion;
    private TiledTextureRegion mEnemyTextureRegion;

    private BitmapTextureAtlas myBackgroundTexture;

    private BitmapTextureAtlas level1;
    private BitmapTextureAtlas quit_bmp;
    private ITextureRegion level1_texture;
    private ITextureRegion quit_texture;

    private int tap = 0;

    private ITextureRegion mParallaxLayerBack;
    private ITextureRegion myLayerMid;
    private ITextureRegion myLayerFront;
    private Font myFont;
    private Font title_font;
    private Text txt;
    private Text title;

    private ButtonSprite lv1;
    private ButtonSprite quit;

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
    }

    @Override
    public Scene onCreateScene() {

        this.mEngine.registerUpdateHandler(new FPSLogger());
        final Scene scene = new Scene();
        final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-10.0f, new Sprite(0, CAMERA_HEIGHT - this.myLayerFront.getHeight(), this.myLayerFront, vertexBufferObjectManager)));
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-5.0f, new Sprite(0, 0, this.myLayerMid, vertexBufferObjectManager)));
        scene.setBackground(autoParallaxBackground);

        final float playerX = CAMERA_WIDTH / 2;
        final float playerY = 7 * CAMERA_HEIGHT / 9;

        //Adicionar texto

        final LoopEntityModifier blinkModifier = new LoopEntityModifier(
                new SequenceEntityModifier(new FadeOutModifier(0.50f), new FadeInModifier(0.50f)));


        txt.registerEntityModifier(blinkModifier);
        scene.attachChild(txt);

        scene.attachChild(title);

        //Adicionar player
        final AnimatedSprite player = new AnimatedSprite(playerX, playerY - 8, this.mPlayerTextureRegion, vertexBufferObjectManager);
        player.setScaleCenterY(this.mPlayerTextureRegion.getHeight());
        player.setScale(2);
        player.animate(new long[]{200, 200, 200, 200}, 20, 23, true);

        scene.attachChild(player);

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
                if(pItem == quit) {
                    finish();
                    System.exit(1);
                }else if(pItem == lv1){
                    Intent game = new Intent(getBaseContext(), Game.class);
                    game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(game);
                    finish();
                }
            }
        });

        lv1 =  new ButtonSprite(200, 100, level1_texture,  vertexBufferObjectManager)
        {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                                         float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    lv1.registerEntityModifier(click);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }};


        quit =  new ButtonSprite(CAMERA_WIDTH - 200, CAMERA_HEIGHT - 300 , quit_texture,  vertexBufferObjectManager)
             {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    quit.registerEntityModifier(click);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        }};


         scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.isActionDown()) {
                    if(tap == 0) {
                        scene.detachChild(txt);
                        scene.detachChild(title);
                        scene.attachChild(lv1);
                        scene.attachChild(quit);
                        scene.registerTouchArea(quit);
                        scene.registerTouchArea(lv1);
                    }
                    tap++;
                    return true;
                }
                return false;
            }

        });

        return scene;
    }

    @Override
    public void onCreateResources() {
        //Background
        this.myBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 3000, 1500);
        this.myLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.myBackgroundTexture, this, "background.jpg", 0, 0);
        this.myLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.myBackgroundTexture, this, "clouds.png", 0, 188);
        this.myBackgroundTexture.load();

        this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 192, 256, TextureOptions.BILINEAR);
        this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "man.png", 0, 0, 6, 4);
        this.mBitmapTextureAtlas.load();

        this.level1 = new BitmapTextureAtlas(this.getTextureManager(), 144, 144, TextureOptions.BILINEAR);
        this.level1_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.level1, this, "level1.png", 0, 0);
        this.level1.load();

        this.quit_bmp = new BitmapTextureAtlas(this.getTextureManager(), 144, 144, TextureOptions.BILINEAR);
        this.quit_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.quit_bmp, this, "quit.png", 0, 0);
        this.quit_bmp.load();

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

        this.txt = new Text(CAMERA_WIDTH / 2 - 160, CAMERA_HEIGHT / 2, myFont, "Tap to play",getVertexBufferObjectManager());
        this.title = new Text(CAMERA_WIDTH / 2 - 270, CAMERA_HEIGHT / 4, title_font, "BLOCK MAN",getVertexBufferObjectManager());
    }



}

