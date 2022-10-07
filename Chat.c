
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>





void IP()
{

  char myIP[16];
  struct sockaddr_in server_addr, my_addr;
  int sockIP;
 #define SERVER_ADDR "172.217.160.99"
 #define SERVER_PORT 80

// Clean buffers:
  bzero(&server_addr, sizeof(server_addr));

//Create socket
  sockIP = socket(AF_INET, SOCK_STREAM, 0);


  if (sockIP < 0) {
    perror("Can't open stream socket.");
    exit(-1);
    return;
  }


  // Set server_addr
  server_addr.sin_family = AF_INET;
  server_addr.sin_addr.s_addr = inet_addr(SERVER_ADDR);
  server_addr.sin_port = htons(SERVER_PORT);

  // Send connection request to server:
  if (connect(sockIP, (struct sockaddr *) &server_addr, sizeof(server_addr)) < 0) {
    perror("Connect server error");
    close(sockIP);
    exit(-1);
    return;
  }

  // Get my ip address
  // Clean buffers:
  bzero(&my_addr, sizeof(my_addr));
  socklen_t len = sizeof(my_addr);

  //retrieve the current address of the sockIP socket, store this address in the sockaddr_in structure (my_addr)
  getsockname(sockIP, (struct sockaddr *) &my_addr, &len);

  //converts a numeric network address in my_addr.sin_addr into a text string and store in myIP
  inet_ntop(AF_INET, &my_addr.sin_addr, myIP, sizeof(myIP));


  printf("The ip address is %s\n", myIP);

  close(sockIP);

}



void Help()
{
  printf("\nChat Application \n---------------- \n myip --> display IP address \n myport --> Display the port on which this process is listening for incoming connections \n connect --> connect to another peer \n send send messages to peers \n list --> Display a numbered list of all the connections this process is part of \n terminate --> terminate the connection \n exit --> Close all connections and terminate this process\n");

  return;

}


int main(int argc, char* argv[]) {

  if (argc < 2) {
    fprintf(stderr, "ERROR, no port provided\n");
    exit(1);
  }

  int port;
  port = atoi(argv[1]);

  char cmd[1024];
  char *arg;
  char array[3][32]; // to store arguments from command line 

  while (fgets(cmd, 1024, stdin)) {


    cmd[strlen(cmd) - 1] = '\0';
    arg = strtok(cmd, " ");
    int i = 0;

    while (arg != NULL) {

      strcpy(array[i], arg);
      i += 1;
      arg = strtok(NULL, " ");

    }
    if (i>3){
      fprintf(stderr, "ERROR, Number of arguments should be 3\n");
      continue;
  
    }

    if (!(strcmp(array[0], "help"))) {
      Help();
    }
    else if (!(strcmp(array[0], "myip"))) {
      IP();
    }
    else if (!(strcmp(array[0], "myport"))) {
      printf("The program runs on port number %u\n", port);
    }
    else if (!(strcmp(array[0], "connect"))) {
      //connect function
      //ip address stored in array[1]
      //port number stored in array[2]
    }
    else if (!(strcmp(array[0], "send"))) {
      //send function
    }
    else if (!(strcmp(array[0], "list"))) {
      //list function
    }
    else if (!(strcmp(array[0], "terminate"))) {
      //terminate function
    }

  }

  exit(-1);
  return 0;

}