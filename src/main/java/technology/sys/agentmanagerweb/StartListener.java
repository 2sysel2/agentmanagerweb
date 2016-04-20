package technology.sys.agentmanagerweb;

import cz.muni.fi.pv168.gmiterkosys.AgentManager;
import cz.muni.fi.pv168.gmiterkosys.AgentManagerImpl;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

/**
 *
 * @author Jaromir Sys
 */
@WebListener
public class StartListener implements ServletContextListener{

    private DataSource ds;
    private AgentManager agentManager;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        try {
            setUp();
        } catch (SQLException ex) {
            Logger.getLogger(StartListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        servletContext.setAttribute("agentManager", agentManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
    
    private static DataSource prepareDataSource() throws SQLException {
		EmbeddedDataSource ds = new EmbeddedDataSource();
		ds.setDatabaseName("memory:mission");
		ds.setCreateDatabase("create");
		return ds;
	}
    
    public void setUp() throws SQLException {
		ds = prepareDataSource();

		try (Connection connection = ds.getConnection()) {
			connection.prepareStatement("CREATE TABLE agent ("
                    + "id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "\"name\" VARCHAR(255) NOT NULL,"
                    + "born DATE,"
                    + "died DATE,"
                    + "\"level\" SMALLINT"
                    + ")").execute();
			}

		agentManager = new AgentManagerImpl(ds);
	}
    
}
