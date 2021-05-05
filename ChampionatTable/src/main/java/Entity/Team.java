package Entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by maxim on 07.04.2021.
 */
@Entity
@DynamicUpdate
@DynamicInsert
public class Team implements Comparable<Team> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int score;
    private int goalScored;
    private int goalConceded;

    public int getGoalScored() {
        return goalScored;
    }

    public void setGoalScored(int goalScored) {
        this.goalScored = goalScored;
    }

    public int getGoalConceded() {
        return goalConceded;
    }

    public void setGoalConceded(int goalConceded) {
        this.goalConceded = goalConceded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public Team(){

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
        return team.getScore() - this.getScore();
    }

}


