package org.okten.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.okten.demo.entity.Product;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDao {

    private final EntityManager entityManager;

    @Transactional
    public Product createProduct(Product product) {
        entityManager.merge(product);
        return product;
    }

    public Product getProduct(Long id) {
        return entityManager.find(Product.class, id);
    }
}
