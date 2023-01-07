import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StopCommand extends ServiceCommand {

    public StopCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        //формируем имя пользователя - поскольку userName может быть не заполнено, для этого случая используем имя и фамилию пользователя
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        //обращаемся к методу суперкласса для отправки пользователю ответа

        if(ThreadWord.currentTh.containsKey(chat.getId())){
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Вы можете продолжить в любое время");
            ThreadWord.currentTh.get(chat.getId()).isLive=false;
            ThreadWord.currentTh.remove(chat.getId());

        }else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Да мы вроде и так ничего не делаем.\nА если хотите начать введите /start");
        }

    }


}