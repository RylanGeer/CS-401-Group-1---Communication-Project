import java.io.*;
import java.net.*;

	// Server class
class Sorvor {
		//this part under main is uses the example given 
		// for multithreaded servers and i only changed 
		//a few things for this part because there wasnt
		//much to change here for the connecting part
		//the part for after the client connects is 
		//compleatly different
	public static void main(String[] args)
	{
		ServerSocket server = null;

		try {

				// server is listening on port 1234
			server = new ServerSocket(1234);
			server.setReuseAddress(true);

				// running infinite loop for getting client request
			while (true) {
					// socket object to receive incoming client requests
				Socket client = server.accept();

					// Shows client connected
				System.out.println("New client connected " + client.getInetAddress().getHostAddress());

					// create a new thread object
				ClientHandler clientSock = new ClientHandler(client);

					// This thread will handle the client separately
				new Thread(clientSock).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

		// ClientHandler class 
		// the client handler was changed obiously to allow messages to be passed as objects
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;

		// Constructor
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		public void run() {
				// allows objects to be recieced and sent
			ObjectOutputStream out = null;
			ObjectInputStream in = null;
			try {
					// connects the in and out with the client
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
					// waiting for login message 
				Message message = (Message)in.readObject();
				System.out.println(message.getType());
				while (!"login".equals(message.getType())) {
					message = (Message)in.readObject();
					System.out.println(message.getType());
				}
					//if login message sent, confirms the login
				Message returnMessage = new Message("login","success","login successful", 1);
				out.writeObject(returnMessage);
					// notifies the terminal that login was confirmed to given address
				System.out.println("client logged in: " + clientSocket.getInetAddress().getHostAddress());
				
					// message handler: converts to upper and also checks for logout
				Message clientMsg = (Message)in.readObject();;
				while (!"logout".equalsIgnoreCase(clientMsg.getType())) {
						// if message is not a logout message replies with upper
					if (!"logout".equalsIgnoreCase(returnMessage.getType())) {
							// replying with upper case 
						Message reply = new Message("text", "sending text to convert", clientMsg.getText().toUpperCase(), 1);
						out.writeObject(reply);
						clientMsg = (Message)in.readObject();

					}
				}
					//logout sequence
				Message logoutConfirm = new Message("logout", "recieved", "Connection terminated" ,1);
				out.writeObject(logoutConfirm);
				System.out.println("disconnecting client: " + clientSocket.getInetAddress().getHostAddress());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
						clientSocket.close();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
