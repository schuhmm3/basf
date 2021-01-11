package com.basf.patentmanager.application.service;

import com.basf.patentmanager.application.exception.ApplicationError;
import com.basf.patentmanager.application.exception.PatentException;
import com.basf.patentmanager.application.model.dto.PatentFieldsPaths;
import com.basf.patentmanager.domain.model.Patent;
import com.basf.patentmanager.domain.service.PatentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
@Slf4j
public class ApplicationPatentService {

    private final PatentService patentService;
    private final XmlMapperService xmlMapperService;

    @Autowired
    public ApplicationPatentService(PatentService patentService, XmlMapperService xmlMapperService) {
        this.patentService = patentService;
        this.xmlMapperService = xmlMapperService;
    }

    public Mono<Void> upload(FilePart filePart, PatentFieldsPaths paths) {

        AtomicReference<File> file = new AtomicReference<>();
        try {
            file.set(Files.createTempFile("", ".zip").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePart.transferTo(file.get())
                .doOnTerminate(() -> processPatents(file.get(), paths))
                .then();

    }

    private void processPatents(File file, PatentFieldsPaths paths) {
        readZip(file, paths);
    }

    /**
     * Reads zip file
     *
     * @param file
     * @param fieldsPaths
     * @return
     */
    void readZip(File file, PatentFieldsPaths fieldsPaths) {
        try (ZipFile zf = new ZipFile(file)) {
            log.info("Reading zip file {}", zf.getName());
            zf.stream().parallel()
                    .map(entry -> readZipEntry(zf, entry, fieldsPaths))
                    .map(patentService::addPatent)
                    .forEach(Mono::subscribe);
        } catch (IOException e) {
            throw new PatentException(ApplicationError.INTERNAL_ERROR, e);
        }
    }

    /**
     * Reads a zip entry and converts it into a Patent
     *
     * @param file        ZipFile where the entry is stored
     * @param entry       Entry to read
     * @param fieldsPaths Paths to retrieve the fields
     * @return The patent
     */
    Patent readZipEntry(ZipFile file, ZipEntry entry, PatentFieldsPaths fieldsPaths) {
        try {
            return this.xmlMapperService.createPatentFromXml(file.getInputStream(entry), fieldsPaths);
        } catch (IOException e) {
            throw new PatentException(ApplicationError.INTERNAL_ERROR, e);
        }
    }

}
