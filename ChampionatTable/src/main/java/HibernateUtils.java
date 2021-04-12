import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import java.sql.Connection;

/**
 * Created by maxim on 10.04.2021.
 */
public class HibernateUtils {
    private static SessionFactory factory;

    static {
        factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public static SessionFactory getFactory(){
        return factory;
    }
}
