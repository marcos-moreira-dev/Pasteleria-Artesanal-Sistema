package com.pasteleria.pedidos.application;

import java.util.List;

import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;
import com.pasteleria.pedidos.application.port.OrderRepositoryPort;
import com.pasteleria.pedidos.application.mapper.OrderDtoMapper;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderQueryService {

  private final OrderRepositoryPort orderRepository;
  private final OrderDtoMapper orderDtoMapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public OrderQueryService(
      OrderRepositoryPort orderRepository,
      OrderDtoMapper orderDtoMapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.orderRepository = orderRepository;
    this.orderDtoMapper = orderDtoMapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<OrderSummary> listOrders() {
    return orderRepository.findAllByOrderByCreatedAtDesc().stream()
        .map(orderDtoMapper::toSummary)
        .toList();
  }

  public PageResponseDto<OrderSummary> listOrdersPage(int page, int size) {
    return pageMapper.toPageResponseDto(
        orderRepository.findAllByOrderByCreatedAtDesc(
            pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).map(orderDtoMapper::toSummary)
    );
  }
}


