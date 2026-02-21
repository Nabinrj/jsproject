package com.hallsymphony.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TextFileStore {
    private final Path filePath;

    public TextFileStore(String filePath) {
        this.filePath = Path.of(filePath);
    }

    public List<String> readAll() {
        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
            }
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeAll(List<String> lines) {
        try {
            Files.write(filePath, new ArrayList<>(lines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
