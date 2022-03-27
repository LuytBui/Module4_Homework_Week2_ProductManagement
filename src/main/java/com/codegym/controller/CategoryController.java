package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @GetMapping("/list")
    public ModelAndView showCategoryList(@PageableDefault(size = 10) Pageable pageable){
        ModelAndView modelAndView= new ModelAndView("category/list");
        modelAndView.addObject("categories", categoryService.findAll(pageable));
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showCreateCategoryForm(){
        ModelAndView modelAndView = new ModelAndView("category/create");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

    @PostMapping("/add")
    public String createCategory(@ModelAttribute Category category){
        categoryService.save(category);
        return "redirect:/categories/list";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView showEditCategoryForm(@PathVariable Long id){
        Optional<Category> category = categoryService.findByID(id);
        if (!category.isPresent())
            return new ModelAndView("redirect:/categories");

        ModelAndView modelAndView = new ModelAndView("category/edit");
        modelAndView.addObject("category", category.get());
        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public String editCategory(@PathVariable Long id, @ModelAttribute Category category){
        Optional<Category> origin = categoryService.findByID(id);

        if (origin.isPresent()){
            Category originCategory = origin.get();
            originCategory.setName(category.getName());
            categoryService.save(originCategory);
        }
        return "redirect:/categories/list";
    }

    @GetMapping("/{id}/delete")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Optional<Category> category = categoryService.findByID(id);
        if (!category.isPresent())
            return new ModelAndView("redirect:/categories");
        ModelAndView modelAndView = new ModelAndView("category/delete");
        modelAndView.addObject("category", category.get());
        return modelAndView;
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id){
        Optional<Category> category = categoryService.findByID(id);
        if (category.isPresent()){

            categoryService.deleteById(id);
        }

        return "redirect:/categories/list";
    }
}
