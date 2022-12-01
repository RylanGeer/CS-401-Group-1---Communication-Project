import java.util.List;

public class Channel {
    /**
     *  This integer holds the ID of the channel
     * 
     * @see setID
     * @see getID
     */
    int id;

    /**
     *  Name of the channel
     * 
     * @see setName
     * @see getName
     */
    String name;

    /**
     * This lists holds all the messages from this channel including the hidden messages
     * 
     * @see addMessage
     */
    List<Message> messages;

    /**
     * Channel hidden from regular users?
     * 
     * @see getHiddenStatus
     * @see hideChannel
     */
    boolean isHidden;

    /**
     *  Holds all the logs for the IT user to read
     *  @see isHidden
     *  @see hideChannels
     */
    Logs logs;

    /**
     * @return ID of the channel
     * 
     * @see setID
     * 
     * @author Aftersol
     */
    int getID() { return this.id; }

    /**
     * @return name of the channel
     * 
     * @see setName
     * 
     * @author Aftersol
     */
    String getName() { return this.name; }

    /**
     * @return is the channel hidden?
     * 
     * @see hideChannel
     * 
     * @author Aftersol
     */
    boolean isHidden() { return this.isHidden; }

    /**
     * Sets the ID of the channel
     * 
     * @param id
     * 
     * @see isHidden
     * @see getID
     * 
     * @author Aftersol
     */
    private void setID(int id) { this.id = id; }

    /**
     * Sets the name of the channel
     * 
     * @param name
     * 
     * @see getID
     * 
     * @author Aftersol
     */
    void setName(String name) { this.name = name; }

    /**
     * Hides the channel from regular users but not the IT
     * 
     * @see getName
     * 
     * @author Aftersol
     */
    void showChannel() { 
        logs.addLog(getName() + " has been unhidden");
        this.isHidden = false; 
    }

    /**
     * Hides the channel from regular users but not the IT
     * 
     * @see getName
     * 
     * @author Aftersol
     */
    void hideChannel() { 
        logs.addLog(getName() + " has been hidden");
        this.isHidden = true; 
    }

    /**
     * Puts the message into the channel
     * @param msg
     * 
     * @see getHiddenStatus
     * 
     * @author Aftersol
     */
    void addMessage(Message msg)
    {
        messages.add(msg);
    }

    public List<Message> getMessages(int x, int y)
    {
        return messages.subList(x, y);
    }

    public List<Message> getLastMessages(int n)
    {
        return getMessages(messages.size() - n, messages.size());
    }

    public List<String> getLogs()
    {
        return logs.getLogs();
    }

    public List<String> getFirstLogs(int n)
    {
        return logs.getFirstLogs(n);
    }

    public List<String> getLogs(int x, int y)
    {
        return logs.getLogs(x, y);
    }

    public List<String> getLastLogs(int n)
    {
        return logs.getLastLogs(n);
    }

    public void logMessage(String log)
    {
        logs.addLog(log);
    }

    /**
     * Constructor for the channel
     * 
     * @param id
     * @param name
     * 
     * @author Aftersol
     */
    Channel(int id, String name)
    {
        setID(id);
        setName(name);
        this.isHidden = false;
        this.logs = new Logs();
    }
}