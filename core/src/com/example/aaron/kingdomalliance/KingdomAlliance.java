package com.example.aaron.kingdomalliance;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.awt.Label;

public class KingdomAlliance extends ApplicationAdapter implements GestureDetector.GestureListener {

	private SpriteBatch batch;
	private BitmapFont fontTitle, fontMessage, fontButton;
	private Texture imgMap, imgKingdom1, imgKingdom2;
	private Sprite spriteMap, spriteKingdom1, spriteKingdom2;
	private int screenWidth, screenHeight;
	private String message = "Touch Me";
	private String title = "Kingdom Alliance";
	private GlyphLayout layoutMessage, layoutTitle;
	private float widthTitle, heightTitle, widthMessage, heightMessage, buttonWidth;
	private OrthographicCamera camera;
	private TextButton buttonQuit, buttonTrain, buttonResources, buttonInformation;
	private TextButton.TextButtonStyle textButtonStyle;
	private Stage stageButton, stageKingdom;
	private Vector3 touchPoint, camPos;
	private Skin skinButton, skinKingdom;
	private TextureAtlas buttonAtlas, kingdomAtlas;
	private InputMultiplexer inputMultiplexer;
	private KingdomAllianceCallback kingdomAllianceCallback;
	private Table dialogKingdomTable;

	public interface KingdomAllianceCallback {
		public void onStartActivityLogin();
	}

	public void setKingdomAllianceCallback(KingdomAllianceCallback callback){
		kingdomAllianceCallback = callback;
	}

	public void intentToMainActivity(){
		Integer x = 1;
		if (kingdomAllianceCallback != null){
			if (x == 1){
				kingdomAllianceCallback.onStartActivityLogin();
			}
		}
	}

	@Override
	public void create () {

		batch = new SpriteBatch();
		layoutMessage = new GlyphLayout();
		layoutTitle = new GlyphLayout();

		setCamera();
		createImages();
		createFont();
		createButton();
		createKingdomDialog();
		createMultipleListeners();
	}

	@Override
	public void dispose() {
		batch.dispose();
		fontTitle.dispose();
		fontMessage.dispose();
		fontButton.dispose();
		imgMap.dispose();
		imgKingdom1.dispose();
		imgKingdom2.dispose();
		stageButton.dispose();
		stageKingdom.dispose();
		skinButton.dispose();
		skinKingdom.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1); //Color White
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //White background

		layoutMessage.setText(fontMessage, message);
		layoutTitle.setText(fontTitle, title);

		widthTitle = -layoutTitle.width/2;
		heightTitle = layoutTitle.height/2 + layoutTitle.height;
		widthMessage = -layoutMessage.width/2;
		heightMessage = layoutMessage.height/2 - layoutMessage.height;
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		spriteMap.draw(batch);
		spriteKingdom1.draw(batch);
		spriteKingdom2.draw(batch);
		fontTitle.draw(batch, title, widthTitle, heightTitle); //(0,0) center of image
		fontMessage.draw(batch, message, widthMessage, heightMessage);
		batch.end();

		stageButton.draw(); //draw buttons last separately works
		stageKingdom.draw();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		touchPoint = new Vector3();
		camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		if (spriteKingdom1.getBoundingRectangle().contains(touchPoint.x, touchPoint.y) || spriteKingdom2.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)){
			dialogKingdomTable.setVisible(true);
			return true;
		}
		else {
			title = "Kingdom Alliance";
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		camera.translate(-deltaX, deltaY);
		keepCameraInBounds();
		camera.update();
		return true;
	}
	
	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	public void createFont(){
		fontMessage = new BitmapFont(); // need to learn proper way to increase text size
		fontMessage.setColor(Color.BLUE);
		fontMessage.getData().scale(5);
		fontTitle = new BitmapFont();
		fontTitle.setColor(Color.GOLD);
		fontTitle.getData().scale(8);
		fontButton = new BitmapFont();
		fontButton.setColor(Color.BROWN);
	}

	public void createImages(){
		imgMap = new Texture("map.jpg");
		spriteMap = new Sprite(imgMap);
		spriteMap.setPosition(-spriteMap.getWidth() / 2, -spriteMap.getHeight() / 2); //image center is center on screen when started

		imgKingdom1 = new Texture("kingdom1.png");
		spriteKingdom1 = new Sprite(imgKingdom1);
		spriteKingdom1.setPosition(50, -1550);

		imgKingdom2 = new Texture("kingdom2.png");
		spriteKingdom2 = new Sprite(imgKingdom2);
		spriteKingdom2.setPosition(-150, 1450);
	}

	public void setCamera(){
		screenWidth = Gdx.graphics.getWidth();  //phones screen size in px
		screenHeight = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(screenWidth, screenHeight ); //area of pixel that camera will show
	}

	public void createButton(){
		stageButton = new Stage();
		skinButton = new Skin();
		buttonAtlas = new TextureAtlas("button/button.pack");
		skinButton.addRegions(buttonAtlas);
		textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = fontButton;
		textButtonStyle.up = skinButton.getDrawable("button.up");
		textButtonStyle.down = skinButton.getDrawable("button.down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		buttonQuit = new TextButton("Quit Map!!", textButtonStyle);
		buttonQuit.pad(20);
		buttonWidth = textButtonStyle.up.getMinWidth();
		buttonQuit.setPosition(screenWidth - buttonWidth, 0);
		buttonQuit.addListener(clickListener);
		stageButton.addActor(buttonQuit);
	}
	public void createKingdomDialog(){
		stageKingdom = new Stage();
		skinKingdom = new Skin();
		buttonAtlas = new TextureAtlas("button/button.pack");
		skinKingdom.addRegions(buttonAtlas);
		textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = fontButton;
		textButtonStyle.up = skinButton.getDrawable("button.up");
		textButtonStyle.down = skinButton.getDrawable("button.down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		buttonTrain = new TextButton("Train Troops", textButtonStyle);
		buttonTrain.pad(20);
		buttonWidth = textButtonStyle.up.getMinWidth();
		buttonTrain.setPosition(screenWidth - buttonWidth, 0);
		buttonTrain.addListener(clickListener);
		dialogKingdomTable = new Table(skinKingdom);
		dialogKingdomTable.setBounds(0, 0, screenWidth, screenHeight);
		dialogKingdomTable.add(buttonTrain);
		dialogKingdomTable.debug();
		dialogKingdomTable.setVisible(false);
		stageKingdom.addActor(dialogKingdomTable);
	}

	public void createMultipleListeners(){
		inputMultiplexer = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(this);
		inputMultiplexer.addProcessor(gd); //panning
		inputMultiplexer.addProcessor(stageButton);//button quit
		inputMultiplexer.addProcessor(stageKingdom);// button train troops
		Gdx.input.setInputProcessor(inputMultiplexer); //listening for all them
	}

	private void keepCameraInBounds () {
		camPos = camera.position;
		float HW = camera.viewportWidth / 2, HH = camera.viewportHeight / 2;
		camPos.x = MathUtils.clamp(camPos.x, spriteMap.getWidth()/2 - spriteMap.getWidth() + HW, spriteMap.getWidth() - spriteMap.getWidth()/2 - HW);
		camPos.y = MathUtils.clamp(camPos.y, spriteMap.getHeight()/2 - spriteMap.getWidth() + HH, spriteMap.getHeight() - spriteMap.getHeight()/2 - HH);
		message = Float.toString(camPos.x) + " " + Float.toString(camPos.y);
	}

	ClickListener clickListener = new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {
			Actor actor = event.getListenerActor();
			if (actor == buttonQuit){
				intentToMainActivity();
			}
			else if (actor == buttonTrain){
				message = "Training Troops";
				dialogKingdomTable.setVisible(false);
			}

		}
	};

}
