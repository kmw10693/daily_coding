package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
