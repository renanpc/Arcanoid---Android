package br.pucpr.posjogos.trabalhofinal.renanfagundes;

import java.io.IOException;
import java.io.InputStream;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import android.R.string;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.Display;

public class Bola extends GameObject {
	CircleShape dynamicBox;
	public int Vidas = 5;
	public int BlocosDestruidos = 0;
	public Typeface fonteJogo;
	public boolean restart = false;
	public Bola(Context context) {
		super(context);	
		AssetManager assetManager = context.getAssets();		
		Src = new Rect();
		try {
			InputStream inputStream = assetManager.open("bola.png");
			sprite = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		this.Scale = 0.03f;
		
		fonteJogo = Typeface.createFromAsset(assetManager, "RINGM.TTF");
		
				
	}
	
	private float totalTime;
	private float secondsToRestart = 3;
	@Override
	public void update(float deltaTime)
	{	
		super.update(deltaTime);
		if (restart){
			Body.setTransform(new Vec2(20,30), 0);			
			totalTime += deltaTime;
			Body.setLinearVelocity(new Vec2(0,0));
			Body.setAngularVelocity(0);
			if (totalTime > secondsToRestart){
				restart = false;
				totalTime = 0;
				Body.applyForceToCenter(new Vec2(5000, 5000));
			}
		}
	}
	
	@Override
	public void SetupPhysics()
	{
		BodyDef = new BodyDef();
		BodyDef.type = BodyType.DYNAMIC;
		
		BodyDef.position = new Vec2(20,30);

	    Body = GameActivity.getWorld().createBody(BodyDef);
	    
	    dynamicBox = new CircleShape();
	    

	    dynamicBox.setRadius((sprite.getWidth() * Scale) / GameActivity.getBox2DScale());
	    dynamicBox.setRadius(1.4f);

	    FixtureDef fixtureDef = new FixtureDef();

	    fixtureDef.shape = dynamicBox;

	    fixtureDef.density = 1;	    	    
	    fixtureDef.restitution = 1.0f;
	    fixtureDef.userData = this;
	    Body.createFixture(fixtureDef);
	    
	    
	    Body.applyForceToCenter(new Vec2(5000, 5000));
	        
	    IdentityMatrix.postScale(Scale, Scale);   
	    	    
	    
	    //IdentityMatrix.postTranslate(position.x * GameActivity.getBox2DScale(), position.y * GameActivity.getBox2DScale());
	}
	
	
	public void OnCollisionEnter(GameObject objeto){
		if (objeto.getClass() == Bloco.class){
			GameActivity.GameObjsToDestroy.add(objeto);
			BlocosDestruidos++;
		}
		if (objeto.getClass() == Chao.class) {			
			this.Vidas--;
			restart = true;
		}
			
	}
	
	@Override
	public void draw(Canvas canvas)
	{			
		paint.setColor(Color.RED);
		paint.setTypeface(fonteJogo);
		canvas.drawText("Vidas: " + Integer.toString(Vidas), 10, 30, paint);
		/*
		canvas.drawCircle((Position().x*GameActivity.getBox2DScale()), 
				(ScreenHeight + (Position().y*-1)*GameActivity.getBox2DScale()), dynamicBox.getRadius()*GameActivity.getBox2DScale(), paint);
				*/
		paint.setColor(Color.WHITE);
		if (restart) {
			
			canvas.drawText(Integer.toString((int)secondsToRestart - (int)totalTime), ScreenWidth/2, ScreenHeight/2, paint);
		}
		
		
		
		super.draw(canvas);
		
	}
		
}
