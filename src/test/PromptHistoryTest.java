package test;


import java.io.*;
import mock.MockPromptHistory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PromptHistoryTest {
    MockPromptHistory mockprompt = new MockPromptHistory();

    @Test
    void testClearAll(){
        assertTrue("/src/mock/questionFile.txt".isEmpty());
        System.out.println("did that");

      
    }
}
