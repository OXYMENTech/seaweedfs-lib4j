package net.anumbrella.seaweedfs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tech.oxymen.seaweedfs.core.Connection;
import tech.oxymen.seaweedfs.core.ConnectionProperties;
import tech.oxymen.seaweedfs.core.FileSource;
import tech.oxymen.seaweedfs.core.MasterWrapper;
import tech.oxymen.seaweedfs.core.content.AssignFileKeyParams;
import tech.oxymen.seaweedfs.core.content.AssignFileKeyResult;
import tech.oxymen.seaweedfs.core.content.ForceGarbageCollectionParams;
import tech.oxymen.seaweedfs.core.content.LookupVolumeParams;
import tech.oxymen.seaweedfs.core.content.LookupVolumeResult;
import tech.oxymen.seaweedfs.core.content.PreAllocateVolumesParams;
import tech.oxymen.seaweedfs.core.content.PreAllocateVolumesResult;
import tech.oxymen.seaweedfs.core.content.SubmitFileResult;
import tech.oxymen.seaweedfs.core.topology.GarbageResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MasterWrapperTest {
    public static FileSource fileSource = new FileSource();
    public static MasterWrapper masterWrapper;
    public static Connection connection;

    @Before
    public void setUp() {
        ConnectionProperties connectionProperties
                = new ConnectionProperties.Builder()
                .host("192.168.0.131")
                .port(9333)
                .maxConnection(100)
                .connectionTimeout(600)
                .build();
        fileSource.setProperties(connectionProperties);
        fileSource.startup();
        connection = fileSource.getConnection();
        masterWrapper = new MasterWrapper(connection);
    }

    @Test
    public void testAssignFileKey() throws IOException {
        AssignFileKeyParams assignFileKeyParams = new AssignFileKeyParams();
        assignFileKeyParams.setDataCenter("dc1");
        assignFileKeyParams.setCollection("test");
        AssignFileKeyResult assignFileKeyResult = masterWrapper.assignFileKey(assignFileKeyParams);
        Assert.assertNotNull(assignFileKeyResult.getFid());
    }

    @Test
    public void testForceGarbageCollection() throws IOException {
        ForceGarbageCollectionParams forceGarbageCollectionParams = new ForceGarbageCollectionParams();
        forceGarbageCollectionParams.setGarbageThreshold(0.4f);
        GarbageResult garbageResult = masterWrapper.forceGarbageCollection(forceGarbageCollectionParams);
        Assert.assertNotNull(garbageResult);
    }

    @Test
    public void testPreAllocateVolumes() throws IOException {
        PreAllocateVolumesParams preAllocateVolumesParams = new PreAllocateVolumesParams();
        preAllocateVolumesParams.setDataCenter("dc1");
        preAllocateVolumesParams.setCollection("test");
        preAllocateVolumesParams.setCount(3);
        PreAllocateVolumesResult preAllocateVolumesResult = masterWrapper.preAllocateVolumes(preAllocateVolumesParams);
        Assert.assertNotNull(preAllocateVolumesResult);
    }

    @Test
    public void testLookupVolume() throws IOException {
        LookupVolumeParams lookupVolumeParams = new LookupVolumeParams();
        lookupVolumeParams.setCollection("test");
        lookupVolumeParams.setVolumeId(10);
        LookupVolumeResult lookupVolumeResult = masterWrapper.lookupVolume(lookupVolumeParams);
        Assert.assertNotNull(lookupVolumeResult);
    }

    @Test
    public void testUploadFileDirectly() throws IOException {
        InputStream inputStream = new FileInputStream(new File("D:\\reba.jpg"));
        SubmitFileResult submitFileResult = masterWrapper.uploadFileDirectly("http://localhost:9333", "reba.jpg", inputStream);
        Assert.assertNotNull(submitFileResult.getFid());
    }

    @Test
    public void testDeleteCollection() throws IOException {
        int responseCode = masterWrapper.deleteCollection("http://localhost:9333", "test");
        Assert.assertEquals(204, responseCode);
    }
}
