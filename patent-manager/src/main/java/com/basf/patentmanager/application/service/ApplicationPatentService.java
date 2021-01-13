package com.basf.patentmanager.application.service;

import com.basf.patentmanager.application.exception.ApplicationError;
import com.basf.patentmanager.application.exception.PatentException;
import com.basf.patentmanager.application.model.dto.PatentFieldsPaths;
import com.basf.patentmanager.domain.model.Patent;
import com.basf.patentmanager.domain.service.PatentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.apache.tika.metadata.TikaMetadataKeys.RESOURCE_NAME_KEY;

/**
 * Application service to interact with the domain layer and map the received file into patents
 *
 * @author robertogomez
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ApplicationPatentService {

    private final PatentService patentService;
    private final XmlMapperService xmlMapperService;

    /**
     * Handles the upload of a file
     *
     * @param filePart File to upload
     * @param paths    Paths from the XML to retrieve all the fields
     * @return {@link Mono} of {@link Void} when the upload ends
     */
    public Mono<Void> uploadPatent(FilePart filePart, PatentFieldsPaths paths, boolean async) {

        AtomicReference<File> file = new AtomicReference<>();
        try {
            file.set(Files.createTempFile("", ".tmp").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePart.transferTo(file.get())
                .onErrorStop()
                .doOnSuccess(v -> processPatents(file.get(), paths, async))
                .then();
    }

    /**
     * Finds a {@link Patent} by its UUID
     *
     * @param id id to find the patent
     * @return {@link Flux} emitting the {@link Patent} found or {@link Flux#empty()} if none.
     */
    public Flux<Patent> findPatent(UUID id) {
        return this.patentService.findPatent(id).flux();
    }

    /**
     * Finds a {@link Patent} by its application reference
     *
     * @param application application reference to find the patent
     * @return {@link Flux} emitting the {@link Patent} found or {@link Flux#empty()} if none.
     */
    public Flux<Patent> findPatent(String application) {
        return this.patentService.findPatent(application);
    }

    /**
     * Deletes the {@link Patent} by its application reference
     *
     * @param id id to delete the patent
     * @return {@link Mono} when the operation is completed.
     */
    public Mono<Void> deletePatent(UUID id) {
        return this.patentService.deletePatent(id);
    }

    /**
     * Deletes all the {@link Patent}
     *
     * @return {@link Mono} when the operation is completed.
     */
    public Mono<Void> deleteAll() {
        return this.patentService.deleteAll();
    }

    /**
     * Processes the file to translate it into multiple patents
     *
     * @param file  File to process
     * @param paths Paths from the XML to retrieve all the fields
     * @param async Flag to process the patents asynchronously
     */
    private void processPatents(File file, PatentFieldsPaths paths, boolean async) {
        try {
            Metadata metadata = new Metadata();
            metadata.set(RESOURCE_NAME_KEY, file.toString());
            MediaType mediaType = new TikaConfig().getDetector().detect(
                    TikaInputStream.get(file.toPath()), metadata);
            if (mediaType.getSubtype().equals("zip"))
                readZip(file, paths, async);
            else if (mediaType.getSubtype().endsWith("xml"))
                readXml(file, paths, async);
            else
                throw new PatentException(ApplicationError.INVALID_FILE_FORMAT);
        } catch (IOException | TikaException e) {
            throw new PatentException(ApplicationError.READ_FILE_ERROR, e);
        }
    }

    /**
     * Reads XML file
     *
     * @param file  XML File to process
     * @param paths Paths from the XML to retrieve all the patent fields
     * @param async Flag to process the patent asynchronously
     */
    private void readXml(File file, PatentFieldsPaths paths, boolean async) {
        try {
            this.patentService.addPatent(this.xmlMapperService.createPatentFromXml(new FileInputStream(file), paths), async).subscribe();
        } catch (IOException e) {
            throw new PatentException(ApplicationError.READ_FILE_ERROR, e);
        }
    }

    /**
     * Reads zip file
     *
     * @param file  File to process
     * @param paths Paths from the XML to retrieve all the fields
     * @param async Flag to process all the patents asynchronously
     */
    private void readZip(File file, PatentFieldsPaths paths, boolean async) {
        try (ZipFile zf = new ZipFile(file)) {
            log.info("Reading zip file {}", zf.getName());
            zf.stream().parallel()
                    .map(entry -> readZipEntry(zf, entry, paths))
                    .map(patent -> patentService.addPatent(patent, async))
                    .forEach(Mono::subscribe);
        } catch (IOException e) {
            throw new PatentException(ApplicationError.READ_FILE_ERROR, e);
        }
    }

    /**
     * Reads a zip entry and converts it into a Patent
     *
     * @param file  ZipFile where the entry is stored
     * @param entry Entry to read
     * @param paths Paths from the XML to retrieve all the fields
     * @return The patent
     */
    private Patent readZipEntry(ZipFile file, ZipEntry entry, PatentFieldsPaths paths) {
        try {
            return this.xmlMapperService.createPatentFromXml(file.getInputStream(entry), paths);
        } catch (IOException e) {
            throw new PatentException(ApplicationError.READ_FILE_ERROR, e);
        }
    }

}
