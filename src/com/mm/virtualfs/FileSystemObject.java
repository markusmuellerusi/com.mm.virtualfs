package com.mm.virtualfs;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

public class FileSystemObject implements Serializable, IFileSystemObject {

    private String name;
    private IDirectoryObject parent;

    public FileSystemObject() {
    }

    public FileSystemObject(String name, IDirectoryObject parent) {
        this.setName(name);
        this.parent = parent;
    }

    @Override
    public String getFullPath() {
        if (parent == null){
            return name;
        }
        String path = name;
        IDirectoryObject p = this.parent;
        while (p != null) {
            path = String.format("%s/%s", p.getName(), path);
            p = p.getParent();
        }

        return String.format("/%s", path);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name == null || name.length() == 0 || name.length() > 100) {
            throw new InvalidParameterException(Constants.INVALID_NAME);
        }
        if (name.contains("/") || name.contains("\\")) {
            throw new InvalidParameterException(Constants.INVALID_CHARACTER);
        }
        this.name = name;
    }

    public IDirectoryObject getParent() {
        return parent;
    }

    public void setParent(IDirectoryObject parent) {
        this.parent = parent;
    }

    @Override
    public void delete(){}

    @Override
    public void rename(String name){
        this.name = name;
    }
}
