package com.codegym.repository;

import com.codegym.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query(value = "select p from Product as p where p.category.id = ?1")
    Iterable<Product> findAllByCategoryId(Long category_id);

    Iterable<Product> findAllByNameContaining(String name);

    @Query(value = "select p.id, p.name, p.price, p.description, p.image, p.category_id " +
            "from Product as p join Category as c on p.category_id = c.id " +
            "where c.name like ?1", nativeQuery = true)
    Iterable<Product> findAllByCategoryNameContaining(String namePattern);
}
