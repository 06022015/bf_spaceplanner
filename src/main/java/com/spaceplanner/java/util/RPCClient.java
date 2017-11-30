package com.spaceplanner.java.util;

import com.rabbitmq.client.*;
import com.spaceplanner.java.exception.RPCExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 09/08/15
 * Time: 9:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class RPCClient {

    private Logger logger = LoggerFactory.getLogger(RPCClient.class);

    private static String HOST_NAME = "192.168.1.100";
    private static int PORT = 22;
    private static String USERNAME = "root";
    private static String PASSWORD = "welcome";
    private static int CONNECTION_TIMEOUT = 20000;
    private static String REQUEST_QUEUE_NAME = "rpc_queue";


    private Connection connection;
    private Channel channel;
    private String replyQueueName;
    private QueueingConsumer consumer;


    public void RPCClient() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST_NAME);
        connectionFactory.setPort(PORT);
        connectionFactory.setConnectionTimeout(CONNECTION_TIMEOUT);
        connectionFactory.setExceptionHandler(new RPCExceptionHandler());
        connectionFactory.setUsername(USERNAME);
        connectionFactory.setPassword(PASSWORD);
        connectionFactory.setAutomaticRecoveryEnabled(true);
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            replyQueueName = channel.queueDeclare().getQueue();
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(replyQueueName, true, consumer);
        } catch (IOException e) {
            logger.error("Unable to connect to remote machine\n"+ e);
            throw e;
        } catch (TimeoutException e) {
            logger.error("Connection Time out\n"+ e);
            throw e;
        }finally {
            close();
        }
    }


    public String call(Object object){
        String response = null;
        try {
            String corrId = java.util.UUID.randomUUID().toString();
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();
            channel.basicPublish("", REQUEST_QUEUE_NAME, props, (byte[]) object);
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                    response = new String(delivery.getBody());
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("Unable to publish Basic property\n", e);
        } catch (InterruptedException e) {
            logger.error("Delivery interrupted\n" + e);
        } finally {
            close();
        }
       return response;
    }

    public void close() {
        try {
            if (null != connection)
                connection.close();
        } catch (IOException e) {
            logger.error("Unable to close connection\n" + e);
        }
    }

}
