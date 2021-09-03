package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final static String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS `maindb`.`user` (\n" +
            "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(45) NULL,\n" +
            "  `lastName` VARCHAR(45) NULL,\n" +
            "  `age` TINYTEXT NULL,\n" +
            "  PRIMARY KEY (`id`));";
    private static final String DROP_USERS_TABLE = "DROP TABLE IF EXISTS `maindb`.`usr`;";
    private static final String CLEAN_TABLE_USER = "DELETE FROM `maindb`.`usr`;";
    private static final String REMOVE_USER_BY_ID = "DELETE FROM `maindb`.`usr` WHERE id=";
    private static final String GET_ALL_USERS = "SELECT * FROM `maindb`.`usr`;";

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        SessionFactory sessionFactory = Util.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.createSQLQuery(CREATE_USERS_TABLE);
    }

    @Override
    public void dropUsersTable() {
        SessionFactory sessionFactory = Util.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.createSQLQuery(DROP_USERS_TABLE);

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        SessionFactory sessionFactory = Util.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = new User(name, lastName, age);
        session.saveOrUpdate(user);
        session.getTransaction().commit();
    }

    @Override
    public void removeUserById(long id) {
        SessionFactory sessionFactory = Util.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(REMOVE_USER_BY_ID + id);
        query.executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public List<User> getAllUsers() {
        SessionFactory sessionFactory = Util.getSessionFactory();
        Session session = sessionFactory.openSession();
        List list = session.createQuery("FROM User").list();

        return list;
    }

    @Override
    public void cleanUsersTable() {
        SessionFactory sessionFactory = Util.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(CLEAN_TABLE_USER);
        query.executeUpdate();
        session.getTransaction().commit();
    }
}
