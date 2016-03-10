package br.pucpr.posjogos.trabalhofinal.renanfagundes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FPS {
	Paint paint = new Paint();
	float elapsedTime = 0;
	public float calcFPS = 0;
	final float fps = 0.5f;
	
	
	public FPS()
	{
		paint.setTextSize(20);
		paint.setColor(Color.WHITE);
		paint.setFakeBoldText(true);
	}
	
	public void update(float deltaTime)
	{
		elapsedTime += deltaTime;
		if (elapsedTime >= fps){
			elapsedTime = 0;
			calcFPS = Math.round(1/deltaTime);
		}
			
	}
	
	public void draw(Canvas canvas)
	{
		canvas.drawText("FPS: " + calcFPS, 10, 750, paint);
	}
}
