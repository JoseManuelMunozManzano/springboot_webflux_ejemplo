package com.jmunoz.springboot.webflux.app;

import com.jmunoz.springboot.webflux.app.models.dao.ProductoDao;
import com.jmunoz.springboot.webflux.app.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoDao dao;

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class  );

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Cada producto lo vamos a guardar en la BD Mongo

		Flux.just(new Producto("TV Panasonic Pantalla LCD", 456.89),
				new Producto("Sony Camara HD Digital", 177.89),
				new Producto("Apple iPod", 46.89),
				new Producto("Sony Notebook", 846.89),
				new Producto("Hewlett Packard Multifuncional", 200.89),
				new Producto("Bianchi Bicicleta", 70.89),
				new Producto("HP Notebook Omen 17", 2500.89),
				new Producto("Mica Cómoda 5 Cajones", 150.89),
				new Producto("TV Sony Bravia OLED 4K Ultra HD", 2255.89)
		)
				// El problema de Map es que save genera un Mono, por lo que ahora NO tenemos un flujo de productos,
				// sino un flujo de Mono de productos.
				//
				// Por eso se usa flatMap, que obtiene un observable (de Flux o Mono) y lo aplana, convirtiéndolo
				// en nuestro objeto producto
				.flatMap(producto -> dao.save(producto))
				.subscribe(producto -> log.info("insert: " + producto.getId() + " " + producto.getNombre()));
	}
}
