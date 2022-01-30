package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book save(Book book);
    Optional<Book> getBookByIsbn(int isbn);

    @Query(value = "select round(avg(score),2) from Review where book_id = :id and deleted = false")
    BigDecimal calcScoreAvg(@Param("id") long id);
}
