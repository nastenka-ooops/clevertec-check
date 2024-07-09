package ru.clevertec.check.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.config.DatabaseConfig;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.repository.database.DatabaseProductRepository;
import ru.clevertec.check.service.ProductService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private ProductService productService;
    private ObjectMapper objectMapper;

    public ProductServlet() {
    }

    public ProductServlet(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.objectMapper = new ObjectMapper();
        try {
            this.productService = new ProductService(new DatabaseProductRepository(new DatabaseConfig().getConnection()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String productId = req.getParameter("id");

        String sortBy = req.getParameter("sortBy");

        resp.setContentType("application/json");

        if (productId != null && !productId.isEmpty()) {
            int id = Integer.parseInt(productId);
            Optional<Product> product = productService.getProductById(id);
            if (product.isPresent()) {
                resp.getWriter().print(objectMapper.writeValueAsString(product.get()));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else if (sortBy != null) {
            List<Product> products = productService.getSortedProducts(sortBy);
            resp.getWriter().print(objectMapper.writeValueAsString(products));
        } else {
            List<Product> products = productService.getAllProducts();
            resp.getWriter().print(objectMapper.writeValueAsString(products));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Product product = objectMapper.readValue(req.getReader(), Product.class);
        productService.createProduct(product);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String productId = req.getParameter("id");
        if (productId != null && !productId.isEmpty()) {
            int id = Integer.parseInt(productId);
            Product updatedProduct = objectMapper.readValue(req.getReader(), Product.class);
            updatedProduct.setId(id);
            boolean updated = productService.updateProductById(id, updatedProduct);
            if (updated) {
                resp.getWriter().print(objectMapper.writeValueAsString(updatedProduct));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("id");
        if (productId != null && !productId.isEmpty()) {
            int id = Integer.parseInt(productId);
            boolean deleted = productService.deleteProductById(id);
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
