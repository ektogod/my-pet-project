package com.tinkoff_lab.service.n;

import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.dto.n.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getUsers();

    UserDTO getUser(long id);

    void deleteUser(long id);

    UserDTO updateUser(UserDTO userDTO);

    UserDTO addUser(UserDTO userDTO);

    List<CityDTO> getUserCities(long chatId);

    UserDTO addCitiesToUser(long chatId, List<CityDTO> cityDTOS);

    void removeCitiesFromUser(long chatId, List<CityDTO> cityDTOS);

    UserDTO addEmailsToUser(long chatId, List<EmailDTO> emailDTOS);

    void removeEmailsFromUser(long chatId, List<EmailDTO> emailDTOS);
}
