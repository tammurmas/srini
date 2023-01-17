package org.tamm.srini.controller;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tamm.srini.service.ClientService;
import org.tamm.srini.service.CountryService;
import org.tamm.srini.service.dto.ClientDTO;
import org.thymeleaf.util.StringUtils;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final CountryService countryService;

    @GetMapping(value = "/list")
    public String showClientList(Model model) {
        List<ClientDTO> clients = clientService.findAllByUser();
        model.addAttribute("clients", clients);
        return "client-list";
    }

    @GetMapping({"/add", "/{clientId}/edit"})
    public String editClient(@PathVariable Optional<Long> clientId, Model model, RedirectAttributes redirAttrs) {
        if (!model.containsAttribute("client")) {
            ClientDTO client = new ClientDTO();
            if (clientId.isPresent()) {
                Optional<ClientDTO> optionalClient = clientService.findClientById(clientId.get());
                if (optionalClient.isEmpty()) {
                    redirAttrs.addFlashAttribute("error", "Client not found!");
                    return "redirect:/client/list";
                } else {
                    client = optionalClient.get();
                }
            }
            model.addAttribute("client", client);
        }

        model.addAttribute("countries", countryService.findAllCountries());
        return "client-edit";
    }

    @PostMapping("/save")
    public String saveClient(@Valid @ModelAttribute("client") ClientDTO client, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        validateClient(client, result);
        if (result.hasErrors()) {
            return editClient(Optional.empty(), model, redirAttrs);
        }

        if (client.getId() != null) {
            clientService.updateClient(client);
            redirAttrs.addFlashAttribute("success", "Client updated successfully!");
        } else {
            clientService.createClient(client);
            redirAttrs.addFlashAttribute("success", "Client created successfully!");
        }

        return "redirect:/client/list";
    }

    private void validateClient(ClientDTO client, Errors result) {
        if (!StringUtils.isEmptyOrWhitespace(client.getEmail())) {
            Optional<ClientDTO> optionalClient = clientService.findClientByEmail(client.getEmail());
            boolean isDuplicateEmail = optionalClient.isPresent() && !optionalClient.get().getId().equals(client.getId());
            if (isDuplicateEmail) {
                result.rejectValue("email", "", "Email is already in use");
            }
        }
        if (!StringUtils.isEmptyOrWhitespace(client.getUsername())) {
            Optional<ClientDTO> optionalClient = clientService.findClientByUsername(client.getUsername());
            boolean isDuplicateUsername = optionalClient.isPresent() && !optionalClient.get().getId().equals(client.getId());
            if (isDuplicateUsername) {
                result.rejectValue("username", "", "Username is already in use");
            }
        }
    }
}
