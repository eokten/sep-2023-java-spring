package org.okten.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.okten.demo.config.TestcontainersConfiguration;
import org.okten.demo.dto.ProductDto;
import org.okten.demo.entity.Product;
import org.okten.demo.entity.ProductAvailability;
import org.okten.demo.entity.User;
import org.okten.demo.repository.ProductRepository;
import org.okten.demo.repository.UserRepository;
import org.okten.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = TestcontainersConfiguration.class
)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @DynamicPropertySource
    static void addCategoriesToConfiguration(DynamicPropertyRegistry registry) {
        registry.add("reference-data.categories", () -> "sample_category1,sample_category2");
    }

    @AfterEach
    void deleteUser() {
        if (user != null) {
            userRepository.deleteById(user.getId());
        }
    }

    void createUser(String role) {
        user = userRepository.save(User.builder()
                .username("test@test.com")
                .role(role)
                .build());
    }

    @SneakyThrows
    @Test
    void getProduct_whenProductExists_shouldReturn200() {
        createUser("BUYER");

        Product createdProduct = productRepository.save(Product.builder()
                .name("test product")
                .build());

        mockMvc
                .perform(get("/v1/products/{id}", createdProduct.getId())
                        .header("Authorization", "Bearer " + jwtService.generateAccessToken(user)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void createProduct_whenUserIsSeller_shouldCreateProduct() {
        createUser("SELLER");

        String requestBody = objectMapper.writeValueAsString(ProductDto.builder()
                .name("product name")
                .description("product description")
                .price(10.99)
                .availability(ProductAvailability.AVAILABLE)
                .category("sample_category1")
                .owner(user.getUsername())
                .build());

        String responseBody = mockMvc
                .perform(post("/v1/products")
                        .header("Authorization", "Bearer " + jwtService.generateAccessToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductDto productFromResponse = objectMapper.readValue(responseBody, ProductDto.class);

        assertTrue(productRepository.existsById(productFromResponse.getId()));
    }

    @SneakyThrows
    @Test
    void createProduct_whenUserIsBuyer_shouldReturn400() {
        createUser("BUYER");

        String requestBody = objectMapper.writeValueAsString(ProductDto.builder()
                .name("product name")
                .description("product description")
                .price(10.99)
                .availability(ProductAvailability.AVAILABLE)
                .category("sample_category1")
                .owner(user.getUsername())
                .build());

        mockMvc
                .perform(post("/v1/products")
                        .header("Authorization", "Bearer " + jwtService.generateAccessToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}