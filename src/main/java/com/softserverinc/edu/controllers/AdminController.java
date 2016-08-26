package com.softserverinc.edu.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.softserverinc.edu.entities.User;
import com.softserverinc.edu.entities.enums.UserRole;
import com.softserverinc.edu.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * For administrator controller
 */

@RestController
@RequestMapping("/admin/rest")
public class AdminController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    UserService userService;

    public interface LocalUserList{};
    public interface LocalUserDetails extends  LocalUserList{};

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @JsonView(LocalUserList.class)
    public ResponseEntity<List<LocalUsers>> adminIndex() {
        List<User> users = userService.findAll();
        List<LocalUsers> localUserses = new ArrayList<>();
        for (User user: users) {
            localUserses.add(addOneUser(user, false));
        }
        LOGGER.debug("admin controller User rest list ");
        return new ResponseEntity<>(localUserses, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String adminDeleteUser(@PathVariable long id) {
        userService.delete(id);
        LOGGER.debug("admin controller delete user" + id);
        return "redirect:/admin/rest";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/deleted/{id}")
    public String adminSetDeletedUser(@PathVariable long id) {
        User user = userService.findOne(id);
        user.setIsDeleted(user.isDeleted() ? false : true);
        userService.update(user);
        LOGGER.debug("admin controller set deleted attr user " + id);
        return "redirect:/admin/rest";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/enabled/{id}")
    public String adminSetEnabledUser(@PathVariable long id) {
        User user = userService.findOne(id);
        user.setEnabled(user.getEnabled() == 1 ? 0 : 1);
        userService.update(user);
        LOGGER.debug("admin controller set enabled attr user " + id);
        return "redirect:/admin/rest";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @JsonView(LocalUserDetails.class)
    public ResponseEntity<LocalUsers> adminUserDetails(@PathVariable long id){
        User user = userService.findOne(id);
        LOGGER.debug("admin controller User rest details " + id);
        return new ResponseEntity<LocalUsers>(addOneUser(user, true), HttpStatus.OK);
    }


    private LocalUsers addOneUser(User user, boolean details) {
        LocalUsers oneLocalUser = new LocalUsers();
        oneLocalUser.setId(user.getId());
        oneLocalUser.setFirstName(user.getFirstName());
        oneLocalUser.setLastName(user.getLastName());
        oneLocalUser.setEmail(user.getEmail());
        oneLocalUser.setRole(user.getRole());
        oneLocalUser.setDeleted(user.isDeleted());
        oneLocalUser.setEnabled(user.getEnabled());
        if(user.getProject() != null)
            oneLocalUser.setProjectTitle(user.getProject().getTitle());
        oneLocalUser.setDescription(user.getDescription());
        if(details){
            oneLocalUser.setImageFilename(user.getImageFilename());
            oneLocalUser.setImageData(user.getImageData());
            oneLocalUser.setDescription(user.getDescription());
        }
        return oneLocalUser;
    }

    private class LocalUsers {

        private long id;
        private String firstName;
        private String lastName;
        private String email;
        private UserRole role;
        private String description;
        private String projectTitle;
        private int deleted;
        private int enabled;
        private byte[] imageData;
        private String imageFilename;

        @JsonView(LocalUserList.class)
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        @JsonView(LocalUserList.class)
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        @JsonView(LocalUserList.class)
        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @JsonView(LocalUserList.class)
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @JsonView(LocalUserList.class)
        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }

        @JsonView(LocalUserDetails.class)
        public String getDescription() {
            if (description == null)
                description = "";
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @JsonView(LocalUserList.class)
        public String getProjectTitle() {
            if (projectTitle == null)
                projectTitle = "";
            return projectTitle;
        }

        public void setProjectTitle(String projectTitle) {
            this.projectTitle = projectTitle;
        }

        @JsonView(LocalUserList.class)
        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted ? 1 : 0;
        }

        @JsonView(LocalUserList.class)
        public int getEnabled() {
            return enabled;
        }

        public void setEnabled(int enabled) {
            this.enabled = enabled;
        }

        @JsonView(LocalUserDetails.class)
        public byte[] getImageData() {
            return imageData;
        }

        public void setImageData(byte[] imageData) {
            this.imageData = imageData;
        }

        @JsonView(LocalUserDetails.class)
        public String getImageFilename() {
            return imageFilename;
        }

        public void setImageFilename(String imageFilename) {
            this.imageFilename = imageFilename;
        }

        @Override
        public String toString() {
            return "LocalUsers{" +
                    "id=" + id +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", email='" + email + '\'' +
                    ", role=" + role +
                    ", description='" + description + '\'' +
                    ", projectTitle='" + projectTitle + '\'' +
                    '}';
        }
    }

}
