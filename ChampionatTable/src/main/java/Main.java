import Entity.Matches;
import Entity.Team;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by maxim on 06.04.2021.
 */
public class Main {
    public static void main(String[] args) {

        Session session = HibernateUtils.getFactory().openSession();
        MathesHelper mathesHelper = new MathesHelper();
        TeamHelper teamHelper = new TeamHelper();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите количество сыгранных матчей");
        int quantity = scanner.nextInt();
        List<Matches> matches = mathesHelper.getMatchesList();
        List<Team> matchesTeams = new ArrayList<Team>();
        for (int i = 0; i < quantity; i++) {
            scanner = new Scanner(System.in);
            System.out.println("Введите имя первой команды");
            String teamName1 = scanner.nextLine();
            scanner = new Scanner(System.in);
            System.out.println("Введите имя второй команды");
            String teamName2 = scanner.nextLine();
            System.out.println("Введите количество забитых голов командой " + teamName1);
            int goals1 = scanner.nextInt();
            scanner = new Scanner(System.in);
            System.out.println("Введите количество забитых голов командой " + teamName2);
            int goals2 = scanner.nextInt();
            System.out.println("Матч введеный вами:");
            System.out.println(teamName1 + " " + goals1 + " : " + goals2 + " " + teamName2);
            System.out.println("Напишите: Да | Нет");
            scanner = new Scanner(System.in);
            String check = scanner.nextLine();
            if (check.equals("Нет")) {
                i = i - 1;
                continue;
            }
            Matches match = new Matches();
            match.setTeam1(teamName1);
            match.setGoals1(goals1);
            match.setGoals2(goals2);
            match.setTeam2(teamName2);

            System.out.println(match.hashCode());
            System.out.println("----");
            boolean flag = false;
            for (Matches tempMatch: matches) {
                System.out.println(tempMatch.toString());
                if(match.equals(tempMatch)) {
                    System.out.println("Этот матч уже был");
                    System.out.println("Задайте его заново");
                    i = i - 1;
                    flag = true;
                    break;
                }
            }

            if(flag){
                continue;
            }
            Team team1 = new Team(teamName1);
            matchesTeams.add(team1);
            Team team2 = new Team(teamName2);
            matchesTeams.add(team2);
            int scoresTeam1 = match.addScore(goals1, goals2);
            team1.setScore(scoresTeam1);
            int scoresTeam2 = match.addScore(goals2, goals1);
            team2.setScore(scoresTeam2);
            session.beginTransaction();
            session.save(match);
            session.getTransaction().commit();
        }
        List<Team> teamList = teamHelper.getTeamList();
        Team testTeam = new Team("Name", 0);
        teamList.add(testTeam);
        for (int i = 0; i < matchesTeams.size(); i++) {
            boolean isInDatabase = false;
            for (int j = 0; j < teamList.size() ; j++) {
                if(teamList.get(j).equals(matchesTeams.get(i))){
                    isInDatabase = true;
                }
            }
            if(!isInDatabase) {
                Team team = new Team();
                team.setName(matchesTeams.get(i).getName());
                team.setScore(matchesTeams.get(i).getScore());
                teamHelper.addTeam(team);
            }
        }

        HibernateUtils.getFactory().close();
        session.close();
        }


    }

