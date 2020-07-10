public class TaskThree {

    public static void main(String[] args) {
        // Print man page if requested in args.
        if (args.length > 0 && (args[0].equals("-h") || args[0].equals("-help") || args[0].equals("help"))) {
            printManPage();
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("The TaskThree fileserver requires a port number as an argument.");
            System.out.println("Execute 'TaskThree -h' for help.");
            return;
        }

        Server server = new Server(port);
        new Thread(server).start();

        try {
            Thread.sleep(1000 * 60 * 60); // Run for an hour..
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping server..");
        server.stop();
    }

    /**
     * Prints the programs manpage.
     * TODO: FIXto TaskThree 
     */
    public static void printManPage() {
        System.out.println(
            "NAME\n" +
            "    TaskThree - HTTP Fileserver.\n\n" +
            "SYNOPSIS \n" +
            "    TaskThree [portnumber] \n\n" +
            "DESCRIPTION\n" +
            "    An HTTP Fileserver that returns files to requesting users.\n" +
            "    Requires one argument to run (port).\n" +
            "    After starting the fileserver, files can be retrieved using HTTP GET requests .\n\n" +
            "ARGUMENTS\n" +
            "    port:         The path the the file to create/overwrite. Must be a valid filepath.\n\n" +
            "EXAMPLES\n" +
            "    TaskThree 8080\n" +
            "        The fileserver starts on port 8080. Users can now connect to the fileserver by \n" +
            "    TaskThree 5000 \n" +
            "        The file 'testFile' will be read and its numbers will be printed in ascending order. \n");
    }
}

