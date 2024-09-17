package core;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;



public class Server extends Thread implements  Runnable{
    private ArrayList<ConnectionHandler> connections ;
    private ServerSocket server;
    boolean done = false;

    public Server(){

    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            connections =  new ArrayList<ConnectionHandler>();
            System.out.println("----STARTED----");
            while(!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                Thread thread = new Thread(handler);
                thread.start();

                connections.add(handler);


            }

        } catch (IOException e) {
            shutdown();
        }
    }

    public void broadcast(String message){
        for(ConnectionHandler ch : connections){
            if(ch != null) {
                ch.sendMessage(message);
            }
        }
    }

    public void shutdown(){
        try {
            done = true;
            if (!server.isClosed()) {
                server.close();
                System.out.println("----CLOSED----");
            }
            for (ConnectionHandler conn : connections) {

                broadcast(conn.nickname + " was kicked out of the server!");
                conn.out.println("The server was closed!");conn.out.println("You were kicked out!");
                System.out.println(conn.nickname + " was kicked out!");

               conn.shutdown();


            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }



    class ConnectionHandler extends Thread implements Runnable {

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;

        public ConnectionHandler(Socket client) throws SocketException {
            this.client = client;
            this.client.setSoTimeout(5000);
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Please enter a nickname:");
                nickname = in.readLine();
                System.out.println(nickname + " connected!");
                broadcast(nickname + " joined the chat!");

                String message;
                while (!done) {
                    try {
                        message = in.readLine(); // Blocking call with timeout

                        if (message == null) {
                            shutdown();
                            break; // Client has disconnected
                        }
                        else if (message.startsWith("/nick")) {
                            String[] messageSplit = message.split(" ", 2);
                            if (messageSplit.length == 2) {
                                broadcast(nickname + " changed their nickname to " + messageSplit[1]);
                                System.out.println(nickname + " changed their nickname to " + messageSplit[1]);
                                nickname = messageSplit[1];
                                out.println("Successfully changed nickname to " + messageSplit[1]);
                            } else {
                                out.println("Error");
                            }
                        } else if (message.equals("/quit")) {
                            System.out.println(nickname + " disconnected!");
                            broadcast(nickname + " left the chat!");
                            shutdown();
                            break; // Exit the loop if user quits
                        } else {
                            broadcast(nickname + ": " + message);
                        }
                    } catch (IOException e) {
                        // This will happen if the socket times out or the connection is lost
                        if (done) {
                            shutdown();
                            break; // Exit the loop if the server is shutting down
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                shutdown();
            }
        }



        public void sendMessage(String message){
            out.println(message);
        }

        public void shutdown() {
            try {
                if (in != null) {
                    client.shutdownInput();
                    in.close(); // Close input stream
                }
                if (out != null) {
                    out.close(); // Close output stream
                }
                if (!client.isClosed()) {
                    client.close(); // Close socket connection
                }
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



    public static void main(String[] args) {
        Server server1 =new Server();
        server1.run();
    }
}







