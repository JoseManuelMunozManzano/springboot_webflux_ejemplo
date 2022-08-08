package com.jmunoz.springboot.webflux.app.models.dao;

import com.jmunoz.springboot.webflux.app.models.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

// La diferencia con un Repository normal es que se trabaja con tipos de datos reactivos: Mono y Flux,
// dependiendo del tipo de operación. Si se devuelve una colección se usa Flux y si solo se devuelve un dato
// se usa Mono
public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {
}
