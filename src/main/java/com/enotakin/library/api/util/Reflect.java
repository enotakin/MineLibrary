package com.enotakin.library.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Reflect {

    private Reflect() {
    }

    public static Class<?> findClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static <R> R get(Class<?> instanceClass, String name) {
        return get(instanceClass, null, name);
    }

    public static <R> R get(Object instanceObject, String name) {
        return get(instanceObject.getClass(), instanceObject, name);
    }

    @SuppressWarnings("unchecked")
    public static <R> R get(Class<?> instanceClass, Object instanceObject, String fieldName) {
        try {
            Field field = instanceClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (R) field.get(instanceObject);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static void set(Class<?> instanceClass, String fieldName, Object valueObject) {
        set(instanceClass, fieldName, valueObject, false);
    }

    public static void set(Class<?> instanceClass, String fieldName, Object valueObject, boolean finalModifier) {
        set(instanceClass, null, fieldName, valueObject, finalModifier);
    }

    public static void set(Object instanceObject, String fieldName, Object valueObject) {
        set(instanceObject, fieldName, valueObject, false);
    }

    public static void set(Object instanceObject, String fieldName, Object valueObject, boolean finalModifier) {
        set(instanceObject.getClass(), instanceObject, fieldName, valueObject, finalModifier);
    }

    public static void set(Class<?> instanceClass, Object instanceObject, String fieldName, Object valueObject) {
        set(instanceClass, instanceObject, fieldName, valueObject, false);
    }

    public static void set(Class<?> instanceClass, Object instanceObject, String fieldName, Object valueObject, boolean finalModifier) {
        try {
            Field field = instanceClass.getDeclaredField(fieldName);
            field.setAccessible(true);

            if (finalModifier) {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            }

            field.set(instanceObject, valueObject);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

}
