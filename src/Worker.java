import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * The Worker class handles incoming HTTP requests.
 * Each instance of this class handles a single request.
 */
public class Worker implements Runnable {
    protected Socket clientSocket;

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Starts the thread given to this worker by the Server threadpool.
     * This function handles a single request from a client.
     */
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream output = new BufferedOutputStream(clientSocket.getOutputStream());
            PrintStream pout = new PrintStream(output);

            // Read request from client
            String request = input.readLine();
            if (request == null) {
                return;
            }

            // Log request
            log(clientSocket, request);

            // Extract filepath from request
            String path = request.substring(5, request.length()-9).trim();
            File requestedFile = new File(path);

            // If the requested file is a directory, add 'index.html'
            if (requestedFile.isDirectory()) {
                path = path + "index.html";
                requestedFile = new File(path);
            }

            // Attempt to read and send file to client
            try {
                InputStream file = new FileInputStream(requestedFile);
                pout.print("HTTP/1.0 200 OK\r\n" +
                        "Content-Type: " + "text/html" + "\r\n" +
                        "Date: " + new Date() + "\r\n" +
                        "Server: TaskThree 1.0\r\n\r\n");
                sendFile(file, output);
                log(clientSocket, "200 OK");
            } catch (FileNotFoundException e) {
                errorReport(pout, clientSocket, "404", "Not Found",
                        "The requested URL was not found on this server.");
            }
            output.flush();
            output.close();
            input.close();
            pout.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a file to a client.
     * @param file The file to send.
     * @param out  The OutputStream connected to the client.
     */
    private static void sendFile(InputStream file, OutputStream out) {
        try {
            byte[] buffer = new byte[1000];
            while (file.available() > 0)
                out.write(buffer, 0, file.read(buffer));
        } catch (IOException e) {
            System.err.println("Could not send file:" + e.getMessage());
        }
    }

    /**
     * Logs an event related to a client.
     * @param connection The clients socket, contains the clients IP and port information etc.
     * @param msg        The message to log.
     */
    private static void log(Socket connection, String msg) {
        System.err.println(new Date() + " [" + connection.getInetAddress().getHostAddress() +
                ":" + connection.getPort() + "] " + msg);
    }

    /**
     * Sends an error message to a client.
     * @param pout       The PrintStream connected to the client.
     * @param connection The clients socket, contains the clients IP and port information etc.
     * @param code       The HTTP error code to send.
     * @param title      The title of the message to send.
     * @param msg        The message to send to the client.
     */
    private static void errorReport(PrintStream pout, Socket connection, String code, String title, String msg) {
        pout.print("HTTP/1.0 " + code + " " + title + "\r\n" +
                "\r\n" +
                "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" +
                "<TITLE>" + code + " " + title + "</TITLE>\r\n" +
                "</HEAD><BODY>\r\n" +
                "<H1>" + title + "</H1>\r\n" + msg + "<P>\r\n" +
                "<HR><ADDRESS>TaskThree 1.0 at " +
                connection.getLocalAddress().getHostName() +
                " Port " + connection.getLocalPort() + "</ADDRESS>\r\n" +
                "</BODY></HTML>\r\n");
        log(connection, code + " " + title);
    }
}
