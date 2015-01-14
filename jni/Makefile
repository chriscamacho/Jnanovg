CC=gcc

#JAVA_HOME=`readlink -f /usr/bin/javac | sed "s:bin/javac::"`
# gentoo is a little less KISS !
JAVA_HOME=/usr/lib/jvm/icedtea-7

LIBS=$(shell pkg-config glesv2 --libs) 

# iced tea has some additional headers in include/linux
CFLAGS=-fPIC -I$ nanovg/src/ -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

jni/libJnanovg.so: jni/nanovg.o jni/nvg.h jni/nvg.o
	$(CC) -fPIC -shared -Wl,-soname,libJnanovg.so jni/nanovg.o jni/nvg.o $(LIBS) -o jni/libJnanovg.so 

jni/nvg.o: jni/nvg.c
	$(CC) $(CFLAGS) -c jni/nvg.c -o jni/nvg.o

jni/nanovg.o:
	$(CC) $(CFLAGS) -c nanovg/src/nanovg.c -o jni/nanovg.o

jni/nvg.h: jni/nvg.java
	javac -sourcepath jni -d jni jni/nvg.java
	javah -sourcepath jni -d jni nvg
	
clean:
	rm -rf jni/nvg.class
	rm -rf jni/libJnanovg.so
	rm -rf jni/nanovg.o
	rm -rf jni/nvg.o
	rm -rf jni/nvg.h
	rm -rf jni/nvg_Color.h
	rm -rf jni/nvg_Paint.h
