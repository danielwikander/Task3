import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main server.
 * The Server holds the Server Socket that clients send requests to.
 * The Server handles these requests by handing them to Worker threads.
 * The threadpool is a fixed pool with 10 threads in order to prevent thread starvation.
 */
public class Server implements Runnable {

    // Uses a fixed threadpool with 10 threads in order to prevent thread starvation.
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private ServerSocket serverSocket = null;
    private boolean running = true;
    private int port = 8080;

    public Server(int port) {
        this.port = port;
    }

    /**
     * Runs the server.
     * Continually looks for clients and hands them to the treadpool for Workers to handle.
     */
    public void run() {
        openServerSocket();
        while (isRunning()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (!isRunning()) {
                    System.out.println("Server stopped.");
                    break;
                }
                System.err.println("Error accepting connection from client: " + e.getMessage());
            }
            this.threadPool.execute(new Worker(clientSocket));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.");
    }

    /**
     * Checks if the server is running or not.
     * @return true if the server is running.
     */
    private synchronized boolean isRunning() {
        return this.running;
    }

    /**
     * Stops the server thread.
     */
    public synchronized void stop() {
        this.running = false;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing serverSocket:" + e.getMessage());
        }
    }

    /**
     * Opens the ServerSocket for incoming connections.
     */
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.err.println("Error opening port: " + this.port + e.getMessage());
        }
    }
}
