package net.anumbrella.seaweedfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tech.oxymen.seaweedfs.core.ConnectionProperties;
import tech.oxymen.seaweedfs.core.FileSource;
import tech.oxymen.seaweedfs.core.FilerWrapper;
import tech.oxymen.seaweedfs.core.http.StreamResponse;

public class FilerWrapperTest {
    private static FileSource fileSource = new FileSource();

    @Before
    public void setUp() {
        ConnectionProperties connectionProperties
                = new ConnectionProperties.Builder().host("localhost").port(8888).maxConnection(100).build();
        fileSource.setProperties(connectionProperties);
        fileSource.startup();
    }

    @Test
    public void testUploadFile() throws IOException {
        FilerWrapper filerWrapper = new FilerWrapper(fileSource.getConnection());
        File file = new File("/Users/neeson/Downloads/IMG_3475.jpg");

        InputStream inputStream = new FileInputStream(file);
        long size = filerWrapper.uploadFile("http://192.168.0.111:8888/test/", "reba.jpg", inputStream, ContentType.DEFAULT_BINARY);
        Assert.assertNotEquals(0, size);
    }

    @Test
    public void testGetFileStream() throws IOException {
        FilerWrapper filerWrapper = new FilerWrapper(fileSource.getConnection());
        StreamResponse streamResponse = filerWrapper.getFileStream("http://localhost:8888/test/reba.jpg");
        InputStream inputStream = streamResponse.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\reba.jpg");
        int index;
        byte[] bytes = new byte[1024];
        while ((index = inputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, index);
            fileOutputStream.flush();
        }
        inputStream.close();
        fileOutputStream.close();
        Assert.assertNotNull(streamResponse);
    }

    @Test
    public void testDeleteFile() throws IOException {
        FilerWrapper filerWrapper = new FilerWrapper(fileSource.getConnection());
        filerWrapper.deleteFile("http://localhost:8888/picture/reba.jpg");
    }
}
