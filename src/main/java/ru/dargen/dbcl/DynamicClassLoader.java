package ru.dargen.dbcl;

import ru.dargen.dbcl.url.JarURLStreamHandler;
import ru.dargen.dbcl.util.IOHelper;
import sun.misc.JavaNetAccess;
import sun.misc.Resource;
import sun.misc.SharedSecrets;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicClassLoader extends URLClassLoader {

    protected static final JavaNetAccess NET_ACCESS = SharedSecrets.getJavaNetAccess();

    protected final URLClassPath classPath;
    protected final JarURLStreamHandler urlStreamHandler;

    public DynamicClassLoader(JarURLStreamHandler urlStreamHandler, ClassLoader parent) throws MalformedURLException {
        super(new URL[]{new URL("dynamic", "", -1, "/", urlStreamHandler)}, parent);
        this.urlStreamHandler = urlStreamHandler;
        this.classPath = NET_ACCESS.getURLClassPath(this);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = findLoadedClass(name);
        if (clazz == null) {
            Resource resource = getClassResource(name);
            clazz = resource != null ? loadClass(name, resource) : super.loadClass(name, resolve);
        }
        if (resolve) resolveClass(clazz);
        return clazz;
    }

    protected Class<?> loadClass(String name, Resource resource) throws ClassNotFoundException {
        byte[] bytes;
        try {
            bytes = IOHelper.readAllBytes(resource.getInputStream());
        } catch (IOException e) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }


    protected Resource getClassResource(String className) {
        return classPath.getResource(resolveClassName(className));
    }

    protected static String resolveClassName(String resourceName) {
        return resourceName.replace('.', '/') + ".class";
    }

    @Override
    public void close() throws IOException {
        super.close();
        urlStreamHandler.close();
    }

}
