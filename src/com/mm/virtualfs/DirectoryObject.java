package com.mm.virtualfs;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class DirectoryObject extends FileSystemObject implements IDirectoryObject {

    private HashSet<IDirectoryObject> directories;
    private HashSet<IFileObject> files;

    public DirectoryObject() {
        init();
    }

    public DirectoryObject(String name, IDirectoryObject parent) {
        super(name, parent);
        init();
    }

    private void init() {
        this.directories = new HashSet<>();
        this.files = new HashSet<>();
    }

    protected void initHierarchy(IDirectoryObject parent) {
        super.setParent(parent);
        for (IDirectoryObject dir: directories) {
            ((DirectoryObject)dir).initHierarchy(this);
        }
        for (IFileObject file: files) {
            file.setParent(this);
        }
    }

    @Override
    public IDirectoryObject addDirectory(IDirectoryObject directory) {
        for (IDirectoryObject dir: directories) {
            if (dir.getName().equals(directory.getName())) {
                throw new InvalidParameterException(Constants.DIRECTORY_ALREADY_EXISTS);
            }
        }
        directories.add(directory);
        directory.setParent(this);
        return directory;
    }

    @Override
    public IDirectoryObject addDirectory(String name) {
        IDirectoryObject directory = new DirectoryObject(name, this);
        return addDirectory(directory);
    }

    @Override
    public void removeDirectory(IDirectoryObject directory) {
        if (directory == null) return;
        directories.remove(directory);
    }

    @Override
    public void removeDirectory(String name) {
        for (IDirectoryObject directory : this.directories) {
            if (directory.getName().equals(name)) {
                removeDirectory(directory);
                break;
            }
        }
    }

    @Override
    public void removeDirectories() {
        directories.clear();
    }

    @Override
    public IFileObject addFile(IFileObject file) {
        for (IFileObject f: files) {
            if (f.getName().equals(file.getName())) {
                throw new InvalidParameterException(Constants.FILE_ALREADY_EXISTS);
            }
        }
        files.add(file);
        file.setParent(this);
        return file;
    }

    @Override
    public IFileObject addFile(String name) {
        IFileObject file = new FileObject(name, this);
        return addFile(file);
    }

    @Override
    public void removeFile(String name) {
        for (IFileObject file : this.files) {
            if (file.getName().equals(name)) {
                removeFile(file);
                break;
            }
        }
    }

    @Override
    public void removeFile(IFileObject file) {
        if (file == null) return;
        files.remove(file);
    }

    @Override
    public void removeFiles() {
        this.files.clear();
    }

    @Override
    public void delete(){
        IDirectoryObject parent = super.getParent();
        if (parent == null) return;
        parent.removeDirectory(this);
    }

    @Override
    public ArrayList<IDirectoryObject> getDirectories() {
        ArrayList list = new ArrayList(this.directories);
        list.sort(Comparator.comparing(IFileSystemObject::getName));
        return list;
    }

    @Override
    public ArrayList<IFileObject> getFiles() {
        ArrayList list = new ArrayList(this.files);
        list.sort(Comparator.comparing(IFileSystemObject::getName));
        return list;
    }
}
