
package com.mm.virtualfs;

public interface IFileSystemObject {
    String getFullPath();
    String getName();
    IDirectoryObject getParent();
    void setParent(IDirectoryObject parent);
    void rename(String name);
    void delete();
}
