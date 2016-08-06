package my.dertraktor.mq.cloudmq;

import my.dertraktor.mq.QueueConnector;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Provides queue access.
 */
public class RabbitQueueConnector implements QueueConnector{

    /**
     * RabbitAdmin used to commenicate with remote cloud MQ
     */
    private RabbitAdmin rabbitAdmin;

    /**
     * AmqpTemplate used to commenicate with remote cloud MQ
     */
    private AmqpTemplate template;

    /**
     * The name of the queue
     */
    private String queueName;

    /**
     * Send file name to the queue
     *
     * @param data text to be sent to queue
     * @throws UnsupportedEncodingException should not be thrown
     */
    public void sendStringToQueue(String data) throws UnsupportedEncodingException {
        Message message = MessageBuilder.withBody(data.getBytes("utf-8")).build();
        template.send(queueName, message);
    }

    /**
     * retrieves data from the queue
     *
     * @return message data, or null if queue is empty
     */
    public String getStringFromQueue() throws UnsupportedEncodingException {
        Message message = template.receive(queueName);
        if (message == null)
            return null;
        return new String(message.getBody(), "utf-8");
    }

    public AmqpTemplate getTemplate() {
        return template;
    }

//    public void setTemplate(AmqpTemplate template) {
//        this.template = template;
//    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    /**
     * Returns count of messages in the queue
     * @return message count
     */
    public int getQueueMessageCount() {
        Properties queueProperties = rabbitAdmin.getQueueProperties(queueName);
        // according to how properties are filled inside RabbitAdmin, message count property is not a string. So properties are used as a map.
        Object messageCount = queueProperties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT);
        if (messageCount == null)
            return 0;
        // actually messageCount is an Integer, but let it be converted through string
        return Integer.parseInt(messageCount.toString());
    }

    /**
     * Purge queue
     */
    public void purgeQueue() {
        rabbitAdmin.purgeQueue(queueName, true);
    }

    public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
        this.template = rabbitAdmin.getRabbitTemplate();
    }
}
