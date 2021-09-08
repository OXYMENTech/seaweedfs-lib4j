package net.anumbrella.seaweedfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tech.oxymen.seaweedfs.core.Connection;
import tech.oxymen.seaweedfs.core.ConnectionProperties;
import tech.oxymen.seaweedfs.core.FileSource;
import tech.oxymen.seaweedfs.core.FileTemplate;
import tech.oxymen.seaweedfs.core.file.FileHandleStatus;

public class FileSourceTest {
    private static FileSource fileSource = new FileSource();
    @Before
    public void setUp() {
        ConnectionProperties connectionProperties
                = new ConnectionProperties.Builder().host("192.168.0.111").port(9333).maxConnection(100).build();
        fileSource.setProperties(connectionProperties);
        fileSource.startup();
    }

    @Test
    public void testStartup() {
        Connection connection = fileSource.getConnection();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testUpload() throws IOException {
        Connection connection = fileSource.getConnection();
        FileTemplate fileTemplate = new FileTemplate(connection);
        File file = new File("/Users/neeson/Downloads/IMG_3475.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileHandleStatus fileHandleStatus = fileTemplate.saveFileByStream("reba.jpg", fileInputStream);
        System.out.println(fileHandleStatus.getFileId());
        Assert.assertNotNull(fileHandleStatus.getFileId());
    }

    @Test
    public void testDeleteFile() throws IOException {
        Connection connection = fileSource.getConnection();
        FileTemplate fileTemplate = new FileTemplate(connection);
        File file = new File("/Users/neeson/Downloads/IMG_3475.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileHandleStatus fileHandleStatus = fileTemplate.saveFileByStream("reba.jpg", fileInputStream);
        fileTemplate.deleteFile(fileHandleStatus.getFileId());
    }

    @Test
    public void testUploadByFiler() throws IOException {
        Connection connection = fileSource.getConnection();
        FileTemplate fileTemplate = new FileTemplate(connection);
        File file = new File("C:\\Users\\zhiqu\\Downloads\\reba.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileHandleStatus fileHandleStatus = fileTemplate.saveFileByFiler("reba.jpg", fileInputStream, "http://localhost:8888/picture/");
        Assert.assertNotEquals(0, fileHandleStatus.getSize());
    }

    @Test
    public void testDeleteFileByFiler() throws IOException {
        Connection connection = fileSource.getConnection();
        FileTemplate fileTemplate = new FileTemplate(connection);
        File file = new File("C:\\Users\\zhiqu\\Downloads\\reba.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        fileTemplate.saveFileByFiler("reba.jpg", fileInputStream, "http://localhost:8888/picture/");
        fileTemplate.deleteFileByFiler("http://localhost:8888/picture/reba.jpg");
    }
}