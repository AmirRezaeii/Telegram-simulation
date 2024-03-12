import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    REGEX1("^register\\si\\s(?<RegisterId>\\S+)\\su\\s(?<RegisterUsername>\\S+)\\sp\\s(?<RegisterPassword>\\S+)$"),
    REGEX2("^[a-zA-Z0-9_]+$"),
    REGEX3("^(?=.*\\d)(?=.*[*.!@$÷^&(){}:;<>,?\\/~_+\\-=|])(?=.*[a-z])(?=.*[A-Z]).{8,32}$"),
    REGEX4("^login\\si\\s(?<LoginId>\\S+)\\sp\\s(?<LoginPassword>\\S+)$"),
    REGEX5("^create\\snew\\schannel\\si\\s(?<ChannelId>\\S+)\\sn\\s(?<ChannelName>\\S+)$"),
    REGEX6("^join\\schannel\\si\\s(?<JoinChannel>\\S+)$"),
    REGEX7("^create\\snew\\sgroup\\si\\s(?<GroupId>\\S+)\\sn\\s(?<GroupName>\\S+)$"),
    REGEX8("^start\\sa\\snew\\sprivate\\schat\\swith\\si\\s(?<PvId>\\S+)$"),
    REGEX9("^enter\\s(?<EnterChatType>\\S+)\\si\\s(?<EnterChatId>\\S+)$"),
    REGEX10("^send\\sa\\smessage\\sc\\s(?<Message>.+)$"),
    REGEX11("^add\\smember\\si\\s(?<AddMember>\\S+)$"),
    REGEX12("^enter\\sprivate\\schat\\si\\s(?<EnterChatId>\\S+)$");
    private String regex;

    private Commands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, Commands command) {
        Pattern pattern = Pattern.compile(command.regex);
        return pattern.matcher(input);
    }
}