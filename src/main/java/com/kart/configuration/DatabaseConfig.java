package com.kart.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {
	
	@Value("${db.driver}")
	private String DB_DRIVER;

	
	@Value("${db.url}")
	private String DB_URL;

	@Value("${db.username}")
	private String DB_USERNAME;

	@Value("${db.password}")
	private String DB_PASSWORD;
	
	@Value("${hibernate.dialect}")
	private String HIBERNATE_DIALECT;

	@Value("${hibernate.show_sql}")
	private String HIBERNATE_SHOW_SQL;


	@Value("${entitymanager.packagesToScan}")
	private String ENTITYMANAGER_PACKAGES_TO_SCAN;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Bean
	@Qualifier("mysqlSource")
	public DataSource dataSource() throws Exception {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(DB_DRIVER);
		dataSource.setUrl(DB_URL);
		System.out.println("DB_URL::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"+DB_URL);
		dataSource.setUsername(DB_USERNAME);
		dataSource.setPassword(DB_PASSWORD);
		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() throws Exception {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);

		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", HIBERNATE_DIALECT);
		hibernateProperties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
		sessionFactoryBean.setHibernateProperties(hibernateProperties);
		return sessionFactoryBean;
	}

	@Bean
	public HibernateTransactionManager transactionManager() throws Exception {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		transactionManager.setAutodetectDataSource(false);
		return transactionManager;
	}


}
