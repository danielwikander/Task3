public class TaskThree {

    public static void main(String[] args) {
        // Print man page if requested in args.
        if (args.length > 0 && (args[0].equals("-h") || args[0].equals("-help") || args[0].equals("help"))) {
            printManPage();
            return;
        }

        // Parse port and documentRootPath from args.
        int port;
        String documentRootPath;
        try {
            port = Integer.parseInt(args[0]);
            documentRootPath = args[1];
        } catch (Exception e) {
            System.out.println("The TaskThree fileserver requires a port number and a document root path as an argument.");
            System.out.println("Execute 'TaskThree -h' for help.");
            return;
        }

        // Create and start server
        Server server = new Server(port, documentRootPath);
        server.start();

        // Stop server if interrupted
        System.out.println("Stopping server..");
        server.stop();
    }

    /**
     * Prints the programs manpage.
     */
    public static void printManPage() {
        System.out.println(
            "NAME\n" +
            "    TaskThree - HTTP Fileserver.\n\n" +
            "SYNOPSIS \n" +
            "    TaskThree [port] \n\n" +
            "DESCRIPTION\n" +
            "    An HTTP Fileserver that returns files to requesting users.\n" +
            "    Requires both arguments to run.\n" +
            "    After starting the fileserver, files can be retrieved using HTTP GET requests on the relevant path.\n\n" +
            "ARGUMENTS\n" +
            "    port:     The port to listen for requests on.\n" +
            "    rootpath: The document root path where file requests will originate from.\n\n" +
            "EXAMPLES\n" +
            "    TaskThree 8080 /home/testname/\n" +
            "        The fileserver starts on port 8080, and the document root path has been set to /home/testname/\n" +
            "    TaskThree 5000 /home/testname/testfolder\n" +
            "        The fileserver starts on port 5000, and the document root path has been set to /home/testname/testfolder\n");
    }
}

