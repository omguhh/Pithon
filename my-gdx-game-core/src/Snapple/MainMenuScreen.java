package Snapple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen {

    final Drop game;

    OrthographicCamera camera;

    public MainMenuScreen(final Drop gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	 public void render(float delta) {
       /* Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
*/
		Texture bg= new Texture(Gdx.files.internal("background.png"));
		
		Texture playbutt= new Texture(Gdx.files.internal("buttons/play.png")); //JUDGE ME
		Texture scorebutt= new Texture(Gdx.files.internal("buttons/scores.png")); //hi
		Texture credbutt= new Texture(Gdx.files.internal("buttons/credit.png")); //wat
		
		Texture sfxbutt= new Texture(Gdx.files.internal("buttons/sfx.png")); //wat
		Texture mscbutt= new Texture(Gdx.files.internal("buttons/musc.png")); //wat
		
		
		TextureRegion tr=new TextureRegion(playbutt);
		tr.flip(false,false);
		
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(bg,0,0);
        game.batch.draw(playbutt, 225,180);
        game.batch.draw(scorebutt, 225,100);
        game.batch.draw(credbutt, 225,20);
        
        game.batch.draw(sfxbutt, 25,80);
        game.batch.draw(mscbutt, 635,80);
        
        game.batch.draw(tr,225,180);

        
       //game.font.draw(game.batch, "SNAPPLE!!!", 300, 300);
       // game.font.draw(game.batch, "Tap screen to start!", 300, 250);
        game.batch.end();


//        BAD PROGRAMMING LEL LITERALLY BAD LOGIC WOO WOO I can't wait to sleep
        if (Gdx.input.isTouched()) {
        	
        	Vector3 tmp= new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
        	  camera.unproject(tmp);
        	  Rectangle textureBounds=new Rectangle(225,180,playbutt.getWidth(),playbutt.getHeight());
        	 
        	  // texture x is the x position of the texture
        	  // texture y is the y position of the texture
        	  // texturewidth is the width of the texture (you can get it with texture.getWidth() or textureRegion.getRegionWidth() if you have a texture region
        	  // textureheight is the height of the texture (you can get it with texture.getHeight() or textureRegion.getRegionhHeight() if you have a texture region
        	
        	  if(textureBounds.contains(tmp.x,tmp.y))
        	     {
//        	     System.out.print("ur touchin");
        	     game.setScreen(new GameScreen(game));
        	     dispose();
        	     
        	     }
        	  
//            game.setScreen(new GameScreen(game));
//            dispose();
        }
    }

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}


        //...Rest of class omitted for succinctness.

}