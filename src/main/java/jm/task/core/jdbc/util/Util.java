package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/maindb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Util instance;
    private static boolean isCreated;
    private static Lock locker = new ReentrantLock(true);
    private static SessionFactory sessionFactory = getInstance().configureSessionFactory();

    public static Util getInstance() {
        if (!isCreated) {
            locker.lock();
            if (instance == null) {
                instance = new Util();
                isCreated = true;
            }
            locker.unlock();
        }

        return instance;
    }

    private SessionFactory configureSessionFactory() {
        Configuration configuration = new Configuration();
        InputStream inputStream = this.getClass().getClassLoader().
                getResourceAsStream("hibernate-mysql.properties");
        Properties hibernateProperties = new Properties();
        try {
            hibernateProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration.setProperties(hibernateProperties);

        configuration.addAnnotatedClass(User.class);


        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().
                applySettings(configuration.getProperties()).buildServiceRegistry();

        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return conn;
    }


    public void closeConnection(Connection connection) {
        getSessionFactory().close();
    }
}
