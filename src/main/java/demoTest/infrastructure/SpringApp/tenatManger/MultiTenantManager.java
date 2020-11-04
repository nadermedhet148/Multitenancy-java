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
    private final DataSourceProperties properties;

    private Function<String, DataSourceProperties> tenantResolver;

    private AbstractRoutingDataSource multiTenantDataSource;

    public MultiTenantManager(DataSourceProperties properties) {
        this.properties = properties;
    }

    @Bean
    public DataSource dataSource() {

        multiTenantDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return currentTenant.get();
            }
        };
        multiTenantDataSource.setTargetDataSources(tenantDataSources);
        multiTenantDataSource.setDefaultTargetDataSource(masterDataSource());
        multiTenantDataSource.afterPropertiesSet();
        return multiTenantDataSource;
    }

    public void setTenantResolver(Function<String, DataSourceProperties> tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    public void setCurrentTenant(String tenantId) throws SQLException, TenantNotFoundException {
        addTenant(tenantId);
        currentTenant.set(tenantId);
        log.debug("[d] Tenant '{}' set as current.", tenantId);
    }

    public void addTenant(String tenantId) throws SQLException {

        DataSource dataSource = masterDataSource(); ;

        if(tenantId == "slave"){
            dataSource = slaveDataSource();
        }

        // Check that new connection is 'live'. If not - throw exception
        try(Connection c = dataSource.getConnection()) {
            tenantDataSources.put(tenantId, dataSource);
            multiTenantDataSource.afterPropertiesSet();
            log.debug("[d] Tenant '{}' added.", tenantId);
        }
    }

    public DataSource removeTenant(String tenantId) {
        Object removedDataSource = tenantDataSources.remove(tenantId);
        multiTenantDataSource.afterPropertiesSet();
        return (DataSource) removedDataSource;
    }

    public boolean tenantIsAbsent(String tenantId) {
        return !tenantDataSources.containsKey(tenantId);
    }

    public Collection<Object> getTenantList() {
        return tenantDataSources.keySet();
    }

    private DriverManagerDataSource masterDataSource() {
        DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
        defaultDataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        defaultDataSource.setUrl("jdbc:mysql://localhost:6603/ds_1_master");
        defaultDataSource.setUsername("root");
        defaultDataSource.setPassword("Admin1234");
        return defaultDataSource;
    }

    private DriverManagerDataSource slaveDataSource() {
        DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
        defaultDataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        defaultDataSource.setUrl("jdbc:mysql://localhost:6603/ds_0_master");
        defaultDataSource.setUsername("root");
        defaultDataSource.setPassword("Admin1234");
        return defaultDataSource;
    }
}