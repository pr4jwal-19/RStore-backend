package com.prajwal.ecomapp.controller;

import com.prajwal.ecomapp.model.Product;
import com.prajwal.ecomapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    // Controller constructor to inject ProductService
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/fetch-all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {

        Product prodFound = productService.getProductById(id);

        if (prodFound != null){
            return new ResponseEntity<>(prodFound, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile) {

        try {
            Product resProd = productService.addProduct(product, imageFile);
            return new ResponseEntity<>(resProd, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetch/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        if (product != null && product.getImageData() != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", product.getImageType())
                    .body(product.getImageData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id,
                                                @RequestPart("product") Product product,
                                                @RequestPart("imageFile") MultipartFile imageFile) {

        Product updatedProd;
        try {
            updatedProd = productService.updateProduct(id, product, imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Product not found", HttpStatus.BAD_REQUEST);
        }
        if (updatedProd != null) {
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {

        Product deletedProduct = productService.getProductById(id);
        if (deletedProduct != null) {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword) {

        List<Product> products = productService.searchProduct(keyword);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

    }
}
