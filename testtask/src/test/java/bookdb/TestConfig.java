package bookdb;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration

@ComponentScan(basePackages = { "com.bookdb.manager" })
@EnableJpaRepositories(basePackages = { "com.bookdb.repositories" })
@EnableTransactionManagement
public class TestConfig {

	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager TrMa = new JpaTransactionManager();
		TrMa.setEntityManagerFactory(this.entityManagerFactory().getObject());
		return TrMa;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		LocalContainerEntityManagerFactoryBean EnMaFa = new LocalContainerEntityManagerFactoryBean();
		EnMaFa.setDataSource(this.dataSource());
		EnMaFa.setPackagesToScan(new String[] { "com.bookdb.domain" });

		HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
		EnMaFa.setJpaVendorAdapter(va);

		Properties ps = new Properties();
		ps.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		ps.put("hibernate.hbm2ddl.auto", "create");
		EnMaFa.setJpaProperties(ps);

		return EnMaFa;
	}

}
