package ro.mpp2024.Domain;

public class Entity <Type>{
    private Type id;

    public Entity(Type id){
        this.id = id;
    }

    public Type getId(){
        return id;
    }
}
