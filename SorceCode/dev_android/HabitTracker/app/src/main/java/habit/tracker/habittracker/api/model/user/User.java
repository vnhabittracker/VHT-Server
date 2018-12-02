package habit.tracker.habittracker.api.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import habit.tracker.habittracker.repository.user.UserEntity;

public class User {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("real_name")
    @Expose
    private String realName;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("user_description")
    @Expose
    private String description;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("last_login_time")
    @Expose
    private String lastLoginTime;
    @SerializedName("continue_using_count")
    @Expose
    private String continueUsingCount;
    @SerializedName("current_continue_using_count")
    @Expose
    private String currentContinueUsingCount;
    @SerializedName("best_continue_using_count")
    @Expose
    private String bestContinueUsingCount;
    @SerializedName("user_score")
    @Expose
    private String userScore;

    private boolean isUpdate = false;

    public User() {}

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
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

    public String getContinueUsingCount() {
        return continueUsingCount;
    }

    public String getCurrentContinueUsingCount() {
        return currentContinueUsingCount;
    }

    public String getBestContinueUsingCount() {
        return bestContinueUsingCount;
    }

    public String getUserScore() {
        return userScore;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public void setContinueUsingCount(String continueUsingDate) {
        this.continueUsingCount = continueUsingDate;
    }

    public void setCurrentContinueUsingCount(String currentContinueUsingCount) {
        this.currentContinueUsingCount = currentContinueUsingCount;
    }

    public void setBestContinueUsingCount(String bestContinueUsingCount) {
        this.bestContinueUsingCount = bestContinueUsingCount;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();
        entity.setUserId(userId);
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setGender(gender);
        entity.setEmail(email);
        entity.setDateOfBirth(dateOfBirth);
        entity.setAvatar(avatar);
        entity.setRealName(realName);
        entity.setDescription(description);
        entity.setCreatedDate(createdDate);
        entity.setLastLoginTime(lastLoginTime);
        entity.setContinueUsingCount(continueUsingCount);
        entity.setCurrentContinueUsingCount(currentContinueUsingCount);
        entity.setBestContinueUsingCount(bestContinueUsingCount);
        entity.setUserScore(userScore);
        return entity;
    }
}
