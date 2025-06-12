package com.sabrina.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sabrina.Models.Film;
import com.sabrina.Services.FilmService;

@RestController
@RequestMapping("/film")
@CrossOrigin(origins = "*")
public class FilmController {
	
    @Autowired
    private FilmService filmService; 

	

	@GetMapping("/filmlist")
	public List<Film> filmlist() {
		return filmService.list(); // richiamo il metdo che ho messo prima nel service 
	}
	
	@GetMapping("/{id}")
	public Film getFilmById(@PathVariable Long id) {
	    return filmService.getById(id);
	}
}
