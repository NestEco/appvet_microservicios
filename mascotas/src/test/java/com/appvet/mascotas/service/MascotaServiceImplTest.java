package com.appvet.mascotas.service;

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

import com.appvet.mascotas.model.Mascota;
import com.appvet.mascotas.repository.MascotaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para MascotaServiceImpl")
class MascotaServiceImplTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaServiceImpl mascotaService;

    private Mascota mascotaTest;
    private String usuarioId = "user123";

    @BeforeEach
    void setUp() {
        mascotaTest = new Mascota(
            "1",
            "Firulais",
            3,
            "Perro",
            "http://foto.com/firulais.jpg",
            usuarioId
        );
    }

    @Test
    @DisplayName("Debe obtener todas las mascotas")
    void debeObtenerTodasLasMascotas() {
        // Given
        List<Mascota> mascotas = Arrays.asList(
            mascotaTest,
            new Mascota("2", "Michi", 2, "Gato", null, usuarioId)
        );
        when(mascotaRepository.findAll()).thenReturn(mascotas);

        // When
        List<Mascota> resultado = mascotaService.obtenerTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Firulais", resultado.get(0).getNombre());
        assertEquals("Michi", resultado.get(1).getNombre());
        verify(mascotaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener mascota por ID cuando existe")
    void debeObtenerMascotaPorIdCuandoExiste() {
        // Given
        when(mascotaRepository.findById("1")).thenReturn(Optional.of(mascotaTest));

        // When
        Optional<Mascota> resultado = mascotaService.obtenerPorId("1");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Firulais", resultado.get().getNombre());
        assertEquals(3, resultado.get().getEdad());
        assertEquals("Perro", resultado.get().getEspecie());
        verify(mascotaRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando mascota no existe")
    void debeRetornarOptionalVacioCuandoMascotaNoExiste() {
        // Given
        when(mascotaRepository.findById("999")).thenReturn(Optional.empty());

        // When
        Optional<Mascota> resultado = mascotaService.obtenerPorId("999");

        // Then
        assertFalse(resultado.isPresent());
        verify(mascotaRepository, times(1)).findById("999");
    }

    @Test
    @DisplayName("Debe obtener mascotas por usuario")
    void debeObtenerMascotasPorUsuario() {
        // Given
        List<Mascota> mascotas = Arrays.asList(
            mascotaTest,
            new Mascota("2", "Michi", 2, "Gato", null, usuarioId)
        );
        when(mascotaRepository.findByUsuarioId(usuarioId)).thenReturn(mascotas);

        // When
        List<Mascota> resultado = mascotaService.obtenerPorUsuario(usuarioId);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(m -> m.getUsuarioId().equals(usuarioId)));
        verify(mascotaRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("Debe obtener mascotas por especie")
    void debeObtenerMascotasPorEspecie() {
        // Given
        List<Mascota> perros = Arrays.asList(
            mascotaTest,
            new Mascota("3", "Rex", 5, "Perro", null, "user456")
        );
        when(mascotaRepository.findByEspecie("Perro")).thenReturn(perros);

        // When
        List<Mascota> resultado = mascotaService.obtenerPorEspecie("Perro");

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(m -> m.getEspecie().equals("Perro")));
        verify(mascotaRepository, times(1)).findByEspecie("Perro");
    }

    @Test
    @DisplayName("Debe guardar mascota correctamente")
    void debeGuardarMascotaCorrectamente() {
        // Given
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaTest);

        // When
        Mascota resultado = mascotaService.guardar(mascotaTest);

        // Then
        assertNotNull(resultado);
        assertEquals("Firulais", resultado.getNombre());
        assertEquals(3, resultado.getEdad());
        assertEquals("Perro", resultado.getEspecie());
        assertEquals(usuarioId, resultado.getUsuarioId());
        verify(mascotaRepository, times(1)).save(mascotaTest);
    }

    @Test
    @DisplayName("Debe actualizar mascota cuando existe")
    void debeActualizarMascotaCuandoExiste() {
        // Given
        Mascota mascotaActualizada = new Mascota(
            "1",
            "Firulais Actualizado",
            4,
            "Perro",
            "http://foto.com/firulais_new.jpg",
            usuarioId
        );
        
        when(mascotaRepository.findById("1")).thenReturn(Optional.of(mascotaTest));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaActualizada);

        // When
        Mascota resultado = mascotaService.actualizar("1", mascotaActualizada);

        // Then
        assertNotNull(resultado);
        assertEquals("Firulais Actualizado", resultado.getNombre());
        assertEquals(4, resultado.getEdad());
        verify(mascotaRepository, times(1)).findById("1");
        verify(mascotaRepository, times(1)).save(any(Mascota.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar mascota que no existe")
    void debeLanzarExcepcionAlActualizarMascotaQueNoExiste() {
        // Given
        when(mascotaRepository.findById("999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mascotaService.actualizar("999", mascotaTest);
        });

        assertTrue(exception.getMessage().contains("Mascota no encontrada"));
        verify(mascotaRepository, times(1)).findById("999");
        verify(mascotaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar mascota cuando existe")
    void debeEliminarMascotaCuandoExiste() {
        // Given
        when(mascotaRepository.existsById("1")).thenReturn(true);
        doNothing().when(mascotaRepository).deleteById("1");

        // When
        assertDoesNotThrow(() -> mascotaService.eliminar("1"));

        // Then
        verify(mascotaRepository, times(1)).existsById("1");
        verify(mascotaRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar mascota que no existe")
    void debeLanzarExcepcionAlEliminarMascotaQueNoExiste() {
        // Given
        when(mascotaRepository.existsById("999")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mascotaService.eliminar("999");
        });

        assertTrue(exception.getMessage().contains("Mascota no encontrada"));
        verify(mascotaRepository, times(1)).existsById("999");
        verify(mascotaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Debe contar mascotas por usuario")
    void debeContarMascotasPorUsuario() {
        // Given
        when(mascotaRepository.countByUsuarioId(usuarioId)).thenReturn(5L);

        // When
        long resultado = mascotaService.contarPorUsuario(usuarioId);

        // Then
        assertEquals(5L, resultado);
        verify(mascotaRepository, times(1)).countByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("Debe retornar cero cuando usuario no tiene mascotas")
    void debeRetornarCeroCuandoUsuarioNoTieneMascotas() {
        // Given
        when(mascotaRepository.countByUsuarioId("user999")).thenReturn(0L);

        // When
        long resultado = mascotaService.contarPorUsuario("user999");

        // Then
        assertEquals(0L, resultado);
        verify(mascotaRepository, times(1)).countByUsuarioId("user999");
    }

    @Test
    @DisplayName("Debe validar que mascota tiene todos los campos requeridos")
    void debeValidarQueMascotaTieneTodosLosCamposRequeridos() {
        // Given
        Mascota mascotaCompleta = new Mascota(
            "1",
            "Firulais",
            3,
            "Perro",
            "http://foto.com/firulais.jpg",
            usuarioId
        );
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaCompleta);

        // When
        Mascota resultado = mascotaService.guardar(mascotaCompleta);

        // Then
        assertNotNull(resultado.getId());
        assertNotNull(resultado.getNombre());
        assertNotNull(resultado.getEdad());
        assertNotNull(resultado.getEspecie());
        assertNotNull(resultado.getUsuarioId());
        assertTrue(resultado.getEdad() >= 0);
        verify(mascotaRepository, times(1)).save(mascotaCompleta);
    }

    @Test
    @DisplayName("Debe permitir guardar mascota sin foto")
    void debePermitirGuardarMascotaSinFoto() {
        // Given
        Mascota mascotaSinFoto = new Mascota(
            "1",
            "Firulais",
            3,
            "Perro",
            null,  // Sin foto
            usuarioId
        );
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaSinFoto);

        // When
        Mascota resultado = mascotaService.guardar(mascotaSinFoto);

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getFotoUri());
        assertEquals("Firulais", resultado.getNombre());
        verify(mascotaRepository, times(1)).save(mascotaSinFoto);
    }
}