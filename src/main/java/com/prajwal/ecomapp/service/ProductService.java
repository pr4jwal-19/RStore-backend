package com.prajwal.ecomapp.service;

import com.prajwal.ecomapp.model.Product;
import com.prajwal.ecomapp.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(Integer id) {
        return productRepo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return productRepo.save(product);
    }

    public Product updateProduct(Integer id, Product product, MultipartFile imageFile) throws IOException {

        product.setId(id);
        product.setImageData(imageFile.getBytes());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());

        // .save() -- Updates the product if it exists or creates new if not found
        return productRepo.save(product);
    }

    public void deleteProduct(Integer id) {
        productRepo.deleteById(id);
    }

    public List<Product> searchProduct(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return productRepo.findAll(); // Return all products if no keyword is provided
        }
        return productRepo.searchProduct(keyword);
    }
}
