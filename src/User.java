import java.util.ArrayList;

public class User {
    private ArrayList<Chat> chats= new ArrayList<>();
    private String id;
    private String name;
    private String password;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    public void addGroup(Group group) {
        chats.add(group);
    }

    public void addChanel(Channel channel) {
        chats.add(channel);
    }

    public void addPrivetChat(PrivateChat pv) {
        chats.add(pv);
    }

    public Group getGroupById(String id) {
        for (Chat chat : chats) {
            if (chat.getClass().equals(Group.class) && chat.getId().equals(id))
                return (Group) chat;
        }
        return null;
    }

    public Channel getChanelById(String id) {
        for (Chat chat : chats) {
            if (chat.getClass().equals(Channel.class) && chat.getId().equals(id))
                return (Channel) chat;
        }
        return null;
    }

    public PrivateChat getPrivateChatById(String id) {
        for (Chat chat : chats) {
            if (chat.getClass().equals(PrivateChat.class) && chat.getId().equals(id))
                return (PrivateChat) chat;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }
}
