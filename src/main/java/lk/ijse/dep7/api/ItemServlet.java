package lk.ijse.dep7.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep7.dto.ItemDTO;
import lk.ijse.dep7.util.DBConnectionPoll;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/items")
public class ItemServlet extends HttpServlet {

    private  Jsonb jsonb = JsonbBuilder.create();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       /* if (!req.getContentType().equals("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }*/
        try{
            Connection connection = DBConnectionPoll.getConnection();
            String sql = "SELECT * FROM item";
            Statement statement = connection.createStatement();
            PrintWriter out = resp.getWriter();
            ArrayList<ItemDTO> itemDTOS = new ArrayList<>();
            ResultSet items = statement.executeQuery(sql);

            while (items.next()){
                itemDTOS.add(new ItemDTO(items.getString("code"),
                        items.getString("description"),
                        items.getBigDecimal("unit_price"),
                        items.getInt("qty_on_hand")));
            }
            out.println(jsonb.toJson(itemDTOS));
            DBConnectionPoll.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      /*  if (req.getContentType().equals("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }*/

        try{
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            if (itemDTO.getCode() == null || itemDTO.getCode().matches("Ii\\d{3}")){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (itemDTO.getDescription() == null || itemDTO.getDescription().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (itemDTO.getPrice() ==null || itemDTO.getPrice().compareTo(new BigDecimal(0))<0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (itemDTO.getQtyOnHand()<0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            System.out.println(itemDTO.getQtyOnHand());
            Connection connection = DBConnectionPoll.getConnection();
            PreparedStatement pst = connection.prepareStatement("INSERT INTO item (code,description,unit_price,qty_On_Hand) VALUES (?,?,?,?)");
            pst.setString(1,itemDTO.getCode());
            pst.setString(2,itemDTO.getDescription());
            pst.setBigDecimal(3, itemDTO.getPrice());
            pst.setInt(4,itemDTO.getQtyOnHand());

            DBConnectionPoll.releaseConnection(connection);
            PrintWriter out = resp.getWriter();
            if (pst.executeUpdate() ==1){
                resp.setContentType("application/json");
                out.print(jsonb.toJson(itemDTO.getCode()));
            }else{
                throw  new RuntimeException("Process failed");
            }

        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*if (req.getContentType().equals("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }*/

        try{
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            if (itemDTO.getCode() == null || itemDTO.getCode().matches("Ii\\d{3}")){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (itemDTO.getDescription() == null || itemDTO.getDescription().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (itemDTO.getPrice() ==null || itemDTO.getPrice().compareTo(new BigDecimal(0))<0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (itemDTO.getQtyOnHand()<0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Connection connection = DBConnectionPoll.getConnection();
            PreparedStatement pst = connection.prepareStatement("UPDATE item SET description = ? , unit_price =? , qty_on_hand =? WHERE code = ?");
            pst.setString(1,itemDTO.getDescription());
           pst.setBigDecimal(2,itemDTO.getPrice());
           pst.setInt(3,itemDTO.getQtyOnHand());
           pst.setString(4,itemDTO.getCode());

           DBConnectionPoll.releaseConnection(connection);
            PrintWriter out = resp.getWriter();
            if (pst.executeUpdate() ==1){
               out.print(jsonb.toJson(itemDTO.getCode() + "hase been updated"));
           }else{
                new RuntimeException("error");
            }


        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if (code == null || code.matches("Ii\\d{3}")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Connection connection = DBConnectionPoll.getConnection();
            PreparedStatement pst = connection.prepareStatement("DELETE FROM item WHERE code=?");
            pst.setString(1,code);

            DBConnectionPoll.releaseConnection(connection);
            PrintWriter out = resp.getWriter();
            if (pst.executeUpdate() ==1){
                out.println(jsonb.toJson(code + "has been deleted"));
            }else{
                throw  new RuntimeException("error in deleting process");
            }

        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
