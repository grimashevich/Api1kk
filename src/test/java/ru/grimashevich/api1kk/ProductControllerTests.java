package ru.grimashevich.api1kk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.grimashevich.api1kk.entity.Product;
import ru.grimashevich.api1kk.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    private static final String API_URL_CREATE_PRODUCT = "/api/v1/product/create";
    private static final String API_URL_CREATE_ALL_PRODUCT = "/api/v1/product/create_all";
    private static final long STEP = 1000L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository; //todo remove

    @Test
    void addProductsTest() throws Exception {
        for (long i = 1; i <= 100_000; i+= STEP) {
            mockMvc.perform(post(API_URL_CREATE_ALL_PRODUCT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(getProductList(i, i + STEP - 1))))
                    .andExpect(status().isCreated());
        }
        System.out.println(1);
    }

    List<Product> getProductList(long fromId, long toId) {
        List<Product> productList = new ArrayList<>( (int) (toId - fromId) + 1);
        for (long i = fromId; i <= toId; i++) {
            Product product = new Product(i, i << 1);
            productList.add(product);
        }
        return productList;
    }
}
