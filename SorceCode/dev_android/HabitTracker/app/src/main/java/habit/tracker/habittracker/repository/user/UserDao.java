package habit.tracker.habittracker.repository.user;

import java.util.List;

public interface UserDao {
    List<UserEntity> fetchUser();
    UserEntity getUser(String userId);
    UserEntity getUser(String username, String password);
    boolean saveUser(UserEntity userEntity);
    boolean deleteUser(String userId);
    boolean update(UserEntity userEntity);
}
