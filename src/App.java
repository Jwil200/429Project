
import java.io.IOException;


public class App {


    public static void main(String[] args) throws Exception {
       
        try {
			
            Chat chat = new Chat();
            System.out.println("--------Welcome--------");

		while (true) {
			System.out.print(">>>");
			String choice = chat.input.readLine();
			// the first argument is the command
			String option = choice.split(" ")[0].toLowerCase();

			switch (option) {
			case "chat":
				if (chat.listenSocket == null)
					chat.Start(choice);
				else
					System.out.println("Error: you can only listen to one port at a time");
				break;
			case "help":
				chat.Help();
				break;
			case "myip":
				System.out.println("My IP Address: " + chat.myIP);
				break;
			case "myport":
				if (chat.listenSocket == null)
					System.out.println("Error: you are not connected");
				else
					System.out.println("The program runs on port number: " + chat.listenPort);
				break;
			case "connect":
				if (chat.listenSocket == null)
					System.out.println("Error: you are not connected");
				else
					chat.Connect(choice);
				break;
			case "list":
				if (chat.listenSocket == null)
					System.out.println("Error: you are not connected");
				else
					chat.List();
				break;
			case "send":
				if (chat.listenSocket == null)
					System.out.println("Error: you are not connected");
				else
					chat.Send(choice);
				break;
			case "terminate":
				if (chat.listenSocket == null)
					System.out.println("Error: you are not connected");
				else
					chat.Terminate(choice);
				break;
			case "exit":
				//breakPeerConnections();
				System.exit(0);
				break;
			default:
				System.out.println("not a valid command");
			}
		}
        } catch (IOException e) {
           System.out.println(e);
        }
    }
}
