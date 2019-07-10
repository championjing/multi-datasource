package cn.rzwl.multidata.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : championjing
 * @ClassName: DBAssistant
 * @Description:
 * @Date: 7/10/2019 3:26 PM
 */
public class DBAssistant extends AbstractRoutingDataSource {
    
    public static final ThreadLocal<String> dbStore = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        String dbBean = dbStore.get();
        return dbBean;
    }
    
    enum SourceName{
        TEST1("test1"),TEST2("test2");
        
        public String dbName;
        
        private SourceName(String name){
            this.dbName = name;
        }
    }
}
