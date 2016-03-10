package br.pucpr.posjogos.trabalhofinal.renanfagundes;

import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity {

	///// Escala de posicionamento adotada 1 unidade box2D = 10 pixels
	
	Tela tela;
	WakeLock wakeLock;	
	Intent gameView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		PowerManager powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My lock");
		wakeLock.acquire();		
		tela = new Tela(this);
		setContentView(tela);
		
		gameView = new Intent(this, GameActivity.class);
		gameView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	}
	
	@Override
	protected void onResume() {		
		super.onResume();		
		tela.resume();
	}
	
	@Override
    public void onStop() {
       super.onStop();
       tela.pause();
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
		Typeface fonteJogo;
		
		boolean newGameClicked = false;
		boolean exitClicked = false;
		Display display;		
		
		public Tela(Context context) {
			super(context);
			holder = this.getHolder();
			startTime = System.nanoTime();
			fps = new FPS();			
			paint.setStrokeWidth(1);
			AssetManager am = context.getAssets();
			fonteJogo = Typeface.createFromAsset(am, "RINGM.TTF");		
			
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			display = wm.getDefaultDisplay();
			
			paint.setTextSize(30);
			paint.setTypeface(fonteJogo);			
			paint.setTextAlign(Paint.Align.CENTER);
			
			paint.getTextBounds("New Game", 0, "New Game".length(), newGameRect);
			paint.getTextBounds("Exit", 0, "Exit".length(), exitRect);
			
			float newGamewidth = newGameRect.right - newGameRect.left;
			float exitwidth = exitRect.width();
			
			newGameRect.left += display.getWidth()/2 - (newGamewidth/2);
			newGameRect.right += display.getWidth()/2 - (newGamewidth/2);
			newGameRect.top += 300;
			newGameRect.bottom += 300;
			
			exitRect.left += display.getWidth()/2 - (exitwidth/2);
			exitRect.right += display.getWidth()/2 - (exitwidth/2);
			exitRect.top += 400;
			exitRect.bottom += 400;
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
		}
		
		
		Rect newGameRect = new Rect();
		Rect exitRect = new Rect();
		
		@Override
		public void run() {			
			
			while (running)
			{
				if (!holder.getSurface().isValid())
					continue;
				
				float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
				startTime = System.nanoTime();
				
				Canvas canvas = holder.lockCanvas();
				
				canvas.drawRGB(0, 0, 0); //Limpa a tela
				
				paint.setTypeface(fonteJogo);
				
				paint.setTextAlign(Paint.Align.CENTER);
				paint.setTextSize(40);
				canvas.drawText("Arcanoid", canvas.getWidth()/2, 100, paint);
				
				paint.setColor(Color.BLUE);
				paint.setTextSize(30);
				
				//paint.setColor(Color.RED);
				//canvas.drawRect(newGameRect, paint);
				if (newGameClicked) paint.setColor(Color.RED);
				else paint.setColor(Color.BLUE);
				canvas.drawText("New Game", canvas.getWidth()/2, 300, paint);
				
				
				//paint.setColor(Color.RED);
				//canvas.drawRect(exitRect, paint);
				if (exitClicked) paint.setColor(Color.RED);
				else paint.setColor(Color.BLUE);
				canvas.drawText("Exit",     canvas.getWidth()/2, 400, paint);
				
				/*for (int i = 0; i < 10000; i++){
					paint.setColor(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
					canvas.drawPoint(rnd.nextInt(canvas.getWidth()), rnd.nextInt(canvas.getHeight()), paint);
				}*/
				fps.update(deltaTime);
				fps.draw(canvas);				
				holder.unlockCanvasAndPost(canvas);
				
				
				
				
				if (exitClicked) {
					running = false;
					finish();					
				}
			}
			
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
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();
			newGameClicked = newGameRect.contains((int)x, (int)y) && !newGameClicked;
			exitClicked = exitRect.contains((int)x, (int)y) && !exitClicked;
			if (newGameClicked){					
				startActivity(gameView);
				running = false;
			}
			return true;
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
		
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(isFinishing())
			wakeLock.release();
		else
			tela.pause();
	}
	/*	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		switch (position)
		{
			case 0:
				startActivity(new Intent(this, RenderView.class));
				break;
			case 1:
				startActivity(new Intent(this, FastRenderView.class));
				break;
		}
	}*/
}
