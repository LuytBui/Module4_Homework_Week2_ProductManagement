package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ICategoryService categoryService;
    @Autowired
    IProductService productService;

    @Value("${file-upload}")
    private String uploadPath;

    @GetMapping("/list")
    public ModelAndView showList(@PageableDefault(value = 10) Pageable pageable,
                                 @RequestParam(name = "category") Optional<Long> category_id,
                                 @RequestParam(name = "q") Optional<String> q,
                                 @RequestParam(name = "searchType") Optional<String> searchType
    ) {
        ModelAndView modelAndView = new ModelAndView("product/list");
        if (category_id.isPresent()) {
            Page<Product> products = productService.findAllByCategoryId(category_id.get(), pageable);
            modelAndView.addObject("products", products);
            return modelAndView;
        } else if (q.isPresent() && searchType.isPresent()) {

            modelAndView.addObject("q", q.get());
            modelAndView.addObject("searchType", searchType.get());

            switch (searchType.get()) {
                default: { // advanced search
                    Page<Product> products = productService.advancedSearch(q.get(), pageable);
                    modelAndView.addObject("products", products);
                    break;
                }
                case "search-by-product-name": {
                    Page<Product> products = productService.findAllByNameContaining(q.get(), pageable);
                    modelAndView.addObject("products", products);
                    break;
                }
                case "search-by-category-name": {
                    Page<Product> products = productService.findAllByCategoryNameContaining(q.get(), pageable);
                    modelAndView.addObject("products", products);
                    break;
                }
            }
            return modelAndView;
        } else {
            Page<Product> products = productService.findAll(pageable);
            modelAndView.addObject("products", products);
            return modelAndView;
        }

    }

    @GetMapping("/add")
    public ModelAndView showAddProductForm(Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("product/create");
        modelAndView.addObject("product", new Product());
        Page<Category> categories = categoryService.findAll(pageable);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductForm productForm) {
        Product product = new Product();

        MultipartFile file = productForm.getImage();
        if (file.getSize() > 0) {
            String fileName = file.getOriginalFilename();
            fileName = addTimeStamp(fileName);
            try {
                FileCopyUtils.copy(file.getBytes(), new File(uploadPath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            product.setImage(fileName);
        }

        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        product.setCategory(productForm.getCategory());

        productService.save(product);

        return "redirect:/products/list";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView showEditProductForm(@PathVariable Long id, Pageable pageable) {
        Optional<Product> product = productService.findByID(id);
        if (!product.isPresent()) {
            return new ModelAndView("redirect:/products/list");
        }

        ModelAndView modelAndView = new ModelAndView("product/edit");
        modelAndView.addObject("product", product.get());
        Page<Category> categories = categoryService.findAll(pageable);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, @ModelAttribute ProductForm productForm) {
        Product product = productService.findByID(id).get();

        MultipartFile file = productForm.getImage();
        if (file.getSize() > 0) {
            String fileName = file.getOriginalFilename();
            fileName = addTimeStamp(fileName);
            try {
                FileCopyUtils.copy(file.getBytes(), new File(uploadPath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            product.setImage(fileName);
        }

        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        product.setCategory(productForm.getCategory());

        productService.save(product);

        return "redirect:/products/list";
    }

    @GetMapping("/{id}/delete")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<Product> product = productService.findByID(id);
        if (!product.isPresent())
            return new ModelAndView("redirect:/products");
        ModelAndView modelAndView = new ModelAndView("product/delete");
        modelAndView.addObject("product", product.get());
        return modelAndView;
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.findByID(id);
        if (product.isPresent()) {
            new File(uploadPath + product.get().getImage()).delete();
            productService.deleteById(id);
        }
        return "redirect:/products/list";
    }

    public String addTimeStamp(String str) {
        Long currentTime = System.currentTimeMillis();
        return currentTime + "_" + str;
    }
}
