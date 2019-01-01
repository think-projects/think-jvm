package org.think.jvm.jnative.java.lang;

import org.think.jvm.jnative.Registry;
import org.think.jvm.rtad.Frame;
import org.think.jvm.rtad.heap.ClassObject;
import org.think.jvm.rtad.heap.Clazz;
import org.think.jvm.rtad.heap.ClazzLoader;
import org.think.jvm.rtad.heap.Method;

import java.util.ArrayList;
import java.util.List;

public class Throwable {
    public Throwable(){
        init();

    }

    public static void init(){
        Registry.getInstance().register("java/lang/Throwable", "fillInStackTrace", "(I)Ljava/lang/Throwable;", frame -> fillInStackTrace(frame));
    }

    public static void fillInStackTrace(Frame frame){
        ClassObject classObject = frame.getLocalVars().getThis();
        frame.getStack().pushRef(classObject);
        classObject.setExtra(createStackTrace(classObject,frame.getThread()));
    }

    private static StackTrace[] createStackTrace(ClassObject classObject,org.think.jvm.rtad.Thread thread){
        Integer skip = distancetoObject(classObject.getClazz())+2;
        Frame[] frames = frames(thread,skip);

        StackTrace[] stackTraces = new StackTrace[frames.length];
        for(int i=0;i<frames.length;i++){
            stackTraces[i] = createStackTraceElement(frames[i]);
        }
        return stackTraces;
    }

    /**
     * 调过的帧数
     * @param thread
     * @param skip
     * @return
     */
    public static Frame[] frames(org.think.jvm.rtad.Thread thread,Integer skip){
        List<Frame> list = new ArrayList<Frame>();
        for(int i=skip;i<thread.getStack().size();i++){
            list.add(thread.getStack().get(i));
        }

        return list.toArray(new Frame[]{});
    }

    public static StackTrace createStackTraceElement(Frame frame){
        org.think.jvm.rtad.heap.Method method = frame.getMethod();
        Clazz clazz = method.getClazz();

        StackTrace stackTrace = new StackTrace(clazz.getSourceFile(),
                clazz.javaName(),
                method.getName(),
                method.getLineNumber(frame.getNextPC() -1));

        return stackTrace;
    }

    public static Integer distancetoObject(Clazz clazz){
        Integer distance = 0;
        for(Clazz c = clazz.getSuperClazz();c != null; c = c.getSuperClazz()){
            distance++;
        }
        return distance;
    }
}

class StackTrace{
    private java.lang.String fileName;
    private java.lang.String className;
    private java.lang.String methodName;
    private Integer lineNumber;

    public StackTrace(java.lang.String fileName,
                      java.lang.String className,
                      java.lang.String methodName,
                      Integer lineNumber){
        this.fileName = fileName;
        this.className = className;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
    }
    public java.lang.String getFileName() {
        return fileName;
    }

    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }

    public java.lang.String getClassName() {
        return className;
    }

    public void setClassName(java.lang.String className) {
        this.className = className;
    }

    public java.lang.String getMethodName() {
        return methodName;
    }

    public void setMethodName(java.lang.String methodName) {
        this.methodName = methodName;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
}