package com.mm.virtualfs;

public class Main {
    public static void main(String[] args) {
        IDirectoryObject currentDirectory = FileSystemProvider.GetDirectory(FileSystemProvider.ProviderType.VIRTUAL);
        IOutput output = new Output();
        CommandProcessor commandProcessor = new CommandProcessor(output, currentDirectory);
        commandProcessor.run();
    }
}
