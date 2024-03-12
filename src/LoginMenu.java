import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu {
    public void run(Scanner scanner) {
        String command = scanner.nextLine();
        while (!command.equals("exit")) {
            //System.out.println(command);
            if (command.startsWith("login")) {
                Matcher matcherLogin = Commands.getMatcher(command, Commands.REGEX4);
                String stringLogin = login(matcherLogin);
                if (stringLogin.equals("User successfully logged in!")) {
                    System.out.println(stringLogin);
                    MessengerMenu messengerMenu= new MessengerMenu();
                    messengerMenu.run(scanner);
                } else System.out.println(stringLogin);
            } else if (command.startsWith("register")) {
                Matcher matcherRegister = Commands.getMatcher(command, Commands.REGEX1);
                System.out.println(register(matcherRegister));
            }else System.out.println("Invalid command!");
            command= scanner.nextLine();
            //command=command.trim();
        }
    }

    private String login(Matcher matcher) {
        if (matcher.find()) {
            String loginId = matcher.group("LoginId");
            String loginPassword = matcher.group("LoginPassword");
            for (User checkForExistingId : Messenger.getUsers()) {
                if (loginId.equals(checkForExistingId.getId())) {
                    if (!loginPassword.equals(checkForExistingId.getPassword())) return "Incorrect password!";
                    else{
                        User user = Messenger.getMemberById(loginId);
                        Messenger.setCurrentUser(user);
                        return "User successfully logged in!";
                    }
                }
            }
            return "No user with this id exists!";
        } else return "Invalid command!";
    }

    private String register(Matcher matcher) {
        if (matcher.find()) {
            String registerUsername = matcher.group("RegisterUsername");
            if (!Commands.getMatcher(registerUsername, Commands.REGEX2).find()) return "Username's format is invalid!";
            String registerPassword = matcher.group("RegisterPassword");
            if (!Commands.getMatcher(registerPassword, Commands.REGEX3).find()) return "Password is weak!";
            String registerId = matcher.group("RegisterId");
            for (User checkForExistingId : Messenger.getUsers()) {
                if (registerId.equals(checkForExistingId.getId())) return "A user with this ID already exists!";
            }
            User user = new User(registerId, registerUsername, registerPassword);
            Messenger.addUser(user);
            return "User has been created successfully!";
        } else return "Invalid command!";
    }
}
