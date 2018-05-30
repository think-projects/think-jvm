package org.think.jvm.instructions.references;

import org.think.jvm.exceptions.VMException;
import org.think.jvm.instructions.Instruction;
import org.think.jvm.Visitor;
import org.think.jvm.rtad.Frame;
import org.think.jvm.rtad.OperandStack;
import org.think.jvm.rtad.heap.*;

/*
*调用接口方法e
*@author lixiaobin
*/
public class INVOKEINTERFAC extends Instruction {
    protected Integer index;
    public void accept(Visitor visitor){
        super.accept(visitor);
        index = visitor.readUInt16();
        visitor.readUInt8();//count
        visitor.readUInt8();//must be 0
    }
    public void execute(Visitor visitor){
        super.execute(visitor);
        Frame frame = visitor.getFrame();
        ConstantPool constantPool = frame.getMethod().getClazz().getConstantPool();
        InterfaceMemberRef methodRef = (InterfaceMemberRef)constantPool.getConstant(index);
//        Clazz clazz = methodRef.resolvedClass();
        Method method = methodRef.resolvedInterfaceMethod();
        Object object = frame.getStack().getRefFromTop(method.argSlotcount-1);
        if(object == null){
            throw new VMException("java.lang.NullPointException");
        }
        Method methodToBeInvoked = method;
//        LookupMethodInClass(clazz.superClazz,method.getName(),method.getDescriptor());
        if("println".equals(methodRef.getName())){
            OperandStack operandStack = visitor.getFrame().getStack();
            log.info(operandStack.popSolt());
        }else{
            invokeMethod(frame,methodToBeInvoked);
        }
    }

//    @Override
//    public String toString() {
//        return "Instruction{}";
//    }
}