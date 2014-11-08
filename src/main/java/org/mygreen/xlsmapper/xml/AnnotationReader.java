package org.mygreen.xlsmapper.xml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods to access to annotations.
 * <p>
 * You can access both hard-coded annotations and dynamic annotations
 * which are declared in the XML file using this class.
 *
 * @author Naoki Takezoe
 */
public class AnnotationReader {

    private XMLInfo xmlInfo;

    /**
     * Constructor.
     *
     * @param xmlInfo the XMLInfo object which contains the dynamic annotations or null.
     */
    public AnnotationReader(XMLInfo xmlInfo){
        this.xmlInfo = xmlInfo;
    }

    /**
     * Returns all class annotations.
     *
     * @param clazz the target class
     * @return all annotations present on target class
     * @throws AnnotationReadException 
     */
    public Annotation[] getAnnotations(Class<?> clazz) throws AnnotationReadException {
        
        if(xmlInfo!=null && xmlInfo.getClassInfo(clazz.getName())!=null){
            ClassInfo classInfo = xmlInfo.getClassInfo(clazz.getName());
            List<Annotation> list = new ArrayList<Annotation>();
            for(AnnotationInfo annInfo: classInfo.getAnnotationInfos()){
                try {
                    list.add(DynamicAnnotationBuilder.buildAnnotation(
                            Class.forName(annInfo.getAnnotationClass()), annInfo));
                } catch (ClassNotFoundException e) {
                    throw new AnnotationReadException(String.format("not found class '%s'", annInfo.getAnnotationClass()), e);
                }
            }
            return list.toArray(new Annotation[list.size()]);
        }
        return clazz.getAnnotations();
    }

    /**
     * Returns a class annotation for the specified type if such an annotation is present, else null.
     *
     * @param <A> the type of the annotation
     * @param clazz the target class
     * @param ann the Class object corresponding to the annotation type
     * @return the target class's annotation for the specified annotation type if present on this element, else null
     * @throws AnnotationReadException 
     */
    @SuppressWarnings("unchecked")
    public <A extends Annotation> A  getAnnotation(Class<?> clazz, Class<A> ann) throws AnnotationReadException {
        if(xmlInfo!=null && xmlInfo.getClassInfo(clazz.getName())!=null){
            ClassInfo classInfo = xmlInfo.getClassInfo(clazz.getName());
            if(classInfo.getAnnotationInfo(ann.getName())!=null){
                AnnotationInfo annInfo = classInfo.getAnnotationInfo(ann.getName());
                try {
                    return (A)DynamicAnnotationBuilder.buildAnnotation(
                            Class.forName(annInfo.getAnnotationClass()), annInfo);
                } catch (ClassNotFoundException e) {
                    throw new AnnotationReadException(String.format("not found class '%s'", annInfo.getAnnotationClass()), e);
                }
            }
        }
        return clazz.getAnnotation(ann);
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getAnnotation(Class<?> clazz, Method method, Class<A> ann) throws AnnotationReadException {
        if(xmlInfo!=null && xmlInfo.getClassInfo(clazz.getName())!=null){
            ClassInfo classInfo = xmlInfo.getClassInfo(clazz.getName());
            if(classInfo.getMethodInfo(method.getName())!=null){
                MethodInfo methodInfo = classInfo.getMethodInfo(method.getName());
                if(methodInfo!=null && methodInfo.getAnnotationInfo(ann.getName())!=null){
                    AnnotationInfo annInfo = methodInfo.getAnnotationInfo(ann.getName());
                    try {
                        return (A)DynamicAnnotationBuilder.buildAnnotation(
                                Class.forName(annInfo.getAnnotationClass()), annInfo);
                    } catch (ClassNotFoundException e) {
                        throw new AnnotationReadException(String.format("not found class '%s'", annInfo.getAnnotationClass()), e);
                    }
                }
            }
        }
        return method.getAnnotation(ann);
    }

    public Annotation[] getAnnotations(Class<?> clazz, Method method) throws AnnotationReadException {
        if(xmlInfo!=null && xmlInfo.getClassInfo(clazz.getName())!=null){
            ClassInfo classInfo = xmlInfo.getClassInfo(clazz.getName());
            if(classInfo.getMethodInfo(method.getName())!=null){
                MethodInfo methodInfo = classInfo.getMethodInfo(method.getName());
                List<Annotation> list = new ArrayList<Annotation>();
                for(AnnotationInfo annInfo: methodInfo.getAnnotationInfos()){
                    try {
                        list.add(DynamicAnnotationBuilder.buildAnnotation(
                                Class.forName(annInfo.getAnnotationClass()), annInfo));
                    } catch (ClassNotFoundException e) {
                        throw new AnnotationReadException(String.format("not found class '%s'", annInfo.getAnnotationClass()), e);
                    }
                }
                return list.toArray(new Annotation[list.size()]);
            }
        }
        return method.getAnnotations();
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getAnnotation(Class<?> clazz, Field field, Class<A> ann) throws AnnotationReadException {
        if(xmlInfo!=null && xmlInfo.getClassInfo(clazz.getName())!=null){
            ClassInfo classInfo = xmlInfo.getClassInfo(clazz.getName());
            if(classInfo.getFieldInfo(field.getName())!=null){
                FieldInfo fieldInfo = classInfo.getFieldInfo(field.getName());
                if(fieldInfo!=null && fieldInfo.getAnnotationInfo(ann.getName())!=null){
                    AnnotationInfo annInfo = fieldInfo.getAnnotationInfo(ann.getName());
                    try {
                        return (A)DynamicAnnotationBuilder.buildAnnotation(
                            Class.forName(annInfo.getAnnotationClass()), annInfo);
                    } catch (ClassNotFoundException e) {
                        throw new AnnotationReadException(String.format("not found class '%s'", annInfo.getAnnotationClass()), e);
                    }
                }
            }
        }
        return field.getAnnotation(ann);
    }

    public Annotation[] getAnnotations(Class<?> clazz, Field field) throws AnnotationReadException {
        if(xmlInfo!=null && xmlInfo.getClassInfo(clazz.getName())!=null){
            ClassInfo classInfo = xmlInfo.getClassInfo(clazz.getName());
            if(classInfo.getFieldInfo(field.getName())!=null){
                FieldInfo fieldInfo = classInfo.getFieldInfo(field.getName());
                List<Annotation> list = new ArrayList<Annotation>();
                for(AnnotationInfo annInfo: fieldInfo.getAnnotationInfos()){
                    try {
                        list.add(DynamicAnnotationBuilder.buildAnnotation(
                                Class.forName(annInfo.getAnnotationClass()), annInfo));
                    } catch (ClassNotFoundException e) {
                        throw new AnnotationReadException(String.format("not found class '%s'", annInfo.getAnnotationClass()), e);
                    }
                }
                return list.toArray(new Annotation[list.size()]);
            }
        }
        return field.getAnnotations();
    }
}
