## Overview
A simple Java based chat application for message exchange among remote peers. The program has the following commands implements:

* help - Shows a list of all the commands in the terminal.
* myip - Shows the IP address of the process.
* myport - Shows the port on which the process is listening for incoming connections.
* connect <destination IP> <port no> - Establishes a new TCP connection to the specified <destination> at the specified <port no>
* list - Shows the list of all the connections the process is part of.
         E.g.
	Id              IP address 		Port no
	1	192.168.21.20		4554
	2	192.168.21.21		5000
* Send <connection id> <message> - This will send the message to the host on the connection that is designated by the id number from the list command.
* terminate <connection id> - This command will terminate the connection listed under the specified id number from the list command.
* exit - Close all connections and terminate the process.
 
Complete details and contribution information can be found at: https://github.com/Jwil200/429Project

## Contributions
Farid
* Researched and implemented the concept of threading which handles server and client at the same time.
* Set up command for help method
* Researched and implemented connect and send method
* Created the list method to display list of connected peers
* Worked with Jarod to fix errors. 
Jarod
* Worked on finalizing the connect method, as well as handlers for servers and peers.
* Worked on and completed list, terminate, and exit methods.
* Created a few utilities for the program.
* Worked on Javadoc comments and formatting.

## Requirements
This build has been tested on Java Versions 8 and 17. Any Java Version between these two should be fine for use. Manual installation steps for Windows can be found below:
https://www.java.com/en/download/help/windows_manual_download.html

## Build & Run
There are a number of ways to run the program, a couple of them will require building class files before the program can run. These are listed based on how highly we recommend using each method.

### Command Prompt
Command prompt would be the preferred way of running this program.
1. Open command prompt in the folder containing Chat.java, or `cd` into it.
2. Run `javac Chat.java` this will compile class files to run with. Once this is run it should not be necessary to do so again.
3. Run `java Chat [port no]`
* [port no] will be the port that the Chat client will run on.
* If no port is supplied the Chat will run on one of its default ports, choosing only a port that is not currently in use.
* Default ports are 5000, 4554, and 4000; chosen in that order.

### IDE
A number of IDEs can be used to run the program, one that would be recommended is VS Code due to being lightweight and easy to execute in.
1. Clone the repository or open this folder in VS Code.
* You should ensure that you have the VS Code Java extensions installed, it should prompt you upon opening Chat.java if you do not have it.
2. Open Chat.java, above the main method there should be an option to run it.
* Alternatively in terminal executing `cd src`, `javac Chat.java`, and `java Chat [port no]` will run the program.
* Replace `[port no]` with your desired port for the chat to run on. If none is provided it will use its own port from one of the default ports, given that the port is not open.
* After running `javac Chat.java` it should not be necessary to run it again.