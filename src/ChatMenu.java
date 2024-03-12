import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;

public class ChatMenu {
    private Chat chat;
    private User currentUser = Messenger.getCurrentUser();

    public void run(Scanner scanner, Chat chat) {
        this.chat = chat;
        String command = scanner.nextLine();
        while (!command.equals("back")) {
            //System.out.println(command);
            if (command.startsWith("send a message")) {
                Matcher matcherSendMessage = Commands.getMatcher(command, Commands.REGEX10);
                System.out.println(sendMessage(matcherSendMessage));
            } else if (command.startsWith("add member")) {
                Matcher matcherAddMember = Commands.getMatcher(command, Commands.REGEX11);
                System.out.println(addMember(matcherAddMember));
            } else if (command.equals("show all messages")) {
                System.out.print(showMessages());
            } else if (command.equals("show all members")) {
                System.out.print(showMembers());
            } else System.out.println("Invalid command!");
            command = scanner.nextLine();
            //command=command.trim();
        }
    }

    private String showMessages() {
        String stringShowMessage = "Messages:\n";
        for (Message message : chat.getMessages()) {
            stringShowMessage = stringShowMessage + message.getOwner().getName() + "(" + message.getOwner().getId() + "): \"" + message.getContent() + "\"\n";
        }
        return stringShowMessage;
    }

    private String showMembers() {
        if (chat.getClass().equals(PrivateChat.class)) return "Invalid command!\n";
        String stringShowMember = "Members:\n";
        for (User user : chat.getMembers()) {
            if (user.equals(chat.getOwner()))
                stringShowMember = stringShowMember + "name: " + user.getName() + ", id: " + user.getId() + " *owner\n";
            else stringShowMember = stringShowMember + "name: " + user.getName() + ", id: " + user.getId() + "\n";
        }
        return stringShowMember;
    }

    private String addMember(Matcher matcher) {
        if (matcher.find()) {
            String addMemberId = matcher.group("AddMember");
            if (chat.getClass().equals(PrivateChat.class)) return "Invalid command!";
            if (!chat.getOwner().equals(currentUser)) return "You don't have access to add a member!";
            if (Messenger.getMemberById(addMemberId) == null) return "No user with this id exists!";
            for (User user : chat.getMembers()) {
                if (user.getId().equals(addMemberId)) return "This user is already in the chat!";
            }
            if (chat.getClass().equals(Channel.class)) {
                Messenger.getChannelById(chat.getId()).addMember(Messenger.getMemberById(addMemberId));
                Messenger.getMemberById(addMemberId).addChat(Messenger.getChannelById(chat.getId()));
            } else if (chat.getClass().equals(Group.class)) {
                Messenger.getGroupById(chat.getId()).addMember(Messenger.getMemberById(addMemberId));
                Messenger.getMemberById(addMemberId).addChat(Messenger.getGroupById(chat.getId()));
            }

            Message message = new Message(currentUser, Messenger.getMemberById(addMemberId).getName() + " has been added to the group!");
            //for (User user : chat.getMembers()) {
            if (chat.getClass().equals(Group.class)) {
                currentUser.getGroupById(chat.getId()).addMessage(message);
                Chat newSortOfChats = currentUser.getGroupById(chat.getId());
                currentUser.getChats().remove(newSortOfChats);
                currentUser.addGroup((Group) newSortOfChats);
            }
            for(User user : chat.getMembers()){
                if (chat.getClass().equals(Group.class)) {
                    Chat newSortOfChats = currentUser.getGroupById(chat.getId());
                    user.getChats().remove(newSortOfChats);
                    user.addGroup((Group) newSortOfChats);
                }
            }
            //}
            return "User has been added successfully!";
        } else return "Invalid command!";
    }

    private String sendMessage(Matcher matcher) {
        if (matcher.find()) {
            String sendMessage = matcher.group("Message");
            if (chat.getClass().equals(Channel.class) && !currentUser.getId().equals(chat.getOwner().getId()))
                return "You don't have access to send a message!";
            Message message = new Message(currentUser, sendMessage);
            if (chat.getClass().equals(Channel.class)) {



                
                for (User user : chat.getMembers()){
                    if (chat.getClass().equals(Channel.class)) {
                        Chat newSortOfChats = currentUser.getChanelById(chat.getId());
                        user.getChats().remove(newSortOfChats);
                        user.addChanel((Channel) newSortOfChats);
                    }

                }
            } else if (chat.getClass().equals(Group.class)) {
                currentUser.getGroupById(chat.getId()).addMessage(message);
                for (User user : chat.getMembers()){
                        if (chat.getClass().equals(Group.class)) {
                            Chat newSortOfChats = currentUser.getGroupById(chat.getId());
                            user.getChats().remove(newSortOfChats);
                            user.addGroup((Group) newSortOfChats);
                        }

                }
            } else {
                Chat newSortOfChats = currentUser.getPrivateChatById(chat.getId());
                Chat newSortOfChats1 = Messenger.getMemberById(chat.getId()).getPrivateChatById(currentUser.getId());
                chat.addMessage(message);
                currentUser.getChats().remove(newSortOfChats);
                currentUser.addPrivetChat((PrivateChat) newSortOfChats);
                if (!chat.getId().equals(currentUser.getId())) {
                    Messenger.getMemberById(chat.getId()).getPrivateChatById(currentUser.getId()).addMessage(message);
                    Messenger.getMemberById(chat.getId()).getChats().remove(newSortOfChats1);
                    Messenger.getMemberById(chat.getId()).addPrivetChat((PrivateChat) newSortOfChats1);
                }
            }
            return "Message has been sent successfully!";
        } else return "Invalid command!";
    }
}
