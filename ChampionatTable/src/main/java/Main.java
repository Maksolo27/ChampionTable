import Entity.Matches;
import Entity.Team;
import org.hibernate.Session;

import java.util.*;

/**
 * Created by maxim on 06.04.2021.
 */
public class Main {
    public static void main(String[] args) {

        Session session = HibernateUtils.getFactory().openSession();
        Scanner scanner = new Scanner(System.in);
        MatсhesHelper matсhesHelper = new MatсhesHelper();
        TeamHelper teamHelper = new TeamHelper();
        scanner = new Scanner(System.in);
        System.out.println("Введите количество сыгранных матчей");
        int quantity = scanner.nextInt();
        List<Matches> matchesFromDatabase = matсhesHelper.getMatchesList();
        List<Team> teamsFromCurrentMatches = new ArrayList<>();
        List<Team> allTeams = teamHelper.getTeamList();
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
            for (Matches tempMatch: matchesFromDatabase) {
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
            Team team2 = new Team(teamName2);
            int scoresTeam1 = match.countScore(goals1, goals2);
            int scoresTeam2 = match.countScore(goals2, goals1);
            team1.setGoalScored(goals1);
            team2.setGoalScored(goals2);
            team1.setGoalConceded(goals2);
            team2.setGoalConceded(goals1);
            team1.setScore(scoresTeam1);
            team2.setScore(scoresTeam2);
            teamsFromCurrentMatches.add(team2);
            teamsFromCurrentMatches.add(team1);
            matсhesHelper.addMatch(match);
        }
        Team testTeam = new Team("Name", 0);//Добавление тестовой команды, если список пуст
        allTeams.add(testTeam);
        for (int i = 0; i < teamsFromCurrentMatches.size(); i++) {
            boolean isInDatabase = false;
            for (int j = 0; j < allTeams.size() ; j++) {
                if(allTeams.get(j).equals(teamsFromCurrentMatches.get(i))){
                    isInDatabase = true;
                }
            }
            if(!isInDatabase) {
                Team team = new Team();
                team.setName(teamsFromCurrentMatches.get(i).getName());
                team.setScore(teamsFromCurrentMatches.get(i).getScore());
                team.setGoalConceded(teamsFromCurrentMatches.get(i).getGoalConceded());
                team.setGoalScored(teamsFromCurrentMatches.get(i).getGoalScored());
                teamHelper.addTeam(team);
            }
        }
        List<Team> teamListForSort = new ArrayList<>();
        Set<Team> updateTeamList = teamHelper.getTeamsWithUpdatedScore();
        Iterator<Team> iterator = updateTeamList.iterator();
        while(iterator.hasNext()){
            teamListForSort.add(iterator.next());
        }
        teamListForSort.sort(Team::compareTo);
        for (int i = 0; i < teamListForSort.size(); i++) {
            System.out.println(teamListForSort.get(i).getName() + " " + teamListForSort.get(i).getScore() + " " + teamListForSort.get(i).getGoalScored() + " "
            + teamListForSort.get(i).getGoalConceded());
        }
        System.out.println("Хотите получить список в json формате?");
        System.out.println("/json");
        scanner = new Scanner(System.in);
        if(scanner.nextLine().equals("/json")){
            for (int i = 0; i < teamListForSort.size(); i++) {
                System.out.println(teamListForSort.get(i).getJsonTeam());
            }
        }
        session.close();
        }


    }

