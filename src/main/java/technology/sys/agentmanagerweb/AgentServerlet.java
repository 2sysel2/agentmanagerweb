package technology.sys.agentmanagerweb;

import cz.muni.fi.pv168.gmiterkosys.Agent;
import cz.muni.fi.pv168.gmiterkosys.AgentManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
                LocalDate born;
                LocalDate died;
                int level;
                if (
                        name == null || name.length() == 0 ||
                        request.getParameter("born") == null || request.getParameter("born").length() == 0||
                        request.getParameter("died") == null || request.getParameter("died").length() == 0 ||
                        request.getParameter("level") == null || request.getParameter("level").length()==0 )
                {
                    request.setAttribute("error", "all atributes are required");
                    showAllAgent(request, response);
                    return;
                }else{
                    try{
                        born = LocalDate.parse(request.getParameter("born"));
                        died = LocalDate.parse(request.getParameter("died"));
                    }catch(DateTimeParseException e){
                        request.setAttribute("error", "date format not correct");
                        showAllAgent(request, response);
                        return;
                    }
                    try{
                        level = Integer.parseInt(request.getParameter("level"));
                    }catch(NumberFormatException e){
                        request.setAttribute("error", "level is supposed to be a number");
                        showAllAgent(request, response);
                        return;
                    }
                    
                }


                Agent agent = new Agent();
                
                agent.setName(name);
                agent.setBorn(born);
                agent.setDied(died);
                agent.setLevel(level);
                if(level>10){
                    agent.setLevel(10);
                }
                if(level<1){
                    agent.setLevel(1);
                }         
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
