import Jgles2.util;
import Jgles2.EGL;
import static Jgles2.GLES2.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.LongBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import java.io.FileInputStream;

public class test {
	public static void main(String[] args) {
		nvg.test(123);
		
		test t=new test();
		t.run();
	}
	
	private long egl_display,egl_surface,native_display,native_win;
	
	public void run() {
		
		float a=0;
		
		createEglContext(640,480);
		glViewport(0, 0, 640, 480);
		
		long vg = nvg.create(nvg.NVG_ANTIALIAS | nvg.NVG_STENCIL_STROKES | nvg.NVG_DEBUG);
		glClearColor(0.5f,0.5f,1f,1f);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);
		
		nvg.Color red = new nvg.Color(1,0,0,1);
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
		
		while(!util.keyDown(util.KEY_ESC)) {

			util.pumpEvents(native_display,native_win);
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
			EGL.eglSwapBuffers(egl_display, egl_surface); 
			
			a=a+0.01f;
		
		}
		
		//nvg.deletePaint(parrotPaint);

		nvg.deleteImage(vg,parrot);
		util.closeWindow(native_display,native_win);
		
	}


	// just moved here to get it out of the way! 
	private void createEglContext(int wx, int wy) {
		
		int attribs[] = {
            EGL.EGL_RED_SIZE, 1, 
            EGL.EGL_GREEN_SIZE, 1,
            EGL.EGL_BLUE_SIZE, 1,
            EGL.EGL_ALPHA_SIZE, 1,
            EGL.EGL_DEPTH_SIZE, 1,
            EGL.EGL_RENDERABLE_TYPE, EGL.EGL_OPENGL_ES2_BIT,
            EGL.EGL_NONE // end of list
        };
        
        int ctx_attribs[] = {
            EGL.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL.EGL_NONE // end of list
        };
        // buffers for EGL attributs 
        IntBuffer attribsBuffer = util.createIntBuffer(attribs.length);
        attribsBuffer.put(attribs);
        
        IntBuffer ctx_attribsBuffer = util.createIntBuffer(ctx_attribs.length);
        ctx_attribsBuffer.put(ctx_attribs);
        
        native_display = util.get_native_display();
        egl_display = EGL.eglGetDisplay( native_display );
        
        if (!EGL.eglInitialize(egl_display)) {
            System.out.println("EGL failed to initialise");
            System.exit(-1);
        }

        // val is a integer intBuffer which is reused for various int return values
        IntBuffer val = util.createIntBuffer(2);
        
        int config_size=1;
        LongBuffer configsBuffer = util.createLongBuffer(config_size);
        
        if (!EGL.eglChooseConfig(egl_display, attribsBuffer,
                                    configsBuffer, config_size, val)) {
            System.out.println("failed to get an EGL config");
            System.exit(-1);            
        }

        long config=configsBuffer.get(0); 
        // last 5 parameters of make_native_window are
        // coordinates of top left corner of window, window width and height
        // and finally a flag for fullscreen
        native_win = util.make_native_window(native_display, egl_display, config,
                    0, 0, wx, wy, false);
                    
        long egl_context = EGL.eglCreateContext(egl_display, config, EGL.EGL_NO_CONTEXT, ctx_attribsBuffer );
        if (egl_context==0) {
            System.out.println("failed to get an EGL context");
            System.exit(-1);
        }
        
        egl_surface = EGL.eglCreateWindowSurface(egl_display, config, native_win, null);
        if (egl_surface == 0) {
            System.out.println("failed to create a windowed surface");
            System.exit(-1);
        }      
        
        if (!EGL.eglMakeCurrent(egl_display, egl_surface, egl_surface, egl_context)) {
            System.out.println("eglMakeCurrent failed");
            System.out.println("error code " + Integer.toHexString(EGL.eglGetError()));
            System.exit(-1);
        }	
		
	}


}
