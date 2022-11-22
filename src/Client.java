import java.io.*;
import java.net.*;
import java.util.*;

	//client class
public class Client {
	public int userID = null;
	
	
		//Client driver
	public static void main(String[] args) {
			// establish a connection by providing host and port number
		try (Socket socket = new Socket("server ip address here", 1234)) {//change to what ever ip you make the server
		
				// writing to server
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

				// reading from server
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			
				//send login message
			String username = "";
			String password = "";
			boolean notLoggedin = true;
        	boolean recieved == null;
        	int input;
        	while (notLoggedin == true) {
        		//ui asks what operation they want to do, login or sign up
        		switch(get input) {  
                case 1: {//login
        			//needs code to ask for username and password calling the UI
        			Message loginMessage = new Message("login", "connecting", username + "," + password);
        			out.writeObject(loginMessage);
        			receved == false
					while (receved == false) {
	        			Message message = (Message)in.readObject();
	        			if ("login".equals(message.getType())) {
	        				receved = true;
	        				if ("success".equals(message.getStatus())) {
	        					notLoggedin = false;
	        				}
	        			}
	        		}
        		}
                case 2: {//sign up
                	//needs code to ask for username and password calling the UI
        			Message SignupMessage = new Message("signup", "creating new user", username + "," + password);
        			out.writeObject(SignupMessage);
        			receved == false
					while (receved == false) {
	        			Message message = (Message)in.readObject();
	        			if ("login".equals(message.getType())) {
	        				receved = true;
	        				if ("success".equals(message.getStatus())) {
	        					notLoggedin = false;
	        				}
	        			}
	        		}
                }
        		
        	}
		
				//recieve login confermation
			Message loginConfirm = (Message)in.readObject();
			setUID((int)loginConfirm.getText())
			//System.out.println(loginConfirm.toString());

				// line scanner to read client text
			Scanner sc = new Scanner(System.in);
			String line = null;

			while (!"logout".equalsIgnoreCase(line)) {
				System.out.println("text to send or type 'logout' to logout: \n");
					// reading from user
				line = sc.nextLine();
			
				if (!"logout".equalsIgnoreCase(line)) {
						// sending the user input to server
					Message message = new Message("text", "sending text to convert", line);
					out.writeObject(message);

						// displaying server reply
					Message reply = (Message)in.readObject();
					System.out.println("New text from server: "+ reply.getText());
				}
			}
				//logout sequence
			Message logoutMessage = new Message("logout", "transmit", "telling server to end communication");
			out.writeObject(logoutMessage);
			Message logoutConfirm = (Message)in.readObject();
			System.out.println(logoutConfirm.toString());
				// closing the scanner object
			sc.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception f) {
			f.printStackTrace();
		}
	}
	
	void setUID(int userID) {
		if (this.userID == null) {
			this.userID = userID
		}
	}

    void doLogin() {

    }

    void doLogout() {

    }

    void doSignup() {

    }

    void doSendMessage() {

    }

    void doCreateChannel() {

    }

    void doJoinChannel() {

    }

    void doDisplayChannels() {

    }

    void doChangeChannel() {

    }

    void doLeaveChannel() {

    }

    void doDisconnect() {
        
    }


}