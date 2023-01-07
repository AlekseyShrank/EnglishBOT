import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends ServiceCommand {

    public StartCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        //формируем имя пользователя - поскольку userName может быть не заполнено, для этого случая используем имя и фамилию пользователя
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());

        if(DBrequest.isNewUser(chat.getId())){
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Вижу, вы здесь первый раз!");
            Users.currentNewUsers.put(chat.getId(), new Users(chat.getId(), userName));

            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "С каким интервалом вы хотите получать новые слова:\n1-каждый час\n2-каждые 2 часа\n3-раз в 3 часа\nВведите только число 1,2 или 3");
        }else if(ThreadWord.currentTh.containsKey(chat.getId())){
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "процесс уже запущен! Все ок!\nЕсли хотите приостановить процесс введите /stop");
        }else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Let's go");
            ThreadWord th = new ThreadWord(userName, chat.getId(), absSender, DBrequest.getUserlvl(chat.getId()),DBrequest.getUserSettings(chat.getId()),DBrequest.getUserTimezone(chat.getId()));
            th.start();
            ThreadWord.currentTh.put(chat.getId(),th);
        }

    }



}


