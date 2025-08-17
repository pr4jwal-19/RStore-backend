package com.prajwal.ecomapp.repo;

import com.prajwal.ecomapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    // Additional query methods can be defined here if needed
    // List<Product> searchProductByBrand(String brand);
    // We can search using multiple fields like brand, name, category, etc.

    // JPQL -- stands for Java Persistence Query Language
    // JPQL is similar to SQL but operates on the entity objects rather than directly on the database tables.
    // class name and field names are used instead of table and column names.

    // For search feature
    @Query("SELECT p FROM Product p WHERE "
            + "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    List<Product> searchProduct(String keyword);

}
