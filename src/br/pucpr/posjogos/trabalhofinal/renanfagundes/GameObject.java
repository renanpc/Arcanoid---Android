package br.pucpr.posjogos.trabalhofinal.renanfagundes;

import java.io.IOException;
import java.io.InputStream;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.shapes.Shape;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.Transformation;

public class GameObject {
	Paint paint = new Paint();
	float elapsedTime = 0;
	float calcFPS = 0;
	final float fps = 0.5f;
	Bitmap sprite;
	Rect Src, Dst;
	public float Scale = 1;	
	public BodyDef BodyDef;
	public Body Body;
	public Shape Shape;
	public int ScreenHeight, ScreenWidth;
	public Matrix IdentityMatrix;
	public Matrix TransformationMatrix;
	
	//public Boolean Enable = true;
	
	public void SetupPhysics()
	{
		
	    
	}
	
	public GameObject(Context context)
	{
		paint.setTextSize(20);
		paint.setColor(Color.WHITE);
		paint.setFakeBoldText(true);
		Dst = new Rect();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		ScreenHeight = display.getHeight();
		ScreenWidth = display.getWidth();
		TransformationMatrix = new Matrix();
		IdentityMatrix = new Matrix();	
	}
	
	public void Intialize()
	{
		SetupPhysics();
	}
	
	public Vec2 Position() {
		return Body.getPosition();
	}
	
	/*public Rect getDestinyRect()
	{	
		
		//H = Screen Height
		//CX = Box2D coordinate
		//BScale = Scale of conversion Box2D to Pixels
		//DP = Drawable position
		//DP = H + ((CX-1)*BScale)
		Vec2 position = Position();
		
		int dpTop = (int) (ScreenHeight + ((position.y * -1) * GameActivity.getBox2DScale()));
		
		
		Dst.set((int)(position.x * GameActivity.getBox2DScale()), 
				dpTop, 
				(int) ((int)ScreenWidth - (position.x * GameActivity.getBox2DScale())-(sprite.getWidth() *Scale)), 
				(int)ScreenHeight - dpTop - (sprite.getHeight() *Scale));
		
		return Dst;
	}*/
	
	public void update(float deltaTime)
	{	
		
		Vec2 position = Position();	
		TransformationMatrix.set(IdentityMatrix);
		float graus = (float) ((180.0f/Math.PI) *  Body.getAngle());
		TransformationMatrix.postRotate(-graus, (sprite.getWidth()*Scale)/2, (sprite.getHeight()*Scale)/2);
		int dpTop = (int) (ScreenHeight + ((position.y * -1) * GameActivity.getBox2DScale() - (sprite.getHeight()/2*Scale)));
		TransformationMatrix.postTranslate(((position.x) * GameActivity.getBox2DScale()) - (sprite.getWidth()/2*Scale), dpTop);		
		//int scale = GameActivity.getBox2DScale();
		//body.applyForce(new Vec2(0, 10), new Vec2());
	}
	
	public void draw(Canvas canvas)
	{			
		canvas.drawBitmap(sprite, TransformationMatrix, paint);
		
		//canvas.drawBitmap(sprite, Src, getDestinyRect(), paint);
	}
	
	public float getHeight(){
		return (sprite.getHeight() * Scale) / GameActivity.getBox2DScale();
	}	
	
	public float getWidth(){
		return (sprite.getWidth() * Scale) / GameActivity.getBox2DScale();
	}
	
	
	public void OnCollisionEnter(GameObject objeto)
	{
		
	}
	
	public void OnCollisionExit(GameObject objeto)
	{
		
	}
	
	
}
