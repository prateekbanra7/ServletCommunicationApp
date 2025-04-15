package in.abc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		urlPatterns = { "/reg" }, 
		initParams = { 
				@WebInitParam(name = "url", value = "jdbc:mysql:///abc"), 
				@WebInitParam(name = "user", value = "root"), 
				@WebInitParam(name = "password", value = "root")
		})
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Connection connection = null;

	static {
		System.out.println("RegisterServlet class is loading...");
	}
	
	public RegistrationServlet() {
		System.out.println("Register Servlet class is instantiated...");
	}
	
	@Override
	public void init() throws ServletException {
		
		System.out.println("Register Servlet class is initialized...");
		
		ServletConfig config = getServletConfig();
		String url = config.getInitParameter("url");
		String user = config.getInitParameter("user");
		String password = config.getInitParameter("password");
		
		
		try {
			  Class.forName("com.mysql.cj.jdbc.Driver");
			  connection = DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String uname = request.getParameter("uname");
		Integer uage = Integer.parseInt(request.getParameter("uage"));
		String email = request.getParameter("email");
		String umobile = request.getParameter("umobile");
		
		//2.use JDBC API to send the data to database
		
				String sqlInsertQuery="insert into person_01(uname,uage,email,umobile) values(?,?,?,?)";
				PreparedStatement pstmt = null;
				
				
				try {
					
					if (connection != null) 
						
						pstmt = connection.prepareStatement(sqlInsertQuery);
					if (pstmt != null) {
						pstmt.setString(1, uname);
						pstmt.setInt(2, uage);
						pstmt.setString(3, email);
						pstmt.setString(4, umobile);
						
						
					}
					
				}catch(SQLException se) {
					se.printStackTrace();
				}catch(Exception e) {
					e.printStackTrace();
				}

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// userage if less than 18 or greater than 30 not eligible for recruitment
		if (uage < 19 || uage > 30) {
			// send the error response
			response.sendError(504, "User age is not sufficient for this Recruitment");
		} else {
			// otherwise, student is eligible for recruitment
			out.println("<html><head><title>Output</title></head></html>");
			out.println("<body>");
			out.println("<font color='red'>");
			out.println("<h2>Abc Consultancy Services</h2>");
			out.println("<h2>User Registration Details</h2>");
			out.println("</font>");
			out.println("<table border = '1'>");
			out.println("<tr><td>User Name</td><td>" + uname + "</td></tr>");
			out.println("<tr><td>User Age</td><td>" + uage + "</td></tr>");
			out.println("<tr><td>Email</td><td>" + email + "</td></tr>");
			out.println("<tr><td>Mobile Number</td><td>" + umobile + "</td></tr>");
			out.println("<tr><td>Registration status</td><td>Registered Successfully</td></tr>");
			out.println("</table>");
			out.println("</body>");
			out.println("</html>");
			
			out.close();

		}

	}

}
