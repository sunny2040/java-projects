package com.surge.vms.repositories;



import org.springframework.data.repository.CrudRepository;

import com.surge.vms.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
}