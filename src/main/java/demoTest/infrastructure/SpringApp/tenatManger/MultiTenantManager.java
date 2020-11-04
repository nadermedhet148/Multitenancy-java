package demoTest.infrastructure.SpringApp.tenatManger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.lang.String.format;

@Slf4j
@Configuration
public class MultiTenantManager {

    private final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private final Map<Object, Object> tenantDataSources = new ConcurrentHashMap<>();
    private  DataConfigurationFactory dataConfigurationFactory = new DataConfigurationFactory();


    private AbstractRoutingDataSource multiTenantDataSource;


    @Bean
    public DataSource dataSource() throws TentNotExisted {

        multiTenantDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return currentTenant.get();
            }
        };
        multiTenantDataSource.setTargetDataSources(tenantDataSources);
        multiTenantDataSource.setDefaultTargetDataSource(dataConfigurationFactory.getDataConfiguration("master"));
        multiTenantDataSource.afterPropertiesSet();
        return multiTenantDataSource;
    }




    public void setTenant(String tenantId) throws SQLException, TentNotExisted {

        DataSource dataSource = dataConfigurationFactory.getDataConfiguration(tenantId);

        try(Connection c = dataSource.getConnection()) {
            tenantDataSources.put(tenantId, dataSource);
            multiTenantDataSource.afterPropertiesSet();
            log.debug("[d] Tenant '{}' added.", tenantId);
        }
    }



}