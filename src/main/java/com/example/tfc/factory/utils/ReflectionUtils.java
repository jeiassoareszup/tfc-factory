    package com.example.tfc.factory.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class ReflectionUtils {

    public ReflectionUtils() {
        // utility class
        throw new UnsupportedOperationException();
    }

    public static List getListField(Object obj, String getter) {

        List<Method> getters = getAllNonNullFieldGetters(obj);

        return getters.stream().filter(g -> getter.equals(g.getName())).findFirst().map(g -> {
            try {
                return (List) g.invoke(obj);
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList();
            }
        }).orElse(new ArrayList());
    }

    public static String getFieldValue(Object obj, String getter) {

        List<Method> getters = getAllNonNullFieldGetters(obj);

        return getters.stream().filter(g -> getter.equals(g.getName())).findFirst().map(g -> {
            try {
                return g.invoke(obj).toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }).orElse("");
    }

    public static List<Method> getAllNonNullFieldGetters(Object obj) {

        PropertyDescriptor pd;
        List<Method> methods = new ArrayList<>();

        for (Field field : obj.getClass().getDeclaredFields()) {

            try {
                pd = new PropertyDescriptor(field.getName(), obj.getClass());

                if (pd.getReadMethod().invoke(obj) != null) {
                    methods.add(pd.getReadMethod());
                }
            } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return methods;
    }
}
