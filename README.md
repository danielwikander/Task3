# TaskThree - HTTP file server

### Installation
```
git clone https://github.com/danielwikander/Task3
cd Task3/src/
javac TaskThree.java
```

### How to run
`java TaskThree [port] [rootpath]`

For example:

`java TaskThree 8080 /home/yourusername`

The fileserver will now be running with the root document path set as `/home/yourusername/`
Files from the server can now be fetched using HTTP GET requests.
For example, if the server has the file `/home/yourusername/helloworld.html`, 
users can now access the helloworld.html file in their browser by going to serverip:8080/helloworld.html
If the browser is running on the same machine as the server, it can be accessed by going to localhost:8080/helloworld.html

### Manpage
```
NAME
    TaskThree - HTTP Fileserver

Synopsis 
    TaskThree [port] [rootpath]
    
Description
    An HTTP Fileserver that returns files to requesting users.
    Requires both arguments to run.
    After starting the fileserver, files can be retrieved using HTTP GET requests on the relevant path.

Arguments
    port:     The port to listen for requests on.
    rootpath: The document root path where file requests will originate from.

Examples
    TaskThree 8080 /home/testname/
        The fileserver starts on port 8080, and the document root path has been set to /home/testname/
    TaskThree 5000 /home/testname/testfolder
        The fileserver starts on port 5000, and the document root path has been set to /home/testname/testfolder
```
