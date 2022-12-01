public class ITUser extends User{
    
    
    /**
     * view the logs for the channel
     * not yet implemented
     * 
     * @param channelID
     */
    void viewLogs(int channelID)
    {

    }

    
    /**
     * Hides the specified message from the user
     * 
     * @param msg
     */
    void hideMessage(Message msg)
    {
        //msg.hideMessage();
    }

    
    /**
     * Hides the specified channel from the user
     * 
     * @param channel
     */
    void hideChannel(Channel channel)
    {
        channel.hideChannel();
    }

    ITUser(int id, String username, String password)
    {
        super(id, username, password);
    }
}
