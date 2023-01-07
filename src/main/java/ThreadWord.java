import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class ThreadWord extends Thread{

    public static HashMap<Long, ThreadWord> currentTh =new HashMap<>();

    int lvl;
    public Long chatId;
    public AbsSender absSender;
    public boolean isLive;
    public int settings;
    public int timezone;

    ThreadWord(){
        this.isLive=true;
    }//Был создан для тестов

    ThreadWord(String name, Long chatId, AbsSender absSender, int lvl, int settings, int timezone){
        super(name);
        this.chatId = chatId;
        this.absSender = absSender;
        this.isLive=true;
        this.lvl = lvl;
        this.settings = settings;
        this.timezone = timezone;
    }

    public void run(){
//        String msg = DBrequest.GetWords(1);
//        System.out.println(msg);
        while (isLive){//основной поток работы с пользователем. Для каждого пользователя свой поток.
            Date now = new Date();
            SimpleDateFormat hour = new SimpleDateFormat("HH");
            System.out.println(Integer.parseInt(hour.format(now)));

            if(Integer.parseInt(hour.format(now))+timezone-3 <= 22 && Integer.parseInt(hour.format(now))+timezone-3 >= 10){
                SendMessage message = new SendMessage();
                //включаем поддержку режима разметки, чтобы управлять отображением текста и добавлять эмодзи
                message.enableMarkdown(true);
                message.setChatId(chatId.toString());
                String msg = DBrequest.getWords(lvl);//запрос фразы из бд



                message.setText(msg);

                try {
                    absSender.execute(message);
                    lvl++;
                    //обновляем lvl в бд
                    DBrequest.setLvl(chatId, lvl);

                } catch (TelegramApiException e) {
                    MainApp.logger.info(e.getMessage());
                }

                switch (settings){
                    case (1):
                        try {
                            Thread.sleep(3600000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case (2):
                        try {
                            Thread.sleep(3600000*2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case (3):
                        try {
                            Thread.sleep(3600000*3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        MainApp.logger.info("User "+chatId+" have not settings.");
                        try {
                            Thread.sleep(3600000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }else {
                try {
                    Thread.sleep(3600000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
