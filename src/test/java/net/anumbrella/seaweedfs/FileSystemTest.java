package net.anumbrella.seaweedfs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import tech.oxymen.seaweedfs.core.FileSystem;
import tech.oxymen.seaweedfs.core.TTL;
import tech.oxymen.seaweedfs.core.file.FileHandleStatus;

public class FileSystemTest {
    

    @Test
    public void uploadFile() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);


        File file = new File("/Users/neeson/Downloads/Material.Templates.xlsx");
        FileHandleStatus fhs = ofs.saveFile(file);

        File f2 = ofs.getFile(fhs.getFileId());
        
        FileHandleStatus fStatus = ofs.getFileStatus(fhs.getFileId());

        assertNotNull(f2);
        assertNotNull(fhs);
        assertEquals(fhs.getFileId(), fStatus.getFileId());
        
        ofs.stop();
    }

    @Test
    public void uploadFileWithTTL() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);


        File file = new File("/Users/neeson/Downloads/IMG_3475.jpg");
        FileHandleStatus fhs = ofs.saveFile(file, TTL.minutes(1));

        System.out.println(fhs.toString());

        ofs.stop();
    }


    @Test
    public void uploadFileWithRandomName() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);


        File file = new File("/Users/neeson/Downloads/IMG_3475.jpg");
        FileHandleStatus fhs = ofs.saveFileWithRandomName(file);

        System.out.println(fhs.toString());

        ofs.stop();
    }

    @Test
    public void getFileHandlerStatus() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);

        FileHandleStatus fhs = ofs.getFileStatus("7,0641b6d06e");

        System.out.println(fhs.toString());

        ofs.stop();
    }

    @Test
    public void getFile() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);

        File file = ofs.getFile("11,1ab89c58aa");

        if (file != null) {
            System.out.println(file.getAbsolutePath());
        } else {
            System.out.println("File does not exist!");
        }
        
        ofs.stop();
    }

    @Test
    public void getFileWithSpecificName() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);

        File file = ofs.getFile("7,0641b6d06e", "mos");

        System.out.println(file.getAbsolutePath());

        ofs.stop();
    }

    @Test
    public void getFileWithSpecificNameAndDateInfo() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);

        File file = ofs.getFile("7,0641b6d06e", "mos", true);

        System.out.println(file.getAbsolutePath());

        ofs.stop();
    }

    @Test
    public void getFileWithDateInfo() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);

        File file = ofs.getFile("7,0641b6d06e", true);

        System.out.println(file.getAbsolutePath());

        ofs.stop();
    }

    @Test
    public void getFileWithOriginalName() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);

        File file = ofs.getFileWithOriginalName("5,1749ad914a");

        System.out.println(file.getAbsolutePath());

        ofs.stop();
    }


    @Test
    public void testDeleteCollection() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);

        ofs.deleteCollection("ABC");

        ofs.stop();
        
    }
}
