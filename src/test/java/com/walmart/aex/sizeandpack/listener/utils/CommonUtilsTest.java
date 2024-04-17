package com.walmart.aex.sizeandpack.listener.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CommonUtilsTest {
    public static String readFileAsString(String fileName, String ext) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName + ext)));
    }
}
