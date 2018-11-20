package habit.tracker.habittracker.repository.user;

public class UserEntity {
    private String userId;
    private String username;
    private String email;
    private String phone;
    private String gender;
    private String dateOfBirth;
    private String password;
    private String userIcon;
    private String avatar;
    private String userDescription;
    private String createdDate;
    private String lastLoginTime;
    private String continueUsingCount;
    private String currentContinueUsingCount;
    private String bestContinueUsingCount;
    private String userScore;

    public UserEntity(String userId, String username, String email, String phone, String gender, String dateOfBirth,
                      String password, String userIcon, String avatar, String userDescription,
                      String lastLoginTime, String continueUsingDate, String currentContinueUsingCount, String bestContinueUsingCount, String userScore) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.userIcon = userIcon;
        this.avatar = avatar;
        this.userDescription = userDescription;
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

    public String getUserIcon() {
        return userIcon;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUserDescription() {
        return userDescription;
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

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
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
}
