This is a Java wrapper for NanoVG https://github.com/memononen/nanovg

as NanoVG is "work in progress" I have made a local copy of just the
C source code I need and the original README.md in the nanovg folder

About the "test" class - currently the jni library compiles to use GLESv2
the test uses my own wrapping of GLESv2 https://github.com/chriscamacho/Jgles2

HOWEVER with very *minor* changes to the Makefile you can link to OpenGL
and use the nanovg with some other wrapper, for example Lwjgl

You will also need to change

\#include \<GLES2/gl2.h>

\#define NANOVG_GLES2_IMPLEMENTATION

inside nvg.c

Jnanovg has NO dependencies on Jgles2 which is only used in the "test"
class...

to compile the wrapper use the jni ant target

to compile the test you will need Jgles2.jar and libJgles2.so in this
directory.

Java and C are not a 1:1 fit and as with any wrapper there are some
differences between how its used in the different languages

Everything lives in a singleton class called nvg

The context (of which there is typically only one) is returned as a 
raw C pointer, obviously you are responsible enough not to be doing
random (or any) mathmatics on this value... 

The NVGcolor structure functions are not entirely wrapped at the moment.
nvg.Color holds a NVGcolor structure there are setter methods to change
its value and this java object is used in any function expecting a 
NVGcolor parameter

The nvg.Paint object is used to hold a copy of a NVGpaint structure

What differes from nanoVG is the way the paint object is used
any function that returns a NVGpaint is a method of the nvg.Paint object
when called they update the instances internal NVGpaint structure
(its stored in a java native buffer so there is no C allocation and
the nvg.Paint object can be safely deleted by the GC without need of a
delete or close method)

Between the C demo the (ahem) "test" class and nvg.java you should be
able to work out how to use nanovg...
