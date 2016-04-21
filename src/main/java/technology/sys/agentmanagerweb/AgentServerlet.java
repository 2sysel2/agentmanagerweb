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
        Long id;
        Agent agent;
        int level;
        switch (action) {
            case "/add":
                
                String name = request.getParameter("name");
                LocalDate born;
                LocalDate died;
                
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
                    if(!validateLocalDate(request, response))return;
                    born = LocalDate.parse(request.getParameter("born"));
                    died = LocalDate.parse(request.getParameter("died"));
                    if(!validateLevel(request,response));
                }
                agent = new Agent();
                
                agent.setName(name);
                agent.setBorn(born);
                agent.setDied(died);
                agent.setLevel(getAgentLevel(request));
                getAgentManager().createAgent(agent);
                response.sendRedirect(request.getContextPath()+URL_MAPPING);
                return;
            case "/delete":
                id = Long.valueOf(request.getParameter("id"));
                getAgentManager().deleteAgent(getAgentManager().getAgentById(id));
                response.sendRedirect(request.getContextPath()+URL_MAPPING);
                return;
            case "/update":
                id = Long.valueOf(request.getParameter("id"));
                agent = getAgentManager().getAgentById(id);
                agent.setName(request.getParameter("name"));
                if(!validateLocalDate(request, response))return;
                agent.setBorn(LocalDate.parse(request.getParameter("born")));
                agent.setDied(LocalDate.parse(request.getParameter("died")));
                if(!validateLevel(request, response))return;
                agent.setLevel(getAgentLevel(request));
                getAgentManager().updateAgent(agent);
                response.sendRedirect(request.getContextPath()+URL_MAPPING);
                return;
        }
    }

    private void showAllAgent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setAttribute("agents", getAgentManager().findAllAgents());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
    }
    
    private int getAgentLevel(HttpServletRequest request){
        int level = Integer.parseInt(request.getParameter("level"));
        if(level>10 || level <1){
            if(level>10)
                level = 10;
            if(level<1)
                level = 1;
        }
        return level;
    }
    
    private boolean validateLocalDate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            LocalDate.parse(request.getParameter("born"));
            LocalDate.parse(request.getParameter("died"));
            return true;
        }catch(DateTimeParseException e){
            request.setAttribute("error", "date format not correct");
            showAllAgent(request, response);
            return false;
        }
    }
    
    public boolean validateLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            Integer.parseInt(request.getParameter("level"));
            return true;
        }catch(NumberFormatException e){
            request.setAttribute("error", "level is supposed to be a number");
            showAllAgent(request, response);
            return false;
        }
    }
    
    private AgentManager getAgentManager(){
        return (AgentManager)getServletContext().getAttribute("agentManager");
    }

}
