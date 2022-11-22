import java.util.List;
import java.util.ArrayList;

import java.io.*;
import java.net.*;

import java.lang.Runnable;
import java.lang.Thread;

public class Server {
		//variables
	int numUsers = 0;
    int numITUsers = 0;
    int numChannels = 0;
    int numPrivateChannels = 0;

    List<User> users;
    List<ITUser> ITUsers;

    List<Channel> channels;
    List<PrivateMessage> privateChannels;
	
		//server main code
	public static void main(String[] args) {
		
		ServerSocket server = null;
		try {
			server = new ServerSocket(1234);
			server.setReuseAddress(true);
			while (true) {
        			//accepts client connections
				Socket client = server.accept();

				ClientHandler handler = new ClientHandler(client);

            		//creates a new thread 
				new Thread(handler).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

	}

    	//Client Handler. interacts with clients
    private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		
    		//constructor
		ClientHandler(Socket newSocket) {
			this.clientSocket = newSocket;
		}
		
        public void run() {
				// allows objects to be recieced and sent
        	ObjectOutputStream out = null;
        	ObjectInputStream in = null;
            try {
					// connects the in and out with the client
            	out = new ObjectOutputStream(clientSocket.getOutputStream());
            	in = new ObjectInputStream(clientSocket.getInputStream());
            	
            	boolean waitLogin = true;
            	Message msg = (Message)in.readObject();
            	while (waitLogin == true) {
            		if ("login".equals(message.getType())) {
            				//searches through users and checks if correct
            			String userpass[] = message.getText().split(",");
            			boolean success = false;
            			for (int i = 0; i < users.size(); i++) { // checks if the password is in there
                            if (users.get(i).getUsername().equals(userpass.get(0)) && users.get(i).getPassword().equals(userpass.get(1))) {
                            	Message returnMessage = new Message("login","success", user(i).getID);
                				out.writeObject(returnMessage);
                				success = true;
                            }
                        }
            			if (success == false) {
            				Message returnMessage = new Message("login","fail", "");
            				out.writeObject(returnMessage);
            			}
            			
            		}
            		else if ("signup".equals(message.getType())) {
            				//pulls the name and password from the message text part 
            			String userpass[] = message.getText().split(",");
            			boolean success = true;
                        for (int i = 0; i < users.size(); i++) { // checks if the password is in there
                            if (users.get(i).getUsername().equals(userpass.get(0))) {
                                success = false;
                            }
                        }
            			if (success == false) {
            				Message returnMessage = new Message("signup","success", numUsers);
            				out.writeObject(returnMessage);
            				createUser(userpass.get(0), userpass.get(1));
            			}
            			//then sends a reply saying success or fail
            		}
            		msg = (Message)in.readObject();
            	}
                
                boolean shouldQuit = false;

                while (shouldQuit == false) {
                	msg = (Message)in.readObject();
                    switch(msg.getNetworkMessageType()) {
                        case None: {
                            break;
                        }
                        case Logout: { //user wants to logout
                            NetworkMessage logOutSucess;
                            shouldQuit = true;
                            outputMessages.add(logOutSucess);
                            break;   
                        }
                        case createchannel: { //user wants new channel
                            String newChannelName = "";
                            for (int i = 0; i < channels.size(); i++) {
                                if (channels.get(i).getName().equals(newChannelName)) {
                                    break;        
                                }
                            }

                            Channel newChannel = new Channel(numChannels, newChannelName);
                            channels.add(newChannel);
                            break;
                        }
                        case hidechannel: { //user wants to not see a channel
                            String channelName = "";
                            for (int i = 0; i < channels.size(); i++) {
                                if (channels.get(i).getName().equals(channelName)) {
                                    channels.get(i).hideChannel();
                                    break;
                                }

                            }
                            break;
                        }
                        case text: { //sent a message to channel
                            String message = "";
                            String channel = "";

                            for (int i = 0; i < channels.size(); i++) {
                                if (channels.get(i).getName().equals(channel)) {
                                    Message newMessage = new Message(channels.get(i).messages.size(), 0, message);
                                    channels.get(i).addMessage(newMessage);
                                    break;
                                }

                            }   
                            break;
                        }
                        case hidemessage: { //user wants to not see a message
                            break;
                        }
                        case 
                        default: {
                            break;
                        }
                    }

                    
                }

                socket.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if(socket != null)
                        if (socket.isClosed() == false)
                            socket.close();
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }

    }
    
    void loadUsers() {
        
    }
    void loadChannels() {
        
    }
    void loadITUser() {
         
    }
    void saveUsers() {
        File fp = new File("users.txt");
    }
    void saveChannels() {
        File fp = new File("channels.txt");
    }
    void saveITUser() {
        File fp = new File("itusers.txt"); 
    }
    void createUser(String username, String password) {
    	//add new user to database here
    	numUsers++;
    }

    void broadcastMessages() {

    }
    
    
}