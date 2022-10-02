
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define SERVER_ADDR "172.217.160.99"
#define SERVER_PORT 80
char myIP[16];
unsigned int myPort;
struct sockaddr_in server_addr, my_addr;
int sockfd;

void IP()
{

  // Get my ip address
  bzero(&my_addr, sizeof(my_addr));
  socklen_t len = sizeof(my_addr);
  getsockname(sockfd, (struct sockaddr *) &my_addr, &len);
  inet_ntop(AF_INET, &my_addr.sin_addr, myIP, sizeof(myIP));
  myPort = ntohs(my_addr.sin_port);

  printf("The ip address is %s\n", myIP);



}



void Port() {

  // Get my port
  bzero(&my_addr, sizeof(my_addr));
  socklen_t len = sizeof(my_addr);
  getsockname(sockfd, (struct sockaddr *) &my_addr, &len);
  //inet_ntop(AF_INET, &my_addr.sin_addr, myIP, sizeof(myIP));
  myPort = ntohs(my_addr.sin_port);


  printf("The program runs on port number %u\n", myPort);


}


void Help()
{
  printf("\nChat Application \n---------------- \n myip --> display IP address \n myport --> Display the port on which this process is listening for incoming connections \n connect --> connect to another peer \n send send messages to peers \n list --> Display a numbered list of all the connections this process is part of \n terminate --> terminate the connection \n exit --> Close all connections and terminate this process\n");

  return;

}


int main() {


  // Connect to server
  if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
    perror("Can't open stream socket.");
    exit(-1);
  }

  // Set server_addr
  bzero(&server_addr, sizeof(server_addr));
  server_addr.sin_family = AF_INET;
  server_addr.sin_addr.s_addr = inet_addr(SERVER_ADDR);
  server_addr.sin_port = htons(SERVER_PORT);


  if (connect(sockfd, (struct sockaddr *) &server_addr, sizeof(server_addr)) < 0) {
    perror("Connect server error");
    close(sockfd);
    exit(-1);
  }

  char choice[12];


  while (strcmp(choice, "exit") != 0) {
    scanf("%s", choice);
    if (strcmp(choice, "help") == 0) {
      Help();
    }
    else if (strcmp(choice, "myip") == 0) {
      IP();
    }
    else if (strcmp(choice, "myport") == 0) {
      Port();
    }
    else if (strcmp(choice, "connect") == 0) {
      //connect function
    }
    else if (strcmp(choice, "send") == 0) {
      //send function
    }
    else if (strcmp(choice, "list") == 0) {
      //list function
    }
    else if (strcmp(choice, "terminate") == 0) {
      //terminate function
    }
  }

  close(sockfd);
  exit(-1);
  return 0;
}
