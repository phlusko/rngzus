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
import com.badlogic.gdx.math.Vector2;

import rngzus.com.dd.p4u1.utils.PaulGraphics;

public class RNGZusGame extends ApplicationAdapter{
	public static final int D20 = 20;
	public static final int COIN = 2;
	public static final int D4 = 4;
	public static final int D6 = 6;
	public static final int D8 = 8;
	public static final int D10 = 10;
	public static final int D12 = 12;
	public static final int EIGHTBALL = 3;
	int[] types = {COIN, EIGHTBALL, D4, D6, D8, D10, D12, D20};
	String[] typeNames = {"Coin", "8ball", "d4", "d6", "d8", "d10", "d12", "d20"};
	String[] eightBall =
			{
					"It is certain",
					"It is decidedly so",
					"Without a doubt",
					"Yes definitely",
					"You may rely on it",
					"As I see it, yes",
					"Most likely",
					"Outlook good",
					"Yes",
					"Signs point to yes",
					"Reply hazy try again",
					"Ask again later",
					"Better not tell you now",
					"Cannot predict now",
					"Concentrate and ask again",
					"Don't count on it",
					"My reply is no",
					"My sources say no",
					"Outlook not so good",
					"Very doubtful"
			};
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
	BitmapFont morganSmall;
	boolean hasRandom = false;
	String randomString = "";
	float animationLength = 200;
	float stepRate = 2f;
	float animationTimer = 0;
	boolean animating = false;
	float alpha;
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
		camera = new OrthographicCamera();
		camera.setToOrtho(false, PaulGraphics.width, PaulGraphics.height);
		shapes = new ShapeRenderer();
		sprite.setSize(PaulGraphics.width, PaulGraphics.height);
		morgan =  new BitmapFont(Gdx.files.internal("morgan.fnt"),
				false);
		morganSmall =  new BitmapFont(Gdx.files.internal("morgan_small.fnt"),
				false);
		prefs = Gdx.app.getPreferences("rngzus-settings");
		showTute = prefs.getBoolean("show-tute", true);
		typeIndex = prefs.getInteger("type", 7);
	}

	public String getEightBall() {
		return eightBall[((int)(Math.random() * 20))];
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
			case EIGHTBALL:
				return getEightBall();
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
					prefs.putInteger("type", typeIndex);
					prefs.flush();
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
        batch.begin();
        sprite.draw(batch);
        morgan.setColor(Color.BLACK);
        morgan.draw(batch, typeNames[typeIndex], (PaulGraphics.width / 2) - (40 * typeNames[typeIndex].length()), PaulGraphics.height -20);
        if (showTute) {
			tute.draw(batch);
		}
		batch.end();

		if (hasRandom) {
			batch.begin();
			burst.draw(batch);
			burst.setAlpha(animationTimer / animationLength );
			morgan.setColor(Color.BLACK);
			morganSmall.setColor(Color.BLACK);
			if (randomString.length() > 2) {
				morganSmall.draw(batch, randomString, (PaulGraphics.width / 2) - (randomString.length() * 22), (PaulGraphics.height / 2) + 20);
			} else {
				morgan.draw(batch, randomString, (PaulGraphics.width / 2) - (randomString.length() * 40), (PaulGraphics.height / 2) + 40);
			}
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

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public static void main(String[] args) {
		System.out.println("wat");
		int heads = 0;
		int tails = 0;
		for (int i = 0; i < 1000000; i++) {
			if (i % 100 == 0) {
				System.out.println(i + " heads: " + heads + "  tails: " + tails);
			}
			if ((Math.random() * 2) >= 1){
				heads++;
			} else {
				tails++;
			}
		}
	}
}
