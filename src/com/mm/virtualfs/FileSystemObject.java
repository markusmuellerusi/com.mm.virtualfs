
package com.mm.virtualfs;

import java.io.Serializable;
import java.security.InvalidParameterException;
/*
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
        var path = name;
        var p = this.parent;
        while (true) {
            if (p == null)
            {
                break;
            }
            path = p.getName() + "/" + path;
            p = p.getParent();
        }

        return path;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name == null || name.length() == 0 || name.length() > 100) {
            throw new InvalidParameterException("Length of name should be greater than 0 and less or equal to 100!");
        }
        if (name.contains("/") || name.contains("\\")) {
            throw new InvalidParameterException("Invalid characters in name!");
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
*/