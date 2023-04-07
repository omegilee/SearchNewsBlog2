package com.example.searchnewsblog.data.repository;


import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Util {

    private static final String TYPE_NAME_PREFIX = "class ";

    public static Class<?> getReclusiveGenericClass(Class<?> clazz, int index) {
        Class<?> targetClass = clazz;
        while (targetClass != null) {
            Class<?> genericClass = getGenericClass(targetClass, index);
            if (genericClass != null) {
                return genericClass;
            }
            targetClass = targetClass.getSuperclass();
        }
        return null;
    }

    static Class<?> getGenericClass(Class<?> clazz, int index) {
        Type types[] = getGenericSuperclassParameterizedTypesInternal(clazz);
        if ((types != null) && (types.length > index)) {
            try {
                return getClassInternal(types[index]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    static String getClassName(Type type) {
        if (type == null) {
            return "";
        }
        String className = type.toString();
        if (className.startsWith(TYPE_NAME_PREFIX)) {
            className = className.substring(TYPE_NAME_PREFIX.length());
        }
        return className;
    }

    static Type[] getGenericSuperclassParameterizedTypesInternal(Class<?> target) {
        Type[] types = getGenericSuperclassType(target);
        if (types.length > 0 && types[0] instanceof ParameterizedType) {
            return ((ParameterizedType) types[0]).getActualTypeArguments();
        }
        return null;
    }

    static Type[] getGenericSuperclassType(Class<?> target) {
        if (target == null) {
            return new Type[0];
        }

        Type type = target.getGenericSuperclass();
        if (type != null) {
            if (type instanceof ParameterizedType) {
                return new Type[]{type};
            }
        }
        return new Type[0];
    }

    static Class<?> getClassInternal(Type type) throws ClassNotFoundException {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getClassInternal(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getClassInternal(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            }
        }
        String className = getClassName(type);
        if (className == null || className.isEmpty()) {
            return null;
        }
        return Class.forName(className);
    }
}
