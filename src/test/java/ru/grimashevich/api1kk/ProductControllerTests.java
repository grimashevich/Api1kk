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
import java.util.concurrent.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    private static final String API_BASE_URL = "/api/v1/product";
    private static final String API_CREATE_PRODUCT_URL = API_BASE_URL + "/create";
    private static final String API_CREATE_ALL_PRODUCT_URL = API_BASE_URL + "/create_all";
    private static final String API_GET_PRODUCT_BY_ID_URL = API_BASE_URL + "/get/";

    private static final int TEST_RECORDS_COUNT = 100_000;
    private static final long INSERT_STEP = 1000L;

    private static final int THREAD_COUNT = 100;
    private static final int REQUEST_COUNT = 300_000;
    private static final int REQUEST_PER_THREAD = REQUEST_COUNT / THREAD_COUNT;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository; //todo remove


    void addProducts() throws Exception {
        for (long i = 1; i <= TEST_RECORDS_COUNT; i+= INSERT_STEP) {
            mockMvc.perform(post(API_CREATE_ALL_PRODUCT_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(getProductList(i, i + INSERT_STEP - 1))))
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

    @Test
    void getByIdTest() throws Exception {
        addProducts();
        ConcurrentLinkedQueue<Long> responseTime = new ConcurrentLinkedQueue<>();
        List<Callable<Void>> taskList = new ArrayList<>(THREAD_COUNT);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            taskList.add(() -> {
                for (int j = 0; j < REQUEST_PER_THREAD; j++) {
                    int randomId = ThreadLocalRandom.current().nextInt(TEST_RECORDS_COUNT + 1);
                    String getUrl = API_GET_PRODUCT_BY_ID_URL + randomId;
                    long startTime = System.nanoTime();
                    mockMvc.perform(get(getUrl)
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk());
                    long endTime = System.nanoTime();
                    responseTime.add(endTime - startTime);
                }
                return null;
            });
        }
        long totalStartTime = System.nanoTime();
        executorService.invokeAll(taskList);
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        long totalEndTime = System.nanoTime();
        System.out.println("total nano: :" + (totalEndTime - totalStartTime));
    }
}
