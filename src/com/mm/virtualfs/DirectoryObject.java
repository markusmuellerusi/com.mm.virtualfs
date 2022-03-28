package com.mm.virtualfs;

import java.security.InvalidParameterException;
import java.util.HashSet;
/*
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
        for (var dir: directories) {
            ((DirectoryObject)dir).initHierarchy(this);
        }
        for (var file: files) {
            file.setParent(this);
        }
    }

    @Override
    public IDirectoryObject addDirectory(IDirectoryObject directory) {
        for (var dir: directories) {
            if (directory.getName() == dir.getName()) {
                throw new InvalidParameterException("Directory already exists");
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
        IDirectoryObject dir = null;
        for (var directory : this.directories) {
            if (name == directory.getName()) {
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
        for (var f: files) {
            if (file.getName() == f.getName()) {
                throw new InvalidParameterException("File already exists");
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
        for (var file : this.files) {
            if (name == file.getName()) {
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
        var parent = super.getParent();
        if (parent == null) return;
        parent.removeDirectory(this);
    }

    @Override
    public HashSet<IDirectoryObject> getDirectories() {
        return this.directories;
    }

    @Override
    public HashSet<IFileObject> getFiles() {
        return this.files;
    }
}
*/