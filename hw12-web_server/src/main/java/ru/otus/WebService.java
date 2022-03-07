package ru.otus;

import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.model.User;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.UserAuthServiceImpl;
import ru.otus.server.ClientWebServerWithFilterBasedSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;


public class WebService {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception{
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class,
                Address.class, Phone.class, User.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var userTemplate = new DataTemplateHibernate<>(User.class);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var userAuthService = new UserAuthServiceImpl(transactionManager, userTemplate);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        final var server = new ClientWebServerWithFilterBasedSecurity(8080, dbServiceClient,
                templateProcessor, userAuthService);

        server.start();
        server.join();
    }
}
