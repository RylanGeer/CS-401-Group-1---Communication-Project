import java.util.ArrayList;
import java.util.List;

public class Logs {
    private List<String> logs;

    public List<String> getLogs()
    {
        return logs;
    }

    public List<String> getFirstLogs(int n)
    {
        return logs.subList(logs.size(), logs.size() + n);
    }

    public List<String> getLogs(int x, int y)
    {
        return logs.subList(x, y);
    }

    public List<String> getLastLogs(int n)
    {
        return logs.subList(logs.size() - n, logs.size());
    }

    public void addLog(String log)
    {
        logs.add(log);
    }

    Logs()
    {
        this.logs = new ArrayList<String>();
    }
}
