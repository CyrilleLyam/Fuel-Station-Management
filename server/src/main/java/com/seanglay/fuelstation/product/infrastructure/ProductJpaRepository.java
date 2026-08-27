package com.seanglay.fuelstation.product.infrastructure;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seanglay.fuelstation.product.domain.Product;

interface ProductJpaRepository extends JpaRepository<Product, Long> {

	boolean existsBySku(String sku);

	Page<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku, Pageable pageable);

	@Query("""
			select p from Product p
			where (:cursor is null or p.id > :cursor)
			and (lower(p.name) like lower(concat('%', :keyword, '%'))
				or lower(p.sku) like lower(concat('%', :keyword, '%')))
			order by p.id asc
			""")
	List<Product> searchAfter(@Param("keyword") String keyword, @Param("cursor") Long cursor, Pageable pageable);

}
