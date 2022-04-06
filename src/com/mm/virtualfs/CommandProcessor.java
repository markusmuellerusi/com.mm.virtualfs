package com.mm.virtualfs;

import java.security.InvalidParameterException;
import java.util.Locale;
import java.util.Scanner;

public class CommandProcessor {
    private final IOutput output;
    private IDirectoryObject currentDirectory;

    public CommandProcessor(IOutput output, IDirectoryObject currentDirectory) {
        this.output = output;
        this.currentDirectory = currentDirectory;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        do {
            String input = scanner.nextLine();
            String command = input.trim();
            String[] parameters = new String[0];
            int blankPos = command.indexOf(Constants.BLANK);
            if (blankPos > 0) {
                command = input.substring(0, blankPos).trim();
                parameters = input.substring(blankPos + 1).trim().split(" ");
            }
            command = command.toLowerCase(Locale.ROOT);

            this.currentDirectory = execute(command, parameters);
        } while (this.currentDirectory != null);
    }

    public IDirectoryObject execute(String command, String parameters) {
        String[] parameterArray = new String[0];
        if (parameters != null && parameters.length() > 0){
            parameterArray = parameters.trim().split(" ");
        }
        return execute(command, parameterArray);
    }

    public IDirectoryObject execute(String command, String[] parameters) {
        switch (command) {
            //Quit
            case Constants.QUIT:
                if (hasParameter(parameters)) {
                    this.output.writeLine(Constants.INVALID_COMMAND);
                } else {
                    return null;
                }
                break;
            //Current Directory
            case Constants.CURRENT_DIRECTORY:
                if (hasParameter(parameters)) {
                    output.writeLine(Constants.INVALID_COMMAND);
                } else if (this.currentDirectory != null) {
                    System.out.println(this.currentDirectory.getFullPath());
                }
                break;
            //List Contents
            case Constants.LIST_CONTENTS:
                if (!hasParameter(parameters)) {
                    printObjects(this.currentDirectory, this.currentDirectory, false, false);
                }
                else if (isSingleParameter(parameters) && parameters[0].equals(Constants.RECURSIVE)) {
                    printObjects(this.currentDirectory, this.currentDirectory, true, false);
                } else {
                    output.writeLine(Constants.INVALID_COMMAND);
                }
                break;
            //Make Directory
            case Constants.MAKE_DIRECTORY:
                if (isSingleParameter(parameters)) {
                    try {
                        this.currentDirectory.addDirectory(parameters[0]);
                    } catch (InvalidParameterException ex) {
                        output.writeLine(ex.getMessage());
                    }
                } else {
                    output.writeLine(Constants.INVALID_COMMAND);
                }
                break;
            //Change Directory Up
            case Constants.CHANGE_DIRECTORY_UP:
                if (!hasParameter(parameters)) {
                    if (this.currentDirectory.getParent() != null) {
                        this.currentDirectory = this.currentDirectory.getParent();
                    }
                } else {
                    output.writeLine(Constants.INVALID_COMMAND);
                }
                break;
            case Constants.CHANGE_DIRECTORY_TOP:
                if (!hasParameter(parameters)) {
                    while (this.currentDirectory.getParent() != null)
                    {
                        this.currentDirectory = this.currentDirectory.getParent();
                    }
                } else {
                    output.writeLine(Constants.INVALID_COMMAND);
                }
                break;
            //Change Directory
            case Constants.CHANGE_DIRECTORY:
                if (isSingleParameter(parameters)) {
                    boolean dirFound = false;
                    for (IDirectoryObject d : this.currentDirectory.getDirectories()) {
                        if (!d.getName().equals(parameters[0])) continue;
                        this.currentDirectory = d;
                        dirFound = true;
                    }
                    if (!dirFound) {
                        output.writeLine(Constants.DIRECTORY_NOT_FOUND);
                    }
                } else {
                    output.writeLine(Constants.INVALID_COMMAND);
                }
                break;
            //Create File
            case Constants.CREATE_FILE:
                if (isSingleParameter(parameters)) {
                    try {
                        this.currentDirectory.addFile(parameters[0]);
                    } catch (InvalidParameterException ex) {
                        output.writeLine(ex.getMessage());
                    }
                } else {
                    output.writeLine(Constants.INVALID_COMMAND);
                }
                break;
            //Delete File or Directory
            case Constants.DELETE:
                if (isSingleParameter(parameters)) {
                    IFileSystemObject fso = currentDirectory.tryFindByName(parameters[0]);
                    if (fso == null) {
                        output.writeLine(Constants.NOT_FOUND);
                    } else {
                        fso.delete();
                    }
                } else {
                    output.writeLine(Constants.INVALID_COMMAND);
                }
                break;
            //Unknown Command
            default:
                output.writeLine(Constants.UNRECOGNIZED_COMMAND);
                break;
        }
        return this.currentDirectory;
    }

    private boolean isSingleParameter(String[] parameters) {
        return parameters.length == 1 && parameters[0] != null &&
                !parameters[0].equals(Constants.STRING_EMPTY);
    }

    private boolean hasParameter(String[] parameters) {
        return parameters.length > 0;
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
