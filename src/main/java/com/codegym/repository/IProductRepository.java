package com.codegym.repository;

import com.codegym.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query(value = "select p from Product as p where p.category.id = ?1")
    Page<Product> findAllByCategoryId(Long category_id, Pageable pageable);

    @Query(value = "select p from Product as p where p.category.id = ?1")
    Iterable<Product> findAllByCategoryId(Long category_id);

    Page<Product> findAllByNameContaining(String name, Pageable pageable);

    @Query(value = "select new Product(p.id, p.name, p.price, p.description,p.image, p.category) "+
            "from Product as p, Category as c " +
            "where c.name like ?1")
    Page<Product> findAllByCategoryNameContaining(String namePattern, Pageable pageable);

}
