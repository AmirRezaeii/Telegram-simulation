import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

public class Chat {
    private ArrayList<User> members= new ArrayList<>();
    private ArrayList<Message> messages= new ArrayList<>();
    private User owner;
    private String id;
    private String name;

    public Chat(User admin, String id, String name) {
        owner = admin;
        this.id = id;
        this.name = name;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public String getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }
}
