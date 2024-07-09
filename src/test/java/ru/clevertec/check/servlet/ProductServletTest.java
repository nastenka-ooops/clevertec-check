package ru.clevertec.check.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.service.ProductService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServletTest {
    @InjectMocks
    private ProductServlet productServlet;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter responseWriter;

    @BeforeEach
    public void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);
        responseWriter = new StringWriter();
    }

    @Test
    public void testDoGetWithProductId() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(productService.getProductById(1)).thenReturn(Optional.of(
                new Product(1, "Test Product", 10, 30, true)));
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        productServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(
                new Product(1, "Test Product", 10, 30, true));

        assertEquals(expectedJson, responseWriter.toString().trim());

    }

    @Test
    public void testDoGetWithNoProductId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        List<Product> mockProducts = List.of(
                new Product(1, "Test Product", 100, 0, false),  // Assuming no need for the quantity
                new Product(2, "Another Product", 50, 0, false) // Assuming no need for the quantity
        );
        when(productService.getAllProducts()).thenReturn(mockProducts);

        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        productServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(mockProducts);

        assertEquals(expectedJson, responseWriter.toString().trim());
    }

    @Test
    public void testDoPost() throws Exception {
        Product newProduct = new Product(1, "Test Product", 100, 0, false);
        String jsonProduct = objectMapper.writeValueAsString(newProduct);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonProduct)));

        productServlet.doPost(request, response);

        verify(productService).createProduct(newProduct);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testDoPutWithProductId() throws Exception {
        String productId = "1";
        Product updatedProduct = new Product(1, "Updated Product", 150, 0, false);
        String jsonProduct = objectMapper.writeValueAsString(updatedProduct);

        when(request.getParameter("id")).thenReturn(productId);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonProduct)));
        when(productService.updateProductById(1, updatedProduct)).thenReturn(true);
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        productServlet.doPut(request, response);

        verify(productService).updateProductById(1, updatedProduct);
        assertEquals(jsonProduct, responseWriter.toString().trim());
    }

    @Test
    public void testDoPutWithInvalidProductId() throws Exception {
        String productId = "1";
        Product updatedProduct = new Product(1, "Updated Product", 150, 0, false);
        String jsonProduct = objectMapper.writeValueAsString(updatedProduct);

        when(request.getParameter("id")).thenReturn(productId);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonProduct)));
        when(productService.updateProductById(1, updatedProduct)).thenReturn(false);

        productServlet.doPut(request, response);

        verify(productService).updateProductById(1, updatedProduct);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void testDoPutWithNoProductId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        productServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoDeleteWithProductId() throws Exception {
        String productId = "1";

        when(request.getParameter("id")).thenReturn(productId);
        when(productService.deleteProductById(1)).thenReturn(true);

        productServlet.doDelete(request, response);

        verify(productService).deleteProductById(1);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    public void testDoDeleteWithInvalidProductId() throws Exception {
        String productId = "1";

        when(request.getParameter("id")).thenReturn(productId);
        when(productService.deleteProductById(1)).thenReturn(false);

        productServlet.doDelete(request, response);

        verify(productService).deleteProductById(1);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void testDoDeleteWithNoProductId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        productServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}
