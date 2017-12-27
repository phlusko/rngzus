package rngzus.com.dd.p4u1;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import rngzus.com.dd.p4u1.utils.PaulGraphics;

public class RNGZusGame extends ApplicationAdapter implements GestureDetector.GestureListener {
	public static final int D20 = 20;
	public static final int COIN = 2;
	public static final int D4 = 4;
	public static final int D6 = 6;
	public static final int D8 = 8;
	public static final int D10 = 10;
	public static final int D12 = 12;
	int[] types = {COIN, D4, D6, D8, D10, D12, D20};
	String[] typeNames = {"Coin", "d4", "d6", "d8", "d10", "d12", "d20"};

	SpriteBatch batch;
	Texture img;
	OrthographicCamera camera;
    ShapeRenderer shapes;
	float step = 0;
	Sprite sprite;
	Sprite leftSprite;
	Sprite rightSprite;
	Texture leftCloud;
	Texture rightCloud;
	Texture burstTexture;
	Sprite burst;
	Texture tuteTexture;
	Sprite tute;
	Sound angels;
	int typeIndex = 6;
	BitmapFont morgan;
	boolean hasRandom = false;
	String randomString = "";

	boolean showTute = false;
	Preferences prefs;

	@Override
	public void create () {
		angels = Gdx.audio.newSound(Gdx.files.internal("angels.mp3"));
		batch = new SpriteBatch();
		img = new Texture("jesus.png");
		sprite = new Sprite(img);
		leftCloud = new Texture("cloud-left.png");
		leftSprite = new Sprite(leftCloud);
		leftSprite.setSize(PaulGraphics.width, PaulGraphics.height);
		rightCloud = new Texture("cloud-right.png");
		rightSprite = new Sprite(rightCloud);
		rightSprite.setSize(PaulGraphics.width, PaulGraphics.height);
		tuteTexture = new Texture("tute.png");
		tute = new Sprite(tuteTexture);
		tute.setSize(PaulGraphics.width, PaulGraphics.height);
		burstTexture = new Texture("burst.png");
		burst = new Sprite(burstTexture);
		burst.setSize(PaulGraphics.width, PaulGraphics.width);
		burst.setY((PaulGraphics.height / 2) - (PaulGraphics.width / 2));
		//burst.setY(PaulGraphics.height / 2);
		//burst.setCenterX(PaulGraphics.width / 2);
		//burst.setOrigin(PaulGraphics.width / 2,PaulGraphics.height / 2);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, PaulGraphics.width, PaulGraphics.height);
		shapes = new ShapeRenderer();
		sprite.setSize(PaulGraphics.width, PaulGraphics.height);
		morgan =  new BitmapFont(Gdx.files.internal("morgan.fnt"),
				false);
		prefs = Gdx.app.getPreferences("rngzus-settings");
		showTute = prefs.getBoolean("show-tute", true);
	}



	public String getRandomString(int type) {
		int dice = 0;
		switch(type) {
			case COIN:
				if ((Math.random() * 2) >= 1){
					return "Heads";
				} else {
					return "Tails";
				}
			case D4:
				dice = D4;
				break;
			case D6:
				dice = D6;
				break;
			case D8:
				dice = D8;
				break;
			case D10:
				dice = D10;
				break;
			case D12:
				dice = D12;
				break;
			case D20:
				dice = D20;
				break;
			default:
				return "Type not found";
		}
		return ((int)(Math.random() * dice) + 1) + "";
	}

	public String getRandomString() {
		return getRandomString(types[(int)(Math.random() * types.length)]);
	}

	float animationLength = 200;
	float stepRate = 2f;
	float animationTimer = 0;
	boolean animating = false;
float alpha;
	public void resetAnimation(){
		step = 0;
		animationTimer = 1;
		animating = true;
		alpha = 1f;
	}
	@Override
	public void render () {
		if (Gdx.input.justTouched()) {
			if (showTute) {
				showTute = false;
				prefs.putBoolean("show-tute", false);
				prefs.flush();
			} else {
				Vector2 input = PaulGraphics.pixelToCoord(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
				if (input.y > PaulGraphics.height - 200) {
					typeIndex++;
					if (typeIndex >= types.length) {
						typeIndex = 0;
					}
				} else {
					hasRandom = true;
					randomString = getRandomString(types[typeIndex]);
					angels.play();
					resetAnimation();
				}
			}
		}
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
		shapes.setProjectionMatrix(camera.combined);



        shapes.begin(ShapeRenderer.ShapeType.Line);

        for (int x = 0; x < PaulGraphics.width; x += 100) {
            shapes.setColor(Color.WHITE);
            //shapes.line(((float)x) + step, 0, ((float)x) + step, PaulGraphics.height);
        }
        shapes.end();
        batch.begin();
        sprite.draw(batch);
        morgan.setColor(Color.BLACK);
        morgan.draw(batch, typeNames[typeIndex], (PaulGraphics.width / 2) - (40 * typeNames[typeIndex].length()), PaulGraphics.height -30);
        if (showTute) {
			tute.draw(batch);
		}
		batch.end();

		if (hasRandom) {
			batch.begin();
			burst.draw(batch);
			//burst.rotate(2);
			burst.setAlpha(animationTimer / animationLength );
			morgan.setColor(Color.BLACK);
			morgan.draw(batch, randomString, (PaulGraphics.width / 2) - (randomString.length() * 40), (PaulGraphics.height / 2) + 40);
			batch.end();
		}
		if (animating) {
			step += stepRate;
			alpha = 1 - (animationTimer * (1 / animationLength));
			leftSprite.setColor(leftSprite.getColor().r,leftSprite.getColor().g, leftSprite.getColor().b, alpha);
			rightSprite.setColor(rightSprite.getColor().r,leftSprite.getColor().g, leftSprite.getColor().b, alpha);
			animationTimer++;
			if (animationTimer >= animationLength) {
				animating = false;
			}
			batch.begin();
			leftSprite.setX(0 - step);
			rightSprite.setX(0 + step);
			leftSprite.draw(batch);
			rightSprite.draw(batch);
			batch.end();
		}
	}


	public void logic(){
    }
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {

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
		return false;
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

	@Override
	public void pinchStop() {

	}
}
