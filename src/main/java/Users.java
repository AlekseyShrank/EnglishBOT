import java.util.HashMap;

public class Users {// Ты думал сдесь что-то будет!!!

    public static HashMap<Long, Users> currentNewUsers =new HashMap<>();

    Long chatid;
    String name;
    int timezone;
    int lvl;
    int settings;

    Users(Long chatid, String name){
        this.chatid = chatid;
        this.name = name;
        this.lvl = 0;
        this.settings = 0;
        this.timezone = 29;
    }

}
