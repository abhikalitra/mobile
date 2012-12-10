package com.abhikalitra.activity;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import javax.microedition.khronos.opengles.GL10;

/**
 * Copyright sushil
 * Date: 29 Dec, 2010
 * Time: 8:10:46 PM
 */
public class TankMasterMainActivity extends BaseGameActivity {

    private static final int CAMERA_WIDTH = 320;
    private static final int CAMERA_HEIGHT = 480;

    private Camera mCamera;
    private Texture mOnScreenControlTexture;    
    private Texture mTexture;

    private TiledTextureRegion mHelicopterTextureRegion;
    private TextureRegion mOnScreenControlBaseTextureRegion;
    private TextureRegion mOnScreenControlKnobTextureRegion;


    public Engine onLoadEngine() {
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new Engine(
                new EngineOptions(true, EngineOptions.ScreenOrientation.PORTRAIT,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
    }

    public void onLoadResources() {

        this.mTexture = new Texture(512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        TextureRegionFactory.setAssetBasePath("gfx/");
        this.mHelicopterTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "helicopter_tiled.png", 400, 0, 2, 2);

        this.mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mOnScreenControlBaseTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
        this.mOnScreenControlKnobTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);

        this.mEngine.getTextureManager().loadTextures(this.mTexture,mOnScreenControlTexture);

    }

    public Scene onLoadScene() {

        this.mEngine.registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene(1);
        scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

        /* Continuously flying helicopter. */
        final AnimatedSprite helicopter = new AnimatedSprite(0, CAMERA_HEIGHT - 50, this.mHelicopterTextureRegion);
        helicopter.animate(new long[] { 100, 100 }, 1, 2, true);
        scene.getTopLayer().addEntity(helicopter);

        /* Continuously flying helicopter. */
        final AnimatedSprite helicopter2 = new AnimatedSprite(CAMERA_WIDTH - 50, 0, this.mHelicopterTextureRegion);
        helicopter.animate(new long[] { 100, 100 }, 1, 2, true);
        scene.getTopLayer().addEntity(helicopter2);

        /*vertical lines */
        float xdist = CAMERA_HEIGHT / 10;        
        for(int i = 0; i < 10; i++) {
            final float y1 = i * xdist;
            final float lineWidth = 1;

            final Line line = new Line(0, y1, CAMERA_WIDTH, y1, lineWidth);
            line.setColor(0,0,0);
            scene.getTopLayer().addEntity(line);
        }

        float ydist =  CAMERA_WIDTH/ 10;
        for (int j = 0; j < 10; j++) {
            final float x1 = j * ydist;
            final float lineWidth = 1;

            final Line line = new Line(x1, 0, x1, CAMERA_HEIGHT, lineWidth);
            line.setColor(0,255,0);
            scene.getTopLayer().addEntity(line);
        }

        final DigitalOnScreenControl digitalOnScreenControl =
                new DigitalOnScreenControl(CAMERA_WIDTH /2 , CAMERA_HEIGHT - 100,
                        this.mCamera,
                        this.mOnScreenControlBaseTextureRegion,
                        this.mOnScreenControlKnobTextureRegion, 0.1f,

                        new BaseOnScreenControl.IOnScreenControlListener() {

                            public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
                                float x = helicopter.getX();
                                float y = helicopter.getY();
                                helicopter.setPosition(x + pValueX/100, y + pValueY/100);
                            }
                        });

        digitalOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        digitalOnScreenControl.getControlBase().setAlpha(0.5f);
        digitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
        digitalOnScreenControl.getControlBase().setScale(1.25f);
        digitalOnScreenControl.getControlKnob().setScale(1.25f);
        digitalOnScreenControl.refreshControlKnobPosition();

        scene.setChildScene(digitalOnScreenControl);
        
        return scene;        
    }

    public void onLoadComplete() {

    }
}
