import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MainApp {

    public static Logger logger = LoggerFactory.getLogger(MainApp.class);



    public static void main(String[] args) {


//        int i = 0;
//        while(i!=1000){
//            new ThreadWord().start();
//            i++;
//        }

        logger.info("\n╭━━━━━━━━╮\n" +
                "┃┈╭╮┈┈╭╮┈┃\n" +
                "┃┈╰╯┈┈╰╯┈┃\n" +
                "┃┈╰━━━━╯┈┃\n" +
                "╰━━━━━━━━╯\n" +
                "EnglishBOT");


        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot=new Bot("Name", "token");
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
