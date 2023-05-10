package lk.ijse.dep7.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep7.util.DBConnectionPoll;

import java.io.IOException;

@WebServlet(urlPatterns = "/Test")
public class TempServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBConnectionPoll.releaseAllConnection();
    }
}
