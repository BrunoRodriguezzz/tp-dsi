package ar.edu.utn.frba.dds.servicioAutenticacion;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions.InvalidPasswordError;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions.NotFoundError;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Permiso;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Rol;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Usuario;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.UsuariosRepository;
import ar.edu.utn.frba.dds.servicioAutenticacion.services.AuthService;
import ar.edu.utn.frba.dds.servicioAutenticacion.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

    @Mock
    private UsuariosRepository usuariosRepository;

    @InjectMocks
    private AuthService authService;

    private Usuario testUser;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

        testUser = new Usuario();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRoles(Arrays.asList(Rol.CONTRIBUYENTE, Rol.ADMINISTRADOR));
        testUser.setPermisos(Arrays.asList(Permiso.EJEMPLO));
    }

    @Test
    void autenticarUsuario_ConCredencialesValidas_DeberiaRetornarUsuario() {
        when(usuariosRepository.findByUsernameIgnoreCase("testuser"))
                .thenReturn(Optional.of(testUser));

        Usuario resultado = authService.autenticarUsuario("testuser", "password123");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsername()).isEqualTo("testuser");
        verify(usuariosRepository).findByUsernameIgnoreCase("testuser");
    }

    @Test
    void autenticarUsuario_ConUsuarioInexistente_DeberiaLanzarNotFoundException() {
        when(usuariosRepository.findByUsernameIgnoreCase("usuarioinexistente"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.autenticarUsuario("usuarioinexistente", "password"))
                .isInstanceOf(NotFoundError.class)
                .hasMessage("Usuario usuarioinexistente no encontrado");

        verify(usuariosRepository).findByUsernameIgnoreCase("usuarioinexistente");
    }

    @Test
    void autenticarUsuario_ConPasswordIncorrecto_DeberiaLanzarNotFoundException() {
        when(usuariosRepository.findByUsernameIgnoreCase("testuser"))
                .thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> authService.autenticarUsuario("testuser", "passwordincorrecto"))
                .isInstanceOf(InvalidPasswordError.class)
                .hasMessage("La contraseña del usuario testuser no es correcta");

        verify(usuariosRepository).findByUsernameIgnoreCase("testuser");
    }

    @Test
    void generarAccessToken_DeberiaGenerarTokenConInformacionCorrecta() {
        try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
            String expectedToken = "mocked.access.token";
            jwtUtilMock.when(() -> JwtUtil.generarAccessToken(testUser))
                    .thenReturn(expectedToken);

            String actualToken = authService.generarAccessToken(testUser);

            assertThat(actualToken).isEqualTo(expectedToken);
            jwtUtilMock.verify(() -> JwtUtil.generarAccessToken(testUser));
        }
    }

    @Test
    void generarRefreshToken_DeberiaGenerarTokenConTipoRefresh() {
        try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
            String expectedToken = "mocked.refresh.token";
            jwtUtilMock.when(() -> JwtUtil.generarRefreshToken(testUser))
                    .thenReturn(expectedToken);

            String actualToken = authService.generarRefreshToken(testUser);

            assertThat(actualToken).isEqualTo(expectedToken);
            jwtUtilMock.verify(() -> JwtUtil.generarRefreshToken(testUser));
        }
    }

    @Test
    void obtenerUsuario_ConUsernameExistente_DeberiaRetornarUsuario() {
        when(usuariosRepository.findByUsernameIgnoreCase("testuser"))
                .thenReturn(Optional.of(testUser));

        Usuario resultado = authService.obtenerUsuario("testuser");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsername()).isEqualTo("testuser");
        verify(usuariosRepository).findByUsernameIgnoreCase("testuser");
    }

    @Test
    void obtenerUsuario_ConUsernameInexistente_DeberiaLanzarNotFoundException() {
        when(usuariosRepository.findByUsernameIgnoreCase("usuarioinexistente"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.obtenerUsuario("usuarioinexistente"))
                .isInstanceOf(NotFoundError.class)
                .hasMessage("Usuario usuarioinexistente no encontrado");

        verify(usuariosRepository).findByUsernameIgnoreCase("usuarioinexistente");
    }

    @Test
    void registrarUsuario_ConDatosValidos_DeberiaCrearYGuardarUsuario() {
        Usuario registerDTO = Usuario.builder()
                .username("nuevousuario")
                .password("password123")
                .roles(Arrays.asList(Rol.CONTRIBUYENTE))
                .permisos(Arrays.asList(Permiso.EJEMPLO))
                .build();

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(2L);
        usuarioGuardado.setUsername("nuevousuario");
        usuarioGuardado.setPassword(passwordEncoder.encode("password123"));
        usuarioGuardado.setRoles(Arrays.asList(Rol.CONTRIBUYENTE));
        usuarioGuardado.setPermisos(Arrays.asList(Permiso.EJEMPLO));

        when(usuariosRepository.findByUsernameIgnoreCase("nuevousuario"))
                .thenReturn(Optional.empty());
        when(usuariosRepository.save(any(Usuario.class)))
                .thenReturn(usuarioGuardado);

        Usuario resultado = authService.registrarUsuario(registerDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(2L);
        assertThat(resultado.getUsername()).isEqualTo("nuevousuario");
        assertThat(resultado.getRoles()).hasSize(1);
        assertThat(resultado.getPermisos()).hasSize(2);

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuariosRepository).save(usuarioCaptor.capture());
        Usuario usuarioAGuardar = usuarioCaptor.getValue();

        assertThat(usuarioAGuardar.getUsername()).isEqualTo("nuevousuario");
        assertThat(usuarioAGuardar.getRoles()).isEqualTo(registerDTO.getRoles());
        assertThat(usuarioAGuardar.getPermisos()).isEqualTo(registerDTO.getPermisos());
        assertThat(usuarioAGuardar.getPassword()).isNotEqualTo("password123");

        verify(usuariosRepository).findByUsernameIgnoreCase("nuevousuario");
    }

    @Test
    void registrarUsuario_ConUsuarioExistente_DeberiaLanzarRuntimeException() {
        Usuario registerDTO = Usuario.builder()
                .username("testuser")
                .password("password123")
                .roles(Arrays.asList(Rol.CONTRIBUYENTE))
                .permisos(Arrays.asList(Permiso.EJEMPLO))
                .build();

        when(usuariosRepository.findByUsernameIgnoreCase("testuser"))
                .thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> authService.registrarUsuario(registerDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El usuario ya existe");

        verify(usuariosRepository).findByUsernameIgnoreCase("testuser");
        verify(usuariosRepository, never()).save(any(Usuario.class));
    }

    @Test
    void modificarUsuario_ConDatosValidos_DeberiaActualizarUsuario() {
        Usuario updateDTO = Usuario.builder()
                .username("usuariomodificado")
                .password("newpassword123")
                .roles(Arrays.asList(Rol.ADMINISTRADOR))
                .permisos(Arrays.asList(Permiso.EJEMPLO))
                .build();

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setUsername("usuariomodificado");
        usuarioActualizado.setPassword(passwordEncoder.encode("newpassword123"));
        usuarioActualizado.setRoles(Arrays.asList(Rol.ADMINISTRADOR));
        usuarioActualizado.setPermisos(Arrays.asList(Permiso.EJEMPLO));

        when(usuariosRepository.findById(1L))
                .thenReturn(Optional.of(testUser));
        when(usuariosRepository.findByUsernameIgnoreCase("usuariomodificado"))
                .thenReturn(Optional.empty());
        when(usuariosRepository.save(any(Usuario.class)))
                .thenReturn(usuarioActualizado);

        Usuario resultado = authService.modificarUsuario(1L, updateDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getUsername()).isEqualTo("usuariomodificado");
        assertThat(resultado.getRoles()).containsExactly(Rol.ADMINISTRADOR);
        assertThat(resultado.getPermisos()).containsExactly(Permiso.EJEMPLO);

        verify(usuariosRepository).findById(1L);
        verify(usuariosRepository).findByUsernameIgnoreCase("usuariomodificado");
        verify(usuariosRepository).save(any(Usuario.class));
    }

    @Test
    void modificarUsuario_SinCambiarPassword_NoDeberiaActualizarPassword() {
        Usuario updateDTO = Usuario.builder()
                .username("usuariomodificado")
                .password(null) // No cambiar password
                .roles(Arrays.asList(Rol.ADMINISTRADOR))
                .permisos(Arrays.asList(Permiso.EJEMPLO))
                .build();

        String passwordOriginal = testUser.getPassword();

        when(usuariosRepository.findById(1L))
                .thenReturn(Optional.of(testUser));
        when(usuariosRepository.findByUsernameIgnoreCase("usuariomodificado"))
                .thenReturn(Optional.empty());
        when(usuariosRepository.save(any(Usuario.class)))
                .thenReturn(testUser);

        authService.modificarUsuario(1L, updateDTO);

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuariosRepository).save(usuarioCaptor.capture());
        Usuario usuarioGuardado = usuarioCaptor.getValue();

        assertThat(usuarioGuardado.getPassword()).isEqualTo(passwordOriginal);
    }

    @Test
    void modificarUsuario_ConUsuarioInexistente_DeberiaLanzarNotFoundException() {
        Usuario updateDTO = Usuario.builder()
                .username("usuariomodificado")
                .roles(Arrays.asList(Rol.CONTRIBUYENTE))
                .permisos(Arrays.asList(Permiso.EJEMPLO))
                .build();

        when(usuariosRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.modificarUsuario(999L, updateDTO))
                .isInstanceOf(NotFoundError.class)
                .hasMessage("Usuario no encontrado");

        verify(usuariosRepository).findById(999L);
        verify(usuariosRepository, never()).save(any(Usuario.class));
    }

    @Test
    void modificarUsuario_ConUsernameExistente_DeberiaLanzarRuntimeException() {
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setUsername("otrousuario");

        Usuario updateDTO = Usuario.builder()
                .username("otrousuario") // Username que ya existe
                .roles(Arrays.asList(Rol.CONTRIBUYENTE))
                .permisos(Arrays.asList(Permiso.EJEMPLO))
                .build();

        when(usuariosRepository.findById(1L))
                .thenReturn(Optional.of(testUser));
        when(usuariosRepository.findByUsernameIgnoreCase("otrousuario"))
                .thenReturn(Optional.of(otroUsuario));

        assertThatThrownBy(() -> authService.modificarUsuario(1L, updateDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El nombre de usuario ya está en uso");

        verify(usuariosRepository).findById(1L);
        verify(usuariosRepository).findByUsernameIgnoreCase("otrousuario");
        verify(usuariosRepository, never()).save(any(Usuario.class));
    }

    @Test
    void modificarUsuario_ConMismoUsername_NoDeberiaValidarDuplicado() {
        Usuario updateDTO = Usuario.builder()
                .username("testuser") // Mismo username del usuario actual
                .password("newpassword")
                .roles(Arrays.asList(Rol.ADMINISTRADOR))
                .permisos(Arrays.asList(Permiso.EJEMPLO))
                .build();

        when(usuariosRepository.findById(1L))
                .thenReturn(Optional.of(testUser));
        when(usuariosRepository.save(any(Usuario.class)))
                .thenReturn(testUser);

        authService.modificarUsuario(1L, updateDTO);

        verify(usuariosRepository).findById(1L);
        verify(usuariosRepository, never()).findByUsernameIgnoreCase(anyString());
        verify(usuariosRepository).save(any(Usuario.class));
    }
}
