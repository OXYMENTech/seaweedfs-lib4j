package net.anumbrella.seaweedfs;

import java.io.File;

import org.junit.Test;

import net.anumbrella.seaweedfs.core.FileSystem;
import net.anumbrella.seaweedfs.core.file.FileHandleStatus;

public class FileSystemTest {
    

    @Test
    public void uploadFile() {

        FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);


        File file = new File("/Users/neeson/Downloads/IMG_3475.jpg");
        FileHandleStatus fhs = ofs.saveFile(file);

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

        File file = ofs.getFile("7,0641b6d06e");

        System.out.println(file.getAbsolutePath());

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

        File file = ofs.getFileWithOriginalName("7,0641b6d06e");

        System.out.println(file.getAbsolutePath());

        ofs.stop();
    }
}