package com.seanglay.fuelstation.product.presentation;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seanglay.fuelstation.iam.RequiresPermission;
import com.seanglay.fuelstation.product.application.ChangeProductPriceUseCase;
import com.seanglay.fuelstation.product.application.CreateProductUseCase;
import com.seanglay.fuelstation.product.application.DeleteProductUseCase;
import com.seanglay.fuelstation.product.application.GetProductUseCase;
import com.seanglay.fuelstation.product.application.ListProductsUseCase;
import com.seanglay.fuelstation.product.application.UpdateProductUseCase;
import com.seanglay.fuelstation.product.domain.Product;
import com.seanglay.fuelstation.product.presentation.dto.ChangeProductPriceRequest;
import com.seanglay.fuelstation.product.presentation.dto.CreateProductRequest;
import com.seanglay.fuelstation.product.presentation.dto.ProductResponse;
import com.seanglay.fuelstation.product.presentation.dto.UpdateProductRequest;
import com.seanglay.fuelstation.shared.domain.CursorPageResult;
import com.seanglay.fuelstation.shared.domain.PageResult;
import com.seanglay.fuelstation.shared.presentation.ApiResponse;
import com.seanglay.fuelstation.shared.presentation.CursorMeta;
import com.seanglay.fuelstation.shared.presentation.PageMeta;

@RestController
@RequestMapping("/products")
class ProductController {

	private final CreateProductUseCase createProductUseCase;

	private final GetProductUseCase getProductUseCase;

	private final ListProductsUseCase listProductsUseCase;

	private final UpdateProductUseCase updateProductUseCase;

	private final ChangeProductPriceUseCase changeProductPriceUseCase;

	private final DeleteProductUseCase deleteProductUseCase;

	ProductController(CreateProductUseCase createProductUseCase, GetProductUseCase getProductUseCase,
			ListProductsUseCase listProductsUseCase, UpdateProductUseCase updateProductUseCase,
			ChangeProductPriceUseCase changeProductPriceUseCase, DeleteProductUseCase deleteProductUseCase) {
		this.createProductUseCase = createProductUseCase;
		this.getProductUseCase = getProductUseCase;
		this.listProductsUseCase = listProductsUseCase;
		this.updateProductUseCase = updateProductUseCase;
		this.changeProductPriceUseCase = changeProductPriceUseCase;
		this.deleteProductUseCase = deleteProductUseCase;
	}

	@PostMapping
	@RequiresPermission(resource = "product", action = "create")
	ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody CreateProductRequest request) {
		Product product = createProductUseCase.execute(request.name(), request.sku(), request.fuelType(),
				request.unit(), request.unitPrice());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.ok("Product created", ProductResponse.from(product)));
	}

	@GetMapping("/{id}")
	@RequiresPermission(resource = "product", action = "read")
	ApiResponse<ProductResponse> get(@PathVariable Long id) {
		Product product = getProductUseCase.execute(id);
		return ApiResponse.ok("Product retrieved", ProductResponse.from(product));
	}

	@GetMapping
	@RequiresPermission(resource = "product", action = "read")
	ApiResponse<List<ProductResponse>> list(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		if (cursor != null) {
			CursorPageResult<Product> products = listProductsUseCase.executeAfterCursor(keyword, cursor, size);
			return ApiResponse.ok("Products retrieved", products.map(ProductResponse::from).content(),
					CursorMeta.from(products, size));
		}

		PageResult<Product> products = listProductsUseCase.execute(keyword, page, size);
		return ApiResponse.ok("Products retrieved", products.map(ProductResponse::from).content(),
				PageMeta.from(products));
	}

	@PutMapping("/{id}")
	@RequiresPermission(resource = "product", action = "update")
	ApiResponse<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
		Product product = updateProductUseCase.execute(id, request.name(), request.fuelType(), request.unit(),
				request.active());
		return ApiResponse.ok("Product updated", ProductResponse.from(product));
	}

	@PatchMapping("/{id}/price")
	@RequiresPermission(resource = "product", action = "update")
	ApiResponse<ProductResponse> changePrice(@PathVariable Long id,
			@Valid @RequestBody ChangeProductPriceRequest request) {
		Product product = changeProductPriceUseCase.execute(id, request.unitPrice());
		return ApiResponse.ok("Product price updated", ProductResponse.from(product));
	}

	@DeleteMapping("/{id}")
	@RequiresPermission(resource = "product", action = "delete")
	ApiResponse<Void> delete(@PathVariable Long id) {
		deleteProductUseCase.execute(id);
		return ApiResponse.ok("Product deleted", null);
	}

}
