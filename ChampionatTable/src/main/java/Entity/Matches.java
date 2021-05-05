package Entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by maxim on 06.04.2021.
 */
@Entity
@DynamicUpdate
@DynamicInsert
public class Matches {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int goals1;
    private int goals2;
    private String team1;
    private String team2;

    public Matches(int goals1, int goals2, String team1, String team2) {
        this.id = id;
        this.team1 = team1;
        this.goals1 = goals1;
        this.goals2 = goals2;
        this.team2 = team2;
    }

    public Matches(){

    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public int getGoals1() {
        return goals1;
    }

    public void setGoals1(int goals1) {
        this.goals1 = goals1;
    }

    public int getGoals2() {
        return goals2;
    }

    public void setGoals2(int goals2) {
        this.goals2 = goals2;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {     //Проверка только по названиях команд
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matches matches = (Matches) o;
        if (team1 != null ? !team1.equals(matches.team1) : matches.team1 != null) return false;
        return team2 != null ? team2.equals(matches.team2) : matches.team2 == null;
    }

    @Override
    public int hashCode() {
        int result = team1 != null ? team1.hashCode() : 0;
        result = 31 * result + (team2 != null ? team2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Matches{" +
                "id=" + id +
                ", team1='" + team1 + '\'' +
                ", goals1=" + goals1 +
                ", goals2=" + goals2 +
                ", team2='" + team2 + '\'' +
                '}';
    }

    public int countScore(int goals1, int goals2){
        if(goals1 > goals2){
            return 3;
        }
        else if(goals2 > goals1){
            return 0;
        }
        else {
            return 1;
        }
    }

}
