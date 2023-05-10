package lk.ijse.dep7.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lk.ijse.dep7.util.DBConnectionPoll;
import lk.ijse.dep7.dto.Customer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    private Jsonb jsonb = JsonbBuilder.create();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            Connection connection = DBConnectionPoll.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer");
            ArrayList<Customer> customers = new ArrayList<>();

            while (resultSet.next()){
                customers.add(new Customer(resultSet.getString("id"),
                                            resultSet.getString("name"),
                                            resultSet.getString("address")));

            }
            DBConnectionPoll.releaseConnection(connection);
            String json = jsonb.toJson(customers);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.println(json);

        } catch (SQLException e) {
            // TODO: 2023-05-07 handle exception
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getContentType() ==null || !req.getContentType().equals("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try{
            Customer customer = jsonb.fromJson(req.getReader(), Customer.class);

            if (customer.getId() == null || customer.getId().matches("cC\\d{3}")){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (customer.getName() == null || customer.getName().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
                
            } else if (customer.getAddress() ==null || customer.getAddress().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Connection connection = DBConnectionPoll.getConnection();
            PreparedStatement pst = connection.prepareStatement("INSERT INTO customer (id,name,address) VALUES (?,?,?)");
            pst.setString(1, customer.getId());
            pst.setString(2, customer.getName());
            pst.setString(3, customer.getAddress());

            PrintWriter out = resp.getWriter();
            if (pst.executeUpdate() == 1) {
                out.print(jsonb.toJson(customer.getId()));

            } else {
                throw new RuntimeException("Failed save the customer");
            }
            DBConnectionPoll.releaseConnection(connection);

        }catch (JsonbException ex){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
       } catch (SQLException | RuntimeException e) {
           throw new RuntimeException(e);
       }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType().equals("application/jason")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Customer customer = jsonb.fromJson(req.getReader(), Customer.class);

        if (customer.getId() == null || customer.getId().matches("cC\\d{3}")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;

        } else if (customer.getAddress() ==null || customer.getAddress().trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        try{
            Connection connection = DBConnectionPoll.getConnection();
            PreparedStatement pst = connection.prepareStatement("update customer SET name=?, address=? WHERE id =?");
            pst.setString(1,customer.getName());
            pst.setString(2,customer.getAddress());
            pst.setString(3,customer.getId());


            PrintWriter out = resp.getWriter();
            if (pst.executeUpdate() == 1){
                resp.setContentType("application/json");
                out.print("Customer has been updated");
            }else{
                throw new RuntimeException();
            }

            DBConnectionPoll.releaseConnection(connection);

        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null || id.matches("cC\\d{3}")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try{
            Connection connection = DBConnectionPoll.getConnection();
            PreparedStatement pst = connection.prepareStatement("DELETE FROM customer WHERE id = ?");
            pst.setString(1,id);
            PrintWriter out = resp.getWriter();
            Jsonb jsonb = JsonbBuilder.create();
            if (pst.executeUpdate() ==1){
                resp.setContentType("application/jason");
                out.print(jsonb.toJson(id + "has been deleted"));
            }else{
                throw  new RuntimeException();
            }
            DBConnectionPoll.releaseConnection(connection);
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }


    }
}
