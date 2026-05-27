package com.pasteleria.cotizaciones.application;

import java.util.List;

import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;
import com.pasteleria.cotizaciones.application.port.QuotationRepositoryPort;
import com.pasteleria.cotizaciones.application.mapper.QuotationDtoMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QuotationQueryService {

  private final QuotationRepositoryPort quotationRepository;
  private final QuotationDtoMapper quotationDtoMapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public QuotationQueryService(
      QuotationRepositoryPort quotationRepository,
      QuotationDtoMapper quotationDtoMapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.quotationRepository = quotationRepository;
    this.quotationDtoMapper = quotationDtoMapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<QuotationSummary> listQuotations() {
    return quotationRepository.findAllByOrderByCreatedAtDesc().stream()
        .map(quotationDtoMapper::toSummary)
        .toList();
  }

  public PageResponseDto<QuotationSummary> pagedQuotations(int page, int size) {
    var pageable = pageRequestFactory.create(page, size, org.springframework.data.domain.Sort.by(
        org.springframework.data.domain.Sort.Order.desc("createdAt")
    ));

    return pageMapper.toPageResponseDto(
        quotationRepository.findAllByOrderByCreatedAtDesc(pageable).map(quotationDtoMapper::toSummary)
    );
  }
}


