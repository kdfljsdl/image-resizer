package my.dertraktor.cloudstorage;

import java.io.File;

/**
 * An interface to remote storage uploaders
 */
public interface FileUploader {

    /**
     * Uploads specified file to remote storage
     * @param file file to upload
     * @throws Exception exception during uploading
     */
    void uploadFile(File file) throws Exception;
}
