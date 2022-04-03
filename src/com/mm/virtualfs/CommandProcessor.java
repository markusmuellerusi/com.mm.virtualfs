package com.mm.virtualfs;

import java.security.InvalidParameterException;
import java.util.Locale;
import java.util.Scanner;

public class CommandProcessor {
    private final IOutput output;
    private IDirectoryObject currentDirectory;
    private final IDirectoryObject rootDirectory;

    public CommandProcessor(IOutput output, IDirectoryObject currentDirectory) {
        this.output = output;
        this.rootDirectory = currentDirectory;
        this.currentDirectory = currentDirectory;
    }

    public void reset() {
        this.currentDirectory = this.rootDirectory;
        this.currentDirectory.removeDirectories();
        this.currentDirectory.removeFiles();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        do {
            String input = scanner.nextLine();
            String command = input.trim();
            String parameter = Constants.STRING_EMPTY;
            int blankPos = input.indexOf(Constants.BLANK);
            if (blankPos > 0) {
                command = input.substring(0, blankPos).trim();
                parameter = input.substring(blankPos + 1).trim();
            }
            command = command.toLowerCase(Locale.ROOT);

            this.currentDirectory = execute(command, parameter);
        } while (this.currentDirectory != null);
    }

    public IDirectoryObject execute(String command, String parameter) {
        switch (command) {
            //Quit
            case Constants.QUIT:
                if (!parameter.equals(Constants.STRING_EMPTY)) {
                    this.output.writeLine(Constants.INVALID_COMMAND);
                } else {
                    return null;
                }
                break;
            //Current Directory
            case Constants.CURRENT_DIRECTORY:
                if (!parameter.equals(Constants.STRING_EMPTY)) {
                    output.writeLine(Constants.INVALID_COMMAND);
                } else if (this.currentDirectory != null) {
                    System.out.println(this.currentDirectory.getFullPath());
                }
                break;
            //List Contents
            case Constants.LIST_CONTENTS:
                if (parameter.equals(Constants.RECURSIVE)) {
                    printObjects(this.currentDirectory, this.currentDirectory, true, false);
                } else if (parameter.equals(Constants.STRING_EMPTY)) {
                    printObjects(this.currentDirectory, this.currentDirectory, false, false);
                } else {
                    output.writeLine(Constants.INVALID_COMMAND);
                }
                break;
            //Make Directory
            case Constants.MAKE_DIRECTORY:
                try {
                    this.currentDirectory.addDirectory(parameter);
                } catch (InvalidParameterException ex) {
                    output.writeLine(ex.getMessage());
                }
                break;
            //Change Directory Up
            case Constants.CHANGE_DIRECTORY_UP:
                if (!parameter.equals(Constants.STRING_EMPTY)) {
                    output.writeLine(Constants.INVALID_COMMAND);
                } else if (this.currentDirectory.getParent() != null) {
                    this.currentDirectory = this.currentDirectory.getParent();
                }
                break;
            //Change Directory
            case Constants.CHANGE_DIRECTORY:
                if (parameter.equals(Constants.STRING_EMPTY)) {
                    output.writeLine(Constants.INVALID_COMMAND);
                } else {
                    boolean dirFound = false;
                    for (IDirectoryObject d : this.currentDirectory.getDirectories()) {
                        if (!d.getName().equals(parameter)) continue;
                        this.currentDirectory = d;
                        dirFound = true;
                    }
                    if (!dirFound) {
                        output.writeLine(Constants.DIRECTORY_NOT_FOUND);
                    }
                }
                break;
            //Create File
            case Constants.CREATE_FILE:
                try {
                    this.currentDirectory.addFile(parameter);
                } catch (InvalidParameterException ex) {
                    output.writeLine(ex.getMessage());
                }
                break;
            //Unknown Command
            default:
                output.writeLine(Constants.UNRECOGNIZED_COMMAND);
                break;
        }
        return this.currentDirectory;
    }

    private void printObjects(IDirectoryObject currentDir,
                              IDirectoryObject directory,
                              boolean recursive, boolean useRelativePath) {
        for (IDirectoryObject dir: directory.getDirectories()) {
            String path = getPath(currentDir, dir, useRelativePath);
            this.output.writeLine(path);
            if (!recursive) continue;
            printObjects(currentDir, dir, true, useRelativePath);
        }
        for (IFileObject file: directory.getFiles()) {
            String path = getPath(currentDir, file, useRelativePath);
            this.output.writeLine(path);
        }
    }

    private static String getPath(IDirectoryObject currentDir, IFileSystemObject fso, boolean useRelativePath) {
        String fullPath = fso.getFullPath();
        if (!useRelativePath) return fullPath;
        return fullPath.substring(currentDir.getFullPath().length());
    }
}
