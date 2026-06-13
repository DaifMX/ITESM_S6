package com.ai.crud.agents;

import com.ai.crud.model.CRUDResult;
import com.ai.crud.model.GeneratedCRUD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Saves the generated source files to disk under {@code <outputDir>/<EntityName>/}.
 *
 * Layout (Task F.4):
 *   src/generated/{EntityName}/Entity.java
 *   src/generated/{EntityName}/Repository.java
 *   src/generated/{EntityName}/Service.java
 *   src/generated/{EntityName}/Controller.java
 *   src/generated/{EntityName}/ServiceTest.java
 */
@Service
public class FileWriterService {

    private static final Logger log = LoggerFactory.getLogger(FileWriterService.class);

    private final String outputDir;

    public FileWriterService(@Value("${app.output-dir:src/generated}") String outputDir) {
        this.outputDir = outputDir;
    }

    public Path write(CRUDResult result) {
        GeneratedCRUD crud = result.getCrud();
        String entityName = crud.getEntityName() == null ? "GeneratedEntity" : crud.getEntityName();
        Path dir = Path.of(outputDir, entityName);
        try {
            Files.createDirectories(dir);
            writeFile(dir, "Entity.java", crud.getEntityCode());
            writeFile(dir, "Repository.java", crud.getRepositoryCode());
            writeFile(dir, "Service.java", crud.getServiceCode());
            writeFile(dir, "Controller.java", crud.getControllerCode());
            writeFile(dir, "ServiceTest.java", result.getServiceTests());
            log.info("Wrote generated files for '{}' to {}", entityName, dir.toAbsolutePath());
        } catch (Exception e) {
            log.error("Failed to write generated files for '{}': {}", entityName, e.getMessage(), e);
        }
        return dir;
    }

    private void writeFile(Path dir, String name, String content) throws Exception {
        if (content == null || content.isBlank()) {
            log.warn("  skipping {} — no content generated", name);
            return;
        }
        Path file = dir.resolve(name);
        Files.writeString(file, content, StandardCharsets.UTF_8);
        log.info("  wrote {} ({} chars)", file.getFileName(), content.length());
    }
}
