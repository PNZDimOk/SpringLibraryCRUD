package ru.koshelev.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.koshelev.springcourse.dao.BookDAO;
import ru.koshelev.springcourse.dao.PersonDAO;
import ru.koshelev.springcourse.models.Person;
import javax.validation.Valid;

@Controller
@RequestMapping("/person")
public class PersonController {
    private final PersonDAO personDAO;
    private final BookDAO bookDAO;

    @Autowired
    public PersonController(PersonDAO personDAO, BookDAO bookDAO) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("ListPerson",personDAO.index());
        return "person/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("person",personDAO.show(id));
        model.addAttribute("listBook",bookDAO.getBooksByPerson(id));
        return "person/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        return "person/new";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person")@Valid Person person, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "person/new";
        }
        personDAO.addPerson(person);
        return "redirect:/person";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id")int id, Model model){
        model.addAttribute("person", personDAO.show(id));
        return "person/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult, @PathVariable("id")int id){

        if (bindingResult.hasErrors()){
            return "person/edit";
        }
        personDAO.update(id, person);
        return "redirect:/person";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id")int id){
        personDAO.delete(id);
        return "redirect:/person";
    }
}
