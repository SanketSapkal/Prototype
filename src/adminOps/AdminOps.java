package adminOps;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;


@WebServlet("/AdminOps")
public class AdminOps extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private AdminOpsHandler aoh = new AdminOpsHandler();
    private Response res = new Response();
	JSONObject json = new JSONObject();
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		
		System.out.println("Inside POST Method");
		
		PrintWriter out = response.getWriter();
		
		String str = request.getParameter("req");
		System.out.println(str);
		
		try {
			
			JSONObject obj = new JSONObject(str);
			
			res = aoh.getInfo(obj);
			
			json.put("code", res.getCode());
			json.put("id", res.getID());
			json.put("message", res.getMessage());
			out.print(json);
			
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				json.put("code", "JSON_EXCEPTION");
				json.put("id", "0");
				json.put("message", "JSON Exception Occured..");
				out.print(json);
			} catch (JSONException e1) {
				out.print(json);
				e1.printStackTrace();
			}
		}
	}

}
