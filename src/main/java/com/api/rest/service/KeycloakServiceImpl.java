package com.api.rest.service;

import com.api.rest.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j //Para manejar los logs

public class KeycloakServiceImpl implements IKeycloakService{


    @Override
    public List<UserRepresentation> findAllUsers() {
        return null;
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return null;
    }

    @Override
    public String createUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {

    }

    @Override
    public void deleteUser(String userId) {

    }
}
