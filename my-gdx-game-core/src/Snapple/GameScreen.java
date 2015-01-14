package Snapple;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
	final Drop game;

	Array<Texture> letterTiles;
	Texture snakeImage;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle snake;
	Array<Rectangle> letters;
	Rectangle obstacle;
	
	long lastLetterTime;
	int lettersGathered;
	Texture tile;

	public GameScreen(final Drop gam) {
		this.game = gam;

		// load the images for the droplet and the snake, 64x64 pixels each
		letterTiles = new Array<Texture>();

		/*
		 * for(int i=0; i<26; ++i) { Texture tile = new
		 * Texture(Gdx.files.internal(i+".png")); letterTiles.add(tile); }
		 */
		snakeImage = new Texture(Gdx.files.internal("snake.png"));

		// load the drop sound effect and the rain background "music"
		// dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		// rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		// rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// create a Rectangle to logically represent the snake 
		//	OH LIKE A COLLISION DETECTOR SHIELD THING OMG
		snake = new Rectangle();
		snake.x = 800 / 2 - 32 / 2; // center the snake horizontally
		snake.y = 480 / 2 - 32 / 2; // bottom left corner of the snake is 20
									// pixels above
		// the bottom screen edge
		snake.width = 32;
		snake.height = 32;

		
//		obstacle = new Rectangle();
//		obstacle.x = 800/2 - 32/2;
//		obstacle.y = 480/2 - 32/2;
//		obstacle.width=20;
//		obstacle.height=60;
//		obstacle.se
		
		// create the letters array and spawn the first letter
		letters = new Array<Rectangle>();
		//spawnletter();

	}

	private void spawnletter() {
		Rectangle letter = new Rectangle();
		letter.x = MathUtils.random(0, 800 - 64);
		letter.y = MathUtils.random(0, 480 - 64);
		letter.width = 32;
		letter.height = 32;
		letters.add(letter);
		lastLetterTime = TimeUtils.nanoTime();
		
		if (letter.x < 100 - 84)
			letter.x = 100 - 84;
		if (letter.x > 830 - 84)
			letter.x= 830 - 84;
		if (letter.y < 160 - 84)
			letter.y = 160 - 84;
		if (letter.y > 450 - 84)
			letter.y= 450 - 84;
		

	}
	
	private Texture randTile()
	{
		//Randomly select character tile to spawn
		Random r = new Random();
//		char c = (char) (r.nextInt(26) + 'a');
		int i = r.nextInt(122 - 97) + 97;
		tile = new Texture(Gdx.files.internal("a.png"));
		return tile;
	}

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
				
		//Gdx.gl.glClearColor(0.2f, 0, 0.2f, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tile=randTile();
		
		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the snake and
		// all drops
		game.batch.begin();		
//		background
		Texture bg= new Texture(Gdx.files.internal("ingameBG/ingameBG.png"));
		game.batch.draw(bg,0,0);
//		score title
		game.font.draw(game.batch, "YOUR SCORE " + lettersGathered, 670,460);
//		snake
		game.batch.draw(snakeImage, snake.x, snake.y, 32, 32);
		
//		letter generation
		for (Rectangle letter : letters) {
			game.batch.draw(tile, letter.x, letter.y);
		}
		
		game.batch.end();

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			snake.x = touchPos.x - 64 / 2;
		}
		
		// TODO: rotate on keypress
		if (Gdx.input.isKeyPressed(Keys.LEFT)){
			snake.x -= 200 * Gdx.graphics.getDeltaTime();
			snakeImage = new Texture(Gdx.files.internal("snakeLeft.png"));}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)){
			snake.x += 200 * Gdx.graphics.getDeltaTime();
			snakeImage = new Texture(Gdx.files.internal("snakeRight.png"));}
		if (Gdx.input.isKeyPressed(Keys.UP)){
			snake.y += 200 * Gdx.graphics.getDeltaTime();
			snakeImage = new Texture(Gdx.files.internal("snake.png"));}
		if (Gdx.input.isKeyPressed(Keys.DOWN)){
			snake.y -= 200 * Gdx.graphics.getDeltaTime();
			snakeImage = new Texture(Gdx.files.internal("snakeDown.png"));}

		// make sure the snake stays within the screen bounds
		if (snake.x < 100 - 84)
			snake.x = 100 - 84;
		if (snake.x > 830 - 84)
			snake.x = 830 - 84;
		if (snake.y < 160 - 84)
			snake.y = 160 - 84;
		if (snake.y > 480 - 84)
			snake.y = 480 - 84;

		// check if we need to create a new letterdrop (every 1 sec)
		if (TimeUtils.nanoTime() - lastLetterTime > 1000000000)
			spawnletter();

		// move the letters, remove any that are beneath the bottom edge of
		// the screen or that hit the snake. In the later case we increase the
		// value our drops counter and add a sound effect.
		Iterator<Rectangle> iter = letters.iterator();
		while (iter.hasNext()) {
			Rectangle letter = iter.next();
			// letter.y -= 200 * Gdx.graphics.getDeltaTime();
			if (letter.y + 64 < 0)
				iter.remove();
			if (letter.overlaps(snake)) {
				//ADD CHAR TO EATEN ARRAY/STACK
				//CHECK STACK FOR WORDS
				lettersGathered++;
				// dropSound.play();
				iter.remove();
				
			}
		}
		
//		poop code
//		if(snake.overlaps(obstacle)){
//			System.out.println("ur ded");
//		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		// rainMusic.play();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		// letterImage.dispose();
		snakeImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}

}