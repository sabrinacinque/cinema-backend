package com.sabrina.Controllers;

import com.sabrina.Models.Sala;
import com.sabrina.Services.SalaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
@CrossOrigin(origins = "*")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @GetMapping("/list")
    public List<Sala> list() {
        return salaService.list();
    }

    @GetMapping("/{id}")
    public Sala getById(@PathVariable int id) {
        return salaService.getById(id);
    }
    
    @GetMapping("/film/{id_film}")
    public Sala getSalaByFilmId(@PathVariable int id_film) {
        return salaService.getSalaByFilmId(id_film);
    }
}