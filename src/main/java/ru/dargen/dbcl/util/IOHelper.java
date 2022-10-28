package ru.dargen.dbcl.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

@UtilityClass
public class IOHelper {

    @SneakyThrows
    public Map<String, byte[]> readJarEntry(byte[] jarBytes) {
        return readJarEntries(new ByteArrayInputStream(jarBytes));
    }

    @SneakyThrows
    public Map<String, byte[]> readJarEntries(InputStream inputStream) {
        val entries = new HashMap<String, byte[]>();
        try (val jarInputStream = new JarInputStream(inputStream)) {
            JarEntry entry;
            while ((entry = jarInputStream.getNextJarEntry()) != null)
                if (!entry.isDirectory()) entries.put(entry.getName(), readAllBytes(jarInputStream, false));
        } finally {
            inputStream.close();
        }
        return entries;
    }

    public byte[] readAllBytes(InputStream inputStream) {
        return readAllBytes(inputStream, true);
    }

    @SneakyThrows
    public byte[] readAllBytes(InputStream inputStream, boolean closeIn) {
        var buffer = new byte[1024 * 4];
        var length = 0;
        try (val outputStream = new ByteArrayOutputStream()) {
            while ((length = inputStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, length);
            buffer = outputStream.toByteArray();
        } finally {
            if (closeIn) inputStream.close();
        }

        return buffer;
    }

}
