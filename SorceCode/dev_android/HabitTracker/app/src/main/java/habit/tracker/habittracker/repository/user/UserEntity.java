package habit.tracker.habittracker.repository.user;

import habit.tracker.habittracker.api.model.user.User;

public class UserEntity {
    private String userId;
    private String username;
    private String email;
    private String phone;
    private String gender;
    private String dateOfBirth;
    private String password;
    private String realName;
    private String avatar;
    private String description;
    private String createdDate;
    private String lastLoginTime;
    private String continueUsingCount;
    private String currentContinueUsingCount;
    private String bestContinueUsingCount;
    private String userScore;
    private boolean isUpdate = false;

    public UserEntity(String userId, String username, String email, String phone, String gender, String dateOfBirth,
                      String password, String realName, String avatar, String description,
                      String lastLoginTime, String continueUsingDate, String currentContinueUsingCount, String bestContinueUsingCount, String userScore) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.realName = realName;
        this.avatar = avatar;
        this.description = description;
        this.lastLoginTime = lastLoginTime;
        this.continueUsingCount = continueUsingDate;
        this.currentContinueUsingCount = currentContinueUsingCount;
        this.bestContinueUsingCount = bestContinueUsingCount;
        this.userScore = userScore;
    }

    public UserEntity(){
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public String getRealName() {
        return realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public String getUserScore() {
        return userScore;
    }

    public String getContinueUsingCount() {
        return continueUsingCount;
    }

    public String getCurrentContinueUsingCount() {
        return currentContinueUsingCount;
    }

    public String getBestContinueUsingCount() {
        return bestContinueUsingCount;
    }

    public boolean isUpdate() {
        return isUpdate;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setDescription(String userDescription) {
        this.description = userDescription;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public void setContinueUsingCount(String continueUsingCount) {
        this.continueUsingCount = continueUsingCount;
    }

    public void setCurrentContinueUsingCount(String currentContinueUsingCount) {
        this.currentContinueUsingCount = currentContinueUsingCount;
    }

    public void setBestContinueUsingCount(String bestContinueUsingCount) {
        this.bestContinueUsingCount = bestContinueUsingCount;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public User toModel() {
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(gender);
        user.setEmail(email);
        user.setDateOfBirth(dateOfBirth);
        user.setAvatar(avatar);
        user.setRealName(realName);
        user.setDescription(description);
        user.setCreatedDate(createdDate);
        user.setLastLoginTime(lastLoginTime);
        user.setContinueUsingCount(continueUsingCount);
        user.setCurrentContinueUsingCount(currentContinueUsingCount);
        user.setBestContinueUsingCount(bestContinueUsingCount);
        user.setUserScore(userScore);
        return user;
    }
}
