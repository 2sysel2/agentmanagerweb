package technology.sys.agentmanagerweb;

import cz.muni.fi.pv168.gmiterkosys.Agent;
import cz.muni.fi.pv168.gmiterkosys.AgentManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jaromir Sys
 */
@WebServlet(name = "AgentServerlet", urlPatterns = {AgentServerlet.URL_MAPPING + "/*"})
public class AgentServerlet extends HttpServlet {
    
    private static final String LIST_JSP = "/list.jsp";
    public static final String URL_MAPPING = "/agents";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        showAllAgent(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        String action = request.getPathInfo();
        switch (action) {
            case "/add":
                
                String name = request.getParameter("name");
                String born = request.getParameter("born");
                String died = request.getParameter("died");
                String level = request.getParameter("level");
                
                //kontrola vyplnění hodnot
                if (
                        name == null || name.length() == 0 ||
                        born == null || born.length() == 0||
                        died == null || died.length() == 0 ||
                        level == null || level.length()==0 )
                {
                    request.setAttribute("error", "all atributes are required");
                    showAllAgent(request, response);
                    return;
                }


                Agent agent = new Agent();
                
                agent.setName(name);
                agent.setBorn(LocalDate.parse(born));
                agent.setDied(LocalDate.parse(died));
                agent.setLevel(Integer.parseInt(level));
                
                getAgentManager().createAgent(agent);
                response.sendRedirect(request.getContextPath()+URL_MAPPING);
                return;
                    
                    
            case "/delete":
                Long id = Long.valueOf(request.getParameter("id"));
                getAgentManager().deleteAgent(getAgentManager().getAgentById(id));
                response.sendRedirect(request.getContextPath()+URL_MAPPING);
        }
    }

    private void showAllAgent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setAttribute("agents", getAgentManager().findAllAgents());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
    }
    
    private AgentManager getAgentManager(){
        return (AgentManager)getServletContext().getAttribute("agentManager");
    }

}
