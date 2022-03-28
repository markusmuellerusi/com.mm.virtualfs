package com.mm.virtualfs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.InvalidParameterException;
import java.util.*;
import java.io.Serializable;

public class Main {

    public static void main(String[] args) {

        IDirectoryObject currentDirectory = new RootDirectory();
        Scanner scanner = new Scanner(System.in);
        IOutput output = new Output();

        do {
            String parameter = Constants.STRING_EMPTY;

            String input = scanner.nextLine();
            String command = input.trim();

            int blankPos = input.indexOf(Constants.BLANK);
            if (blankPos > 0) {
                command = input.substring(0, blankPos).trim();
                parameter = input.substring(blankPos + 1).trim();
            }
            command = command.toLowerCase(Locale.ROOT);

            currentDirectory = Execute(output, currentDirectory, command, parameter);
        } while (currentDirectory != null);
    }

    public static IDirectoryObject Execute(IOutput output, IDirectoryObject currentDirectory, String command, String parameter) {
        switch (command) {
            //Quit
            case Constants.QUIT:
                if (!parameter.equals(Constants.STRING_EMPTY)) {
                    output.writeLine(Constants.INVALID_COMMAND);
                } else {
                    return null;
                }
                break;
            //Current Directory
            case Constants.CURRENT_DIRECTORY:
                if (!parameter.equals(Constants.STRING_EMPTY)) {
                    output.writeLine(Constants.INVALID_COMMAND);
                } else if (currentDirectory != null) {
                    System.out.println(currentDirectory.getFullPath());
                }
                break;
            //List Contents
            case Constants.LIST_CONTENTS:
                if (parameter.equals(Constants.RECURSIVE)) {
                    printObjects(output, currentDirectory, currentDirectory, true, false);
                } else if (parameter.equals(Constants.STRING_EMPTY)) {
                    printObjects(output, currentDirectory, currentDirectory, false, false);
                } else {
                    output.writeLine(Constants.INVALID_COMMAND);
                }
                break;
            //Make Directory
            case Constants.MAKE_DIRECTORY:
                try {
                    currentDirectory.addDirectory(parameter);
                } catch (InvalidParameterException ex) {
                    output.writeLine(ex.getMessage());
                }
                break;
            //Change Directory Up
            case Constants.CHANGE_DIRECTORY_UP:
                if (!parameter.equals(Constants.STRING_EMPTY)) {
                    output.writeLine(Constants.INVALID_COMMAND);
                } else if (currentDirectory.getParent() != null) {
                    currentDirectory = currentDirectory.getParent();
                }
                break;
            //Change Directory
            case Constants.CHANGE_DIRECTORY:
                if (parameter.equals(Constants.STRING_EMPTY)) {
                    output.writeLine(Constants.INVALID_COMMAND);
                } else {
                    boolean dirFound = false;
                    for (IDirectoryObject d : currentDirectory.getDirectories()) {
                        if (!d.getName().equals(parameter)) continue;
                        currentDirectory = d;
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
                    currentDirectory.addFile(parameter);
                } catch (InvalidParameterException ex) {
                    output.writeLine(ex.getMessage());
                }
                break;
            //Unknown Command
            default:
                output.writeLine(Constants.UNRECOGNIZED_COMMAND);
                break;
        }
        return currentDirectory;
    }

    private static void printObjects(IOutput output,
                                     IDirectoryObject currentDirectory, IDirectoryObject directory,
                                     boolean recursive, boolean useRelativePath) {
        for (IDirectoryObject dir: directory.getDirectories()) {
            output.writeLine(dir.getFullPath());
            if (!recursive) continue;
            printObjects(output, currentDirectory, dir, true, useRelativePath);
        }
        for (IFileObject file: directory.getFiles()) {
            output.writeLine(file.getFullPath());
        }
    }
}

class MainTest {

    private IDirectoryObject root;
    private IOutput output;

    @BeforeEach
    void setUp() {
        root = new RootDirectory();
        output = new BufferedOutput();
    }

    @AfterEach
    void tearDown() {
        root = null;
        output.clear();
        output = null;
    }

    @Test
    void runTestLsRoot() {
        Main.Execute(output, root, Constants.LIST_CONTENTS, "");
        var out = output.getLines();
        assertEquals(out.size(), 0);
    }

    @ParameterizedTest
    @CsvSource(value = {"MM,/root/MM", "DM,/root/DM"})
    void runTestCreateDir(String input, String expected) {
        Main.Execute(output, root, Constants.MAKE_DIRECTORY, input);
        Main.Execute(output, root, Constants.LIST_CONTENTS, "");
        var out = output.getLines();
        assertEquals(out.get(0), expected);
    }


    @Test
    void runTestCreateDir() {
        Main.Execute(output, root, Constants.MAKE_DIRECTORY, "Z");
        Main.Execute(output, root, Constants.MAKE_DIRECTORY, "X");
        Main.Execute(output, root, Constants.MAKE_DIRECTORY, "Y");
        Main.Execute(output, root, Constants.LIST_CONTENTS, "");
        var out = output.getLines();
        assertEquals(out.size(), 3);
    }

    @ParameterizedTest
    @CsvSource(value = {"MM,/root/MM", "DM,/root/DM"})
    void runTestCreateSubDir(String input, String expected) {
        Main.Execute(output, root, Constants.MAKE_DIRECTORY, input);
        IDirectoryObject current = Main.Execute(output, root, Constants.CHANGE_DIRECTORY, input);
        assertEquals(current.getName(), input);
        current = Main.Execute(output, current, Constants.LIST_CONTENTS, "");
        current = Main.Execute(output, current, Constants.CHANGE_DIRECTORY_UP, "");
        assertEquals(current.getName(), "root");
        Main.Execute(output, current, Constants.LIST_CONTENTS, "");
        var out = output.getLines();
        assertEquals(out.get(0), expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"MM.txt,/root/sub/MM.txt", "DM.txt,/root/sub/DM.txt"})
    void runTestCreateFile(String input, String expected) {
        IDirectoryObject current = Main.Execute(output, root, Constants.MAKE_DIRECTORY, "sub");
        current = Main.Execute(output, current, Constants.CHANGE_DIRECTORY, "sub");
        Main.Execute(output, current, Constants.CREATE_FILE, input);
        current = Main.Execute(output, current, Constants.CHANGE_DIRECTORY_UP, "");
        Main.Execute(output, current, Constants.LIST_CONTENTS, "-r");
        var out = output.getLines();
        assertEquals(out.get(1), expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"MM.txt,/root/sub", "DM.txt,/root/sub"})
    void runTestCreateFileNotListed(String input, String expected) {
        IDirectoryObject current = Main.Execute(output, root, Constants.MAKE_DIRECTORY, "sub");
        current = Main.Execute(output, current, Constants.CHANGE_DIRECTORY, "sub");
        Main.Execute(output, current, Constants.CREATE_FILE, input);
        current = Main.Execute(output, current, Constants.CHANGE_DIRECTORY_UP, "");
        Main.Execute(output, current, Constants.LIST_CONTENTS, "");
        var out = output.getLines();
        assertEquals(out.size(), 1);
        assertEquals(out.get(0), expected);
    }
}

interface IOutput {
    void writeLine(String text);
    void clear();
    ArrayList<String> getLines();
}

class Output implements IOutput{

    @Override
    public void writeLine(String text) {
        System.out.println(text);
    }

    public void clear(){}

    @Override
    public ArrayList<String> getLines() { return new ArrayList<>(); }
}

class BufferedOutput implements IOutput {

    public static ArrayList<String> buffer;

    public BufferedOutput(){
        buffer = new ArrayList<>();
    }

    @Override
    public void writeLine(String text) {
        System.out.println(text);
        buffer.add(text);
    }

    public void clear(){
        buffer.clear();
    }

    @Override
    public ArrayList<String> getLines() {
        return buffer;
    }
}

class Constants {
    public static final String INVALID_COMMAND = "Invalid command";
    public static final String UNRECOGNIZED_COMMAND = "Unrecognized command";
    public static final String DIRECTORY_NOT_FOUND ="Directory not found";
    public static final String QUIT = "quit";
    public static final String CURRENT_DIRECTORY = "pwd";
    public static final String LIST_CONTENTS = "ls";
    public static final String RECURSIVE = "-r";
    public static final String MAKE_DIRECTORY = "mkdir";
    public static final String CHANGE_DIRECTORY = "cd";
    public static final String CHANGE_DIRECTORY_UP = "cd..";
    public static final String CREATE_FILE = "touch";

    public static final String STRING_EMPTY = "";
    public static final String BLANK = " ";
    public static final String INVALID_CHARACTER = "Invalid characters in name";
    public static final String INVALID_NAME = "Length of name should be greater than 0 and less or equal to 100!";
    public static final String DIRECTORY_ALREADY_EXISTS = "Directory already exists";
    public static final String FILE_ALREADY_EXISTS = "File already exists";
}

interface IFileSystemObject {
    public String getFullPath();
    public String getName();
    public IDirectoryObject getParent();
    public void setParent(IDirectoryObject parent);
    public void rename(String name);
    public void delete();
}

class FileSystemObject implements Serializable, IFileSystemObject {

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
        while (true) {
            if (p == null)
            {
                break;
            }
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

interface IFileObject extends IFileSystemObject {}

class FileObject extends FileSystemObject implements IFileObject {
    public FileObject() {}

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

interface IDirectoryObject extends IFileSystemObject {
    public ArrayList<IDirectoryObject> getDirectories();
    public ArrayList<IFileObject> getFiles();
    public IDirectoryObject addDirectory(IDirectoryObject directory);
    public IDirectoryObject addDirectory(String name);
    public void removeDirectory(IDirectoryObject directory);
    public void removeDirectory(String name);
    public void removeDirectories();
    public IFileObject addFile(IFileObject file);
    public IFileObject addFile(String name);
    public void removeFile(String name);
    public void removeFile(IFileObject file);
    public void removeFiles();
}

class DirectoryObject extends FileSystemObject implements IDirectoryObject {

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
        for (IDirectoryObject dir: directories) {
            if (dir.getName().equals(directory.getName())) {
                throw new InvalidParameterException(Constants.DIRECTORY_ALREADY_EXISTS);
            }
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
        IDirectoryObject dir = null;
        for (IDirectoryObject directory : this.directories) {
            if (directory.getName().equals(name)) {
                removeDirectory(directory);
                break;
            }
        }
    }

    @Override
    public void removeDirectories() {
        directories.clear();
    }

    @Override
    public IFileObject addFile(IFileObject file) {
        for (IFileObject f: files) {
            if (f.getName().equals(file.getName())) {
                throw new InvalidParameterException(Constants.FILE_ALREADY_EXISTS);
            }
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
        for (IFileObject file : this.files) {
            if (file.getName().equals(name)) {
                removeFile(file);
                break;
            }
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
        ArrayList<IDirectoryObject> list = new ArrayList(this.directories);
        list.sort(Comparator.comparing(IFileSystemObject::getName));
        return list;
    }

    @Override
    public ArrayList<IFileObject> getFiles() {
        ArrayList<IFileObject> list = new ArrayList(this.files);
        list.sort(Comparator.comparing(IFileSystemObject::getName));
        return list;
    }
}

class RootDirectory extends DirectoryObject {
    public RootDirectory() {
        super.setName("root");
        initHierarchy(null);
    }
}
