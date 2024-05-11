package net.syphlex.skyblock.manager.leaderboard;

import lombok.Getter;

import java.util.*;

@Getter
public class Leaderboard {


    private final String name;
    private final ArrayList<LeaderData> list = new ArrayList<>();

    public Leaderboard(String name){
        this.name = name;
    }

    public void calc(){
        this.list.sort(Comparator.comparing(LeaderData::getValue));
        Collections.reverse(this.list);
    }

    public void insert(String key, int value){
        if (get(key) != -1) {
            for (LeaderData data : this.list) {
                if (data.getKey() instanceof String && ((String) data.getKey()).equalsIgnoreCase(key)) {
                    data.setValue(value);
                    break;
                }
            }
            return;
        }
        this.list.add(new LeaderData(key, value));
    }

    public void insert(UUID key, int value){
        if (get(key) != -1) {
            for (LeaderData data : this.list) {
                if (data.getKey() instanceof UUID && ((UUID) data.getKey()).equals(key)) {
                    data.setValue(value);
                    break;
                }
            }
            return;
        }
        this.list.add(new LeaderData(key, value));
    }

    public int get(String key){
        for (LeaderData data : this.list) {
            if (data.getKey() instanceof String && ((String) data.getKey()).equalsIgnoreCase(key))
                return data.getValue();
        }
        return -1;
    }

    public int get(UUID key){
        for (LeaderData data : this.list) {
            if (data.getKey() instanceof UUID && ((UUID) data.getKey()).equals(key))
                return data.getValue();
        }
        return -1;
    }

    public LeaderData get(int index){
        return this.list.get(index);
    }

    public int getSize(){
        return this.list.size();
    }
}
