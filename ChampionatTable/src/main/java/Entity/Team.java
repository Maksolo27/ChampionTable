package Entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.omg.CORBA.TRANSIENT;

import javax.persistence.*;
import java.lang.reflect.Modifier;

/**
 * Created by maxim on 07.04.2021.
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Data
@NoArgsConstructor
public class Team implements Comparable<Team> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private String name;
    private int score;
    private int goalScored;
    private int goalConceded;


    public Team(String name) {
        this.name = name;
    }

    public Team(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        return name != null ? name.equals(team.name) : team.name == null;   //Проверка только по названиях команд
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public int compareTo(Team team) {
        if(team.getScore() - this.getScore() == 0){
            return (team.getGoalScored() - team.getGoalConceded()) - (this.getGoalScored() - this.getGoalConceded());
        }
        return team.getScore() - this.getScore();
    }

    public String getJsonTeam(){
        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).setPrettyPrinting().create();
        String result = gson.toJson(this);
        return result;
    }

}


