import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.Runnable;
import java.lang.Thread;

public class Server {

	//variables

    static int numUsers = 0;
    static int numITUsers = 0;
    static int numChannels = 0;
    static int numPrivateChannels = 0;

    static User[] users = new User[25];
    static ITUser[] ITUsers = new ITUser[5];

    static List<Channel> channels;
    //List<PrivateMessage> privateChannels;
	
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
					saveUsers();
			        saveITUsers();
			        saveChannels();
					server.close();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

        saveUsers();
        saveITUsers();
        saveChannels();
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
            		if ("login".equals(msg.getType())) {
            				//searches through users and checks if correct
            			String userpass[] = msg.getText().split(",");
            			boolean success = false;
            			if (numUsers != 0) {
            				for (int i = 0; i < users.length; i++) { // checks if the password is in there
                            	if (users[i].getUsername().equals(userpass[0]) && users[i].getPassword().equals(userpass[1])) {
                            		Message returnMessage = new Message("login","success", "", users[i].getID());
                					out.writeObject(returnMessage);
                					success = true;
                            	}
                        	}
            			}
            			if (success == false) {
            				Message returnMessage = new Message("login","fail", "", 0);
            				out.writeObject(returnMessage);
            			}
            			else {
            				Message returnMessage = new Message("login","success","login successful", msg.getUserID());
            				out.writeObject(returnMessage);
            				waitLogin = false;
            			}
            			
            		}
            		else if ("signup".equals(msg.getType())) {
            				//pulls the name and password from the message text part 
            			String userpass[] = msg.getText().split(",");
            			boolean success = true;
            			if (numUsers != 0) {//checks if the user is in the database when there are more than 0 users
            				for (int i = 0; i < users.length; i++) { // checks if the user is in there
            					if (users[i].getUsername().equals(userpass[0])) {
            						success = false;
            					}
            				}
            			}
            			else {//for when there are 0 users
            				success = false;
            			}
            			if (success == false) {
            				Message returnMessage = new Message("signup","success","", numUsers);
            				out.writeObject(returnMessage);
            				createUser(userpass[0], userpass[1]);
            				System.out.println("client logged in: " + clientSocket.getInetAddress().getHostAddress());
            				waitLogin = false;
            			}
            			//then sends a reply saying success or fail
            		}
            		if (waitLogin == true) {
            			msg = (Message)in.readObject();
            		}
            	}
                
                boolean shouldQuit = false;

                while (shouldQuit == false) {
                	msg = (Message)in.readObject();
                	System.out.println("message recieved type: " + msg.getType());
                    switch(msg.getType()) {
                        case "none": {
                            Message returnMessage = new Message("","","", msg.getUserID());
                            out.writeObject(returnMessage);
                            break;
                        }
                        case "logout": { //user wants to logout
                            Message returnMessage = new Message("logout", "success", "", msg.getUserID());
                            shouldQuit = true;
                            out.writeObject(returnMessage);
                            break;   
                        }
                        case "createchannel": { //user wants new channel
                            String newChannelName = msg.getText();

                            boolean success = true;

                            for (int i = 0; i < channels.size(); i++) {
                                if (channels.get(i).getName().equals(newChannelName)) {

                                    success = false;
                                    break;        
                                }
                            }

                            if (success)
                            {
                                Channel newChannel = new Channel(numChannels, newChannelName);
                                newChannel.logMessage(users[msg.getUserID()].getUsername() + " created " + newChannelName);
                                channels.add(newChannel);
                            }
                            
                            String successMsg = (success) ? "success" : "fail";
                            Message returnMessage = new Message("createchannel", successMsg, "", msg.getUserID());
                            shouldQuit = true;
                            out.writeObject(returnMessage);

                            break;
                        }
                        case "hidechannel": { //user wants to not see a channel
                            boolean success = false;
                            String channelName = msg.getText();
                            for (int i = 0; i < channels.size(); i++) {
                                if (channels.get(i).getName().equals(channelName)) {
                                    channels.get(i).hideChannel();
                                    channels.get(i).logMessage(channelName + " has been hidden");
                                    success = true;
                                    break;
                                }

                            }
                            String successMsg = (success) ? "success" : "fail";
                            Message returnMessage = new Message("hidechannel", successMsg, "", msg.getUserID());
                            out.writeObject(returnMessage);
                            break;
                        }
                        case "text": { //sent a message to channel
                            String msgStr = msg.getText();
                            String tokens[] = msgStr.split("\n");
                            String message = tokens[1];
                            String channel = tokens[0];
                            int userID = msg.getUserID();

                            for (int i = 0; i < channels.size(); i++) {
                                if (channels.get(i).getName().equals(channel)) {
                                    Message newMessage = new Message("text", "active", message, userID);
                                    channels.get(i).addMessage(newMessage);
                                    channels.get(i).logMessage(users[msg.getUserID()].getUsername() + ": " + message);
                                    break;
                                }

                            }   
                            Message returnMessage = new Message("text","success","", userID);
                            out.writeObject(returnMessage);
                            break;
                        }
                        case "hidemessage": { //user wants to not see a message
                            String tokens[] = msg.getText().split("\n");
                            
                            String channel = tokens[1];
                            int msgID = Integer.parseInt(tokens[0]);
                            boolean success = false;
                            for (int i = 0; i < channels.size(); i++) {
                                if (channels.get(i).getName().equals(channel)) {
                                    for (Message channelMsg : channels.get(i).messages)
                                    {
//                                        if (channelMsg.ID == msgID && msg.getUserID() == channelMsg.getUserID())
//                                        {
//                                            channelMsg.setStatus("hidden");
//                                            channels.get(i).logMessage(Integer.toString(msgID) + " has been hidden by " + users.get(msg.getUserID()).getUsername());
//                                            success = true;
//                                        }
                                    }
                                }

                                if (success) break;
                            }   

                            String successStr = (success) ? "success" : "fail";
                            Message returnMessage = new Message("text", successStr,"", msg.getUserID());
                            out.writeObject(returnMessage);
                            break;
                        }
                        case "viewlogs":
                        {
                            boolean success = false;

                            String channel = msg.getText();
                            String logText = "";

                            for (int i = 0; i < channels.size(); i++) {

                                if (channels.get(i).getName().equals(channel)) {
                                    List<String> logs = channels.get(i).getLogs();

                                    for (int log = 0; log < logs.size(); log++)
                                    {
                                        logText += logs.get(log) + "\n";
                                    }
                                    success = true;
                                }

                                if (success) break;
                            }  
                            
                            String successStr = (success) ? "success" : "fail";
                            Message returnMessage = new Message("text", successStr, logText, msg.getUserID());
                            out.writeObject(returnMessage);
                            break;
                        }

                        default: {
                            Message returnMessage = new Message("","","",0);
                            out.writeObject(returnMessage);
                            break;
                        }
                    }

                    
                }

                clientSocket.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if(clientSocket != null)
                        if (clientSocket.isClosed() == false)
                        clientSocket.close();
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }

    }
    
    static void loadUsers()
    {
        File fp = new File("users.txt");
        try
        {
            Scanner scan = new Scanner(fp);

            while(scan.hasNextLine())
            {
                String line = scan.nextLine();
                String tokens[] = line.split(";");

                User user = new User(Integer.parseInt(tokens[0]), tokens[1], tokens[2]);
                users[numUsers] = user;
                numUsers += 1;
            }
            scan.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    static void saveUsers()
    {
        String str = "";

		for (int user = 0; user < users.length; user++)
		{
			str += Integer.toString(user)+ ";" + 
            users[user].getUsername() + ";" +
            users[user].getPassword() + "\n";
			
		}
        
		FileWriter fp;
		
		try
		{
			fp = new FileWriter("users.txt");
			fp.write(str);
		}
		catch (Exception e)
		{
			System.out.println("Cannot write " + "users.txt");
			return;
		}
		try {
		fp.close();
		}
		catch(Exception e)
		{
			return;
		}
		
    }

    static void loadChannels()
    {
        File fp = new File("channels.txt");

        try {
            Files.createDirectory(Paths.get("channels"));
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        try
        {
            Scanner scan = new Scanner(fp);
            while (scan.hasNextLine())
            {
                String line = scan.nextLine();

                // https://stackoverflow.com/a/18893443
                String tokens[] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                tokens[1] = tokens[1].substring(1, tokens.length - 1);

                Channel channel = new Channel(Integer.parseInt(tokens[0]), tokens[1]);
                if (Boolean.parseBoolean(tokens[2]))
                {
                    channel.hideChannel();
                }
                
                // Read message from file in channels directory
                try
                {
                    File channelFP = new File("channels/" + channel.getName() + ".txt");

                    Scanner scan0 = new Scanner(channelFP);
                    while (scan0.hasNextLine())
                    {
                        String secondLine = scan0.nextLine();
                        String secondTokens[] = secondLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                        Message msg = new Message("text", secondTokens[1],secondTokens[2],Integer.parseInt(secondTokens[0]));

                        /*Message msg = new Message(
                            Integer.parseInt(secondTokens[0]),
                            Integer.parseInt(secondTokens[1]),
                            secondTokens[3].substring(1, secondTokens.length - 1)
                        );*/

                        /*if (Boolean.parseBoolean(secondTokens[2]))
                        {
                            msg.hideMessage();
                        }*/

                        channel.addMessage(msg);
                    }

                    scan0.close();
                }
                catch (Exception e0)
                {
                    e0.printStackTrace();
                }

                channels.add(channel);
                numChannels += 1;
            }

            scan.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    static void saveChannels()
    {
        
        String str = "";
        try {
            Files.createDirectory(Paths.get("channels"));
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        List<String> channelMetadata = new ArrayList<>(channels.size());

		for (int channel = 0; channel < channels.size(); channel++)
		{
            try
            {
                String channelStr = "";

                String channelLine = 
                    Integer.toString(channels.get(channel).getID()) + ";" +
                    "\"" + channels.get(channel).getName() + "\"" + ";" +
                    Boolean.toString(channels.get(channel).isHidden());

                channelMetadata.add(channelLine);


                FileWriter fp = new FileWriter("channels/" + channels.get(channel).getName() + ".txt");
                for (int message = 0; message < channels.get(channel).messages.size(); message++)
                {
                    str += 
                        
                        Integer.toString(channels.get(channel).messages.get(message).getUserID()) + ";" +
                        channels.get(channel).messages.get(message).getStatus() + ";" +
                        "\"" + channels.get(channel).messages.get(message).getText() + "\""+ "\n";

                }
                fp.write(channelStr);
                fp.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

		}
        
		FileWriter fp;
		
		try
		{
			fp = new FileWriter("channels.txt");

            for (int channel = 0; channel < channelMetadata.size(); channel++)
            {
                str += channelMetadata.get(channel) + "\n";
            }

			fp.write(str);
		}
		catch (Exception e)
		{
			System.out.println("Cannot write " + "channels.txt");
			return;
		}
		try {
		fp.close();
		}
		catch(Exception e)
		{
			return;
		}
    }
    static void loadITUser()
    {
        File fp = new File("ITUsers.txt");
        try
        {
            Scanner scan = new Scanner(fp);

            while(scan.hasNextLine())
            {
                String line = scan.nextLine();
                String tokens[] = line.split(";");

                ITUser user = new ITUser(Integer.parseInt(tokens[0]), tokens[1], tokens[2]);
                ITUsers[numITUsers] = user;
                numITUsers += 1;
            }
            scan.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }

    static void saveITUsers()
    {
        String str = "";

		for (int user = 0; user < ITUsers.length; user++)
		{
			str += Integer.toString(user)+ ";" + 
            ITUsers[user].getUsername() + ";" +
            ITUsers[user].getPassword() + "\n";
		}
        
		FileWriter fp;
		
		try
		{
			fp = new FileWriter("ITUsers.txt");
			fp.write(str);
		}
		catch (Exception e)
		{
			System.out.println("Cannot write " + "users.txt");
			return;
		}
		try {
		fp.close();
		}
		catch(Exception e)
		{
			return;
		}
    }
    static void createUser(String username, String password) {
    	//add new user to database here
    	User user = new User(numUsers, username, password);
    	users[numUsers] = user;
    	numUsers++;
    }

    void broadcastMessages() {

    }
    
    
}