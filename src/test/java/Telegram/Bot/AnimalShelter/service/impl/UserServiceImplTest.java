package Telegram.Bot.AnimalShelter.service.impl;

import Telegram.Bot.AnimalShelter.entity.User;
import Telegram.Bot.AnimalShelter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private final Long telegramId = 1L;
    private final String testFirstName = "FName";
    private final String testLastName = "LName";
    private final String testPhone = "11111111";
    private final String testShelterType = "CAT";
    private final User validUser = new User(telegramId, testFirstName, testLastName, testPhone);
    private final User secondValidUser = new User(telegramId, testLastName, null, testPhone);
    private final User thirdValidUser = new User(telegramId, testLastName, testLastName, testPhone);
    private final User validUserWithShelterType = new User(telegramId, testLastName, testLastName, testPhone,
            testShelterType, null);
    private final List<User> listOfUser = List.of(validUser);

    @Mock
    UserRepository userRepositoryMock;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void shouldCreateAndReturnUserWithAllArgs() {
        when(userRepositoryMock.save(validUser)).thenReturn(validUser);
        User actual = userService.create(validUser);
        assertEquals(validUser, actual);
        verify(userRepositoryMock, times(1)).save(validUser);
    }

    @Test
    void shouldReturnUserFoundById() {
        when(userRepositoryMock.findByTelegramId(telegramId)).thenReturn(Optional.of(validUser));
        User actual = userService.getById(telegramId);
        assertEquals(validUser, actual);
        verify(userRepositoryMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    void shouldReturnShelterTypeFoundById() {
        when(userRepositoryMock.findByTelegramId(telegramId)).thenReturn(Optional.of(validUserWithShelterType));
        String actual = userService.getShelterById(telegramId);
        assertEquals(testShelterType, actual);
        verify(userRepositoryMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    void shouldThrowNotFoundExWhenGetShelterById() {
        when(userRepositoryMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getShelterById(telegramId));
        verify(userRepositoryMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    void shouldThrowNotFoundExWhenFindUserById() {
        when(userRepositoryMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getById(telegramId));
        verify(userRepositoryMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    void shouldReturnListOfUsersWhenGetAllUser() {
        when(userRepositoryMock.findAll()).thenReturn(listOfUser);
        List<User> actual = userService.getAll();
        assertEquals(listOfUser, actual);
        verify(userRepositoryMock, times(1)).findAll();
    }

    @Test
    void shouldUpdateUserWithoutNullFields() {
        when(userRepositoryMock.findByTelegramId(telegramId)).thenReturn(Optional.of(validUser));
        when(userRepositoryMock.save(thirdValidUser)).thenReturn(thirdValidUser);
        User actual = userService.update(secondValidUser);
        assertEquals(thirdValidUser, actual);
        verify(userRepositoryMock, times(1)).findByTelegramId(telegramId);
        verify(userRepositoryMock, times(1)).save(thirdValidUser);
    }

    @Test
    void shouldThrowNotFoundExWhenUpdatingUser() {
        when(userRepositoryMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.update(validUser));
        verify(userRepositoryMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    void shouldThrowNotFoundExWhenDeletingUserById() {
        when(userRepositoryMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.deleteById(telegramId));
        verify(userRepositoryMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    void shouldThrowNotFoundExWhenDeletingUser() {
        when(userRepositoryMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.delete(validUser));
        verify(userRepositoryMock, times(1)).findByTelegramId(telegramId);
    }
}