package Entity;

/**
 * @author lxxxxxxy
 * @time 2019/07/02 00:29
 */
public class UserInfo {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UserInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
