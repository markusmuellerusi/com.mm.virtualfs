package com.mm.virtualfs;

import java.nio.file.ProviderNotFoundException;

public class FileSystemProvider {
    public enum ProviderType {
        VIRTUAL,
        PHYSICAL
    }

    public static IDirectoryObject GetDirectory(ProviderType type){
        switch (type) {
            case VIRTUAL:
                return new RootDirectory();
            default:
                throw new ProviderNotFoundException();
        }
    }
}