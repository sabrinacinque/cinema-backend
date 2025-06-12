package com.sabrina.Controllers;

import com.sabrina.Models.Posto;
import com.sabrina.Services.PostoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posti")
@CrossOrigin(origins = "*")
public class PostoController {

	@Autowired
	private PostoService postoService;

    @GetMapping("/list")
    public List<Posto> list() {
        return postoService.list();
    }

    @GetMapping("/{id}")
    public Posto getById(@PathVariable int id) {
        return postoService.getById(id);
    }
    
  
    @GetMapping("/film/{id_film}")
    public List<Posto> getPostiByFilmId(@PathVariable int id_film) {
        return postoService.getPostiByFilmId(id_film);
    }
}
