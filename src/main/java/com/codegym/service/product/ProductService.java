package com.codegym.service.product;


import com.codegym.model.Product;
import com.codegym.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findByID(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public boolean save(Product product) {
        productRepository.save(product);
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        productRepository.deleteById(id);
        return true;
    }


    @Override
    public Page<Product> findAllByCategoryId(Long id, Pageable pageable) {
        return productRepository.findAllByCategoryId(id, pageable);
    }

    @Override
    public Page<Product> findAllByNameContaining(String namePattern, Pageable pageable) {
        return productRepository.findAllByNameContaining(namePattern, pageable);
    }

    @Override
    public Page<Product> findAllByCategoryNameContaining(String namePattern, Pageable pageable) {
        return productRepository.findAllByCategoryNameContaining(namePattern, pageable);
    }

    @Override
    public Page<Product> advancedSearch(String namePattern, Pageable pageable) {
        return productRepository.advancedSearch(namePattern, pageable);
    }
}
