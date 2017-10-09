package com.bookdb.config;

import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ws.MarshallingWebServiceOutboundGateway;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import com.bookdb.soap.GetBookRequest;
import com.bookdb.soap.GetBookResponse;

@Configuration
@ComponentScan({ "com.bookdb.manager", "com.bookdb.rest", "com.bookdb.jms", "com.bookdb.forsoap" })
@EnableJpaRepositories(basePackages = { "com.bookdb.repositories" })
@EnableTransactionManagement
@EnableJms
@EnableWs
@IntegrationComponentScan
@EnableIntegration

public class SpringConfig extends WsConfigurerAdapter {

	@MessagingGateway(name = "wBookService")
	public interface WBookService {
		@Gateway(requestChannel = "booksChannel")
		GetBookResponse getBook(GetBookRequest request);
	}

	@Bean(name = "booksChannel")
	public MessageChannel requestChannel() {
		return new DirectChannel();
	}

	@Bean
	public IntegrationFlow BookFlow() {
		return IntegrationFlows.from(requestChannel()).handle(wsOutboundGateway()).get();
	}

	@Bean
	public MessageHandler wsOutboundGateway() {
		MarshallingWebServiceOutboundGateway gw = new MarshallingWebServiceOutboundGateway(
				"http://localhost:8088/mockBookSsPortSoap11", jaxb2Marshaller());
		return gw;
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.bookdb.soap");
		return marshaller;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		LocalContainerEntityManagerFactoryBean EnMaFa = new LocalContainerEntityManagerFactoryBean();
		EnMaFa.setDataSource(this.dataSource());
		EnMaFa.setPackagesToScan(new String[] { "com.bookdb.domain" });

		HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
		EnMaFa.setJpaVendorAdapter(va);
		EnMaFa.setJpaProperties(this.factProperties());
		EnMaFa.afterPropertiesSet();
		return EnMaFa;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager TrMa = new JpaTransactionManager();
		TrMa.setEntityManagerFactory(this.entityManagerFactory().getObject());
		return TrMa;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/test");
		dataSource.setUsername("root");
		dataSource.setPassword("c9bUP");
		return dataSource;
	}

	@Bean
	public ActiveMQConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL("tcp://localhost:61616"); //
		connectionFactory.setUserName("admin");
		connectionFactory.setPassword("admin");
		connectionFactory.setTrustedPackages(Arrays.asList("com.bookdb"));
		return connectionFactory;
	}

	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet messageServlet = new MessageDispatcherServlet();
		messageServlet.setApplicationContext(applicationContext);
		messageServlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(messageServlet, "/soapws/*");
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setConcurrency("1-1");
		return factory;
	}

	@Bean(name = "books")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema bookSsSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("BookSsPort");
		wsdl11Definition.setLocationUri("/soapws");
		wsdl11Definition.setTargetNamespace("http://bookdb.com/soap");
		wsdl11Definition.setSchema(bookSsSchema);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema bookSsSchema() {
		return new SimpleXsdSchema(new ClassPathResource("books.xsd"));
	}

	Properties factProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		return properties;
	}

}
