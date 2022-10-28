package ru.dargen.dbcl;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.dargen.dbcl.url.JarURLStreamHandler;
import ru.dargen.dbcl.util.IOHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

@UtilityClass
public class DynamicByteClassLoader {

    @SneakyThrows
    public ClassLoader createFromInputStream(InputStream inputStream, ClassLoader classLoader) {
        return new DynamicClassLoader(new JarURLStreamHandler(IOHelper.readJarEntries(inputStream)), classLoader);
    }

    public ClassLoader createFromInputStream(InputStream inputStream) {
        return createFromInputStream(inputStream, null);
    }

    public ClassLoader createFromBytes(byte[] bytes, ClassLoader classLoader) {
        return createFromInputStream(new ByteArrayInputStream(bytes), classLoader );
    }

    public ClassLoader createFromBytes(byte[] bytes) {
        return createFromBytes(bytes);
    }

    @SneakyThrows
    public ClassLoader createFromURL(URL url, ClassLoader classLoader) {
        return createFromInputStream(url.openStream(), classLoader);
    }

    @SneakyThrows
    public ClassLoader createFromURL(URL url) {
        return createFromURL(url, null);
    }

}
