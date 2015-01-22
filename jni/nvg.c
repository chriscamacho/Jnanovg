#include <stdio.h> // for lameass debugging (printf)
#include <assert.h> // slightly less lame debugging


#include "stdlib.h"

#include <GLES2/gl2.h>
#define NANOVG_GLES2_IMPLEMENTATION

#include "nvg_nvg.h"
#include "nanovg.h"

#include "nanovg_gl.h"


#define checkJO(JO, MSG) if (JO==NULL) { printf(MSG); fflush(stdout); return; }


// used for debuging : from the gnu libc manual 
/*
#include <execinfo.h>
void print_trace (void)
{
  void *array[10];
  size_t size;
  char **strings;
  size_t i;

  size = backtrace (array, 10);
  
  strings = backtrace_symbols (array, size);

  printf ("Obtained %zd stack frames.\n", size);

  for (i = 0; i < size; i++)
     printf ("%s\n", strings[i]);

  fflush(stdout);
  free (strings);
}
*/

// both nvg.Paint and nvg.RGBA have a field "buff" this gets the 
// physical address of this buffer
void* getBuffPtr(JNIEnv *e, jobject jo)
{
	jobject cls = (*e)->GetObjectClass(e, jo);
	jfieldID fid = (*e)->GetFieldID(e, cls,"buff","Ljava/nio/FloatBuffer;");
	jobject jbuff = (*e)->GetObjectField(e, jo, fid);
	return (*e)->GetDirectBufferAddress(e, jbuff);
}

// given a jni env and NVGpaint this creates a java object
// encapsulating the NVGpaint structure (nvg.Paint)

/* depricated
jobject createPaintJobject(JNIEnv *e, NVGpaint p) {
	jclass cls = (*e)->FindClass(e, "nvg$Paint");
	assert(cls != NULL);

	jmethodID constructor = (*e)->GetMethodID(e, cls, "<init>", "()V");
	assert(constructor != NULL);
	
	jobject nvgpaint = (*e)->NewObject(e, cls, constructor);
	assert(nvgpaint!=NULL);
	
	jfieldID fid = (*e)->GetFieldID(e, cls,"buff","Ljava/nio/FloatBuffer;");
	assert(fid != NULL);
	jobject jbuff = (*e)->GetObjectField(e, nvgpaint, fid);
	assert(jbuff != NULL);
	NVGpaint* ptr = (void*)(*e)->GetDirectBufferAddress(e, jbuff);
	assert(ptr!=NULL);
	*ptr = p;
	return nvgpaint;
}
*/

JNIEXPORT jlong JNICALL Java_nvg_nvg_test
  (JNIEnv *e, jclass c, jint i)
{
	printf("i=%i\n",i);
	//printf("sizeof NVGpaint=%i\n",sizeof(NVGpaint));
	//printf("sizeof NVGpaint=%i\n",sizeof(NVGcolor));
	
	// this is only here in case of a change with nanovg struct sizes
	// failure of these asserts will need nvg subclass buffer changes
	assert(sizeof(NVGpaint)==76);
	assert(sizeof(NVGcolor)==16);
	
	return glGetError();
}


JNIEXPORT jlong JNICALL Java_nvg_nvg_create
  (JNIEnv *e, jclass c, jint flags)
{
	return (jlong)(long) nvgCreateGLES2(flags);
}

JNIEXPORT void JNICALL Java_nvg_nvg_beginFrame
  (JNIEnv *e, jclass c, jlong ctx, jint wid, jint hgt, jfloat r)
{
	nvgBeginFrame((NVGcontext*)ctx, wid, hgt, r);
}

JNIEXPORT void JNICALL Java_nvg_nvg_cancelFrame
  (JNIEnv *e, jclass c, jlong ctx)
{
	nvgCancelFrame((NVGcontext*)ctx);	
}

JNIEXPORT void JNICALL Java_nvg_nvg_endFrame
  (JNIEnv *e, jclass c, jlong ctx)
{
	nvgEndFrame((NVGcontext*)ctx);
}

JNIEXPORT void JNICALL Java_nvg_nvg_beginPath
  (JNIEnv *e, jclass c, jlong ctx)
{
	nvgBeginPath((NVGcontext*)ctx);
}

JNIEXPORT void JNICALL Java_nvg_nvg_stroke
  (JNIEnv *e, jclass c, jlong ctx)
{
	nvgStroke((NVGcontext*)ctx);
}

JNIEXPORT void JNICALL Java_nvg_nvg_moveTo
  (JNIEnv *e, jclass c, jlong ctx, jfloat x, jfloat y)
{
	nvgMoveTo((NVGcontext*)ctx,x,y);
}

JNIEXPORT void JNICALL Java_nvg_nvg_lineTo
  (JNIEnv *e, jclass c, jlong ctx, jfloat x, jfloat y)
{
	nvgLineTo((NVGcontext*)ctx,x,y);
}

JNIEXPORT void JNICALL Java_nvg_nvg_fillColor
  (JNIEnv *e, jclass c, jlong ctx, jobject jo)
{
	checkJO(jo,"\nNull colour was passed to fillColor\n");
	nvgFillColor((NVGcontext*)ctx, (NVGcolor)*((NVGcolor*)getBuffPtr(e,jo)));
}

JNIEXPORT void JNICALL Java_nvg_nvg_fillColorf
  (JNIEnv *e, jclass c, jlong ctx, jfloat r, jfloat g, jfloat b, jfloat a)
{
	nvgFillColor((NVGcontext*)ctx, nvgRGBAf(r,g,b,a) );
}

JNIEXPORT void JNICALL Java_nvg_nvg_strokeColor
  (JNIEnv *e, jclass c, jlong ctx, jobject jo)
{
	checkJO(jo,"\nNull colour was passed to strokeColor\n");
	nvgStrokeColor((NVGcontext*)ctx, (NVGcolor)*((NVGcolor*)getBuffPtr(e,jo)));
}

JNIEXPORT void JNICALL Java_nvg_nvg_strokeColorf
  (JNIEnv *e, jclass c, jlong ctx, jfloat r, jfloat g, jfloat b, jfloat a) 
{
	nvgStrokeColor((NVGcontext*)ctx, nvgRGBAf(r,g,b,a));
}

JNIEXPORT void JNICALL Java_nvg_nvg_strokeWidth
  (JNIEnv *e, jclass c, jlong ctx, jfloat w)
{
	nvgStrokeWidth((NVGcontext*)ctx, w);
}

JNIEXPORT void JNICALL Java_nvg_nvg_rect
  (JNIEnv *e, jclass c, jlong ctx, jfloat x, jfloat y, jfloat w, jfloat h)
{
	nvgRect((NVGcontext*)ctx, x,y, w,h);
}

JNIEXPORT void JNICALL Java_nvg_nvg_fill
  (JNIEnv *e, jclass c, jlong ctx)
{
	nvgFill((NVGcontext*)ctx);
}

JNIEXPORT void JNICALL Java_nvg_nvg_save
  (JNIEnv *e, jclass c, jlong ctx)
{
	nvgSave((NVGcontext*)ctx);
}

JNIEXPORT void JNICALL Java_nvg_nvg_restore
  (JNIEnv *e, jclass c, jlong ctx)
{
	nvgRestore((NVGcontext*)ctx);
}

JNIEXPORT void JNICALL Java_nvg_nvg_reset
  (JNIEnv *e, jclass c, jlong ctx)
{
	nvgReset((NVGcontext*)ctx);
}

JNIEXPORT void JNICALL Java_nvg_nvg_miterLimit
  (JNIEnv *e, jclass c, jlong ctx, jfloat l)
{
	nvgMiterLimit((NVGcontext*)ctx, l);
}


JNIEXPORT void JNICALL Java_nvg_nvg_quadTo
  (JNIEnv *e, jclass c, jlong ctx, jfloat cx, jfloat cy, jfloat x, jfloat y)
{
	nvgQuadTo((NVGcontext*)ctx, cx,cy, x,y);
}

JNIEXPORT void JNICALL Java_nvg_nvg_bezierTo
  (JNIEnv *e, jclass c, jlong ctx, jfloat c1x, jfloat c1y, jfloat c2x, jfloat c2y, jfloat x, jfloat y)
{
	nvgBezierTo((NVGcontext*)ctx, c1x,c1y, c2x,c2y, x,y);
}

JNIEXPORT void JNICALL Java_nvg_nvg_lineCap
  (JNIEnv *e, jclass c, jlong ctx, jint cap)
{
	nvgLineCap((NVGcontext*)ctx, cap);
}

JNIEXPORT void JNICALL Java_nvg_nvg_lineJoin
  (JNIEnv *e, jclass c, jlong ctx, jint join)
{
	nvgLineJoin((NVGcontext*)ctx, join);
}

JNIEXPORT void JNICALL Java_nvg_nvg_circle
  (JNIEnv *e, jclass c, jlong ctx, jfloat cx, jfloat cy, jfloat r)
{
	nvgCircle((NVGcontext*)ctx, cx, cy, r);	
}

JNIEXPORT int JNICALL Java_nvg_nvg_createImage
  (JNIEnv *e, jclass c, jlong ctx, jstring fname, jint flags)
{
	int r;
    const char *filename = (*e)->GetStringUTFChars(e, fname, 0);

	r = nvgCreateImage((NVGcontext*)ctx, filename, flags);	
    
    (*e)->ReleaseStringUTFChars(e, fname, filename);   
	return r;
}

JNIEXPORT void JNICALL Java_nvg_nvg_deleteImage
  (JNIEnv *e, jclass c, jlong ctx, jint im) 
{
	nvgDeleteImage((NVGcontext*)ctx, im);  
}

JNIEXPORT void JNICALL Java_nvg_nvg_roundedRect
  (JNIEnv *e, jclass c, jlong ctx, jfloat x, jfloat y, jfloat w, jfloat h, jfloat r)
{  
	nvgRoundedRect((NVGcontext*)ctx, x,y, w,h, r);
}


JNIEXPORT void JNICALL Java_nvg_nvg__1_1imagePattern
  (JNIEnv *e, jclass c, jlong ctx, jobject paint, jfloat ox, jfloat oy,
		jfloat ex, jfloat ey, jfloat angle, jint image, jfloat alpha)
{
	// paint can't be null as it's passed internally as the objects instance as a convenience 
	NVGpaint p = nvgImagePattern((NVGcontext*)ctx, ox, oy, ex, ey, angle, image, alpha);
	NVGpaint* dp = getBuffPtr(e,paint);
	*dp = p;
}  

JNIEXPORT void JNICALL Java_nvg_nvg__1_1linearGradient
  (JNIEnv *e, jclass cls, jlong ctx, jobject paint, jfloat sx, jfloat sy, jfloat ex, jfloat ey, jobject scol, jobject ecol)
{
	checkJO(scol,"\nLinearGradient was passed NULL start colour\n");
	checkJO(ecol,"\nLinearGradient was passed NULL end colour\n");
	NVGpaint p = nvgLinearGradient((NVGcontext*)ctx,sx,sy,ex,ey,
					(NVGcolor)*((NVGcolor*)getBuffPtr(e,scol)),
					(NVGcolor)*((NVGcolor*)getBuffPtr(e,ecol)));
	NVGpaint* dp = (NVGpaint*)getBuffPtr(e,paint);
	*dp = p;
}

JNIEXPORT void JNICALL Java_nvg_nvg__1_1boxGradient
  (JNIEnv *e, jclass cls, jlong ctx, jobject paint, jfloat x, jfloat y, 
				jfloat w, jfloat h, jfloat r, jfloat f, jobject scol, jobject ecol)
{
	checkJO(scol,"\nBoxGradient was passed NULL start colour\n");
	checkJO(ecol,"\nBoxGradient was passed NULL end colour\n");
	NVGpaint p = nvgBoxGradient((NVGcontext*)ctx, x, y, w, h, r, f, 
					(NVGcolor)*((NVGcolor*)getBuffPtr(e,scol)),
					(NVGcolor)*((NVGcolor*)getBuffPtr(e,ecol)));
	NVGpaint* dp = (NVGpaint*)getBuffPtr(e,paint);
	*dp = p;							
}

JNIEXPORT void JNICALL Java_nvg_nvg__1_1radialGradient
  (JNIEnv *e, jclass c, jlong ctx, jobject paint, jfloat cx, jfloat cy, jfloat inr, jfloat outr, jobject scol, jobject ecol)
{
	checkJO(scol,"\nRadialGradient was passed NULL start colour\n");
	checkJO(ecol,"\nRadialGradient was passed NULL end colour\n");
	NVGpaint p = nvgRadialGradient((NVGcontext*)ctx, cx, cy, inr, outr,
					(NVGcolor)*((NVGcolor*)getBuffPtr(e,scol)),
					(NVGcolor)*((NVGcolor*)getBuffPtr(e,ecol)));
	NVGpaint* dp = (NVGpaint*)getBuffPtr(e,paint);
	*dp = p;
}
  
JNIEXPORT void JNICALL Java_nvg_nvg_fillPaint
  (JNIEnv *e, jclass c, jlong ctx, jobject paint)
{
	checkJO(paint,"\nFillPaint was passed NULL paint\n");  
	nvgFillPaint((NVGcontext*)ctx, (NVGpaint)*((NVGpaint*)getBuffPtr(e,paint)));
}

JNIEXPORT void JNICALL Java_nvg_nvg_scissor
  (JNIEnv *e, jclass c, jlong ctx, jfloat x, jfloat y, jfloat w, jfloat h)
{
	nvgScissor((NVGcontext*)ctx, x,y, w,h);
}

JNIEXPORT void JNICALL Java_nvg_nvg_intersectScissor
  (JNIEnv *e, jclass c, jlong ctx, jfloat x, jfloat y, jfloat w, jfloat h)
{
	nvgIntersectScissor((NVGcontext*)ctx, x, y, w, h);
}

JNIEXPORT void JNICALL Java_nvg_nvg_resetScissor
  (JNIEnv *e, jclass c, jlong ctx)
{
	nvgResetScissor((NVGcontext*)ctx);
}

JNIEXPORT void JNICALL Java_nvg_nvg_fontSize
  (JNIEnv *e, jclass c, jlong ctx, jfloat sz)
{
	nvgFontSize((NVGcontext*)ctx, sz);
}

JNIEXPORT void JNICALL Java_nvg_nvg_fontFace
  (JNIEnv *e, jclass c, jlong ctx, jstring jfont)
{
	checkJO(jfont, "\nfontFace was passed a NULL string\n");
    const char *font = (*e)->GetStringUTFChars(e, jfont, 0);

	nvgFontFace((NVGcontext*)ctx, font);	
    
    (*e)->ReleaseStringUTFChars(e, jfont, font);   	
}

JNIEXPORT void JNICALL Java_nvg_nvg_textAlign
  (JNIEnv *e, jclass c, jlong ctx, jint align)
{
	nvgTextAlign((NVGcontext*)ctx, align);
}

JNIEXPORT void JNICALL Java_nvg_nvg_fontBlur
  (JNIEnv *e, jclass c, jlong ctx, jfloat blr)
{
	nvgFontBlur((NVGcontext*)ctx, blr);
}

JNIEXPORT jfloat JNICALL Java_nvg_nvg_text
  (JNIEnv *e, jclass c, jlong ctx, jfloat x, jfloat y, jstring jmesg)
{
	checkJO(jmesg,"\nvg.Text was passed a NULL string\n");
    const char *mesg = (*e)->GetStringUTFChars(e, jmesg, 0);
    	
	float r=nvgText((NVGcontext*)ctx, x,y, mesg, NULL);

    (*e)->ReleaseStringUTFChars(e, jmesg, mesg);
    return r;  	
}

JNIEXPORT jint JNICALL Java_nvg_nvg_createFont
  (JNIEnv *e, jclass c, jlong ctx, jstring jname, jstring jpath)
{
	checkJO(jname,"\ncreateFont was passed a NULL name string\n");
	checkJO(jpath,"\ncreateFont was passed a NULL path string\n");
	
	const char *name = (*e)->GetStringUTFChars(e, jname, 0);
	const char *path = (*e)->GetStringUTFChars(e, jpath, 0);
	
	int r = nvgCreateFont((NVGcontext*)ctx, name, path);

    (*e)->ReleaseStringUTFChars(e, jname, name);
    (*e)->ReleaseStringUTFChars(e, jpath, path);

	return r;
}

JNIEXPORT void JNICALL Java_nvg_nvg_rotate
  (JNIEnv *e, jclass c, jlong ctx, jfloat angle)
{
	nvgRotate((NVGcontext*)ctx, angle);
}

JNIEXPORT void JNICALL Java_nvg_nvg_resetTransform
  (JNIEnv *e, jclass c, jlong ctx)
{
	nvgResetTransform((NVGcontext*)ctx);
}

JNIEXPORT void JNICALL Java_nvg_nvg_translate
  (JNIEnv *e, jclass c, jlong ctx, jfloat x, jfloat y)
{
	nvgTranslate((NVGcontext*)ctx, x, y);
}

JNIEXPORT void JNICALL Java_nvg_nvg_scale
  (JNIEnv *e, jclass c, jlong ctx, jfloat x, jfloat y)
{
	nvgScale((NVGcontext*)ctx, x, y);
}

JNIEXPORT void JNICALL Java_nvg_nvg_strokePaint
  (JNIEnv *e, jclass c, jlong ctx, jobject paint)
{
	checkJO(paint,"\nstrokePaint was passed a NULL paint\n");
	nvgStrokePaint((NVGcontext*)ctx, (NVGpaint)*((NVGpaint*)getBuffPtr(e,paint)));
}

JNIEXPORT void JNICALL Java_nvg_nvg_globalAlpha
  (JNIEnv *e, jclass c, jlong ctx, jfloat alpha)
{
	nvgGlobalAlpha((NVGcontext*)ctx, alpha);
}

JNIEXPORT void JNICALL Java_nvg_nvg_transform
  (JNIEnv *env, jclass cls, jlong ctx, jfloat a, jfloat b, jfloat c, jfloat d, jfloat e, jfloat f)
{
	nvgTransform((NVGcontext*)ctx, a, b, c, d, e, f);
}

JNIEXPORT void JNICALL Java_nvg_nvg_skewX
  (JNIEnv *e, jclass c, jlong ctx, jfloat angle)
{
	nvgSkewX((NVGcontext*)ctx, angle);
}

JNIEXPORT void JNICALL Java_nvg_nvg_skewY
  (JNIEnv *e, jclass c, jlong ctx, jfloat angle)
{
	nvgSkewY((NVGcontext*)ctx, angle);
}

JNIEXPORT void JNICALL Java_nvg_nvg_arcTo
  (JNIEnv *e, jclass c, jlong ctx, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jfloat radius)
{
	nvgArcTo((NVGcontext*)ctx, x1, y1, x2, y2, radius);
}

JNIEXPORT void JNICALL Java_nvg_nvg_pathWinding
  (JNIEnv *e, jclass c, jlong ctx, jint dir)
{
	nvgPathWinding((NVGcontext*)ctx, dir);
}

JNIEXPORT void JNICALL Java_nvg_nvg_arc
  (JNIEnv *e, jclass c, jlong ctx, jfloat cx, jfloat cy, jfloat r, jfloat a0, jfloat a1, jint dir)
{
	nvgArc((NVGcontext*)ctx, cx, cy, r, a0, a1, dir);
}

JNIEXPORT void JNICALL Java_nvg_nvg_ellipse
  (JNIEnv *e, jclass c, jlong ctx, jfloat cx, jfloat cy, jfloat rx, jfloat ry)
{
	nvgEllipse((NVGcontext*)ctx, cx, cy, rx, ry);
}

JNIEXPORT void JNICALL Java_nvg_nvg_textLetterSpacing
  (JNIEnv *e, jclass c, jlong ctx, jfloat spacing)
{
	nvgTextLetterSpacing((NVGcontext*)ctx, spacing);
}

JNIEXPORT void JNICALL Java_nvg_nvg_textLineHeight
  (JNIEnv *e, jclass c, jlong ctx, jfloat lineHeight)
{
	nvgTextLineHeight((NVGcontext*)ctx, lineHeight);
}

JNIEXPORT jint JNICALL Java_nvg_nvg_findFont
  (JNIEnv *e, jclass c, jlong ctx, jstring jname)
{
	checkJO(jname,"\nfindFont was passed a NULL name string\n");
	const char *name = (*e)->GetStringUTFChars(e, jname, 0);

	int r = nvgFindFont((NVGcontext*)ctx, name);

    (*e)->ReleaseStringUTFChars(e, jname, name);
    
    return r;	
}

JNIEXPORT void JNICALL Java_nvg_nvg_fontFaceId
  (JNIEnv *e, jclass c, jlong ctx, jint fid)
{
	nvgFontFaceId((NVGcontext*)ctx, fid);
}  
  
JNIEXPORT void JNICALL Java_nvg_nvg__1_1getTransform
  (JNIEnv *e, jclass c, jlong ctx, jobject jdest)
{
	float* dest = (float*)getBuffPtr(e,jdest);
	nvgCurrentTransform((NVGcontext*)ctx, dest);
}  

JNIEXPORT void JNICALL Java_nvg_nvg__1_1setTransform
  (JNIEnv *e, jclass c, jlong ctx, jobject jsrc)
{
	float* src = (float*)getBuffPtr(e,jsrc);
	nvgResetTransform((NVGcontext*)ctx);
	nvgTransform((NVGcontext*)ctx, src[0], src[1], src[2], src[3], src[4], src[5] );
}
