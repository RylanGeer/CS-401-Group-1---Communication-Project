import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

import java.net.*;

public class GUI {
	String username = "";
	String password = "";
	public int userID = 0;
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	JFrame frame;
	
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new GUI();
//        });
//    }
    
    GUI(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        this.frame = new JFrame("Communication Systems"); // app's title
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setResizable(false);
        frame.setSize(400, 300); // not recommended, but used here for convenience
        frame.setLocationRelativeTo(null);  // Center on screen
        frame.setVisible(true);
        this.socket = socket;
        this.out = out;
        this.in = in;
        createGUI(frame);
//        showLoginDialog(frame); // pauses until user clicks login/sign up
        
    }
	
    public String getUser() {
    	return this.username;
    }
    
    public String getPass() {
    	return this.password;
    }
    
	void setUID(int userID) {
		if (this.userID == 0) {
			this.userID = userID;
			
		}
	}
    
//    private static void doSignup(String username, String password) {
//    	/** TODO: Handle sign up here. **/
//
//    }
//    
//    private static void doLogin(String username, String password) {
//    	/** TODO: Handle login here. **/
//
//    }
	private void doLogout() {
			//logout sequence
		Message logoutMessage = new Message("logout", "transmit", "telling server to end communication", userID);
		try {
			out.writeObject(logoutMessage);
			Message logoutConfirm = (Message)in.readObject();
			System.out.println(logoutConfirm.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    private static void displayChannels() {
    	/** TODO: Handle showing channels **/

    }
    
    private static void createChannel(String channelID) {
    	/** TODO: Handle creating channels **/

    }
    
    private static void joinChannel(String channelID) {
    	/** TODO: Handle display channels **/

    }
    
    private static void leaveChannel(String channelID) {
    	/** TODO: Handle leaving channels **/

    }
    
    private static void viewLogs() {
    	/** TODO: Handle showing logs, only IT users can use **/

    }
    
    private static void sendMessage(String message) {
    	/** TODO: Handle sending message here. **/

    }
    
    public int showLoginDialog() {
        JPanel panel = new JPanel(new BorderLayout(5,5));

        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        labels.add(new JLabel("Username", SwingConstants.TRAILING));
        labels.add(new JLabel("Password", SwingConstants.TRAILING));
        panel.add(labels, BorderLayout.WEST); // Place labels left side

        JPanel controls = new JPanel(new GridLayout(0,1,2,2));
        JTextField username = new JTextField();
        username.addAncestorListener(new RequestFocusListener(false)); // set focus on username
        controls.add(username);
        JPasswordField password = new JPasswordField();
        controls.add(password);
        panel.add(controls, BorderLayout.CENTER);
        
        // Get user's button choice
        Object[] options = {"Signup", "Login"};
        int choice = JOptionPane.showOptionDialog(frame, panel, "Login", 0, JOptionPane.INFORMATION_MESSAGE, null, options, options);
        
        // Store user's username and password
        this.username = username.getText();
    	this.password = password.getPassword().toString();
    	
    	// Button click listener (0 = sign up, 1 = login)
        if (choice == 0) {
        	return 0;
        	//doSignup(username.getText(), password.getPassword().toString());
        } else { 
        	return 1;
        	//doLogin(username.getText(), password.getPassword().toString());
        }
    }
    
    private void createGUI(JFrame frame){  
        JPanel gui = new JPanel(new BorderLayout(5,5));
        frame.setContentPane(gui);
        JTextArea chatArea = new JTextArea(15, 50);
        JTextField chatBox = new JTextField("Send a message!");
        JScrollPane scroll = new JScrollPane(chatArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JPanel controls = new JPanel(new GridLayout(0, 1)); // inf rows, 1 col
        JButton displayChannelsButton = new JButton("Display Channels");
        JButton createChannelButton = new JButton("Create Channel");
        JButton joinChannelButton = new JButton("Join Channel");
        JButton leaveChannelButton = new JButton("Leave Channel");
        JButton viewLogsButton = new JButton("View Logs");
        viewLogsButton.setEnabled(true); //make false
        
        // Button listeners
        displayChannelsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	chatArea.append(
                		"Channels available (4):\r\n"
                		+ "1: cs401 \r\n"
                		+ "2: cs471 \r\n"
                		+ "3: cs101 \r\n"
                		+ "4: cs201 \r\n"
                		+ "Group Message: /group cs401 message_here\n"
                		+ "****************************************************\n");
//            	displayChannels();
            }
         });
        
        createChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String channelID = (String)JOptionPane.showInputDialog(frame,"Enter channel unique id to create (ex. cs401):","Create Channel",JOptionPane.PLAIN_MESSAGE, null, null,null);
            	
            	// User entered text
            	if ((channelID != null) && (channelID.length() > 0)) {
            		chatArea.append("Channel Created [" + channelID + "]!\r\n");
//            		createChannel(channelID);
            	}
            }
         });
        
        joinChannelButton.addActionListener(new ActionListener() {        	
            public void actionPerformed(ActionEvent e) {
            	String channelID = (String)JOptionPane.showInputDialog(frame,"Enter channel unique id to join (ex. cs401):","Join Channel",JOptionPane.PLAIN_MESSAGE,null,null,null);
            	
            	if ((channelID != null) && (channelID.length() > 0)) {
            		chatArea.append("Channel joined [" + channelID + "]!\r\n");
//                	joinChannel(channelID);
            	}
            }
         });
        
        leaveChannelButton.addActionListener(new ActionListener() {        	
            public void actionPerformed(ActionEvent e) {
            	String channelID = (String)JOptionPane.showInputDialog(frame,"Enter channel unique id to leave (ex. cs401):","Leave Channel",JOptionPane.PLAIN_MESSAGE,null, null,null);
            	
            	if ((channelID != null) && (channelID.length() > 0)) {
            		chatArea.append("Channel left [" + channelID + "] ;c\r\n");
//                	leaveChannel(channelID);
            	}
            }
         });

        viewLogsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(null, 
            			"Message Log (3):\r\n"
                        + "timmy123: [all] hello world \r\n"
                        + "bob456: [cs401] how is the weather? \r\n"
                        + "bob456: [to timmy123] how is life? \r\n", "Log", JOptionPane.PLAIN_MESSAGE, null);
//               viewLogs();
            }
         });
        
        controls.add(displayChannelsButton);
        controls.add(createChannelButton);
        controls.add(joinChannelButton);
        controls.add(leaveChannelButton);
        controls.add(viewLogsButton);
        
        JLabel title = new JLabel(
                "Welcome to CS401 Communication Systems Project! " +
                "Start by typing below. Click the X to log out.");
        
        chatBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String[] text = chatBox.getText().split(" ", 3);
            	
//            	sendMessage(text);
            	String channel;
            	if ("/dm".equals(text[0])) {
            		channel = "to " + text[1];
            		// Remove later
                	chatArea.append(getUser() + ": [" + channel + "] " + text[2] + "\n");
                	chatBox.setText(text[0] + " " + text[1] + " "); // clear input
            	}
            	else if ("/group".equals(text[0])) {
            		channel = "to " + text[1];
                	chatArea.append(getUser() + ": [" + channel + "] " + text[2] + "\n");
                	chatBox.setText(text[0] + " " + text[1] + " "); // clear input
            	}
            	else {
            		chatArea.append(getUser() + ": [all] " + chatBox.getText() + "\n");
                	chatBox.setText(""); // clear input
            	}
            }
        });
        
        // Sample text (remove later)
        chatArea.append(
        		"Commands available:\r\n"
        		+ "Direct Message: /dm person message_here \r\n"
        		+ "Group Message: /group cs401 message_here\n"
        		+ "****************************************************\n");
        chatArea.append("timmy123: [all] hello world\n");
        chatArea.append("bob456: [cs401] how is the weather?\n");
        chatArea.append("bob456: [to timmy123] how is life?\n");
        
        
        gui.add(title, BorderLayout.NORTH);
        gui.add(scroll); // default: center?
        gui.add(chatBox, BorderLayout.SOUTH);
        gui.add(controls, BorderLayout.EAST);
        gui.setBorder(new EmptyBorder(5,5,5,5));
        
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {//logout
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                doLogout();
            }
        });

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     }
}

// Ignore this..
class RequestFocusListener implements AncestorListener
{
    private boolean removeListener;

    public RequestFocusListener()
    {
        this(true);
    }

    public RequestFocusListener(boolean removeListener)
    {
        this.removeListener = removeListener;
    }

    @Override
    public void ancestorAdded(AncestorEvent e)
    {
        JComponent component = e.getComponent();
        component.requestFocusInWindow();

        if (removeListener)
            component.removeAncestorListener( this );
    }

    @Override
    public void ancestorMoved(AncestorEvent e) {}

    @Override
    public void ancestorRemoved(AncestorEvent e) {}
}