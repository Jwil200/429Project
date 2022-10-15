## Requirements
This build has been tested on Java Versions 8 and 17. Any Java Version between these two should be fine for use. Manual instillation steps for Windows can be found below:
https://www.java.com/en/download/help/windows_manual_download.html

## Build & Run
To build the program either run in an IDE that can run Java code (such as VS Code), or build in a terminal using javac.
1. Open a terminal and cd into the folder containing Chat.java
2. Run `javac Chat.java`, this will compile class files.
3. Run `java Chat [port no]`
    * [port no] will be the port that the Chat client will run on.
    * If no port is supplied the Chat will run on one of its default ports, choosing only a port that is not currently in use.
    * Default ports are 5000, 4554, and 4000; chosen in that order.
4. Once the program is running use `help` to get a list of commands and information.