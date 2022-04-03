package com.mm.virtualfs;

public class FileObject extends FileSystemObject implements IFileObject {

    public FileObject(String name, IDirectoryObject parent) {
        super(name, parent);
    }

    @Override
    public void delete(){
        IDirectoryObject parent = super.getParent();
        if (parent == null) return;
        parent.removeFile(this);
    }
}
