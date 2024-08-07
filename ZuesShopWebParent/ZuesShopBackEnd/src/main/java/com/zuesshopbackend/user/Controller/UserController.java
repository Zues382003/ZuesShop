package com.zuesshopbackend.user.Controller;

import com.zuesshop.entity.Role;
import com.zuesshop.entity.User;
import com.zuesshopbackend.FileUploadUtil;
import com.zuesshopbackend.user.Repository.RoleRepository;
import com.zuesshopbackend.user.UserNotFoundException;
import com.zuesshopbackend.user.Service.UserService;
import com.zuesshopbackend.user.Export.UserCsvExporter;
import com.zuesshopbackend.user.Export.UserExcelExporter;
import com.zuesshopbackend.user.Export.UserPdfExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model) throws UserNotFoundException {
        return listByPage(1, model, "id", "asc", null, 0);
    }

    @GetMapping("/users/page/{PageNumber}")
    public String listByPage(@PathVariable("PageNumber") int pageNumber, Model model,
                             @Param("sortField") String sortField,
                             @Param("sortDirection") String sortDirection,
                             @Param("keyword") String keyword,
                             @Param("roleId") Integer roleId
    ) throws UserNotFoundException {
        Page<User> page = userService.getListByPage(pageNumber, sortField, sortDirection, keyword, roleId);
        List<User> listUsers = page.getContent();
        List<Role> listRoles = userService.getListRole();

        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";

        if(roleId != null && roleId > 0){
            model.addAttribute("roleId", roleId);
        }

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("listRoles", listRoles);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);

        return "/users/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Create New User");

        List<Role> listRoles = userService.getListRole();
        model.addAttribute("listRoles", listRoles);
        return "/users/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String photos = StringUtils.cleanPath(fileName);
            user.setPhotos(photos);
            User saveUser = userService.save(user);

            String uploadDir = "./user-photos/" + saveUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }
            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("messageSuccess", "The user has been saved successfully.");

        return "redirect:/users/page/1?sortField=id&sortDirection=asc&searchField=id&keyword=" + user.getId();
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID:" + id + ")");

            List<Role> listRoles = userService.getListRole();
            model.addAttribute("listRoles", listRoles);
            return "/users/user_form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id);
            String Email = user.getEmail();
            userService.delete(id);
            redirectAttributes.addFlashAttribute("messageSuccess", "The user Email: " + Email + " has been deleted successfully");
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID: " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("messageSuccess", message);
        return "redirect:/users/page/1?sortField=id&sortDirection=asc&searchField=id&keyword=" + id;
    }

    @GetMapping("/users/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.getListUser();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.getListUser();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.getListUser();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(listUsers, response);
    }


}
