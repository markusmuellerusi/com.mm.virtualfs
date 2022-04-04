package com.mm.virtualfs;

import java.security.InvalidParameterException;
import java.util.*;

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
        IDirectoryObject d = tryFindDirectory(directory.getName());
        if (d != null) {
            throw new InvalidParameterException(Constants.FILE_ALREADY_EXISTS);
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
        IDirectoryObject directory = tryFindDirectory(name);
        if (directory == null)
            return;

        removeDirectory(directory);
    }

    @Override
    public void removeDirectories() {
        directories.clear();
    }

    @Override
    public IFileSystemObject tryFind(String path) {
        if (path == null || path.length() == 0)
            return null;

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        if (!(path + "/").startsWith(getName() + "/")) {
            return null;
        }

        String[] s = path.split("/");
        if (s.length == 1) {
            return null;
        }
        IDirectoryObject dir = tryFindDirectory(s[1]);
        if (s.length == 2) {
            if (dir != null) return dir;
            return tryFindFile(s[0]);
        } else {
            if (dir == null) return null;
            List<String> list = Arrays.asList(s);
            list.remove(0);
            return dir.tryFind(String.join("/", list));
        }
    }

    @Override
    public IFileObject addFile(IFileObject file) {
        IFileObject f = tryFindFile(file.getName());
        if (f != null) {
            throw new InvalidParameterException(Constants.FILE_ALREADY_EXISTS);
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
        IFileObject file = tryFindFile(name);
        if (file != null) {
            removeFile(file);
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

    private IFileObject tryFindFile(String name) {
        if (name == null || name.length() == 0)
            return null;
        for (IFileObject f: files) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    private IDirectoryObject tryFindDirectory(String name) {
        for (IDirectoryObject dir: directories) {
            if (dir.getName().equals(name)) {
                return dir;
            }
        }
        return null;
    }
}
