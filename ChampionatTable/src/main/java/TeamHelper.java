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
import java.util.ArrayList;
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
    public boolean isEmpty(){
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Team> cq = cb.createQuery(Team.class);
        Root<Team> root = cq.from(Team.class);
        Selection[] selections = {root.get("name")};
        cq.select(cb.construct(Team.class, selections));
        Query query = session.createQuery(cq);
        try {
            List<Team> teamList = query.getResultList();
        }catch (HibernateException e){
            return true;
        }

        session.close();
        return false;
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
    public List<Team> getTeamByColum(String column, String value){
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();// не использовать session.createCriteria, т.к. deprecated
        CriteriaQuery cq = cb.createQuery(Team.class);
        Root<Team> root = cq.from(Team.class);// первостепенный, корневой entity (в sql запросе - from)
        cq.select(root).where(cb.like(root.<String>get(column), value));
        //этап выполнения запроса
        Query query = session.createQuery(cq);
        List<Team> teamList = query.getResultList();
        session.close();
        return teamList;
    }
    public void upadateTeam(Team updateTeam, int score){

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Team> teamList = getTeamList();
        for (int i = 0; i < teamList.size(); i++) {
            if(teamList.get(i).equals(updateTeam)){
                teamList.get(i).setScore(score);
                session.save(teamList.get(i));
            }
        }
        session.getTransaction().commit();
    }
    public Set<Team> getToutnamentTable(){
        Set<Team> updateTeamList = new HashSet<>();
        List<Team> teamList = getTeamList();
        MathesHelper mathesHelper = new MathesHelper();
        List<Matches> matchesList = mathesHelper.getMatchesList();
        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < matchesList.size(); j++) {
                if(teamList.get(i).getName().equals(matchesList.get(j).getTeam1())){
                    Matches tempMatch = matchesList.get(j);
                    int score = tempMatch.countScore(tempMatch.getGoals1(), tempMatch.getGoals2());
                    Team tempTeam = teamList.get(i);
                    tempTeam.setScore(tempTeam.getScore() + score);
                    updateTeamList.add(tempTeam);
                }
                if(teamList.get(i).getName().equals(matchesList.get(j).getTeam2())){
                    Matches tempMatch = matchesList.get(j);
                    int score = tempMatch.countScore(tempMatch.getGoals2(), tempMatch.getGoals1());
                    Team tempTeam = teamList.get(i);
                    tempTeam.setScore(tempTeam.getScore() + score);
                    updateTeamList.add(tempTeam);
                }
            }
        }
        return updateTeamList;
    }

}
