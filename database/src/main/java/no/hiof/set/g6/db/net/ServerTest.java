package no.hiof.set.g6.db.net;

import com.github.simplenet.Server;

import java.util.Scanner;

/**
 * @author Frederik Dahl
 * 30/09/2024
 */


public class ServerTest {
    
    
    public static void main(String[] args) {
        
        // Instantiate a new server.
        var server = new Server();
        

        // Register one connection listener.
        server.onConnect(client -> {
            System.out.println(client + " has connected!");
            
            /*
             * When one byte arrives from the client, switch on it.
             * If the byte equals 1, then request an int and print it
             * when it arrives.
             *
             * Because `readByteAlways` is used, this will loop when
             * the callback completes, but does not hang the executing thread.
             */
            client.readByteAlways(opcode -> {
                if (opcode == 1) {
                    client.readInt(System.out::println);
                }
                
            });
            
            
            
            // Register a pre-disconnection listener.
            client.preDisconnect(() -> System.out.println(client + " is about to disconnect!"));
            
            // Register a post-disconnection listener.
            client.postDisconnect(() -> server.close());
        });

        // Bind the server to an address and port AFTER registering listeners.
        server.bind("localhost", 43594);
        
        
        
    }
}
