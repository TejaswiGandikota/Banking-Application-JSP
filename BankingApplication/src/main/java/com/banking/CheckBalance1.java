package com.banking;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CheckBalance1 extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
        response.setContentType("text/html");
        HttpSession session = request.getSession(false);
        if (session != null) 
        {
            String user = (String) session.getAttribute("name");
            pw.print("<h1 align='center'>Welcome, " + user + " Continue with your transactions</h1>");
            Connection con = null;
            try
            {
                con = DBConnection.get();
                int num = Integer.parseInt(request.getParameter("accountNumber").trim());

                String query = "select balance from account where num=?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, num);

                ResultSet rs = ps.executeQuery();
                if (rs.next())
                {
                    int balance = rs.getInt("balance");
                    pw.print("<h3 align='center'>Balance for Account Number " + num + ": " + balance + "</h3>");
                    RequestDispatcher rd = request.getRequestDispatcher("/user.html");
                    rd.include(request, response);
                } 
                else 
                {
                    pw.print("<h3 align='center'>Invalid Account Number Given - Try again</h3>");
                    RequestDispatcher rd = request.getRequestDispatcher("/check_balance.html");
                    rd.include(request, response);
                }
            }
            catch (Exception e) 
            {
                pw.print("<h3 align='center'>Invalid Account Number Given - Try Again</h3>");
                request.getRequestDispatcher("/check_balance.html").include(request, response);
            } 
            finally 
            {
                if (con != null)
                {
                    try 
                    {
                        con.close();
                    } 
                    catch (SQLException e)
                    {
                    	
                    }
                }
            }
        }
        else 
        {
            pw.print("<h3>You logged out from previous Session - Please Login</h3>");
            request.getRequestDispatcher("login.html").include(request, response);
        }
    }

}