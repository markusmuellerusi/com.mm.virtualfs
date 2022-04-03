package com.mm.virtualfs;

public  class RootDirectory extends DirectoryObject {
    public RootDirectory() {
        super.setName("root");
        initHierarchy(null);
    }
}
