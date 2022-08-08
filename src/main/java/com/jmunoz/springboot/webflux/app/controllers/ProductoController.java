package com.jmunoz.springboot.webflux.app.controllers;

import com.jmunoz.springboot.webflux.app.models.dao.ProductoDao;
import com.jmunoz.springboot.webflux.app.models.documents.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

// Registrando la clase como controlador de Spring.
// Por debajo se usa el stack reactivo en vez de usar el stack servlet que se usa en Spring Web MVC.
// Recordar que siendo reactivo no se bloquean las peticiones y se trabaja de forma asíncrona.
@Controller
public class ProductoController {

    @Autowired
    private ProductoDao dao;

    @GetMapping({"/listar", "/"})
    public String listar(Model model) {
        // Esta es la única diferencia, que se usa un Flux.
        // Siendo este el observable, no es necesario subscribirse.
        Flux<Producto> productos = dao.findAll();

        // Aquí, por debajo, se va a subscribir de forma automática, es decir,
        // mostrar los datos en una plantilla con Thymeleaf.
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de productos");

        return "listar";
    }
}
