package com.quick.recording.gateway.util;

import org.apache.commons.lang.WordUtils;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectUtil {

    private static final String TEMPLATE_GETTER = "get%s";
    private static final String TEMPLATE_SETTER = "set%s";

    public static List<Field> fieldFromClass(Class<?> aClass){
        List<Field> result = Arrays.stream(aClass.getDeclaredFields()).collect(Collectors.toList());
        Class<?> superclass = aClass.getSuperclass();
        if(Objects.nonNull(superclass)){
            result.addAll(fieldFromClass(superclass));
        }
        return result;
    }

    public static List<Field> classTypeFieldFromClass(Class<?> aClass, Class<?> type){
        return Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> type.isAssignableFrom(field.getType()))
                .collect(Collectors.toList());
    }

    public static List<Field> annotatedFieldFromClass(Class<?> aClass, Class<? extends Annotation> type){
        return Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> Objects.nonNull(field.getAnnotation(type)))
                .collect(Collectors.toList());
    }

    public static List<Field> arrayFieldFromClass(Class<?> aClass){
        return Arrays.stream(aClass.getDeclaredFields())
                        .filter(field -> Collection.class.isAssignableFrom(field.getType()))
                        .collect(Collectors.toList());
    }

    public static DataWorkerList getDataWorker(Class<?> aClass){
        return getDataWorker(fieldFromClass(aClass), aClass);
    }

    public static DataWorkerList getDataWorker(List<Field> fields, Class<?> aClass){
        DataWorkerList dataWorkerList = new DataWorkerList();
        fields.stream().map(field -> getDataWorker(field, aClass)).collect(Collectors.toList())
                .forEach(dataWorkerList::addDataWorker);
        return dataWorkerList;
    }

    public static DataWorker getDataWorker(Field field, Class<?> aClass){
        Method getter = getterFromField(field, aClass);
        Method setter = setterFromField(field, aClass);
        return new DataWorker(field, getter, setter);
    }

    @Nullable
    public static Method getterFromField(Field field, Class<?> aClass) {
        try {
            return aClass.getMethod(getterNameFromField(field));
        }
        catch (NoSuchMethodException exception){
            return null;
        }
    }

    @Nullable
    public static Method setterFromField(Field field, Class<?> aClass) {
        try {
            return aClass.getMethod(setterNameFromField(field), field.getType());
        }
        catch (NoSuchMethodException exception){
            return null;
        }
    }

    public static String getterNameFromField(Field field){
        return String.format(TEMPLATE_GETTER, WordUtils.capitalize(field.getName()));
    }

    public static String setterNameFromField(Field field){
        return String.format(TEMPLATE_SETTER, WordUtils.capitalize(field.getName()));
    }

    public static class DataWorkerList implements Iterable<DataWorker> {

        private Map<String, DataWorker> map = new HashMap<>();

        public DataWorker addDataWorker(DataWorker dataWorker){
            return map.put(dataWorker.getField().getName(), dataWorker);
        }

        public DataWorker getDataWorker(String fieldName){
            return map.get(fieldName);
        }

        @Override
        public Iterator<DataWorker> iterator() {
            return map.values().iterator();
        }

    }

    public static class DataWorker{

        private Field field;
        private Method getter;
        private Method setter;

        public DataWorker(Field field, Method getter, Method setter) {
            this.field = field;
            this.getter = getter;
            this.setter = setter;
        }

        public Field getField() {
            return field;
        }

        public Method getGetter() {
            return getter;
        }

        public Method getSetter() {
            return setter;
        }

    }

}
