package com.lambdaschool.javatodos.controller;

import com.lambdaschool.javatodos.model.Todo;
import com.lambdaschool.javatodos.service.QuoteService;
import com.lambdaschool.javatodos.model.Todo;
import com.lambdaschool.javatodos.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/quotes")
public class QuotesController
{
    @Autowired
    QuoteService quoteService;

    @GetMapping(value = "/quotes", produces = {"application/json"})
    public ResponseEntity<?> listAllQuotes()
    {
        List<Todo> allQuotes = quoteService.findAll();
        return new ResponseEntity<>(allQuotes, HttpStatus.OK);
    }


    @GetMapping(value = "/quote/{quoteId}", produces = {"application/json"})
    public ResponseEntity<?> getQuote(@PathVariable Long quoteId)
    {
        Todo q = quoteService.findQuoteById(quoteId);
        return new ResponseEntity<>(q, HttpStatus.OK);
    }


    @GetMapping(value = "/username/{userName}", produces = {"application/json"})
    public ResponseEntity<?> findQuoteByUserName(@PathVariable String userName)
    {
        List<Todo> theQuotes = quoteService.findByUserName(userName);
        return new ResponseEntity<>(theQuotes, HttpStatus.OK);
    }



    @PostMapping(value = "/quote")
    public ResponseEntity<?> addNewQuote(@Valid @RequestBody Todo newQuote) throws URISyntaxException
    {
        newQuote = quoteService.save(newQuote);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newQuoteURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{quoteid}")
                .buildAndExpand(newQuote.getTodoid())
                .toUri();
        responseHeaders.setLocation(newQuoteURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @DeleteMapping("/quote/{id}")
    public ResponseEntity<?> deleteQuoteById(@PathVariable long id)
    {
        quoteService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
