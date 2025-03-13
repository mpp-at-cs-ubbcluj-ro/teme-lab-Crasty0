package ro.mpp2024.Domain;

public class Voluntar extends  Entity<Integer>{

    public String username;
    public String password;

    public Voluntar(Integer id, String username, String password){
        super(id);
        this.password = password;
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    @Override
    public String toString() {
        return "Voluntar{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
