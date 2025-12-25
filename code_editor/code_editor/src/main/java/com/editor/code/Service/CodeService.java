package com.editor.code.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.editor.code.Model.CodeTemplate;
import com.editor.code.repository.CodeRepo;

@Service
public class CodeService {

    // Base directory where code files are stored
    private static final String BaseDir = "uploads/code";

    @Autowired
    private CodeRepo codeRepo;

    // Language → file extension mapping
    private static final Map<String, String> EXTENSIONS = Map.of(
            "java", ".java",
            "python", ".py",
            "javascript", ".js",
            "c", ".c",
            "cpp", ".cpp",
            "html", ".html",
            "css", ".css");

    /**
     * Save code file based on language
     */
    public Path saveCode(String codeBody, String langType, String fileName) throws IOException {

        CodeTemplate template = new CodeTemplate();
        template.setCodeBody(codeBody);
        template.setLangType(langType);
        template.setFileName(fileName);

        codeRepo.save(template);

        String ext = EXTENSIONS.get(langType.toLowerCase());
        if (ext == null) {
            throw new IllegalArgumentException("Unsupported language");
        }

        // Create directory for language
        Path dirPath = Paths.get(BaseDir, langType);
        Files.createDirectories(dirPath);

        // Create and write code file
        Path filePath = dirPath.resolve(fileName + ext);
        Files.write(
                filePath,
                codeBody.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        return filePath;
    }

    /**
     * Compile and/or run code depending on language
     */
    public String codeRun(Path filePath) {

        Path directory = filePath.getParent();
        String fileName = filePath.getFileName().toString();
        String extension = getExtension(fileName);

        try {
            switch (extension) {

                // ================= JAVA =================
                case "java":
                    return runJava(directory, fileName);

                // ================= PYTHON =================
                case "py":
                    return runCommand(directory, "python", fileName);

                // ================= JAVASCRIPT =================
                case "js":
                    return runCommand(directory, "node", fileName);

                // ================= C =================
                case "c":
                    return compileAndRun(
                            directory,
                            new String[] { "gcc", fileName, "-o", "program" },
                            new String[] { "./program" });

                // ================= C++ =================
                case "cpp":
                    return compileAndRun(
                            directory,
                            new String[] { "g++", fileName, "-o", "program" },
                            new String[] { "./program" });

                // ================= HTML =================
                case "html":
                    return "HTML file saved successfully.\n" +
                            "Open in browser: " + filePath.toAbsolutePath();

                // ================= CSS =================
                case "css":
                    return "CSS file saved successfully.\n" +
                            "Link this file with an HTML file to preview.";

                default:
                    return "Unsupported file type";
            }

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Compile and run Java program
     */
    private String runJava(Path directory, String fileName) throws Exception {

        String className = fileName.replace(".java", "");

        // Compile Java file
        Process compileProcess = new ProcessBuilder(
                "javac", fileName).directory(directory.toFile())
                .redirectErrorStream(true)
                .start();

        String compileOutput = readOutput(compileProcess);
        if (compileProcess.waitFor() != 0) {
            return "Compilation failed:\n" + compileOutput;
        }

        // Run Java class
        Process runProcess = new ProcessBuilder(
                "java", className).directory(directory.toFile())
                .redirectErrorStream(true)
                .start();

        return readOutput(runProcess);
    }

    /**
     * Compile (C/C++) and then run executable
     */
    private String compileAndRun(Path directory, String[] compileCmd, String[] runCmd) throws Exception {

        Process compileProcess = new ProcessBuilder(compileCmd)
                .directory(directory.toFile())
                .redirectErrorStream(true)
                .start();

        String compileOutput = readOutput(compileProcess);
        if (compileProcess.waitFor() != 0) {
            return "Compilation failed:\n" + compileOutput;
        }

        Process runProcess = new ProcessBuilder(runCmd)
                .directory(directory.toFile())
                .redirectErrorStream(true)
                .start();

        return readOutput(runProcess);
    }

    /**
     * Run single command (Python, JS)
     */
    private String runCommand(Path directory, String... command) throws Exception {

        Process process = new ProcessBuilder(command)
                .directory(directory.toFile())
                .redirectErrorStream(true)
                .start();

        return readOutput(process);
    }

    /**
     * Read output from process stream
     */
    private static String readOutput(Process process) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Utility: get file extension
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
