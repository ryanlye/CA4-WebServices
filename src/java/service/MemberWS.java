/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Entity.Member;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import javax.ws.rs.POST;
import javax.ws.rs.core.GenericEntity;

/**
 * REST Web Service
 */

@Path("memberWS")
public class MemberWS {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MemberWS
     */
    public MemberWS() {
    }

    /**
     * Retrieves representation of an instance of service.MemberWS
     * @return an instance of java.lang.String
     */
       
    //Done by Ryan Lye p1638611
    @GET
    @Path("getMember")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getMember(@QueryParam("email") String Email)throws ClassNotFoundException, SQLException 
 {
             //TODO return proper representation object
        //throw new UnsupportedOperationException();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // Step 2: Define Connection URL
            String connURL = "jdbc:mysql://localhost/islandfurniture-it07?user=root&password=12345";//DBConn.DBConnectionSettings.connectionURL;
            // Step 3: Establish connection to URL
            Connection conn = DriverManager.getConnection(connURL);
            //String sqlStr = "Select * from inventory where functions like '%" + searchString + "%' order by brand, model";
            String sqlStr = "Select * from memberentity where email=? ";

            PreparedStatement pstmt = conn.prepareStatement(sqlStr);

            pstmt.setString(1, Email);
            ResultSet rs = pstmt.executeQuery();

            System.out.println(sqlStr);
            
            System.out.println(rs);
            
            Member mem = null;
            
            
            if (rs.next()) {
                Long id = rs.getLong("id");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                String city = rs.getString("city");
                int securityQuestion = rs.getInt("securityQuestion");
                String securityAnswer = rs.getString("securityAnswer");
                int age = rs.getInt("age");
                int income = rs.getInt("income");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int loyaltyPoints = rs.getInt("loyaltyPoints");
                double cumulativeSpending = rs.getDouble("cumulativeSpending");
                
                mem = new Member(id, phone, address, city, securityQuestion, securityAnswer, 
                        age, income, name, email, loyaltyPoints, cumulativeSpending);

            }
            conn.close();
            GenericEntity<Member> entity = new GenericEntity<Member>(mem) {};
            return Response
                    .status(200)
                    .entity(entity)
                    .build();
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("error encountered..." + e.toString());
            return null;
        }
 }
    
    //Done by Mark Loh p1636846
    @Path("updateProfile")
    @POST
    @Consumes("application/json")
    //@Produces("application/json")
    public Response updateProfile(@QueryParam("name") String name, @QueryParam("email") String email, @QueryParam("phone") String phone, @QueryParam("city") String city, @QueryParam("address") String address,
    @QueryParam("securityQuestion")int securityQuestion, @QueryParam("securityAnswer") String securityAnswer, @QueryParam("age") int age, @QueryParam("income")int income) {
        try {
            //Class.forName("com.sql.jdbc.Driver");
            String connURL = "jdbc:mysql://localhost/islandfurniture-it07?user=root&password=12345";
            Connection conn = DriverManager.getConnection(connURL);
            //String stmt = "UPDATE memberentity set name=? where email=?";
            String stmt = "UPDATE memberentity SET name=?, phone=?, city=?, address=?, securityquestion=?, securityanswer=?, age=?, income=? WHERE email=?";
            PreparedStatement ps = conn.prepareStatement(stmt);
            
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, city);
            ps.setString(4, address);
            ps.setInt(5, securityQuestion);
            ps.setString(6, securityAnswer);
            ps.setInt(7,age);
            ps.setInt(8,income);
            ps.setString(9,email);
            int result = ps.executeUpdate();
            
            if (result > 0) {
                return Response.status(Response.Status.OK).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    /**
     * PUT method for updating or creating an instance of MemberWS
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}