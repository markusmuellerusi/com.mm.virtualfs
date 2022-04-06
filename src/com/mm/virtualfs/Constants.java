package com.mm.virtualfs;

class Constants {

    // Commands
    public static final String QUIT = "quit";
    public static final String CURRENT_DIRECTORY = "pwd";
    public static final String LIST_CONTENTS = "ls";
    public static final String MAKE_DIRECTORY = "mkdir";
    public static final String CHANGE_DIRECTORY = "cd";
    public static final String CHANGE_DIRECTORY_UP = "cd..";
    public static final String CHANGE_DIRECTORY_TOP = "cd\\";
    public static final String CREATE_FILE = "touch";
    public static final String DELETE = "del";

    //Parameters
    public static final String RECURSIVE = "-r";

    // Simple constants
    public static final String STRING_EMPTY = "";
    public static final String BLANK = " ";

    // Error messages
    public static final String INVALID_COMMAND = "Invalid command";
    public static final String UNRECOGNIZED_COMMAND = "Unrecognized command";
    public static final String NOT_FOUND ="File or directory not found";
    public static final String DIRECTORY_NOT_FOUND ="Directory not found";
    public static final String INVALID_CHARACTER = "Invalid characters in name";
    public static final String INVALID_NAME = "Length of name should be greater than 0 and less or equal to 100!";
    public static final String DIRECTORY_ALREADY_EXISTS = "Directory already exists";
    public static final String FILE_ALREADY_EXISTS = "File already exists";
}

