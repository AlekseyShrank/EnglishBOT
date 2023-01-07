import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Random;

public final class Bot extends TelegramLongPollingCommandBot {

    private final String BOT_NAME;
    private final String BOT_TOKEN;

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;

        //регистрируем команды
        register(new StartCommand("start", "Старт"));
        register(new StopCommand("stop", "Стоп"));

    }



    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        //Обработка сообщений не являющимися командой

        Random rn = new Random();
        //сообщения для пользователя при ошибках
        String[] err={"Кажется, кто-то из нас допустил ошибку. Введите значение ещё раз!",
                "Ох... это не то, что я от вас ожидал.\nВведите значение ещё раз!",
                "Допускать ошибки - это нормально...\nЯ так думал...\nДо этого момента...\nВведите значение ещё раз!",
                "Мне легче признать вину за ошибку и попросить вас ввести значение ещё раз чем упрашивать разработчика стереть мой программный код..."};

        Message msg = update.getMessage();
        Long chatId = msg.getChatId();

        //Обработчик новых пользователей (все новые пользователи попадают в HashMap currentNewUsers)
        if(Users.currentNewUsers.containsKey(chatId)){
            if (Users.currentNewUsers.get(chatId).settings == 0){
                int sett = -1;
                try{
                    sett = Integer.parseInt(msg.getText());
                }catch (NumberFormatException e){
                    MainApp.logger.info("User "+chatId + " trying to install settings: "+msg.getText());
                }

                if(sett == 1 || sett == 2 || sett == 3){
                    Users.currentNewUsers.get(chatId).settings = sett;
                    setAnswer(chatId, "Отлично!\nТеперь мне нужен ваш часовой пояс в формате +5 или -5.\nНапример если вы живете в Москве отправте +3\nВы же не хотите получать сообщения в 4:20... верно...?");
                }else {
                    setAnswer(chatId, err[rn.nextInt(4)]);
                }
            } else if(Users.currentNewUsers.get(chatId).timezone == 29){
                int timez = 29;
                try{
                    timez = Integer.parseInt(msg.getText());
                }catch (NumberFormatException e){
                    MainApp.logger.info("User "+chatId + " trying to install timezone: "+msg.getText());
                }

                if(timez >= -13 && timez <= 13){
                    Users.currentNewUsers.get(chatId).timezone = timez;
                    DBrequest.setNewUser(Users.currentNewUsers.get(chatId)); //Запись пользователя в БД
                    Users.currentNewUsers.remove(chatId);
                    setAnswer(chatId, "Отлично! Все готово! Отправте /start и мы начнём");
                }else {
                    setAnswer(chatId, err[rn.nextInt(4)]);
                }
            }
        }else { //Ответ на любой бред от пользователя
            String userName = getUserName(msg);
            String[] ansarr={"Да-Да Я",
                    "PAJILOY рэбит",
                    "Вот пажалста...\nВот палубуйтис...",
                    "34520 \nУ АУ А АУАУ АУ АУА УАУ А УААУ А У АУАУ ААУ АУАУ АУА А УАУАУАУА АУ"};

            String answer = ansarr[rn.nextInt(4)];
            setAnswer(chatId, answer);
        }
    }

    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    public void setAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            //логируем сбой Telegram Bot API, используя userName
            MainApp.logger.info(e.getMessage());
        }
    }
}
