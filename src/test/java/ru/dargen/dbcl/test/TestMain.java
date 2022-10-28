package ru.dargen.dbcl.test;

import ru.dargen.dbcl.DynamicByteClassLoader;
import ru.dargen.dbcl.DynamicClassLoader;

import java.io.IOException;
import java.net.URL;

public class TestMain {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        URL url = new URL("https://repo1.maven.org/maven2/com/google/code/gson/gson/2.7/gson-2.7.jar");
        DynamicClassLoader classLoader = (DynamicClassLoader) DynamicByteClassLoader.createFromURL(url, TestMain.class.getClassLoader());

        Class<?> gsonClass = classLoader.loadClass("com.google.gson.Gson");
        System.out.println(gsonClass);

        classLoader.close();
        try {
            gsonClass = classLoader.loadClass("com.google.gson.GsonBuilder");
            System.out.println(gsonClass);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("Error while loading class (its normal because classloader closed)");
        }
    }
}
