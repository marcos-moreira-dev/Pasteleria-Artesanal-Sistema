package com.pasteleria.casosuso.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.pasteleria.casosuso.api.dto.CasoUsoHubResponse;
import com.pasteleria.casosuso.api.dto.CasoUsoModuloResponse;
import com.pasteleria.casosuso.api.dto.CasoUsoOperativoResponse;
import com.pasteleria.casosuso.api.dto.PasoCasoUsoResponse;
import com.pasteleria.casosuso.infrastructure.persistence.entity.CasoUsoModuloEntity;
import com.pasteleria.casosuso.infrastructure.persistence.entity.CasoUsoOperativoEntity;
import com.pasteleria.casosuso.infrastructure.persistence.entity.PasoCasoUsoEntity;
import com.pasteleria.casosuso.infrastructure.persistence.repository.CasoUsoModuloRepository;
import com.pasteleria.casosuso.infrastructure.persistence.repository.CasoUsoOperativoRepository;
import com.pasteleria.casosuso.infrastructure.persistence.repository.PasoCasoUsoRepository;
import com.pasteleria.common.error.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CasoUsoOperativoService {

  private final CasoUsoOperativoRepository casos;
  private final PasoCasoUsoRepository pasos;
  private final CasoUsoModuloRepository modulos;

  public CasoUsoOperativoService(
      CasoUsoOperativoRepository casos,
      PasoCasoUsoRepository pasos,
      CasoUsoModuloRepository modulos
  ) {
    this.casos = casos;
    this.pasos = pasos;
    this.modulos = modulos;
  }

  public List<CasoUsoOperativoResponse> listar(String modulo) {
    List<CasoUsoOperativoEntity> data = modulo == null || modulo.isBlank()
        ? casos.findByActivoTrueOrderByModuloAscOrdenVisualAscCodigoAsc()
        : casos.findByModuloAndActivoTrueOrderByOrdenVisualAscCodigoAsc(normalize(modulo));

    return toResponses(data);
  }

  public CasoUsoOperativoResponse obtenerPorCodigo(String codigo) {
    CasoUsoOperativoEntity caso = casos.findByCodigoAndActivoTrue(normalize(codigo))
        .orElseThrow(() -> new ResourceNotFoundException("Flujo de guía operativa no encontrado."));

    return toResponse(caso, pasos.findByCasoUsoIdOrderByNumeroAsc(caso.getId()));
  }

  public CasoUsoHubResponse hub() {
    List<CasoUsoModuloEntity> activeModules = modulos.findByActivoTrueOrderByOrdenVisualAscCodigoAsc();
    List<CasoUsoOperativoEntity> activeCases = casos.findByActivoTrueOrderByModuloAscOrdenVisualAscCodigoAsc();
    Map<Long, List<PasoCasoUsoEntity>> stepsByCase = stepsByCase(activeCases);
    Map<String, List<CasoUsoOperativoResponse>> casesByModule = activeCases.stream()
        .collect(Collectors.groupingBy(
            CasoUsoOperativoEntity::getModulo,
            LinkedHashMap::new,
            Collectors.mapping(caso -> toResponse(caso, stepsByCase.getOrDefault(caso.getId(), List.of())),
                Collectors.toList())
        ));

    List<CasoUsoModuloResponse> moduleResponses = new ArrayList<>();
    for (CasoUsoModuloEntity modulo : activeModules) {
      List<CasoUsoOperativoResponse> moduleCases = casesByModule.remove(modulo.getCodigo());
      if (moduleCases == null || moduleCases.isEmpty()) {
        continue;
      }
      moduleResponses.add(new CasoUsoModuloResponse(
          modulo.getCodigo(),
          modulo.getNombre(),
          modulo.getDescripcion(),
          modulo.getGrupo(),
          modulo.getOrdenVisual(),
          moduleCases
      ));
    }

    casesByModule.entrySet().stream()
        .filter(entry -> !entry.getValue().isEmpty())
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> moduleResponses.add(new CasoUsoModuloResponse(
            entry.getKey(),
            humanizeModule(entry.getKey()),
            null,
            "OPERACION",
            999,
            entry.getValue()
        )));

    return new CasoUsoHubResponse(activeCases.size(), moduleResponses);
  }

  private List<CasoUsoOperativoResponse> toResponses(List<CasoUsoOperativoEntity> data) {
    Map<Long, List<PasoCasoUsoEntity>> stepsByCase = stepsByCase(data);
    return data.stream()
        .map(caso -> toResponse(caso, stepsByCase.getOrDefault(caso.getId(), List.of())))
        .toList();
  }

  private Map<Long, List<PasoCasoUsoEntity>> stepsByCase(List<CasoUsoOperativoEntity> cases) {
    List<Long> ids = cases.stream()
        .map(CasoUsoOperativoEntity::getId)
        .toList();

    if (ids.isEmpty()) {
      return Map.of();
    }

    return pasos.findByCasoUsoIdInOrderByCasoUsoIdAscNumeroAsc(ids).stream()
        .collect(Collectors.groupingBy(
            PasoCasoUsoEntity::getCasoUsoId,
            LinkedHashMap::new,
            Collectors.toList()
        ));
  }

  private CasoUsoOperativoResponse toResponse(CasoUsoOperativoEntity caso, List<PasoCasoUsoEntity> casoPasos) {
    List<PasoCasoUsoResponse> responseSteps = casoPasos.stream()
        .sorted(Comparator.comparing(PasoCasoUsoEntity::getNumero))
        .map(paso -> new PasoCasoUsoResponse(paso.getNumero(), paso.getDescripcion()))
        .toList();

    return new CasoUsoOperativoResponse(
        caso.getId(),
        caso.getCodigo(),
        caso.getModulo(),
        caso.getTitulo(),
        caso.getActorPrincipal(),
        caso.getObjetivo(),
        caso.getPuntoInicio(),
        caso.getOrdenVisual(),
        caso.getEstado(),
        caso.getVersionFlujo(),
        caso.isActivo(),
        responseSteps
    );
  }

  private String normalize(String value) {
    return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
  }

  private String humanizeModule(String code) {
    String lower = code == null ? "" : code.toLowerCase(Locale.ROOT).replace('_', ' ');
    if (lower.isBlank()) {
      return "Módulo sin nombre";
    }
    return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
  }
}
