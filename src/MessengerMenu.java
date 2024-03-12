import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessengerMenu {
    private Chat chat;
    private User currentUser = Messenger.getCurrentUser();

    public void run(Scanner scanner) {
        String command = scanner.nextLine();
        while (!command.equals("logout")) {
            //System.out.println(command);
            if (command.equals("show all channels")) {
                System.out.print(showAllChannels());
            } else if (command.startsWith("create new channel")) {
                Matcher matcherCreateNewChannel = Commands.getMatcher(command, Commands.REGEX5);
                System.out.println(createChannel(matcherCreateNewChannel));
            } else if (command.startsWith("join channel")) {
                Matcher matcherJoinChannel = Commands.getMatcher(command, Commands.REGEX6);
                System.out.println(joinChannel(matcherJoinChannel));
            } else if (command.equals("show my chats")) {
                System.out.print(showChats());
            } else if (command.startsWith("create new group")) {
                Matcher matcherCreateNewGroup = Commands.getMatcher(command, Commands.REGEX7);
                System.out.println(createGroup(matcherCreateNewGroup));
            } else if (command.startsWith("start a new private chat with")) {
                Matcher matcherStartNewPv = Commands.getMatcher(command, Commands.REGEX8);
                System.out.println(createPrivateChat(matcherStartNewPv));
            } else if (command.startsWith("enter")) {
                Matcher matcherEnterChat = Commands.getMatcher(command, Commands.REGEX9);
                String stringMatcherEnterChat = enterChat(matcherEnterChat, command);
                if (stringMatcherEnterChat.equals("You have successfully entered the chat!")) {
                    System.out.println(stringMatcherEnterChat);
                    ChatMenu chatMenu = new ChatMenu();
                    chatMenu.run(scanner, chat);
                } else System.out.println(stringMatcherEnterChat);
            } else System.out.println("Invalid command!");
            command = scanner.nextLine();
            //command=command.trim();
        }
        System.out.println("Logged out");
    }

    private String showAllChannels() {
        int counter = 1;
        String stringToShowAllChannels = "All channels:\n";
        for (Chat chat : Messenger.getChannels()) {
            //if (chat.getMembers().contains(currentUser)) {
            stringToShowAllChannels = stringToShowAllChannels + counter + ". " + chat.getName() + ", id: " + chat.getId() + ", members: " + chat.getMembers().size() + "\n";
            counter++;
            //}
        }
        return stringToShowAllChannels;
    }

    private String showChats() {
        String stringShowChats = "Chats:\n";
        int counter = 1;
        for (int i = currentUser.getChats().size(); i > 0; i--) {
            Chat chat = currentUser.getChats().get(i - 1);
            if (chat.getClass().equals(Channel.class)) {
                stringShowChats = stringShowChats + counter + ". " + chat.getName() + ", id: " + chat.getId() + ", channel\n";
                counter++;
            } else if (chat.getClass().equals(Group.class)) {
                stringShowChats = stringShowChats + counter + ". " + chat.getName() + ", id: " + chat.getId() + ", group\n";
                counter++;
            } else if (chat.getClass().equals(PrivateChat.class)) {
                stringShowChats = stringShowChats + counter + ". " + chat.getName() + ", id: " + chat.getId() + ", private chat\n";
                counter++;
            }
        }
        return stringShowChats;
    }

    private String enterChat(Matcher matcher, String command) {
        Matcher matcherEnterPv = Commands.getMatcher(command, Commands.REGEX12);
        if (matcher.find()) {
            String stringChatType = matcher.group("EnterChatType");
            String stringChatId = matcher.group("EnterChatId");
            if(!stringChatType.equals("group") && !stringChatType.equals("channel")) return "Invalid command!";
            for (Chat chat : currentUser.getChats()) {
                if (stringChatType.equals("channel")) {
                    if (chat.getClass().equals(Channel.class) && chat.getId().equals(stringChatId)) {
                        this.chat = chat;
                        return "You have successfully entered the chat!";
                    }
                } else if (stringChatType.equals("group")) {
                    if (chat.getClass().equals(Group.class) && chat.getId().equals(stringChatId)) {
                        this.chat = chat;
                        return "You have successfully entered the chat!";
                    }
                }
            }
            return "You have no " + stringChatType + " with this id!";
        } else if (matcherEnterPv.find()) {
            String stringChatId = matcherEnterPv.group("EnterChatId");
            for (Chat chat : currentUser.getChats()) {
                if (chat.getClass().equals(PrivateChat.class) && chat.getId().equals(stringChatId)) {
                    this.chat = chat;
                    return "You have successfully entered the chat!";
                }
            }
            return "You have no " + "private chat" + " with this id!";
        }
        return "Invalid command!";
    }

    private String createChannel(Matcher matcher) {
        if (matcher.find()) {
            String channelName = matcher.group("ChannelName");
            if (!Commands.getMatcher(channelName, Commands.REGEX2).find()) return "Channel name's format is invalid!";
            String channelId = matcher.group("ChannelId");
            if (Messenger.getChannelById(channelId) != null) return "A channel with this id already exists!";
            Channel channel = new Channel(currentUser, channelId, channelName);
            Messenger.addChannel(channel);
            Messenger.getChannelById(channelId).addMember(currentUser);
            currentUser.addChanel(Messenger.getChannelById(channelId));
            return "Channel " + channelName + " has been created successfully!";
        } else return "Invalid command!";
    }

    private String createGroup(Matcher matcher) {
        if (matcher.find()) {
            String groupName = matcher.group("GroupName");
            if (!Commands.getMatcher(groupName, Commands.REGEX2).find()) return "Group name's format is invalid!";
            String groupId = matcher.group("GroupId");
            if (Messenger.getGroupById(groupId) != null) return "A group with this id already exists!";
            Group group = new Group(currentUser, groupId, groupName);
            Messenger.addGroup(group);
            currentUser.addGroup(Messenger.getGroupById(groupId));
            Messenger.getGroupById(groupId).addMember(currentUser);
            return "Group " + groupName + " has been created successfully!";
        } else return "Invalid command!";
    }

    private String createPrivateChat(Matcher matcher) {
        if (matcher.find()) {
            String pvId = matcher.group("PvId");
            if (currentUser.getPrivateChatById(pvId) != null) return "You already have a private chat with this user!";
            for (User user : Messenger.getUsers()) {
                if (pvId.equals(user.getId())) {
                    PrivateChat privateChatForCurrentUser = new PrivateChat(currentUser, user.getId(), user.getName());
                    currentUser.addPrivetChat(privateChatForCurrentUser);
                    privateChatForCurrentUser.addMember(currentUser);
                    if (!currentUser.equals(user)) {
                        PrivateChat privateChatForUser = new PrivateChat(currentUser, currentUser.getId(), currentUser.getName());
                        user.addPrivetChat(privateChatForUser);
                        privateChatForUser.addMember(user);
                    }
                    return "Private chat with " + user.getName() + " has been started successfully!";
                }
            }
            return "No user with this id exists!";
        } else return "Invalid command!";
    }

    private String joinChannel(Matcher matcher) {
        if (matcher.find()) {
            String channelId = matcher.group("JoinChannel");
            ArrayList<Chat> chats = currentUser.getChats();
            if (Messenger.getChannelById(channelId) != null) {
                for (User user : Messenger.getChannelById(channelId).getMembers()) {
                    if (currentUser.getId().equals(user.getId())) return "You're already a member of this channel!";
                }
                Messenger.getChannelById(channelId).addMember(currentUser);
                currentUser.addChat(Messenger.getChannelById(channelId));
                return "You have successfully joined the channel!";
            }
            return "No channel with this id exists!";
        } else return "Invalid command!";
    }
}
