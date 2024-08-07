package com.zuesshopbackend.customer;

import com.zuesshop.entity.Country;
import com.zuesshop.entity.Customer;
import com.zuesshop.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired private CustomerService service;

    @GetMapping("/customers")
    public String listFirstPage(Model model){
        return listByPage(model, 1, "firstName", "asc", null);
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(Model model,
                             @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField")String sortField,
                             @Param("sortDirection")String sortDirection,
                             @Param("keyword")String keyword){
        Page<Customer> page = service.listByPage(pageNum, sortField,sortDirection, keyword);
        List<Customer> listCustomers = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");

        return "customers/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(@PathVariable("id")Integer id,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes ra){
        service.updateCustomerEnabledStatus(id,enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Customer ID " + id + " has been " + status;
        ra.addFlashAttribute("messageSuccess" , message);

        return "redirect:/customers";
    }

    @GetMapping("/customers/detail/{id}")
    public String viewCustomer(@PathVariable("id") Integer id,
                               Model model,
                               RedirectAttributes ra){
        try{
            Customer customer = service.get(id);
            model.addAttribute("customer", customer);

            return "customers/customer_detail_modal";
        }catch (CustomerNotFoundException ex){
            ra.addFlashAttribute("messageError", ex.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("/customer/edit/{id}")
    public String editCustomer(@PathVariable("id")Integer id, Model model, RedirectAttributes ra) throws CustomerNotFoundException {
        try{
            Customer customer = service.get(id);
            List<Country> countries = service.listAllCountries();

            model.addAttribute("listCountries", countries);
            model.addAttribute("customer", customer);
            model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)",id));

            return "customers/customer_form";
        }catch (CustomerNotFoundException ex){
            ra.addFlashAttribute("messageError", ex.getMessage());
            return "redirect: /customers";
        }
    }

    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer, Model model, RedirectAttributes ra){
        service.save(customer);
        ra.addFlashAttribute("messageSuccess", "The customer ID " + customer.getId() + " has been updated successful");
        return "redirect:/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra){
        try{
            service.delete(id);
            ra.addFlashAttribute("messageSuccess", "The customer ID " + id + " has been deleted successful");
        } catch (CustomerNotFoundException e) {
            ra.addFlashAttribute("messageError", e.getMessage());
        }

        return "redirect:/customers";
    }
}
