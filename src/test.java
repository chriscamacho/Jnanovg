import Jgles2.util;
import static Jgles2.GLES2.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.LongBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import java.io.FileInputStream;


import nvg.*;


public class test {
	public static void main(String[] args) {
		nvg.test(123);
		
		test t=new test();
		t.run();
	}
	

	
	public void run() {
		
		float a=0;
		
		util.createWindow(640, 480, "", false);
		glViewport(0, 0, 640, 480);
		
		long vg = nvg.create(nvg.NVG_ANTIALIAS | nvg.NVG_STENCIL_STROKES | nvg.NVG_DEBUG);
		glClearColor(0.5f,0.5f,1f,1f);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);
		
		nvg.Color red = new nvg.Color(1f,0f,0f,1f);
		nvg.Color green = new nvg.Color(0,1,0,1);
		nvg.Color blue = new nvg.Color(0,0,1,1);
		
		nvg.Color col = new nvg.Color();
		
		int parrot = nvg.createImage(vg,"data/parrot.png", nvg.NVG_IMAGE_REPEATX | nvg.NVG_IMAGE_REPEATY);
		nvg.Paint parrotPaint = new nvg.Paint().imagePattern(vg,0f,0f,64f,64f,-45f,parrot,1f);
		
		int font = nvg.createFont(vg, "play", "data/Playball.ttf");
		int fontid = nvg.findFont(vg,"play");
		if (font!=fontid) System.out.println("font id mismatch!");
		//nvg.fontFace(vg, "play"); // seems to work from frame to frame
		nvg.fontFaceId(vg, font);
		
		nvg.Paint gradient = new nvg.Paint();
		
		while (!util.shouldClose() && !util.keyDown(util.KEY_ESCAPE)) {

			util.pollEvents();
			glClear(GL_COLOR_BUFFER_BIT);
			
			nvg.beginFrame(vg,640,480,1.333f);
			
			nvg.scissor(vg,0,0,400,480);
			nvg.skewX(vg,0.1f);
			nvg.skewY(vg,0.1f);
			nvg.beginPath(vg);
			nvg.roundedRect(vg,32,32,640-64,480-64,64);
			nvg.fillPaint(vg,parrotPaint);
			nvg.fill(vg);
			nvg.resetScissor(vg);
			nvg.resetTransform(vg);

			nvg.beginPath(vg);
			nvg.strokeWidth(vg, 32f);
			nvg.lineCap(vg,nvg.NVG_ROUND);
			nvg.strokePaint(vg, parrotPaint);
			nvg.moveTo(vg, 440,200);
			nvg.lineTo(vg, 500,400);
			nvg.stroke(vg);

			nvg.globalAlpha(vg,(float)Math.abs(Math.sin(a*4f)));
			
			nvg.beginPath(vg);
			nvg.strokeWidth(vg, 8f);
			nvg.strokeColor(vg, red);
			nvg.moveTo(vg, 320f,240f);
			nvg.lineTo(vg, 320f+300f*(float)Math.cos(a),240f+200f*(float)Math.sin(a));
			nvg.stroke(vg);

			nvg.beginPath(vg);
			nvg.strokeColor(vg, blue);
			nvg.moveTo(vg, 320f+300f*(float)Math.cos(a+1.5f),240f+200f*(float)Math.sin(a+1.5f));
			nvg.lineTo(vg, 320f,240f);
			nvg.stroke(vg);
						
			nvg.beginPath(vg);
			nvg.strokeWidth(vg, 4f);
			nvg.strokeColor(vg, green);
			nvg.moveTo(vg, 320f+300f*(float)Math.cos(a),240f+200f*(float)Math.sin(a));
			nvg.lineTo(vg, 320f+300f*(float)Math.cos(a+0.75f),240f+200f*(float)Math.sin(a+0.75f));
			nvg.lineTo(vg, 320f+300f*(float)Math.cos(a+1.5f),240f+200f*(float)Math.sin(a+1.5f));
			nvg.stroke(vg);

			nvg.strokeWidth(vg, 16f);
			nvg.beginPath(vg);
			nvg.strokeColorf(vg, (float)Math.cos(a*2),1f,(float)Math.sin(a),1f);
			nvg.moveTo(vg, 320f+300f*(float)Math.cos(a+0.75f),240f+200f*(float)Math.sin(a+0.75f));
			nvg.lineTo(vg, 320f,240f);
			nvg.stroke(vg);

			nvg.globalAlpha(vg,1f);

			nvg.beginPath(vg);
			nvg.rect(vg, 100,100, 120,30);
			//nvg.fillColorf(vg, 1f,.75f,0f,1f);
			float c = (float)Math.abs(Math.sin(a));
			col.set(c,c,c,c);
			gradient.linearGradient(vg,100,100,220,130,green,col);
			nvg.fillPaint(vg,gradient);
			nvg.fill(vg);
			
			nvg.beginPath(vg);
			nvg.rect(vg, 100,140, 120,30);
			gradient.boxGradient(vg,100,140,120,30,32,c*32,green,col);
			nvg.fillPaint(vg,gradient);
			nvg.fill(vg);

			nvg.beginPath(vg);
			nvg.rect(vg, 100,180, 80,80);
			gradient.radialGradient(vg,140,220,16,40,green,col);
			nvg.fillPaint(vg,gradient);
			nvg.fill(vg);

			nvg.beginPath(vg);
			nvg.strokeWidth(vg, 1f);
			nvg.moveTo(vg,-16f,240f);
			for (float i=0;i<320f;i=i+32) {
				nvg.quadTo(vg,i,240f+(float)(Math.sin(a+(i/100f))*120f),i+16f,240f);
			}
			nvg.stroke(vg);
						
			nvg.beginPath(vg);
			col.set(1,0,0,1);
			nvg.lineCap(vg,nvg.NVG_ROUND);
			nvg.lineJoin(vg,nvg.NVG_ROUND);	
			nvg.strokeColor(vg,col);
			nvg.strokeWidth(vg, 4f);
			nvg.moveTo(vg,-16f,340f);
			for (float i=0;i<640f;i=i+64) {
				nvg.bezierTo(vg,i,340f+(float)(Math.sin(a+(i/100f))*220f),i+(float)(Math.cos(a*2+(i/100f))*64f),340f+(float)(-Math.cos(a*2+(i/100f))*220f),i-16,340f);
			}
			nvg.stroke(vg);
			
			nvg.beginPath(vg);
			nvg.moveTo(vg,500,100);
			nvg.arcTo(vg, 450,140, 550,140, 64);
			nvg.stroke(vg);
			
			nvg.beginPath(vg);
			nvg.arc(vg,500,160,60,0,(float)Math.PI,nvg.NVG_CCW);
			nvg.stroke(vg);
			
			nvg.beginPath(vg);
			nvg.ellipse(vg,500,200,64,16);
			nvg.stroke(vg);
			
			
			
			nvg.strokeWidth(vg, 1f);
			for (float i=0;i<640f;i=i+64) {
				nvg.beginPath(vg);
				col.set(1,0,1,1);
				nvg.strokeColor(vg,col);
				nvg.circle(vg,i,340f+(float)(Math.sin(a+(i/100f))*220f),2f);
				nvg.stroke(vg);

				nvg.beginPath(vg);
				col.set(1,1,0,1);
				nvg.strokeColor(vg,col);
				nvg.circle(vg,i+(float)(Math.cos(a*2+(i/100f))*64f),340f+(float)(-Math.cos(a*2+(i/100f))*220f),2f);
				nvg.stroke(vg);
			}			
			
			
			nvg.fontSize(vg, 148.0f);
			nvg.textAlign(vg,nvg.NVG_ALIGN_CENTER | nvg.NVG_ALIGN_MIDDLE);
			nvg.translate(vg,280,260);
			nvg.rotate(vg,(float)Math.cos(-a*2f)/2f);
			
			nvg.fontBlur(vg,4f);
			col.set(0f,0f,0f,1f);
			nvg.fillColor(vg, col);
			nvg.text(vg, 0,0 , "NanoVG!");
			
			nvg.scale(vg,0.99f,0.9f);
			nvg.fontBlur(vg,0);
			col.set(1f,1f,1f,1f);
			nvg.fillColor(vg, col);
			nvg.text(vg, 0,-2f , "NanoVG!");
			
			nvg.resetTransform(vg);

			
			nvg.endFrame(vg);
			util.swapBuffer();
			
			a=a+0.01f;
		
		}
		


		nvg.deleteImage(vg,parrot);
		util.terminate();
		
	}


}
