package my.dertraktor.cloudstorage.dropboxuploader;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import my.dertraktor.cloudstorage.FileUploader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * This class allows to upload file into dropbox storage.
 * First, appKey, appSecret, and accessToken properties must be set to appropriate values.
 * The corresponding values have to be obtained on dropbox app settings page.
 */
public class DropBoxUploader implements FileUploader {
    private static Logger log = LogManager.getLogger(DropBoxUploader.class);

    /**
     * client Id
     */
    private String clientId = "ImageResizerBox/1.0";
    /**
     * Drop box access token.
     * It should be generated on https://www.dropbox.com/developers/apps, application settings tab
     */
    private String accessToken;
    /**
     * The forled inside dropbox storage to which files will be uploaded.
     * By default it's root folder
     */
    private String dropBoxFolder = "/";
    private DbxRequestConfig config;
    private DbxClient client;
    private boolean open;


    /**
     * Open dropbox connection.
     */
    public void open() throws DbxException {
        log.trace("enter open()");
        if (isOpen()) {
            log.trace(String.format("leave open(). Already open. Dropbox account: \"%s\"", client.getAccountInfo().displayName));
            return;
        }
        config = new DbxRequestConfig(clientId, Locale.getDefault().toString());
        client = new DbxClient(config, accessToken);
        log.trace(String.format("leave open(). Dropbox account: \"%s\"", client.getAccountInfo().displayName));
    }

    /**
     * Checks if DropBoxUploader is already connected to Dropbox
     *
     * @return true, if DropBoxUploader is already connected, false otherwise
     */
    public boolean isOpen() {
        return client != null;
    }

    /**
     * Upload gived file to dropbox
     *
     * @param file file to upload
     * @throws DbxException
     * @throws IOException
     */
    public void uploadFile(File file) throws DbxException, IOException {
        log.trace("enter uploadFile (" + file.getPath() + ")");

        open();

        InputStream fileIn = new FileInputStream(file);


        log.trace(String.format("start uploading image \"%s\" to dropbox folder \"%s\"", file.getName(), getDropBoxFolder()));

        client.uploadFile(getDropBoxFolder() + file.getName(), DbxWriteMode.add(), file.length(), fileIn);

        log.trace(String.format("image %s uploaded to drop box folder %s)", file.getName(), dropBoxFolder));
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDropBoxFolder() {
        return dropBoxFolder;
    }

    /**
     * Set path inside dropbox to upload files
     *
     * @param dropBoxFolder - path inside dropbox
     */
    public void setDropBoxFolder(String dropBoxFolder) {
        // dropbox path has to start with /
        if (!dropBoxFolder.startsWith("/"))
            dropBoxFolder = "/" + dropBoxFolder;
        // add / at the end
        if (!dropBoxFolder.endsWith("/"))
            dropBoxFolder += "/";
        this.dropBoxFolder = dropBoxFolder;

    }

}
