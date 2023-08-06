package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.CatShelter;
import Telegram.Bot.AnimalShelter.repository.CatRepository;
import Telegram.Bot.AnimalShelter.repository.CatShelterRepository;
import Telegram.Bot.AnimalShelter.service.impl.CatShelterServiceImpl;
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
class CatShelterServiceImplTest {
    @Mock
    private CatShelterRepository catShelterRepository;
    @Mock
    private CatRepository catRepository;
    @InjectMocks
    private CatShelterServiceImpl catShelterService;


    @Test
    void createCatTest() {

        when(catShelterRepository.save(CAT_SHELTER_1)).thenReturn(CAT_SHELTER_1);
        assertEquals(CAT_SHELTER_1, catShelterService.create(CAT_SHELTER_1));
    }


    @Test
    void checkUpdateCatTest() {
        when(catShelterRepository.findById(any(Long.class))).thenReturn(Optional.of(CAT_SHELTER_1));
        when(catShelterRepository.save(any(CatShelter.class))).thenReturn(CAT_SHELTER_1);

        CAT_SHELTER_2.setId(SHELTER_ID_1);
        assertEquals(CAT_SHELTER_2, catShelterService.update(CAT_SHELTER_2));
    }


    @Test
    void checkGetShelterByIdTest() {
        when(catShelterRepository.findById(SHELTER_ID_1)).thenReturn(Optional.of(CAT_SHELTER_1));
        assertEquals(CAT_SHELTER_1, catShelterService.getShelterById(SHELTER_ID_1));

    }

    @Test
    void checkGetShelterByNameTest() {
        when(catShelterRepository.findByName(NAME_CAT_1)).thenReturn(Optional.of(CAT_SHELTER_1));
        assertEquals(CAT_SHELTER_1, catShelterService.getShelterByName(NAME_CAT_1));
    }

    @Test
    void getShelterTest() {
        List<CatShelter> catShelters = new ArrayList<>();
        catShelters.add(CAT_SHELTER_1);
        catShelters.add(CAT_SHELTER_2);

        when(catShelterRepository.findAll()).thenReturn(catShelters);
        assertEquals(catShelters, catShelterService.getShelter());
    }

    //    @Test
//    void getAnimalTest() {
//        List<Cat> cats = new ArrayList<>();
//        cats.add(CAT_1);
//}

    @Test
    void deleteTest() {
        when(catShelterRepository.save(CAT_SHELTER_1)).thenReturn(CAT_SHELTER_1);
        assertEquals(CAT_SHELTER_1, catShelterService.create(CAT_SHELTER_1));
        catShelterRepository.delete(CAT_SHELTER_1);
        assertFalse(catShelterRepository.findAll().contains(CAT_SHELTER_1));
    }
}