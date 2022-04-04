package com.mm.virtualfs;

import java.util.ArrayList;

public interface IDirectoryObject extends IFileSystemObject {
    ArrayList<IDirectoryObject> getDirectories();
    ArrayList<IFileObject> getFiles();
    IDirectoryObject addDirectory(IDirectoryObject directory);
    IDirectoryObject addDirectory(String name);
    void removeDirectory(IDirectoryObject directory);
    void removeDirectory(String name);
    void removeDirectories();
    IFileObject addFile(IFileObject file);
    IFileObject addFile(String name);
    void removeFile(String name);
    void removeFile(IFileObject file);
    void removeFiles();
    IFileSystemObject tryFind(String path);
}
