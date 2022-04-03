package com.mm.virtualfs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    private IOutput output;
    private CommandProcessor commandProcessor;

    @BeforeEach
    void setUp() {
        IDirectoryObject root = FileSystemProvider.GetDirectory(FileSystemProvider.ProviderType.VIRTUAL);
        output = new BufferedOutput();
        commandProcessor = new CommandProcessor(output, root);
    }

    @AfterEach
    void tearDown() {
        output.clear();
        output = null;
    }

    @Test
    public void runTestLsRoot() {
        commandProcessor.execute(Constants.LIST_CONTENTS, "");
        var out = output.getLines();
        assertEquals(out.size(), 0);
    }

    @ParameterizedTest
    @CsvSource(value = {"MM,/root/MM", "DM,/root/DM"})
    public void runTestCreateDir(String input, String expected) {
        commandProcessor.execute(Constants.MAKE_DIRECTORY, input);
        commandProcessor.execute(Constants.LIST_CONTENTS, "");
        var out = output.getLines();
        assertEquals(out.get(0), expected);
    }

    @Test
    public void runTestCreateDir() {
        commandProcessor.execute(Constants.MAKE_DIRECTORY, "Z");
        commandProcessor.execute(Constants.MAKE_DIRECTORY, "X");
        commandProcessor.execute(Constants.MAKE_DIRECTORY, "Y");
        commandProcessor.execute(Constants.LIST_CONTENTS, "");
        var out = output.getLines();
        assertEquals(out.size(), 3);
    }

    @ParameterizedTest
    @CsvSource(value = {"MM,/root/MM", "DM,/root/DM"})
    public void runTestCreateSubDir(String input, String expected) {
        commandProcessor.execute(Constants.MAKE_DIRECTORY, input);
        IDirectoryObject current = commandProcessor.execute(Constants.CHANGE_DIRECTORY, input);
        assert current != null;
        assertEquals(current.getName(), input);
        current = commandProcessor.execute(Constants.LIST_CONTENTS, "");
        current = commandProcessor.execute(Constants.CHANGE_DIRECTORY_UP, "");
        assertEquals(current.getName(), "root");
        commandProcessor.execute(Constants.LIST_CONTENTS, "");
        var out = output.getLines();
        assertEquals(out.get(0), expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"MM.txt,/root/sub/MM.txt", "DM.txt,/root/sub/DM.txt"})
    public void runTestCreateFile(String input, String expected) {
        IDirectoryObject current = commandProcessor.execute(Constants.MAKE_DIRECTORY, "sub");
        current = commandProcessor.execute(Constants.CHANGE_DIRECTORY, "sub");
        commandProcessor.execute(Constants.CREATE_FILE, input);
        current = commandProcessor.execute(Constants.CHANGE_DIRECTORY_UP, "");
        commandProcessor.execute(Constants.LIST_CONTENTS, "-r");
        var out = output.getLines();
        assertEquals(out.get(1), expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"MM.txt,/root/sub", "DM.txt,/root/sub"})
    public void runTestCreateFileNotListed(String input, String expected) {
        IDirectoryObject current = commandProcessor.execute(Constants.MAKE_DIRECTORY, "sub");
        current = commandProcessor.execute(Constants.CHANGE_DIRECTORY, "sub");
        commandProcessor.execute(Constants.CREATE_FILE, input);
        current = commandProcessor.execute(Constants.CHANGE_DIRECTORY_UP, "");
        commandProcessor.execute(Constants.LIST_CONTENTS, "");
        var out = output.getLines();
        assertEquals(out.size(), 1);
        assertEquals(out.get(0), expected);
    }
}

