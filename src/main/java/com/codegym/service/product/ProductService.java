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
    public Iterable<Product> findAllByCategoryId(Long id) {
        return productRepository.findAllByCategoryId(id);
    }

    @Override
    public Iterable<Product> findAllByNameContaining(String namePattern) {
        return productRepository.findAllByNameContaining(namePattern);
    }

    @Override
    public Iterable<Product> findAllByCategoryNameContaining(String namePattern) {
        return productRepository.findAllByCategoryNameContaining("%" + namePattern + "%");
    }
}
