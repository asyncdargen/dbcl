package ru.dargen.dbcl.url;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Map;

@RequiredArgsConstructor
public class JarURLStreamHandler extends URLStreamHandler {

    private final Map<String, byte[]> entries;

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        val entryBytes = entries.get(resolveURLAsEntryName(url));

        if (entryBytes == null)
            throw new IOException("entry with name (" + url.getFile() + " not exists");

        return new JarEntryURLConnection(url, entryBytes);
    }

    public void close() {
        entries.clear();
    }

    private static String resolveURLAsEntryName(URL url) {
        val path = url.getFile();
        return path.isEmpty() || path.charAt(0) != '/' ? path : path.substring(1);
    }

}
