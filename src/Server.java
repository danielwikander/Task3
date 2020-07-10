import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Server holds the ServerSocket that clients send requests to.
 * The Server handles these requests by handing them over to Worker threads.
 * The Worker threads are in turn handled by a fixed size threadpool,
 * which limits the number of simultaneously running Workers to 10 in order to prevent thread starvation.
 */
public class Server {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private ServerSocket serverSocket = null;
    private boolean running = true;
    private int port;
    private String documentRootPath;

    public Server(int port, String documentRootPath) {
        this.port = port;
        this.documentRootPath = documentRootPath;
    }

    /**
     * Runs the server.
     * Continually looks for clients and hands them to the treadpool for Workers to handle.
     */
    public void start() {
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
            this.threadPool.execute(new Worker(clientSocket, documentRootPath));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.");
    }

    /**
     * Checks if the server is running or not.
     * @return true if the server is running.
     */
    private boolean isRunning() {
        return this.running;
    }

    /**
     * Stops the server thread.
     */
    public void stop() {
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
