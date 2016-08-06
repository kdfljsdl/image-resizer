package my.dertraktor.mq;

/**
 * Queue connector interface
 */
public interface QueueConnector {

    /**
     * Send file name to the queue
     *
     * @param data text to be sent to queue
     * @throws java.io.UnsupportedEncodingException should not be thrown
     */
    void sendStringToQueue(String data) throws Exception;

    /**
     * retrieves data from the queue
     *
     * @return message data, or null if queue is empty
     */
    String getStringFromQueue() throws Exception;


    /**
     * Returns count of messages in the queue
     *
     * @return message count
     */
    int getQueueMessageCount() throws Exception;

    /**
     * Purge queue
     */
    void purgeQueue();

}
