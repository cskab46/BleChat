package mobi.mateam.blechat.model.repository;

import java.util.List;
import mobi.mateam.blechat.model.pojo.Chat;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.model.pojo.User;
import rx.Observable;

public interface ChatRepository {

  //region Message methods
  Observable<List<Message>> getAllMessages();

  Observable<Message> getMessageById(String messageId);

  Observable<List<Message>> getMessagesByUserId(String userID);

  Observable<List<Message>> getMessagesByChatId(String chatId);

  Observable<Boolean> saveMessage(Message message);

  Observable<Boolean> removeMessage(Message message);

  Observable<Boolean> removeAllMessages();
  //endregion

  //region Chats methods
  Observable<List<Chat>> getAllChats();

  Observable<Chat> getChatById(String chatId);

  Observable<List<Chat>> getChatsByUserId(String userID);

  Observable<Boolean> saveChat(Chat chat);

  Observable<Boolean> removeChat(Chat chat);

  Observable<Boolean> removeAllChats();
  //endregion

  //region User methods
  Observable<List<User>> getAllUsers();

  Observable<User> getUserById(String id);

  Observable<Boolean> saveUser(User user);

  Observable<Boolean> removeUser(User user);

  Observable<Boolean> removeAllUsers();
  //endregion

}
