package com.codegym.service.category;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.repository.ICategoryRepository;
import com.codegym.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService implements ICategoryService{
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IProductRepository productRepository;

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Optional<Category> findByID(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public boolean save(Category category) {
        categoryRepository.save(category);
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        Iterable<Product> products = productRepository.findAllByCategoryId(id);
        for (Product product: products) {
            product.setCategory(null);
            productRepository.save(product);
        }
        categoryRepository.deleteById(id);
        return true;
    }
}
