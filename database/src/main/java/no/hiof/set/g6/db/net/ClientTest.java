package no.hiof.set.g6.db.net;


import com.github.simplenet.Client;
import com.github.simplenet.packet.Packet;

import java.io.IOException;

/**
 * @author Frederik Dahl
 * 30/09/2024
 */


public class ClientTest {
    
    public static void main(String[] args) throws IOException {
        // Instantiate a new client.
        var client = new Client();
        // Register a connection listener.
        client.onConnect(() -> {
            System.out.println(client + " has connected to the server!");
            // Builds a packet and sends it to the server immediately.
            Packet.builder().putByte(1).putInt(42).queueAndFlush(client);
            
        });

        // Register a pre-disconnection listener.
        client.preDisconnect(() -> System.out.println(client + " is about to disconnect from the server!"));

        // Register a post-disconnection listener.
        client.postDisconnect(() -> System.out.println(client + " successfully disconnected from the server!"));

        // Attempt to connect to a server AFTER registering listeners.
        client.connect("localhost", 43594);
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.close();
        
        
    }
}
