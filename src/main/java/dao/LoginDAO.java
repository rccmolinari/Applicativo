package dao;

public interface LoginDAO {
    int login(String email, String password);
    boolean registrazione(String email, String password);
}
