package org.okten.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.okten.demo.dto.ProductDto;
import org.okten.demo.entity.Product;
import org.okten.demo.entity.ProductAvailability;
import org.okten.demo.mapper.ProductMapper;
import org.okten.demo.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // enable @Mock, @Spy, @InjectMocks annotations
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
//    ProductRepository productRepository = Mockito.mock(ProductRepository.class);

    @Spy
    private ProductMapper productMapper = new ProductMapper();
//    ProductMapper productMapper = Mockito.spy(new ProductMapper());

    @InjectMocks
    private ProductService productService;
//    ProductService productService = new ProductService(productRepository, productMapper);

    @Captor
    private ArgumentCaptor<Product> productCaptor;
//    ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

    @Test
    void createProduct_shouldSaveProduct() {
        ProductDto productToCreate = ProductDto.builder()
                .name("product name")
                .description("product description")
                .price(1.99)
                .category("product category")
                .availability(ProductAvailability.AVAILABLE)
                .owner("product owner")
                .build();

        // when() is static method from org.mockito.Mockito class
        when(productRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0, Product.class));

        productService.createProduct(productToCreate);


        verify(productRepository).save(productCaptor.capture());

        Product productToSave = productCaptor.getValue();
        // assertEquals() is static method from org.junit.jupiter.api.Assertions class
        assertEquals(productToCreate.getName(), productToSave.getName());
        assertEquals(productToCreate.getPrice(), productToSave.getPrice());
        assertEquals(productToCreate.getAvailability(), productToSave.getAvailability());
        assertEquals(productToCreate.getOwner(), productToSave.getOwner());

        // verify() is static method from org.mockito.Mockito class
        verify(productMapper).mapToEntity(any());
        verify(productMapper).mapToDto(any());
    }
}