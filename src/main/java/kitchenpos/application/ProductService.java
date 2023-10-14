package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product(request.getName(), request.getPrice());
        return ProductResponse.from(productDao.save(product));
    }

    public List<ProductResponse> list() {
        return productDao.findAll().stream()
                .map(ProductResponse::from)
                .collect(toList());
    }
}
