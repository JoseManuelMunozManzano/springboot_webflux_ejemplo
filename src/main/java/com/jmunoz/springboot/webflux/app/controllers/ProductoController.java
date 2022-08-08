package com.jmunoz.springboot.webflux.app.controllers;

import com.jmunoz.springboot.webflux.app.models.dao.ProductoDao;
import com.jmunoz.springboot.webflux.app.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping({"/listar", "/"})
    public String listar(Model model) {
        // Se añade un map para transformar el nombre del producto en mayúsculas
        Flux<Producto> productos = dao.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        });

        // Se va a agregar otro subscriptor, el log con el nombre
        productos.subscribe(prod -> log.info(prod.getNombre()));

        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de productos");

        return "listar";
    }
}
