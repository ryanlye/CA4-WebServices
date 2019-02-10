package service;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Eugene Koh You Jun
 */

@Path("entity.commerce")
public class ECommerceFacadeREST {

    @Context
    private UriInfo context;

    public ECommerceFacadeREST() {
    }

    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ECommerce
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
    
    @Path("createECommerceTransactionRecord")
    @PUT
    @Consumes("application/json")
    public Response createECommerceTransactionRecord(@QueryParam("memberID") Long memberID, @QueryParam("totalPrice") double totalPrice, @QueryParam("storeId") Long storeId){
        int transid = -1;
    try {
            //Class.forName("com.sql.jdbc.Driver");
            String connURL = "jdbc:mysql://localhost/islandfurniture-it07?user=root&password=12345";
            Connection conn = DriverManager.getConnection(connURL);
            String currency = "SGD";
            String posName = "Counter 1";
            String staff = "Cashier 1";
            Date dt = new Date();
        
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long receiptNo = dt.getTime();
            //String stmt = "UPDATE memberentity set name=? where email=?";
            //String stmt = "INSERT INTO salesrecordentity ('MEMBERID', 'AMOUNTPAID') VALUES (?, ?)";
            String stmt = "INSERT INTO salesrecordentity (AMOUNTDUE,AMOUNTPAID,CREATEDDATE,CURRENCY,POSNAME,RECEIPTNO,SERVEDBYSTAFF,MEMBER_ID,STORE_ID) VALUES (?,?,?,?,?,?,?,?,?)";
            
            
            PreparedStatement ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            
            ps.setDouble(1,totalPrice);
            ps.setDouble(2,totalPrice);
            ps.setString(3, sdf.format(dt));
            ps.setString(4, currency);
            ps.setString(5, posName);
            ps.setString(6,String.valueOf(receiptNo));
            ps.setString(7,staff);
            ps.setLong(8,memberID);
            ps.setLong(9,storeId);
            ps.executeUpdate();
            //ps.setLong(1, memberID);
           // ps.setDouble(2, amountPaid);
            //ps.setLong(3, countryID);
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                transid = rs.getInt(1);
                return Response.status(201).entity(transid).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @Path("createECommerceLineItemRecord")
    @PUT
    @Consumes("application/json")
    public Response createECommerceLineItemRecord(@QueryParam("quantity") int quantity, @QueryParam("itemId")String itemId){
       int result = 0;
    try {
            String connURL = "jdbc:mysql://localhost/islandfurniture-it07?user=root&password=12345";
            Connection conn = DriverManager.getConnection(connURL);
            String stmt = "INSERT INTO lineitementity (QUANTITY,ITEM_ID) VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, quantity);
            ps.setInt(2, Integer.parseInt(itemId));
            result =  ps.executeUpdate();
            
            if (result == 0)
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else {
                return Response.status(200).build();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            
        }
        
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
    public List<Integer> getQuantityFromSKU(String SKU) {
        List<Integer> ints = new ArrayList<>();
        try {
            String connURL = "jdbc:mysql://localhost/islandfurniture-it07?user=root&password=12345";
            Connection conn = DriverManager.getConnection(connURL);
            String stmt = "SELECT QUANTITY,lineItems_Id FROM storeentity s, warehouseentity w, storagebinentity sb, storagebinentity_lineitementity sbli, lineitementity l, itementity i where s.WAREHOUSE_ID=w.ID and w.ID=sb.WAREHOUSE_ID and sb.ID=sbli.StorageBinEntity_ID and sbli.lineItems_ID=l.ID and l.ITEM_ID=i.ID and s.ID=? and i.SKU=?";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setLong(1, 59);
            ps.setString(2, SKU);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("QUANTITY");
                int lineItemsId = rs.getInt("lineItems_Id");
                ints.add(quantity);
                ints.add(lineItemsId);
            }
            return ints;
        } catch (Exception ex) {

            ex.printStackTrace();
            return null;
        }

    }
}