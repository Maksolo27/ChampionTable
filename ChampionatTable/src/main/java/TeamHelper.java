import Entity.Matches;
import Entity.Team;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.List;

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
}
