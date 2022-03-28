package com.codegym.service.product;

import com.codegym.model.Product;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService extends IGeneralService<Product> {

    Page<Product> findAllByCategoryId(Long id, Pageable pageable);

    Page<Product> findAllByNameContaining(String namePattern, Pageable pageable);

    Page<Product> findAllByCategoryNameContaining(String namePattern, Pageable pageable);

    Page<Product> advancedSearch(String namePattern, Pageable pageable);
}
