package ru.clevertec.check.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.config.DatabaseConfig;
import ru.clevertec.check.dto.CheckRequest;
import ru.clevertec.check.entity.Check;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.exception.CheckException;
import ru.clevertec.check.repository.database.DataBaseCheckRepository;
import ru.clevertec.check.repository.database.DatabaseDiscountCardRepository;
import ru.clevertec.check.repository.database.DatabaseProductRepository;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.util.CsvUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/check")
public class CheckServlet extends HttpServlet {

    private CheckService checkService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        this.objectMapper = new ObjectMapper();
        try {
            Connection connection = new DatabaseConfig().getConnection();
            this.checkService = new CheckService(new ProductService(new DatabaseProductRepository(connection)),
                    new DiscountCardService(new DatabaseDiscountCardRepository(connection)),
                    new DataBaseCheckRepository(connection));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        List<Check> checks = checkService.getAllChecks();
        resp.getWriter().print(objectMapper.writeValueAsString(checks));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CheckRequest checkRequest = objectMapper.readValue(req.getInputStream(), CheckRequest.class);

        resp.setContentType("text/csv");
        resp.setHeader("Content-Disposition", "attachment; filename=\"check.csv\"");

        try {
            Check check = checkService.createCheck(checkRequest);
            checkService.saveCheck(check);
            DiscountCard discountCard = checkService.getDiscountCard(checkRequest.getDiscountCard());
            List<String> checkLines = CsvUtil.saveCheck(check, discountCard);
            try (PrintWriter writer = resp.getWriter()) {
                for (String line : checkLines) {
                    writer.println(line);
                }
            }
        } catch (SQLException | CheckException e) {
            List<String> checkLines = CsvUtil.saveError(e.getMessage());
            try (PrintWriter writer = resp.getWriter()) {
                for (String line : checkLines) {
                    writer.println(line);
                }
            }
        }
    }
}
