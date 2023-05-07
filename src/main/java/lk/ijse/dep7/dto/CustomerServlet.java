package lk.ijse.dep7.dto;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep7.api.Customer;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Customer customer = new Customer("123", "Dasith", "galle");
        PrintWriter out = resp.getWriter();
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(customer);
        out.print(json);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().equals("application/json")){
            /*TODO: 2023-05-06 send an error massage*/
            return;
        }
        /*BufferedReader out = req.getReader();
        while ( (line = out.readLine()) != null){
            stringBuffer.append(line);
        }*/
        /*StringBuffer body = new StringBuffer();
        req.getReader().lines().forEach(line -> body.append(line));
        System.out.println(body);
*/
        Jsonb jsonb = JsonbBuilder.create();
        Customer customer = jsonb.fromJson(req.getReader(), Customer.class);
        System.out.println(customer.getId());
        System.out.println(customer.getName());
        System.out.println(customer.getAddress());


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
