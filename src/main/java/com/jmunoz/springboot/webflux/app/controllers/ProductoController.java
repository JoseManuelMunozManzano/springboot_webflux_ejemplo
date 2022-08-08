package com.jmunoz.springboot.webflux.app.controllers;

import com.jmunoz.springboot.webflux.app.models.dao.ProductoDao;
import com.jmunoz.springboot.webflux.app.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;

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
        Flux<Producto> productos = dao.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        });

        productos.subscribe(prod -> log.info(prod.getNombre()));

        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de productos");

        return "listar";
    }

    // Formas de manejar la contrapresión en Thymeleaf y con Flux
    //
    // 1. Usando una variable de contexto de Thymeleaf que permite manejar la cantidad de elementos en el buffer,
    //    es decir, se va a configurar el tamaño del buffer en CANTIDAD DE ELEMENTOS que se van a ir emitiendo.
    //    Esta variable de contexto de la vista se conoce como Reactive Data Driver. Es un controlador que maneja
    //    internamente eventos con los datos del stream.
    //    Es una de las maneras más potentes y recomendadas cuando hay algún tipo de delay o estamos recibiendo
    //    una gran cantidad de elementos.
    @GetMapping("/listar-datadriver")
    public String listarDataDriver(Model model) {
        // Se hace un delay para emular que se tarda mucho en obtener la información pedida.
        // Como son 9 elementos y con cada uno vamos a tardar 1sg, se va a tirar 9sg para terminar.
        Flux<Producto> productos = dao.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        }).delayElements(Duration.ofSeconds(1));

        productos.subscribe(prod -> log.info(prod.getNombre()));

        // Pero instanciando la variable de contexto vamos mostrando los elementos de 2 en 2, conforme vamos
        // teniendolos
        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 2));
        model.addAttribute("titulo", "Listado de productos");

        return "listar";
    }

    // 2. Chunked.
    //    El buffer se MIDE EN UN TAMAÑO EN BYTES y no en cantidad de elementos.
    //
    //    Modalidad 1: Se establece un límite máximo
    //    Se utiliza cuando el stream tiene una gran cantidad de elementos, miles.
    //
    //    Modalidad 2: Full o completo
    //    No se configura ningún límite para el tamaño máximo del buffer o chunk. Toda la salida de la plantilla se
    //    genera como un solo fragmento, de un solo golpe.
    //    Solo se recomienda cuando no hay muchos datos que renderizar en la vista o se usa paginación.
    @GetMapping("/listar-full")
    public String listarFull(Model model) {
        // Se usa repeat para emular que hay una gran cantidad de elementos en el flujo
        Flux<Producto> productos = dao.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        }).repeat(5000);

        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de productos");

        return "listar";
    }


}
