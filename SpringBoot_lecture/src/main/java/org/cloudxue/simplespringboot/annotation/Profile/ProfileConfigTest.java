package org.cloudxue.simplespringboot.annotation.Profile;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringValueResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @ClassName ProfileConfigTest
 * @Description:
 * @Author: xuexiao
 * @Date: 2023年01月03日 15:11:57
 * @Version 1.0
 **/

@PropertySource("classpath:/datasource.properties")
//@Configuration
public class ProfileConfigTest implements EmbeddedValueResolverAware {

    @Value("${dataSource.user}")
    private String user;
    private StringValueResolver resolver;
    private String password;

    @Profile("dev")
    @Bean
    public DataSource dataSourceDev(@Value("${dataDriveClassName}") String driverClass) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/dev");
        return dataSource;
    }

    @Profile("test")
    @Bean
    public DataSource dataSourceTest(@Value("${dataDriveClassName}") String driverClass) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        return dataSource;
    }

    @Profile("prod")
    @Bean
    public DataSource dataSourceProd(@Value("${dataDriveClassName}") String driverClass) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/prod");
        return dataSource;
    }

    /**
     * 通过StringValueResolver解析dataSource.password的值
     * @param resolver
     */
    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        password = resolver.resolveStringValue("${dataSource.password}");
    }
}
