package br.pucpr.posjogos.trabalhofinal.renanfagundes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.MotionEvent;

public class GameActivity extends Activity {
	Tela tela;
	private static final int _box2DScale = 10;
	private static World _gameWorld;
	private static Vec2 _gravity = new Vec2(0, 0f);
	public static List<GameObject> GameObjsToDestroy = new ArrayList<GameObject>();
	
    int velocityIterations = 6;

    int positionIterations = 3;
    WakeLock wakeLock;		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);	    
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    PowerManager powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My lock");
		tela = new Tela(this);
		
		
		setContentView(tela);
	}
	
	@Override
	protected void onResume() {		
		super.onResume();
		tela.resume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		tela.pause();
	}
	
	private static void BarraEsquerda(){
		BodyDef body = new BodyDef();
		body.type = BodyType.STATIC;
		
		body.position = new Vec2(-4, 0);

	    Body Body = GameActivity.getWorld().createBody(body);
	    Body.setType(BodyType.STATIC);    
		
		PolygonShape b = new PolygonShape();
		b.setAsBox(1, 1000);

		FixtureDef fd = new FixtureDef();
		fd.shape = b;
		fd.friction = 0.1f;
		fd.density = 0;
		fd.restitution = 1;
		Body.createFixture(fd);
	}
	private static void BarraDireita(){
		BodyDef body = new BodyDef();
		body.type = BodyType.STATIC;
		
		body.position = new Vec2(48, 0);

	    Body Body = GameActivity.getWorld().createBody(body);
	    Body.setType(BodyType.STATIC);    
		
		PolygonShape b = new PolygonShape();
		b.setAsBox(1, 1000);

		FixtureDef fd = new FixtureDef();
		fd.shape = b;
		fd.friction = 0.1f;
		fd.density = 0;
		fd.restitution = 1;
		Body.createFixture(fd);
	}
	
	private static void BarraCima(){
		BodyDef body = new BodyDef();
		body.type = BodyType.STATIC;
		
		body.position = new Vec2(0, 83);

	    Body Body = GameActivity.getWorld().createBody(body);
	    Body.setType(BodyType.STATIC);    
		
		PolygonShape b = new PolygonShape();
		b.setAsBox(1000, 1);

		FixtureDef fd = new FixtureDef();
		fd.shape = b;
		fd.friction = 0.1f;
		fd.density = 0;
		fd.restitution = 1;
		Body.createFixture(fd);
	}
	
	private static void BarraBaixo(Context cx){
		BodyDef body = new BodyDef();
		body.type = BodyType.STATIC;
		
		body.position = new Vec2(0, 0);

	    Body Body = GameActivity.getWorld().createBody(body);
	    Body.setType(BodyType.STATIC);    
		
		PolygonShape b = new PolygonShape();
		b.setAsBox(1000, 1);

		FixtureDef fd = new FixtureDef();
		fd.shape = b;
		fd.friction = 0.1f;
		fd.density = 0;
		fd.restitution = 1;
		fd.userData = new Chao(cx);
		Body.createFixture(fd);
	}

	public static void setupScenario(Context cx)
	{ 
	
		BarraCima();
		BarraBaixo(cx);
		BarraEsquerda();
		BarraDireita();
	}
		
	public static World getWorld() {
		if (_gameWorld == null) {
			_gameWorld = new World(_gravity);
			_gameWorld.setContactListener(new ArcanoidContactListener());
		//_gameWorld.setSleepingAllowed(true);			
		}
		return _gameWorld;
	}	

	public static int getBox2DScale() {
		return _box2DScale;
	}

	public class Tela extends SurfaceView implements Runnable, SurfaceHolder.Callback
	{
		Thread process = null;
		SurfaceHolder holder;
		boolean running = false;
		float startTime;
		FPS fps;
		Paint paint = new Paint();
		Random rnd = new Random();
		List<Bloco> blocos;
		
		Bola bola;
		Barra barra;
		Bloco bloco;		
		boolean GameOver = false;
		boolean Win = false;
		public Typeface fonteJogo;
		Rect restartRect;
		Display display;
		Bitmap restartSprite, backgroundSprite;
		
		
		public Tela(Context context) {
			super(context);
			holder = this.getHolder();
			startTime = System.nanoTime();
			fps = new FPS();
			paint.setStrokeWidth(1);
			bola = new Bola(context);
			barra = new Barra(context);		
			bola.Intialize();
			barra.Intialize();
			blocos = new ArrayList<Bloco>();
			setupScenario(context);
			Vec2 inicial = new Vec2(5, 70);
			Vec2 posAtual = new Vec2(inicial);
			int i = 0;
			for (int c = 0; c < 4; c++)
			{
				for (int l = 0; l < 5; l++){
					bloco = new Bloco(context, posAtual);
					posAtual.y = inicial.y - ((bloco.getHeight() + 1) * c);
					posAtual.x += bloco.getWidth() + 1;
					bloco.Intialize();
					blocos.add(bloco);
					i++;
				}
				posAtual.x = inicial.x;
			}
			
			AssetManager am = context.getAssets();
			fonteJogo = Typeface.createFromAsset(am, "RINGM.TTF");
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			display = wm.getDefaultDisplay();
			
			restartRect = new Rect(display.getWidth()-50, 0, display.getWidth(), 50);
			
			try {
				InputStream inputStream = am.open("restart.png");
				restartSprite = BitmapFactory.decodeStream(inputStream);
				inputStream.close();
			} catch (IOException e) { 
				e.printStackTrace();
			}
			
			try {
				InputStream inputStream = am.open("background.png");
				backgroundSprite = BitmapFactory.decodeStream(inputStream);
				inputStream.close();
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}
		
		public void resume()
		{
			running = true;
			process = new Thread(this);
			process.start();
		}
		
		public void pause()
		{
			running = false;
			while (true){
				try {
					process.join();
					break;
				}
				catch (Exception e)
				{
					
				}
			}
			process = null;
		}
		
		
		Rect dst, src;
		
		
		@Override
		public void run() {
			//Fui obrigado a criar uma thread separada para atualizar a física, senão o FPS ficava muito baixo.
			
			Thread t = new Thread(new PhysicsUpdater()); 
			src = new Rect();
			dst = new Rect();
			src.set(0, 0, 0, 0);
			dst.set(100, 100, 228, 228);
			t.start();
			float tempoEsperar = 3;
			float tempoTotal = 0;
			Rect rectDstRestart = new Rect(0,0,restartSprite.getWidth(), restartSprite.getHeight());
			Rect srcBackground = new Rect(0,0, backgroundSprite.getWidth(), backgroundSprite.getHeight());
			Rect dstBackground = new Rect(0,0, display.getWidth(), display.getHeight());
			while (running)
			{
				
				paint.setTypeface(fonteJogo);
				paint.setColor(Color.BLUE);
				paint.setTextSize(30);
				paint.setTextAlign(Paint.Align.CENTER);
				
				if (!holder.getSurface().isValid())
					continue;
				
				float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
				startTime = System.nanoTime();
				
				Canvas canvas = holder.lockCanvas();
				canvas.drawBitmap(backgroundSprite, srcBackground, dstBackground, paint);		
				
				fps.update(deltaTime);
				fps.draw(canvas);
				
				if (!GameOver && !Win) {
					for (int i = 0; i < blocos.size(); i++){
						blocos.get(i).update(deltaTime);
						blocos.get(i).draw(canvas);
					}
					
					for (int i = 0; i < GameObjsToDestroy.size();i++){
						Fixture fix = GameObjsToDestroy.get(i).Body.getFixtureList(); 
						Body body = GameObjsToDestroy.get(i).Body;
						body.destroyFixture(fix);
						GameActivity.getWorld().destroyBody(body);
						blocos.remove(GameObjsToDestroy.get(i));
					}
					
					GameObjsToDestroy = new ArrayList<GameObject>();
					
					bola.update(deltaTime);
					bola.draw(canvas);	
					
					barra.update(deltaTime);
					barra.draw(canvas);	
					
					
					
					canvas.drawBitmap(restartSprite, rectDstRestart, restartRect, paint);
					
					canvas.drawPoint(100, 10, paint);
					
				}
				
				if (bola.Vidas == 0) {
					GameOver = true;					
				}
				
				if (bola.BlocosDestruidos == 20)
					Win = true;
				
				if (GameOver || Win){
					tempoTotal += deltaTime;
					if (tempoTotal >= tempoEsperar)
						running = false;
					Display display = getWindowManager().getDefaultDisplay();
					Point size = new Point();
					display.getSize(size);					
					if (GameOver) {
						paint.setColor(Color.RED);
						canvas.drawText("Game Over", size.x/2, size.y/4, paint);
					}
					else { 
						paint.setColor(Color.GREEN);
						canvas.drawText("You Win", size.x/2, size.y/4, paint);
					}
				}			
				
				holder.unlockCanvasAndPost(canvas);
				
			}			
			finish();
			
		}
		
		public class PhysicsUpdater implements Runnable {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				float timeStep = 1.0f/45.0f;
				while (running && !GameOver){
					// Record the start time, so we know how long it took to sim everything

					long startTime = System.currentTimeMillis();
					// Step for 1/60th of a second
					getWorld().step(timeStep, velocityIterations, positionIterations);
					// Figure out how long it took

					long simTime = System.currentTimeMillis() - startTime;
					// Sleep for the excess

					if(simTime < 16) {

					  try {

					    Thread.sleep(16 - simTime);

					  } catch (Exception e) {

					    e.printStackTrace();

					  }

					}
					//Está indo muito rápido
					
				}
								
			}

		}
		
		public boolean process(MotionEvent event) {
			Input.LEFT = (event.getX() <= (getWidth()/2)) && !(event.getAction() == MotionEvent.ACTION_UP);	
			Input.RIGHT = (event.getX() >= (getWidth()/2)) && !Input.LEFT && !(event.getAction() == MotionEvent.ACTION_UP);
			
			
			if (restartRect.contains((int)event.getX(), (int)event.getY()))
				bola.restart = true;
			return true;
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			return process(event);	
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	
	
	
	
}
