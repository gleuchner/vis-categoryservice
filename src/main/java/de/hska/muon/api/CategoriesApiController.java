package de.hska.muon.api;


import de.hska.muon.model.Category;
import de.hska.muon.model.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoriesApiController  {

    @Autowired
    private CategoryRepo repo;

    @RequestMapping(value = "/categories",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<Iterable<Category>> CategoryGet() {
        Iterable<Category> ret = repo.findAll();
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/categories/{categoryId}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<Category> CategoryIdGet(
            @PathVariable("categoryId") Integer categoryId) {

        Category ret = repo.findOne(categoryId);
        if (ret != null) {
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("#oauth2.hasScope('write')")
    @RequestMapping(value = "/categories/{categoryId}",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> categoriesIdDelete(
            @PathVariable("categoryId") Integer categoryId,
            @RequestHeader(value = "userId", required = true) Integer userId) {
        Category ret = repo.findOne(categoryId);

        if (ret != null) {
            repo.delete(categoryId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("#oauth2.hasScope('write')")
    @RequestMapping(value = "/categories",
            produces = { "application/json" },
            method = RequestMethod.POST)
    public ResponseEntity<Category> categoriesPost(
            @RequestBody Category newCategory,
            @RequestHeader(value = "userId", required = true) Integer userId) {

        Category ret = repo.save(newCategory);
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

}
