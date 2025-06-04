package com.example.memecommerceback.domain.orders.service;

import com.example.memecommerceback.domain.orderProduct.converter.OrderProductConverter;
import com.example.memecommerceback.domain.orderProduct.dto.OrderProductRequestDto;
import com.example.memecommerceback.domain.orderProduct.entity.OrderProduct;
import com.example.memecommerceback.domain.orders.converter.OrderConverter;
import com.example.memecommerceback.domain.orders.dto.OrderRequestDto;
import com.example.memecommerceback.domain.orders.dto.OrderResponseDto;
import com.example.memecommerceback.domain.orders.entity.Order;
import com.example.memecommerceback.domain.orders.exception.OrderCustomException;
import com.example.memecommerceback.domain.orders.exception.OrderExceptionCode;
import com.example.memecommerceback.domain.orders.repository.OrderRepository;
import com.example.memecommerceback.domain.products.entity.Product;
import com.example.memecommerceback.domain.products.entity.ProductStatus;
import com.example.memecommerceback.domain.products.service.ProductServiceV1;
import com.example.memecommerceback.domain.users.entity.User;
import com.example.memecommerceback.global.redis.service.OrderNumberServiceV1;
import com.example.memecommerceback.global.utils.DateUtils;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImplV1 implements OrderServiceV1 {

  private static final String ORDER_NUMBER_PREFIX = "MCO_";

  private final ProductServiceV1 productService;
  private final OrderNumberServiceV1 orderNumberService;

  private final OrderRepository orderRepository;

  @Override
  @Transactional
  public OrderResponseDto.CreateOneDto createOne(
      OrderRequestDto.CreateOneDto requestDto, User purchaser) {
    List<OrderProductRequestDto.CreateOneDto> orderedProducts
        = requestDto.getProducts();
    // 1. 상품 ID 리스트 수집
    List<UUID> productIdList = orderedProducts.stream()
        .map(OrderProductRequestDto.CreateOneDto::getProductId)
        .toList();

    // 2. 상품 전체 조회 후 Map으로 매핑
    List<Product> productList
        = productService.findAllByIdInAndStatus(productIdList, ProductStatus.ON_SALE);
    Map<UUID, Product> productMap = productList.stream()
        .collect(Collectors.toMap(Product::getId, p -> p));

    long totalPrice = 0;

    // 3. 각 주문상품에 대해 가격 합산, 상품 미존재시 예외
    for (OrderProductRequestDto.CreateOneDto orderProductDto : orderedProducts) {
      UUID productId = orderProductDto.getProductId();
      Long quantity = orderProductDto.getQuantity();

      Product product = productMap.get(productId);
      if (product == null) {
        throw new OrderCustomException(OrderExceptionCode.PRODUCT_NOT_FOUND,
            "주문한 상품이 존재하지 않습니다. (id=" + productId + ")");
      }
      totalPrice += product.getPrice() * quantity;
    }

    String todayIsoDate = DateUtils.toTodayBasicIsoDateFormat();
    String orderNumber
        = ORDER_NUMBER_PREFIX + orderNumberService.setTodayOrderNumber(todayIsoDate);
    Order order = OrderConverter.toEntity(purchaser, totalPrice, orderNumber);

    List<OrderProduct> orderProductList
        = OrderProductConverter.toEntityList(productList, order);
    order.resetOrderProductList(orderProductList);
    orderRepository.save(order);

    Order savedOrder = orderRepository.findByIdWithAllDetails(order.getId());
    return OrderConverter.toCreateOneDto(
        savedOrder, savedOrder.getOrderProductList(), purchaser.getNickname());
  }
}
