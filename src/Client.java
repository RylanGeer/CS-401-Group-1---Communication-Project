import java.io.*;
import java.net.*;
import java.util.*;

	//client class
public class Client {
	
		//Client driver
	public static void main(String[] args) {

			// establish a connection by providing host and port number
		try (Socket socket = new Socket("172.17.0.91", 1234)) {//change to what ever ip you make the server
		

				// writing to server
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

				// reading from server
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			
				//creates the gui
			GUI ui = new GUI(socket, out, in);
			
				//send login message
			boolean notLoggedin = true;
        	boolean recieved = false;
        	while (notLoggedin == true) {
        			//ui asks what operation they want to do, login or sign up
        		switch(ui.showLoginDialog()) {  
                	case 1: {//login
                		//sends login message with username and password
                		Message loginMessage = new Message("login", "connecting", ui.getUser() + "," + ui.getPass(), 0);
                		out.writeObject(loginMessage);
                		while (recieved == false) {
                			Message message = (Message)in.readObject();
                			if ("login".equals(message.getType())) {
                				recieved = true;
                				if ("success".equals(message.getStatus())) {
                					notLoggedin = false;
                					ui.setUID(message.getUserID());
                				}
                			}
                		}
                	}
                	case 0: {//sign up
                		//needs code to ask for username and password calling the UI
                		Message SignupMessage = new Message("signup", "creating new user", ui.getUser() + "," + ui.getUser(), 0);
                		out.writeObject(SignupMessage);
                		while (recieved == false) {
                			Message message = (Message)in.readObject();
                			if ("signup".equals(message.getType())) {
                				recieved = true;
                				if ("success".equals(message.getStatus())) {
                					notLoggedin = false;
                					ui.setUID(message.getUserID());
                				}
                			}
                		}
                	}
        		}	
        		
        	}
        		//System.out.println(loginConfirm.toString());

					// line scanner to read client text
//        		Scanner sc = new Scanner(System.in);
//        		String line = null;

        	Message reply;
			do {
					// reading from server
        		reply = (Message)in.readObject();
        		switch(reply.getType()) {  
            		case "text": {//revieves message 
            		
            		}
            		case "hide": {//recieves hide confirm
            		
            		}
            		case "new": {//recieves new confirm
            		
            		}
        		}
			

        	} while (!"logout".equalsIgnoreCase(reply.getType()));
//					//logout sequence
//        		Message logoutMessage = new Message("logout", "transmit", "telling server to end communication", userID);
//        		out.writeObject(logoutMessage);
//        		Message logoutConfirm = (Message)in.readObject();
//        		System.out.println(logoutConfirm.toString());
//					// closing the scanner object
//        		sc.close();
        	
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception f) {
			f.printStackTrace();
		}
	}
	

//    void doLogin() {
//
//    }

    void doLogout() {
//			//logout sequence
//		Message logoutMessage = new Message("logout", "transmit", "telling server to end communication", userID);
//		out.writeObject(logoutMessage);
//		Message logoutConfirm = (Message)in.readObject();
//		System.out.println(logoutConfirm.toString());

    }

//    void doSignup() {
//
//    }

    void doSendMessage(String text) {
//			// sending the user input to server
//		Message message = new Message("text", "sending text to server", text, userID);
//		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//		try {
//			out.writeObject(message);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//			
//		}

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