package com.api.rest.service;

import com.api.rest.dto.UserDTO;
import com.api.rest.utils.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Slf4j //Para manejar los logs

public class KeycloakServiceImpl implements IKeycloakService{


    @Override
    public List<UserRepresentation> findAllUsers() {

        return KeycloakProvider.getRealmResource()
                .users()
                .list();
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {

        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }

    @Override
    public String createUser(@NonNull UserDTO userDTO) {

        int status = 0;

        UsersResource userResource = KeycloakProvider.getRealmResource().users();


        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

       Response response = userResource.create(userRepresentation);

       status = response.getStatus();

       if(status == 201){
           String path = response.getLocation().getPath();
           String userId = path.substring(path.lastIndexOf("/") + 1);

           CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
           credentialRepresentation.setTemporary(false);
           credentialRepresentation.setType(OAuth2Constants.PASSWORD);
           credentialRepresentation.setValue(userDTO.getPassword());

           userResource.get(userId).resetPassword(credentialRepresentation);

           RealmResource realmResource = KeycloakProvider.getRealmResource();

           List<RoleRepresentation> roleRepresentations = null;

           if(userDTO.getRoles() == null || userDTO.getRoles().isEmpty()){
               roleRepresentations = List.of(realmResource.roles().get("user").toRepresentation());
           } else {
               roleRepresentations = realmResource.roles()
                       .list()
                       .stream()
                       .filter(role -> userDTO.getRoles()
                               .stream()
                               .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                       .toList();
           }

           realmResource.users()
                   .get(userId)
                   .roles()
                   .realmLevel()
                   .add(roleRepresentations);

           return "Usuario creado con exito!";
       } else if (status == 409) {
           log.error("El usuario ya existe");
           return "El usuario ya existe";
       } else {
           log.error("Error al crear usuario, contacte al administrador");
           return "Error al crear usuario, contacte al administrador";
       }

    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {

    }

    @Override
    public void deleteUser(String userId) {
        KeycloakProvider.getRealmResource().users()
                .get(userId)
                .remove();

    }
}
