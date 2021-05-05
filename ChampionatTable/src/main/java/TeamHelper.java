import Entity.Matches;
import Entity.Team;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by maxim on 11.04.2021.
 */
public class TeamHelper {

    private SessionFactory sessionFactory;

    public TeamHelper(){
        sessionFactory = HibernateUtils.getFactory();
    }

    public void addTeam(Team team){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(team);
        session.getTransaction().commit();
        session.close();
    }

    public Team getTeamById(long id){
        Session session = sessionFactory.openSession();
        Team team = session.get(Team.class, id);
        session.close();
        return team;
    }

    public List<Team> getTeamList(){
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Team> cq = cb.createQuery(Team.class);
        Root<Team> root = cq.from(Team.class);
        Selection[] selections = {root.get("name")};
        cq.select(cb.construct(Team.class, selections));
        Query query = session.createQuery(cq);
        List<Team> teamList = query.getResultList();
        session.close();
        return teamList;
    }


    public Set<Team> getTeamsWithUpdatedScore(){
        Set<Team> updateTeamList = new HashSet<>();//Удаление повторяющихся команд
        List<Team> allTeams = getTeamList();
        MatсhesHelper matсhesHelper = new MatсhesHelper();
        List<Matches> allMatches = matсhesHelper.getMatchesList();
        for (int i = 0; i < allTeams.size(); i++) {
            for (int j = 0; j < allMatches.size(); j++) {
                if(allTeams.get(i).getName().equals(allMatches.get(j).getTeam1())){
                    Matches tempMatch = allMatches.get(j);
                    int score = tempMatch.countScore(tempMatch.getGoals1(), tempMatch.getGoals2());
                    int goalScored = tempMatch.getGoals1();
                    int goalConced = tempMatch.getGoals2();
                    Team tempTeam = allTeams.get(i);
                    tempTeam.setGoalScored(tempTeam.getGoalScored() + goalScored);
                    tempTeam.setGoalConceded(tempTeam.getGoalConceded() + goalConced);
                    tempTeam.setScore(tempTeam.getScore() + score);
                    updateTeamList.add(tempTeam);
                }
                if(allTeams.get(i).getName().equals(allMatches.get(j).getTeam2())){
                    Matches tempMatch = allMatches.get(j);
                    int score = tempMatch.countScore(tempMatch.getGoals2(), tempMatch.getGoals1());
                    int goalScored = tempMatch.getGoals2();
                    int goalConced = tempMatch.getGoals1();
                    Team tempTeam = allTeams.get(i);
                    tempTeam.setScore(tempTeam.getScore() + score);
                    tempTeam.setGoalScored(tempTeam.getGoalScored() + goalScored);
                    tempTeam.setGoalConceded(tempTeam.getGoalConceded() + goalConced);
                    updateTeamList.add(tempTeam);
                }
            }
        }
        return updateTeamList;
    }

}
