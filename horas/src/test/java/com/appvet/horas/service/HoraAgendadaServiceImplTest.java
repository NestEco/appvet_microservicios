package com.appvet.horas.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.appvet.horas.model.HoraAgendada;
import com.appvet.horas.repository.HoraAgendadaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para HoraAgendadaServiceImpl")
class HoraAgendadaServiceImplTest {

    @Mock
    private HoraAgendadaRepository horaAgendadaRepository;

    @InjectMocks
    private HoraAgendadaServiceImpl horaAgendadaService;

    private HoraAgendada horaAgendadaTest;
    private String usuarioId = "user123";
    private String mascotaId = "pet456";

    @BeforeEach
    void setUp() {
        horaAgendadaTest = new HoraAgendada(
            "1",
            System.currentTimeMillis(), // fecha
            14, // hora
            30, // minuto
            "Consulta General",
            usuarioId,
            mascotaId, mascotaId, mascotaId, null, null
        );
    }

    @Test
    @DisplayName("Debe obtener todas las horas agendadas")
    void debeObtenerTodasLasHorasAgendadas() {
        // Given
        List<HoraAgendada> horas = Arrays.asList(
            horaAgendadaTest,
            new HoraAgendada("2", System.currentTimeMillis(), 15, 0, "Vacunación", usuarioId, mascotaId, mascotaId, mascotaId, null, null)
        );
        when(horaAgendadaRepository.findAll()).thenReturn(horas);

        // When
        List<HoraAgendada> resultado = horaAgendadaService.obtenerTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Consulta General", resultado.get(0).getTipo());
        assertEquals("Vacunación", resultado.get(1).getTipo());
        verify(horaAgendadaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener hora agendada por ID cuando existe")
    void debeObtenerHoraAgendadaPorIdCuandoExiste() {
        // Given
        when(horaAgendadaRepository.findById("1")).thenReturn(Optional.of(horaAgendadaTest));

        // When
        Optional<HoraAgendada> resultado = horaAgendadaService.obtenerPorId("1");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Consulta General", resultado.get().getTipo());
        assertEquals(14, resultado.get().getHora());
        assertEquals(30, resultado.get().getMinuto());
        verify(horaAgendadaRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando hora agendada no existe")
    void debeRetornarOptionalVacioCuandoHoraAgendadaNoExiste() {
        // Given
        when(horaAgendadaRepository.findById("999")).thenReturn(Optional.empty());

        // When
        Optional<HoraAgendada> resultado = horaAgendadaService.obtenerPorId("999");

        // Then
        assertFalse(resultado.isPresent());
        verify(horaAgendadaRepository, times(1)).findById("999");
    }

    @Test
    @DisplayName("Debe obtener horas agendadas por usuario")
    void debeObtenerHorasAgendadasPorUsuario() {
        // Given
        List<HoraAgendada> horas = Arrays.asList(
            horaAgendadaTest,
            new HoraAgendada("2", System.currentTimeMillis(), 16, 0, "Control", usuarioId, mascotaId, mascotaId, mascotaId, null, null)
        );
        when(horaAgendadaRepository.findByUsuarioId(usuarioId)).thenReturn(horas);

        // When
        List<HoraAgendada> resultado = horaAgendadaService.obtenerPorUsuario(usuarioId);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(h -> h.getUsuarioId().equals(usuarioId)));
        verify(horaAgendadaRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("Debe obtener horas agendadas por mascota")
    void debeObtenerHorasAgendadasPorMascota() {
        // Given
        List<HoraAgendada> horas = Arrays.asList(
            horaAgendadaTest,
            new HoraAgendada("2", System.currentTimeMillis(), 10, 0, "Vacunación", usuarioId, mascotaId, mascotaId, mascotaId, null, null)
        );
        when(horaAgendadaRepository.findByMascotaId(mascotaId)).thenReturn(horas);

        // When
        List<HoraAgendada> resultado = horaAgendadaService.obtenerPorMascota(mascotaId);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(h -> h.getMascotaId().equals(mascotaId)));
        verify(horaAgendadaRepository, times(1)).findByMascotaId(mascotaId);
    }

    @Test
    @DisplayName("Debe obtener horas agendadas por estado")
    void debeObtenerHorasAgendadasPorEstado() {
        // Given
        List<HoraAgendada> horasPendientes = Arrays.asList(
            horaAgendadaTest,
            new HoraAgendada("2", System.currentTimeMillis(), 11, 0, "Control", usuarioId, mascotaId, mascotaId, mascotaId, null, null)
        );
        when(horaAgendadaRepository.findByEstado("Pendiente")).thenReturn(horasPendientes);

        // When
        List<HoraAgendada> resultado = horaAgendadaService.obtenerPorEstado("Pendiente");

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(horaAgendadaRepository, times(1)).findByEstado("Pendiente");
    }

    @Test
    @DisplayName("Debe obtener horas agendadas por tipo")
    void debeObtenerHorasAgendadasPorTipo() {
        // Given
        List<HoraAgendada> consultas = Arrays.asList(
            horaAgendadaTest,
            new HoraAgendada("2", System.currentTimeMillis(), 9, 0, "Consulta General", "user456", "pet789", mascotaId, mascotaId, null, null)
        );
        when(horaAgendadaRepository.findByTipo("Consulta General")).thenReturn(consultas);

        // When
        List<HoraAgendada> resultado = horaAgendadaService.obtenerPorTipo("Consulta General");

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(h -> h.getTipo().equals("Consulta General")));
        verify(horaAgendadaRepository, times(1)).findByTipo("Consulta General");
    }

    @Test
    @DisplayName("Debe guardar hora agendada correctamente")
    void debeGuardarHoraAgendadaCorrectamente() {
        // Given
        when(horaAgendadaRepository.save(any(HoraAgendada.class))).thenReturn(horaAgendadaTest);

        // When
        HoraAgendada resultado = horaAgendadaService.guardar(horaAgendadaTest);

        // Then
        assertNotNull(resultado);
        assertEquals("Consulta General", resultado.getTipo());
        assertEquals(14, resultado.getHora());
        assertEquals(30, resultado.getMinuto());
        assertEquals(usuarioId, resultado.getUsuarioId());
        assertEquals(mascotaId, resultado.getMascotaId());
        verify(horaAgendadaRepository, times(1)).save(horaAgendadaTest);
    }

    @Test
    @DisplayName("Debe actualizar hora agendada cuando existe")
    void debeActualizarHoraAgendadaCuandoExiste() {
        // Given
        HoraAgendada horaActualizada = new HoraAgendada(
            "1",
            System.currentTimeMillis(),
            15, // hora actualizada
            45, // minuto actualizado
            "Vacunación", // tipo actualizado
            usuarioId,
            mascotaId, mascotaId, mascotaId, null, null
        );
        
        when(horaAgendadaRepository.findById("1")).thenReturn(Optional.of(horaAgendadaTest));
        when(horaAgendadaRepository.save(any(HoraAgendada.class))).thenReturn(horaActualizada);

        // When
        HoraAgendada resultado = horaAgendadaService.actualizar("1", horaActualizada);

        // Then
        assertNotNull(resultado);
        assertEquals("Vacunación", resultado.getTipo());
        assertEquals(15, resultado.getHora());
        assertEquals(45, resultado.getMinuto());
        verify(horaAgendadaRepository, times(1)).findById("1");
        verify(horaAgendadaRepository, times(1)).save(any(HoraAgendada.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar hora agendada que no existe")
    void debeLanzarExcepcionAlActualizarHoraAgendadaQueNoExiste() {
        // Given
        when(horaAgendadaRepository.findById("999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            horaAgendadaService.actualizar("999", horaAgendadaTest);
        });

        assertTrue(exception.getMessage().contains("Hora agendada no encontrada"));
        verify(horaAgendadaRepository, times(1)).findById("999");
        verify(horaAgendadaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe cambiar estado de hora agendada")
    void debeCambiarEstadoDeHoraAgendada() {
        // Given
        when(horaAgendadaRepository.findById("1")).thenReturn(Optional.of(horaAgendadaTest));
        when(horaAgendadaRepository.save(any(HoraAgendada.class))).thenAnswer(invocation -> {
            HoraAgendada hora = invocation.getArgument(0);
            return hora;
        });

        // When
        HoraAgendada resultado = horaAgendadaService.cambiarEstado("1", "Completada");

        // Then
        assertNotNull(resultado);
        assertEquals("Completada", resultado.getEstado());
        verify(horaAgendadaRepository, times(1)).findById("1");
        verify(horaAgendadaRepository, times(1)).save(any(HoraAgendada.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al cambiar estado de hora agendada que no existe")
    void debeLanzarExcepcionAlCambiarEstadoDeHoraAgendadaQueNoExiste() {
        // Given
        when(horaAgendadaRepository.findById("999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            horaAgendadaService.cambiarEstado("999", "Completada");
        });

        assertTrue(exception.getMessage().contains("Hora agendada no encontrada"));
        verify(horaAgendadaRepository, times(1)).findById("999");
        verify(horaAgendadaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar hora agendada cuando existe")
    void debeEliminarHoraAgendadaCuandoExiste() {
        // Given
        when(horaAgendadaRepository.existsById("1")).thenReturn(true);
        doNothing().when(horaAgendadaRepository).deleteById("1");

        // When
        assertDoesNotThrow(() -> horaAgendadaService.eliminar("1"));

        // Then
        verify(horaAgendadaRepository, times(1)).existsById("1");
        verify(horaAgendadaRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar hora agendada que no existe")
    void debeLanzarExcepcionAlEliminarHoraAgendadaQueNoExiste() {
        // Given
        when(horaAgendadaRepository.existsById("999")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            horaAgendadaService.eliminar("999");
        });

        assertTrue(exception.getMessage().contains("Hora agendada no encontrada"));
        verify(horaAgendadaRepository, times(1)).existsById("999");
        verify(horaAgendadaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Debe contar horas agendadas por usuario")
    void debeContarHorasAgendadasPorUsuario() {
        // Given
        when(horaAgendadaRepository.countByUsuarioId(usuarioId)).thenReturn(3L);

        // When
        long resultado = horaAgendadaService.contarPorUsuario(usuarioId);

        // Then
        assertEquals(3L, resultado);
        verify(horaAgendadaRepository, times(1)).countByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("Debe validar que hora está en rango válido (0-23)")
    void debeValidarQueHoraEstaEnRangoValido() {
        // Given
        HoraAgendada horaValida = new HoraAgendada(
            "1",
            System.currentTimeMillis(),
            23, // hora máxima válida
            59, // minuto máximo válido
            "Consulta",
            usuarioId,
            mascotaId, mascotaId, mascotaId, null, null
        );
        when(horaAgendadaRepository.save(any(HoraAgendada.class))).thenReturn(horaValida);

        // When
        HoraAgendada resultado = horaAgendadaService.guardar(horaValida);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.getHora() >= 0 && resultado.getHora() <= 23);
        assertTrue(resultado.getMinuto() >= 0 && resultado.getMinuto() <= 59);
        verify(horaAgendadaRepository, times(1)).save(horaValida);
    }

    @Test
    @DisplayName("Debe validar que hora agendada tiene todos los campos requeridos")
    void debeValidarQueHoraAgendadaTieneTodosLosCamposRequeridos() {
        // Given
        when(horaAgendadaRepository.save(any(HoraAgendada.class))).thenReturn(horaAgendadaTest);

        // When
        HoraAgendada resultado = horaAgendadaService.guardar(horaAgendadaTest);

        // Then
        assertNotNull(resultado.getId());
        assertNotNull(resultado.getFecha());
        assertNotNull(resultado.getHora());
        assertNotNull(resultado.getMinuto());
        assertNotNull(resultado.getTipo());
        assertNotNull(resultado.getUsuarioId());
        verify(horaAgendadaRepository, times(1)).save(horaAgendadaTest);
    }

    @Test
    @DisplayName("Debe permitir hora agendada sin mascota asignada")
    void debePermitirHoraAgendadaSinMascotaAsignada() {
        // Given
        HoraAgendada horaSinMascota = new HoraAgendada(
            "1",
            System.currentTimeMillis(),
            10,
            0,
            "Consulta General",
            usuarioId,
            null // Sin mascota
, mascotaId, mascotaId, null, null
        );
        when(horaAgendadaRepository.save(any(HoraAgendada.class))).thenReturn(horaSinMascota);

        // When
        HoraAgendada resultado = horaAgendadaService.guardar(horaSinMascota);

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getMascotaId());
        assertEquals("Consulta General", resultado.getTipo());
        verify(horaAgendadaRepository, times(1)).save(horaSinMascota);
    }

    @Test
    @DisplayName("Debe obtener horas agendadas vacías cuando no hay resultados")
    void debeObtenerHorasAgendadasVaciasCuandoNoHayResultados() {
        // Given
        when(horaAgendadaRepository.findByUsuarioId("userSinHoras")).thenReturn(Arrays.asList());

        // When
        List<HoraAgendada> resultado = horaAgendadaService.obtenerPorUsuario("userSinHoras");

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(horaAgendadaRepository, times(1)).findByUsuarioId("userSinHoras");
    }
}