package Snapple;

import java.io.IOException;
import java.util.ArrayList;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameScreen implements Screen {
	final Drop game;

	Array<Texture> letterTiles;
	Texture snakeImage;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle snake;
	ArrayList<Rectangle> letters;
	Rectangle obstacle;
	Rectangle obstacle2;

	Music bgMusic;

	long lastLetterTime;
	int lettersGathered;
	Texture tile;
	char let;
	Texture bg;

	Texture obstacleImage;
	Texture obstacleImage2;
	
	Texture foundWord;

	String eatenLetters;
	StringBuilder sb;

	String wordFound;
	Array<String> wordSack;

	public GameScreen(final Drop gam) {
		this.game = gam;

		eatenLetters = " ";
		wordFound = " ";
		sb = new StringBuilder();
		wordSack = new Array<String>();
		// load the images for the droplet and the snake, 64x64 pixels each
		letterTiles = new Array<Texture>();

		/*
		 * for(int i=0; i<26; ++i) { Texture tile = new
		 * Texture(Gdx.files.internal(i+".png")); letterTiles.add(tile); }
		 */
		snakeImage = new Texture(Gdx.files.internal("snake.png"));
		
		foundWord = new Texture(Gdx.files.internal("buttons/found_word.png"));
		
		bg = new Texture(Gdx.files.internal("ingameBG/ingameBG.png"));

		// load the drop sound effect and the rain background "music"
		// dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("music/bg_music.mp3"));
		bgMusic.setLooping(true);

		obstacleImage = new Texture(Gdx.files.internal("ingameBG/block1.png"));
		obstacleImage2 = new Texture(Gdx.files.internal("ingameBG/block2.png"));

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// create a Rectangle to logically represent the snake
		// OH LIKE A COLLISION DETECTOR SHIELD THING OMG
		snake = new Rectangle();
		snake.x = 800 / 2 - 32 / 2; // center the snake horizontally
		snake.y = 480 / 2 - 32 / 2; // bottom left corner of the snake is 20
									// pixels above
		// the bottom screen edge
		snake.width = 32;
		snake.height = 32;

		obstacle = new Rectangle();
		obstacle.x = 500;
		obstacle.y = 480 / 2 - 32 / 2;
		obstacle.width = 250;
		obstacle.height = 42;

		obstacle2 = new Rectangle();
		obstacle2.x = 200;
		obstacle2.y = 10;
		obstacle2.width = 25;
		obstacle2.height = 160;

		// create the letters array and spawn the first letter
		letters = new ArrayList<Rectangle>();
		// let=randTile();
		spawnletter();

	}

	private void spawnletter() {
		tile = randTile();
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
			letter.x = 830 - 84;
		if (letter.y < 160 - 84)
			letter.y = 160 - 84;
		if (letter.y > 450 - 84)
			letter.y = 450 - 84;

	}

	private Texture randTile() {
		// Randomly select character tile to spawn
		Random r = new Random();
		char c = (char) (r.nextInt(26) + 'a');
		// let = c;
		// int i = r.nextInt(122 - 97) + 97;
		tile = new Texture(Gdx.files.internal(c + ".png"));

		return tile;
	}

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.

		// Gdx.gl.glClearColor(0.2f, 0, 0.2f, 1);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tile=randTile();

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the snake and
		// all drops
		game.batch.begin();
		// background

		game.batch.draw(bg, 0, 0);
		// score title
		game.font.draw(game.batch, "YOUR SCORE " + lettersGathered, 670, 460);

		// snake

		// obstacle collsion poopy

		game.batch.draw(obstacleImage, obstacle.x, obstacle.y, 550, 42);
		game.batch.draw(obstacleImage2, obstacle2.x, obstacle2.y, 25, 200);

		game.batch.draw(snakeImage, snake.x, snake.y, 32, 32);

		// your score font
		game.font.draw(game.batch, "YOUR LETTERS: " + eatenLetters, 25, 50);

		// word found
		game.font.draw(game.batch, "LAST WORD FOUND: " + wordFound, 600, 50);

		// word sack
		game.font.draw(game.batch, "WORD SACK: " + wordSack, 600, 25);
		int len = letters.size();
		// letter generation

		// for(int i =0; i < len; i++)
		// {
		int i = 0;
		// for (Rectangle letter : letters)
		// {
		// while(i<len)

		game.batch.draw(tile, letters.get(len - 1).x, letters.get(len - 1).y);

		// i++;}

		// }

		// tile=randTile();

		// for (int i= 0; i < letters.size(); i++)
		// {
		//
		// game.batch.draw(tile, letters.get(letters.size()-1).x,
		// letters.get(letters.size()-1).y);
		//
		// }

		game.batch.end();

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			snake.x = touchPos.x - 64 / 2;
			snake.y = 480 / 2 - 32 / 2; // bottom left corner of the snake is 20
		}

		// TODO: rotate on keypress
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			snake.x -= 200 * Gdx.graphics.getDeltaTime();
			snakeImage = new Texture(Gdx.files.internal("snakeLeft.png"));
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			snake.x += 200 * Gdx.graphics.getDeltaTime();
			snakeImage = new Texture(Gdx.files.internal("snakeRight.png"));
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			snake.y += 200 * Gdx.graphics.getDeltaTime();
			snakeImage = new Texture(Gdx.files.internal("snake.png"));
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			snake.y -= 200 * Gdx.graphics.getDeltaTime();
			snakeImage = new Texture(Gdx.files.internal("snakeDown.png"));
		}

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
		if (TimeUtils.nanoTime() - lastLetterTime > 1000000000) {

			spawnletter();
		}

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
				// ADD CHAR TO EATEN ARRAY/STACK
				// CHECK STACK FOR WORDS
				lettersGathered++;
				// dropSound.play();

				Random r = new Random();
				String alphabet = "abcdedfghijklmnopqrstuvwxyz";
				char eaten = alphabet.charAt(r.nextInt(alphabet.length()));
				sb.append(eaten);
				eatenLetters = sb.toString();
				// System.out.println(eatenLetters);
				try {
					float delay = 1; // seconds
					wordFound = new FindWord(eatenLetters).getWordFound();
					if (wordFound != null || wordSack.contains(wordFound, false)) {
						wordSack.add(wordFound);

					}
					lettersGathered += 20;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				iter.remove();
			}

			if (snake.overlaps(obstacle) || snake.overlaps(obstacle2)) {
				game.setScreen(new FailScreen(game));

			}
			
		

		}

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		bgMusic.play();
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