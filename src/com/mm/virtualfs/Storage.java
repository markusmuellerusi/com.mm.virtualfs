package com.mm.virtualfs;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class Storage {

    private static final String FILE_PATH = "fso.txt";
    private static final String XML_FILE_PATH = "fso.xml";

    public static void save(IDirectoryObject dir) throws java.io.IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(FILE_PATH);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(dir);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    public static IDirectoryObject load()
            throws java.io.IOException,
            java.lang.ClassNotFoundException {

        if (new File(FILE_PATH).isFile()) {
            FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            RootDirectory dir = (RootDirectory) objectInputStream.readObject();
            objectInputStream.close();
            return dir;
        }

        return new RootDirectory();
    }

    public static void saveXml(IDirectoryObject dir) throws FileNotFoundException {
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(XML_FILE_PATH)));
        encoder.writeObject(dir);
        encoder.close();
    }

    public static IDirectoryObject loadXml() throws FileNotFoundException {
        XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(XML_FILE_PATH)));
        return (RootDirectory)decoder.readObject();
    }}
