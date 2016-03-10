package br.pucpr.posjogos.trabalhofinal.renanfagundes;

import java.io.IOException;
import java.io.InputStream;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Barra extends GameObject {
	
	PolygonShape dynamicBox;
	public Barra(Context context) {
		super(context);	
		AssetManager assetManager = context.getAssets();		
		Src = new Rect();
		try {
			InputStream inputStream = assetManager.open("barra.png");
			sprite = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		this.Scale = 0.8f;
		
				
	}
	
	@Override
	public void SetupPhysics()
	{
		BodyDef = new BodyDef();
		BodyDef.type = BodyType.KINEMATIC;
		
		BodyDef.position = new Vec2(20, 10);

	    Body = GameActivity.getWorld().createBody(BodyDef);
	    
	    dynamicBox = new PolygonShape();	    

	    dynamicBox.setAsBox((sprite.getWidth() * Scale) / GameActivity.getBox2DScale(), (sprite.getHeight() * Scale) / GameActivity.getBox2DScale());
	    dynamicBox.setAsBox(3, 1);	    
	    FixtureDef fixtureDef = new FixtureDef();   

	    fixtureDef.shape = dynamicBox;

	    fixtureDef.density = 1;	    	    
	    fixtureDef.restitution = 1.0f;
	    fixtureDef.userData = this;
	    Body.createFixture(fixtureDef);
	        
	    
	    Body.applyForceToCenter(new Vec2(30000, 30000));
	        
	    IdentityMatrix.postScale(Scale, Scale);
	    //IdentityMatrix.postTranslate(position.x * GameActivity.getBox2DScale(), position.y * GameActivity.getBox2DScale());
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		
		if (Input.LEFT && (Body.getPosition().x*GameActivity.getBox2DScale()) > sprite.getWidth()*Scale)
			Body.setLinearVelocity(new Vec2(-10, 0));
		else if (Input.RIGHT && (Body.getPosition().x*GameActivity.getBox2DScale()) < ScreenWidth-(sprite.getWidth()*Scale))
			Body.setLinearVelocity(new Vec2(10, 0)); 
		else
			Body.setLinearVelocity(new Vec2(0, 0));
	}	
	
	@Override
	public void draw(Canvas canvas)
	{			
		super.draw(canvas);
		/*
		paint.setColor(Color.RED);		
		for (int i = 0; i < dynamicBox.getVertices().length-1; i++){
			Vec2 curr = dynamicBox.getVertices()[i];
			Vec2 next = dynamicBox.getVertices()[i+1];
			canvas.drawLine((curr.x*GameActivity.getBox2DScale())+(Position().x*GameActivity.getBox2DScale()), 
							(curr.y*GameActivity.getBox2DScale())+(ScreenHeight + (Position().y*-1)*GameActivity.getBox2DScale()), 
							(next.x*GameActivity.getBox2DScale())+(Position().x*GameActivity.getBox2DScale()), 
							(next.y*GameActivity.getBox2DScale())+(ScreenHeight + (Position().y*-1)*GameActivity.getBox2DScale()), paint);
		}*/
		
		//canvas.drawBitmap(sprite, Src, getDestinyRect(), paint);
	}
}
