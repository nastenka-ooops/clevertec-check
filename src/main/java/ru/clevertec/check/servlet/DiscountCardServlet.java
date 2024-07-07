package ru.clevertec.check.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.config.DatabaseConfig;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.repository.database.DatabaseDiscountCardRepository;
import ru.clevertec.check.service.DiscountCardService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/discountCards")
public class DiscountCardServlet extends HttpServlet {
    private DiscountCardService discountCardService;
    private ObjectMapper objectMapper;


    @Override
    public void init() throws ServletException {
        super.init();
        this.objectMapper = new ObjectMapper();
        try {
            this.discountCardService = new DiscountCardService(new DatabaseDiscountCardRepository(
                    new DatabaseConfig().getConnection()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String discountCardId = req.getParameter("id");
        resp.setContentType("application/json");
        if (discountCardId != null && !discountCardId.isEmpty()) {
            int id = Integer.parseInt(discountCardId);
            Optional<DiscountCard> discountCard = discountCardService.getDiscountCardById(id);
            if (discountCard.isPresent()) {
                resp.getWriter().print(objectMapper.writeValueAsString(discountCard.get()));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            List<DiscountCard> discountCards = discountCardService.getAllDiscountCards();
            resp.getWriter().print(objectMapper.writeValueAsString(discountCards));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DiscountCard discountCard = objectMapper.readValue(req.getReader(), DiscountCard.class);
        discountCardService.createDiscountCard(discountCard);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String discountCardId = req.getParameter("id");
        if (discountCardId != null && !discountCardId.isEmpty()) {
            int id = Integer.parseInt(discountCardId);
            DiscountCard updatedDiscountCard = objectMapper.readValue(req.getReader(), DiscountCard.class);
            updatedDiscountCard.setId(id);
            boolean updated = discountCardService.updateDiscountCardById(id, updatedDiscountCard);
            if (updated) {
                resp.getWriter().print(objectMapper.writeValueAsString(updatedDiscountCard));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String discountCardId = req.getParameter("id");
        if (discountCardId != null && !discountCardId.isEmpty()) {
            int id = Integer.parseInt(discountCardId);
            boolean deleted = discountCardService.deleteDiscountCardById(id);
            if (deleted) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
