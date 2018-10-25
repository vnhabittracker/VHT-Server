package habit.tracker.habittracker.repository.user;

public interface UserDao {
    UserEntity fetchUser();
    UserEntity getUser(int userId);
    UserEntity getUser(String username, String password);
    boolean saveUser(UserEntity userEntity);
    boolean deleteUser(String userId);
    boolean update(UserEntity userEntity);
}
