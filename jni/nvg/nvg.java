package nvg;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;

public class nvg {
    static {
        System.loadLibrary("Jnanovg");
    }
    
    // TODO should there be a native routine that returns sizeof for
    // structs used by the sub class buffers?
    public static class Paint {
		
		FloatBuffer buff;  // must be called buff see void* getBuffPtr(JNIEnv *e, jobject jo)
		
		public Paint() {
			buff = ByteBuffer.allocateDirect(76)
						.order(ByteOrder.nativeOrder()).asFloatBuffer();
		}
		
		public Paint imagePattern(long ctx, float ox, float oy, float ex, float ey,
												float angle, int image, float alpha)
		{
			nvg.__imagePattern(ctx, this, ox, oy, ex, ey, angle, image, alpha);
			return this; // for chaining with constructor 
		}
		
		public Paint linearGradient(long ctx, float sx, float sy, float ex, float ey, 
												Color icol, Color ocol)
		{
			nvg.__linearGradient(ctx, this, sx, sy, ex, ey, icol, ocol);
			return this;
		}
		
		public Paint boxGradient(long ctx, float x, float y, float w, float h,
							float r, float f, Color icol, Color ocol)
		{
			nvg.__boxGradient(ctx, this, x, y, w, h, r, f, icol, ocol);
			return this;
		}
		
		public Paint radialGradient(long ctx, float cx, float cy, float inr, float outr, Color icol, Color ocol)
		{
			nvg.__radialGradient(ctx, this, cx, cy, inr, outr, icol, ocol);
			return this;
		}
	}
    
    public static class Color {
		
		FloatBuffer buff;  // must be called buff see void* getBuffPtr(JNIEnv *e, jobject jo)
		
		public Color(float r, float g, float b, float a) {
			buff = ByteBuffer.allocateDirect(16)
						.order(ByteOrder.nativeOrder()).asFloatBuffer();
			buff.put(0, r);
			buff.put(1, g);
			buff.put(2, b);
			buff.put(3, a);
		}
		
		public Color() {
			this(0f,0f,0f,1f);
		}
		
		public void setRed(float r) { buff.put(0,r); }
		public void setGreen(float g) { buff.put(1,g); }
		public void setBlue(float b) { buff.put(2,b); }
		public void setAlpha(float a) { buff.put(3,a); }
		
		public void set(float r, float g, float b, float a) { 
			buff.put(0, r);
			buff.put(1, g);
			buff.put(2, b);
			buff.put(3, a);
		}

		public float r() { return buff.get(0); }
		public float g() { return buff.get(1); }
		public float b() { return buff.get(2); }
		public float a() { return buff.get(3); }
		
	}

	// a lot of these are straight ports as JNI is probably slower for
	// such simple routines
	public static class Transform {
		
		public FloatBuffer buff;
		
		public Transform() {
			buff = ByteBuffer.allocateDirect(6*4)
						.order(ByteOrder.nativeOrder()).asFloatBuffer();
			identity();			
		}
		
		public void identity() {
			buff.put(0, 1.0f); buff.put(1, 0.0f);
			buff.put(2, 0.0f); buff.put(3, 1.0f);
			buff.put(4, 0.0f); buff.put(5, 0.0f);
		}
		
		public void translate(float x, float y) {
			buff.put(0, 1.0f); buff.put(1, 0.0f);
			buff.put(2, 0.0f); buff.put(3, 1.0f);
			buff.put(4, x); buff.put(5, y);
		}

		public void scale(float x, float y) {
			buff.put(0, x); buff.put(1, 0.0f);
			buff.put(2, 0.0f); buff.put(3, y);
			buff.put(4, 0.0f); buff.put(5, 0.0f);
		}

		public void rotate(float a) {
			float cs = (float)Math.cos(a); 
			float sn = (float)Math.sin(a);
			buff.put(0, cs); buff.put(1, sn);
			buff.put(2, -sn); buff.put(3, cs);
			buff.put(4, 0.0f); buff.put(5, 0.0f);
		}

		public void skewX(float a) {
			buff.put(0, 1.0f); buff.put(1, 0.0f);
			buff.put(2, (float)Math.tan(a)); buff.put(3, 1.0f);
			buff.put(4, 0.0f); buff.put(5, 0.0f);			
		}
		
		public void skewY(float a) {
			buff.put(0, 1.0f); buff.put(1, (float)Math.tan(a));
			buff.put(2, 0.0f); buff.put(3, 1.0f);
			buff.put(4, 0.0f); buff.put(5, 0.0f);			
		}
		
		public void multiply(Transform s) {
			float t0 = buff.get(0) * s.buff.get(0) + buff.get(1) * s.buff.get(2);
			float t2 = buff.get(2) * s.buff.get(0) + buff.get(3) * s.buff.get(2);
			float t4 = buff.get(4) * s.buff.get(0) + buff.get(5) * s.buff.get(2) + s.buff.get(4);
			buff.put(1, buff.get(0) * s.buff.get(1) + buff.get(1) * s.buff.get(3) );
			buff.put(3, buff.get(2) * s.buff.get(1) + buff.get(3) * s.buff.get(3) );
			buff.put(5, buff.get(4) * s.buff.get(1) + buff.get(5) * s.buff.get(3) + s.buff.get(5) );
			buff.put(0, t0);
			buff.put(2, t2);
			buff.put(4, t4);			
		}
		
		public void getContextTransform(long ctx) {
			__getTransform(ctx,this);
		}
		
		public void setContextTransform(long ctx) {
			__setTransform(ctx,this);
		}
	}

	private static native void __getTransform(long ctx, Transform t);

	private static native void __setTransform(long ctx, Transform t);

	private static native void __imagePattern(long ctx, Paint pnt, float ox, float oy, float ex, float ey,
												float angle, int image, float alpha);
	private static native void __linearGradient(long ctx, Paint pnt, float sx, float sy, float ex, float ey, 
												Color icol, Color ocol);
	private static native void __boxGradient(long ctx, Paint pnt, float x, float y, float w, float h,
												float r, float f, Color icol, Color ocol);
						
	private static native void __radialGradient(long ctx, Paint pnt, float cx, float cy, float inr, float outr,
												Color icol, Color ocol);
  
    
	nvg() {
		System.out.println("nvg - this is designed to be a static singleton object, you should not instance it!");
	}

	// Flag indicating if geometry based anti-aliasing is used (may not be needed when using MSAA).
	public static int	NVG_ANTIALIAS 		= 1<<0;
	// Flag indicating if strokes should be drawn using stencil buffer. The rendering will be a little
	// slower, but path overlaps (i.e. self-intersecting or sharp turns) will be drawn just once.
	public static int	NVG_STENCIL_STROKES	= 1<<1;
	// Flag indicating that additional debug checks are done.
	public static int	NVG_DEBUG 			= 1<<2;    
	
	public static int	NVG_CCW = 1;			// Winding for solid shapes
	public static int	NVG_CW = 2;				// Winding for holes
    
	public static int	NVG_SOLID = 1;			// CCW
	public static int	NVG_HOLE = 2;			// CW
	
	public static int	NVG_BUTT = 0;
	public static int	NVG_ROUND = 1;
	public static int	NVG_SQUARE = 2;
	public static int	NVG_BEVEL = 3;
	public static int	NVG_MITER = 4;
	
	// Horizontal align
	public static int	NVG_ALIGN_LEFT 		= 1<<0;	// Default, align text horizontally to left.
	public static int	NVG_ALIGN_CENTER 	= 1<<1;	// Align text horizontally to center.
	public static int	NVG_ALIGN_RIGHT 	= 1<<2;	// Align text horizontally to right.
	// Vertical align
	public static int	NVG_ALIGN_TOP 		= 1<<3;	// Align text vertically to top.
	public static int	NVG_ALIGN_MIDDLE	= 1<<4;	// Align text vertically to middle.
	public static int	NVG_ALIGN_BOTTOM	= 1<<5;	// Align text vertically to bottom. 
	public static int	NVG_ALIGN_BASELINE	= 1<<6; // Default, align text vertically to baseline. 
	
	public static int	NVG_IMAGE_GENERATE_MIPMAPS	= 1<<0;     // Generate mipmaps during creation of the image.
	public static int	NVG_IMAGE_REPEATX			= 1<<1;		// Repeat image in X direction.
	public static int	NVG_IMAGE_REPEATY			= 1<<2;		// Repeat image in Y direction.
	public static int	NVG_IMAGE_FLIPY				= 1<<3;		// Flips (inverses) image in Y direction when rendered.
	public static int	NVG_IMAGE_PREMULTIPLIED		= 1<<4;		// Image data has premultiplied alpha.


// TODO nvgCurrentTransform and all the nvgTransform* functions -- use java ?



// deg->rad rad->deg use java!	

// definatly TODO
// nvgCreateImageMem nvgCreateImageRGBA nvgUpdateImage nvgImageSize
// textBox support routines

    
    static public native long create(int flags);
    public native static void beginFrame(long ctx, int width, int height, float ratio);
	native static public void endFrame(long ctx);
	
	public static native void cancelFrame(long ctx);
	public static native void beginPath(long ctx);
	public static native void stroke(long ctx);
	public static native void moveTo(long ctx, float x, float y);
	public static native void lineTo(long ctx, float x, float y);
	public static native void strokeColor(long ctx, Color col);
	public static native void strokeColorf(long ctx, float r, float g, float b, float a);
	public static native void fillColor(long ctx, Color col);
	public static native void fillColorf(long ctx, float r, float g, float b, float a);
	public static native void strokeWidth(long ctx, float size);
	public static native void rect(long ctx, float x, float y, float w, float h);
	public static native void fill(long ctx);
	public static native void save(long ctx);
	public static native void restore(long ctx);
	public static native void reset(long ctx);
	public static native void quadTo(long ctx, float cx, float cy, float x, float y);
	public static native void bezierTo(long ctx, float c1x, float c1y, float c2x, float c2y, float x, float y);
	public static native void lineCap(long ctx, int cap);
	public static native void lineJoin(long ctx, int join);
	public static native void circle(long ctx, float x, float y, float r);
	public static native int createImage(long ctx, String filename, int imageFlags);
	public static native void deleteImage(long ctx, int image);
	public static native void roundedRect(long ctx, float x, float y, float w, float h, float r);
	

	public static native void fillPaint(long ctx, Paint paint);
	
	public static native void scissor(long ctx, float x, float y, float w, float h);
	public static native void intersectScissor(long ctx, float x, float y, float w, float h);
	public static native void resetScissor(long ctx);
	public static native void fontSize(long ctx, float size);
	public static native void fontFace(long ctx, String font);
	public static native void textAlign(long ctx, int align);
	public static native void fontBlur(long ctx, float blur);
	public static native float text(long ctx, float x, float y, String message);
	public static native int createFont(long ctx, String name, String path);
	public static native void rotate(long ctx, float angle);
	public static native void resetTransform(long ctx);
	public static native void translate(long ctx, float x, float y);
	public static native void scale(long ctx, float x, float y);
	public static native void strokePaint(long ctx, Paint paint);
	public static native void miterLimit(long ctx, float limit);	// no idea what this does....
	public static native void globalAlpha(long ctx, float alpha);
	public static native void transform(long ctx, float a, float b, float c, float d, float e, float f);
	public static native void skewX(long ctx, float angle);
	public static native void skewY(long ctx, float angle);
	public static native void arcTo(long ctx, float x1, float y1, float x2, float y2, float radius);
	public static native void pathWinding(long ctx, int dir);
	public static native void arc(long ctx, float cx, float cy, float r, float a0, float a1, int dir);
	public static native void ellipse(long ctx, float cx, float cy, float rx, float ry);
	public static native void textLetterSpacing(long ctx, float spacing);
	public static native void textLineHeight(long ctx, float lineHeight);
	public static native int findFont(long ctx, String name);
	public static native void fontFaceId(long ctx, int font);
}
