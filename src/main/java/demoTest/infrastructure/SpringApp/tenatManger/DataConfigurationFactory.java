package demoTest.infrastructure.SpringApp.tenatManger;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataConfigurationFactory {


    public  DriverManagerDataSource getDataConfiguration(String name) throws TentNotExisted {
        switch (name){
            case  "master" :
                return getDataSource("jdbc:mysql://localhost:6603/ds_1_master" , "root" , "Admin1234", com.mysql.jdbc.Driver.class.getName());
            case  "master2" :
                return getDataSource("jdbc:mysql://localhost:6603/ds_0_master" , "root" , "Admin1234", com.mysql.jdbc.Driver.class.getName());
        }
        throw new TentNotExisted();
    };

    private DriverManagerDataSource getDataSource(String url , String userName , String password , String driver) {
        DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
        defaultDataSource.setDriverClassName(driver);
        defaultDataSource.setUrl(url);
        defaultDataSource.setUsername(userName);
        defaultDataSource.setPassword(password);
        return defaultDataSource;
    };
}
