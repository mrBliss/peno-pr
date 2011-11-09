/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pcpanic.Server;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.JTextComponent;
import pcpanic.NXT.Pilot;

/**
 *
 * @author Me
 */
public class Server {

    public Connection co;
    public Channel ch;

    public Server() throws IOException {
        co = MQ.createConnection();
        ch = MQ.createChannel(co);
    }

    /**
     * Send a string message to an amqp server
     *
     * @param channel 
     * @param message
     */
    public void sendMessage(String channel, String message) {
        // set some properties of the message
        AMQP.BasicProperties props = new AMQP.BasicProperties();
        props.setTimestamp(new Date());	// set the time of the message, otherwise the
        // receivers do not know when the message is sent
        props.setContentType("text/plain"); // the body of the message is plain text
        props.setDeliveryMode(1);			// do not make the message persistant

        try {
            // publish the message to the exchange with the race.$teamname routing key
            ch.basicPublish(Config.EXCHANGE_NAME, channel, props, message.getBytes());
        } catch (IOException e) {
            System.err.println("Unable to send message to AMQP server");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            co.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void subscribe(String chId, final PrintStream tc, final Pilot p) throws IOException {
        final AMQP.Queue.DeclareOk queue = ch.queueDeclare();
        ch.queueBind(queue.getQueue(), Config.EXCHANGE_NAME, chId);
        ch.basicConsume(queue.getQueue(), false, new DefaultConsumer(ch) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                    AMQP.BasicProperties properties, byte[] body) throws IOException {
                // get the delivery tag to ack that we processed the message successfully
                long deliveryTag = envelope.getDeliveryTag();

                // properties.getTimestamp() contains the timestamp
                // that the sender added when the message was published. This
                // time is the time on the sender and NOT the time on the
                // AMQP server. This implies that clients are possibly out of
                // sync!
                if (tc != null) {
                    tc.append(String.format("@%d: %s -> %s",
                            properties.getTimestamp().getTime(),
                            envelope.getRoutingKey(),
                            new String(body)) + "\n");
                }

                String mess = new String(body);
                if (p != null) {
                    if (mess.equals("start")) {
                        p.stopRem();
                    } else if (mess.equals("stop")) {
                        p.startRem();
                    }
                }


                // send an ack to the server so it can remove the message from
                // the queue.
                ch.basicAck(deliveryTag, false);
            }
        });
    }
}
