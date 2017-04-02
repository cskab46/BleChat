package mobi.mateam.blechat.model.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import mobi.mateam.blechat.model.db.DbHelper;
import mobi.mateam.blechat.model.pojo.Chat;
import mobi.mateam.blechat.model.pojo.Message;
import mobi.mateam.blechat.model.pojo.MessageBuilder;
import mobi.mateam.blechat.model.pojo.User;
import rx.Observable;

public class ChatRepositoryMockImpl implements ChatRepository {
  private final DbHelper dbHelper;
  ArrayList<Message> testMessages = new ArrayList<>();
  private final Message testMessage;

  public ChatRepositoryMockImpl(DbHelper dbHelper) {
    this.dbHelper = dbHelper;
    testMessage = new MessageBuilder()
        .setText("This is test testMessage from repository")
        .setTime(Calendar.getInstance()
        .getTimeInMillis())
        .setSenderName("Test name")
        .setIsIncoming(false).createMessage();

    testMessages.add(testMessage);
  }

  @Override public Observable<List<Message>> getAllMessages() {
    return Observable.just(testMessages);
  }

  @Override public Observable<Message> getMessageById(String messageId) {
    return Observable.just(testMessage);
  }

  @Override public Observable<List<Message>> getMessagesByUserId(String userID) {
    return Observable.just(testMessages);
  }

  @Override public Observable<List<Message>> getMessagesByChatId(String chatId) {
    return Observable.just(testMessages);
  }

  @Override public Observable<Boolean> saveMessage(Message message) {
    return Observable.just(testMessages.add(message));
  }

  @Override public Observable<Boolean> removeMessage(Message message) {
    return null;
  }

  @Override public Observable<Boolean> removeAllMessages() {
    return null;
  }

  @Override public Observable<List<Chat>> getAllChats() {
    return null;
  }

  @Override public Observable<Chat> getChatById(String chatId) {
    return null;
  }

  @Override public Observable<List<Chat>> getChatsByUserId(String userID) {
    return null;
  }

  @Override public Observable<Boolean> saveChat(Chat chat) {
    return null;
  }

  @Override public Observable<Boolean> removeChat(Chat chat) {
    return null;
  }

  @Override public Observable<Boolean> removeAllChats() {
    return null;
  }

  @Override public Observable<List<User>> getAllUsers() {
    return null;
  }

  @Override public Observable<User> getUserById(String id) {
    return null;
  }

  @Override public Observable<Boolean> saveUser(User user) {
    return null;
  }

  @Override public Observable<Boolean> removeUser(User user) {
    return null;
  }

  @Override public Observable<Boolean> removeAllUsers() {
    return null;
  }
}
