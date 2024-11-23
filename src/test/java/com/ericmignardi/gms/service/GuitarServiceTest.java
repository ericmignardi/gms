package com.ericmignardi.gms.service;

import com.ericmignardi.gms.model.Guitar;
import com.ericmignardi.gms.repository.GuitarRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GuitarServiceTest {

    @InjectMocks
    private GuitarService guitarService;
    @Mock
    private GuitarRepository guitarRepositoryMock;
    @Captor
    private ArgumentCaptor<Guitar> guitarArgumentCaptor;
    private Guitar guitar;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        guitarService = new GuitarService(guitarRepositoryMock);
        guitar = new Guitar(
                1L,
                "Fender",
                "Starcaster",
                "Olympic White",
                "Electric",
                "asdfasdfas",
                "This is a cool guitar.");
    }

    @Test
    void readByIdTest() {
        when(guitarRepositoryMock.findById(guitar.getId())).thenReturn(Optional.of(guitar));
        Guitar guitar1 = guitarService.readById(guitar.getId());
        assertThat(guitar).isEqualTo(guitar1);
        verify(guitarRepositoryMock, times(1)).findById(guitar.getId());
    }

    @Test
    void readByIdThrowsExceptionTest() {
        when(guitarRepositoryMock.findById(guitar.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> guitarService.readById(guitar.getId())).isInstanceOf(IllegalArgumentException.class);
        verify(guitarRepositoryMock, times(1)).findById(guitar.getId());
    }

    @Test
    void readAllTest() {
        List<Guitar> guitars = List.of(new Guitar(
                1L,
                "Fender",
                "Starcaster",
                "Olympic White",
                "Electric",
                "asdfasdfas",
                "This is a cool guitar."));
        when(guitarRepositoryMock.findAll()).thenReturn(guitars);
        List<Guitar> guitars1 = guitarService.readAll();
        assertThat(guitars1).isEqualTo(guitars);
        verify(guitarRepositoryMock, times(1)).findAll();
    }

    @Test
    void createTest() {
        guitarService.create(guitar);
        verify(guitarRepositoryMock, times(1)).save(guitarArgumentCaptor.capture());
        Guitar guitarArgumentCaptorValue = guitarArgumentCaptor.getValue();
        assertThat(guitarArgumentCaptorValue).isEqualTo(guitar);
    }

    @Test
    void updateTest() {
        when(guitarRepositoryMock.findById(guitar.getId())).thenReturn(Optional.of(guitar));
        guitarService.update(guitar.getId(), guitar);
        verify(guitarRepositoryMock, times(1)).save(guitarArgumentCaptor.capture());
        Guitar guitarArgumentCaptorValue = guitarArgumentCaptor.getValue();
        assertThat(guitarArgumentCaptorValue).isEqualTo(guitar);
    }

    @Test
    void updateThrowsExceptionTest() {
        when(guitarRepositoryMock.findById(guitar.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> guitarService.update(guitar.getId(), guitar)).isInstanceOf(IllegalArgumentException.class);
        verify(guitarRepositoryMock, never()).save(any());
    }

    @Test
    void deleteTest() {
        when(guitarRepositoryMock.findById(guitar.getId())).thenReturn(Optional.of(guitar));
        guitarService.delete(guitar.getId());
        verify(guitarRepositoryMock, times(1)).delete(guitarArgumentCaptor.capture());
        Guitar guitarArgumentCaptorValue = guitarArgumentCaptor.getValue();
        assertThat(guitarArgumentCaptorValue).isEqualTo(guitar);
    }

    @Test
    void deleteThrowsExceptionTest() {
        when(guitarRepositoryMock.findById(guitar.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> guitarService.delete(guitar.getId())).isInstanceOf(IllegalArgumentException.class);
        verify(guitarRepositoryMock, never()).delete(any());
    }
}