package dao;

public interface LoginDAO {
    public int login(String email, String password);
    public boolean registrazione(String email, String password);
}
