package io.github.rahulojha07;

import io.github.rahulojha07.services.ExcelToJsonServiceImpl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

class Scratch {
    public static void main(String[] args) {
        StringBuilder stringMethods = findMethods(ExcelToJsonServiceImpl.class);
        System.out.println(stringMethods);
    }

    private static StringBuilder findMethods(Class clas) {
        StringBuilder builder = new StringBuilder();

        Method[] methods = clas.getMethods();
        for (Method method : methods) {
            builder.append(" ")
                   .append(method.getName())
                   .append("(");

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> aClass = parameterTypes[i];
                builder.append(aClass.getSimpleName());
                if (i < parameterTypes.length - 1) {
                    builder.append(", ");
                }
            }

            builder.append(")");

            builder.append(": ")
                   .append(method.getReturnType().equals(Void.class) ? "void" : method.getReturnType().getSimpleName())
                   .append(" ");

            if (method.getExceptionTypes().length > 0) {
                builder.append(" throws ")
                       .append(Arrays.stream(method.getExceptionTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")));
            }

            builder.append(System.lineSeparator());

        }
        return builder;

    }

}