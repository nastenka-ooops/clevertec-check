package ru.clevertec.check.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.service.DiscountCardService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DiscountCardServletTest {
    @InjectMocks
    private DiscountCardServlet discountCardServlet;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DiscountCardService discountCardService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter responseWriter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        responseWriter = new StringWriter();
    }

    @Test
    public void testDoGetWithDiscountCardId() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(discountCardService.getDiscountCardById(1)).thenReturn(Optional.of(
                new DiscountCard(1, 1111, 3)));
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        discountCardServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(
                new DiscountCard(1, 1111, 3));

        assertEquals(expectedJson, responseWriter.toString().trim());
    }

    @Test
    public void testDoGetWithNoDiscountCardId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        List<DiscountCard> mockProducts = List.of(
                new DiscountCard(1, 1111, 3),
                new DiscountCard(2, 2222, 5)
        );
        when(discountCardService.getAllDiscountCards()).thenReturn(mockProducts);

        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        discountCardServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(mockProducts);

        assertEquals(expectedJson, responseWriter.toString().trim());
    }

    @Test
    public void testDoPost() throws Exception {
        DiscountCard newDiscountCard = new DiscountCard(1, 1111, 3);
        String jsonProduct = objectMapper.writeValueAsString(newDiscountCard);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonProduct)));

        discountCardServlet.doPost(request, response);

        verify(discountCardService).createDiscountCard(newDiscountCard);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testDoPutWithDiscountCardId() throws Exception {
        String discountCardId = "1";
        DiscountCard updatedDiscountCard = new DiscountCard(1, 1111, 3);
        String jsonProduct = objectMapper.writeValueAsString(updatedDiscountCard);

        when(request.getParameter("id")).thenReturn(discountCardId);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonProduct)));
        when(discountCardService.updateDiscountCardById(1, updatedDiscountCard)).thenReturn(true);
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        discountCardServlet.doPut(request, response);

        verify(discountCardService).updateDiscountCardById(1, updatedDiscountCard);
        assertEquals(jsonProduct, responseWriter.toString().trim());
    }

    @Test
    public void testDoPutWithInvalidDiscountCardId() throws Exception {
        String discountCardId = "1";
        DiscountCard updatedDiscountCard = new DiscountCard(1, 1111, 3);
        String jsonProduct = objectMapper.writeValueAsString(updatedDiscountCard);

        when(request.getParameter("id")).thenReturn(discountCardId);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonProduct)));
        when(discountCardService.updateDiscountCardById(1, updatedDiscountCard)).thenReturn(false);

        discountCardServlet.doPut(request, response);

        verify(discountCardService).updateDiscountCardById(1, updatedDiscountCard);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void testDoPutWithNoDiscountCardId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        discountCardServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoDeleteWithDiscountCardId() throws Exception {
        String discountCardId = "1";

        when(request.getParameter("id")).thenReturn(discountCardId);
        when(discountCardService.deleteDiscountCardById(1)).thenReturn(true);

        discountCardServlet.doDelete(request, response);

        verify(discountCardService).deleteDiscountCardById(1);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    public void testDoDeleteWithInvalidDiscountCardId() throws Exception {
        String discountCardId = "1";

        when(request.getParameter("id")).thenReturn(discountCardId);
        when(discountCardService.deleteDiscountCardById(1)).thenReturn(false);

        discountCardServlet.doDelete(request, response);

        verify(discountCardService).deleteDiscountCardById(1);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void testDoDeleteWithNoDiscountCardId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        discountCardServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}
