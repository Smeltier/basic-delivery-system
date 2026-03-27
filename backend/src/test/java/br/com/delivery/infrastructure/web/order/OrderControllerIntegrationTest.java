package br.com.delivery.infrastructure.web.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "app.seed.enabled=true")
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndCancelOrderThroughHttpFlow() throws Exception {
        String createOrderPayload = """
            {
              "accountId": "11111111-1111-1111-1111-111111111111",
              "restaurantId": "33333333-3333-3333-3333-333333333333",
              "menuItemId": "44444444-4444-4444-4444-444444444444",
              "quantity": 1
            }
            """;

        MvcResult createOrderResult = mockMvc.perform(post("/orders/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createOrderPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderId").isNotEmpty())
            .andReturn();

        JsonNode responseBody = objectMapper.readTree(createOrderResult.getResponse().getContentAsString());
        String orderId = responseBody.get("orderId").asText();

        mockMvc.perform(delete("/orders/{orderId}", orderId))
            .andExpect(status().isNoContent());
    }
}
