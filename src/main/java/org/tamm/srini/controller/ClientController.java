package org.tamm.srini.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tamm.srini.service.ClientService;
import org.tamm.srini.service.CountryService;
import org.tamm.srini.service.dto.ClientDTO;

@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CountryService countryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String showClientList(Model model) {
        List<ClientDTO> clients = clientService.findAllUserClients();
        model.addAttribute("clients", clients);
        return "client-list";
    }

    @GetMapping({"/add", "/{clientId}/edit"})
    public String editClient(@PathVariable Optional<Long> clientId, Model model) {
        ClientDTO client = new ClientDTO();
        if (!model.containsAttribute("client")) {
            if (clientId.isPresent()) {
                client = clientService.findUserById(clientId.get());
            }
            model.addAttribute("client", client);
        }

        model.addAttribute("countries", countryService.findAllCountries());
        return "client-edit";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("client") ClientDTO client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return editClient(Optional.empty(), model);
        }

        if (client.getId() != null) {
            clientService.updateClient(client);
        } else {
            clientService.createClient(client);
        }

        return "redirect:/list";
    }
}
