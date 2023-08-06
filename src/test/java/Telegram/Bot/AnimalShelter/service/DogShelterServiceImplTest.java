package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.DogShelter;
import Telegram.Bot.AnimalShelter.repository.DogRepository;
import Telegram.Bot.AnimalShelter.repository.DogShelterRepository;
import Telegram.Bot.AnimalShelter.service.impl.DogShelterServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static Telegram.Bot.AnimalShelter.service.AnimalShelterConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DogShelterServiceImplTest {

    @Mock
    private DogShelterRepository dogShelterRepository;
    @Mock
    private DogRepository dogRepository;
    @InjectMocks
    private DogShelterServiceImpl dogShelterService;


    @Test
    void createDogTest() {

        when(dogShelterRepository.save(DOG_SHELTER_1)).thenReturn(DOG_SHELTER_1);
        assertEquals(DOG_SHELTER_1, dogShelterService.create(DOG_SHELTER_1));
    }


    @Test
    void checkUpdateDogTest() {
        when(dogShelterRepository.findById(any(Long.class))).thenReturn(Optional.of(DOG_SHELTER_1));
        when(dogShelterRepository.save(any(DogShelter.class))).thenReturn(DOG_SHELTER_1);

        DOG_SHELTER_2.setId(SHELTER_ID_1);
        assertEquals(DOG_SHELTER_2, dogShelterService.update(DOG_SHELTER_2));
    }


    @Test
    void checkGetShelterByIdTest() {
        when(dogShelterRepository.findById(SHELTER_ID_1)).thenReturn(Optional.of(DOG_SHELTER_1));
        assertEquals(DOG_SHELTER_1, dogShelterService.getShelterById(SHELTER_ID_1));

    }

    @Test
    void checkGetShelterByNameTest() {
        when(dogShelterRepository.findByName(NAME_DOG_1)).thenReturn(Optional.of(DOG_SHELTER_1));
        assertEquals(DOG_SHELTER_1, dogShelterService.getShelterByName(NAME_DOG_1));
    }

    @Test
    void getShelterTest() {
        List<DogShelter> dogShelters = new ArrayList<>();
        dogShelters.add(DOG_SHELTER_1);
        dogShelters.add(DOG_SHELTER_2);

        when(dogShelterRepository.findAll()).thenReturn(dogShelters);
        assertEquals(dogShelters, dogShelterService.getShelter());
    }

    //    @Test
//    void getAnimalTest() {
//        List<Dog> dogs = new ArrayList<>();
//        dogs.add(DOG_1);
//}

    @Test
    void deleteTest() {
        when(dogShelterRepository.save(DOG_SHELTER_1)).thenReturn(DOG_SHELTER_1);
        assertEquals(DOG_SHELTER_1, dogShelterService.create(DOG_SHELTER_1));
        dogShelterRepository.delete(DOG_SHELTER_1);
        assertFalse(dogShelterRepository.findAll().contains(DOG_SHELTER_1));
    }
}