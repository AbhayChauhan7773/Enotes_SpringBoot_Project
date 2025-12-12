package com.becoder.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.becoder.entity.Notes;
import com.becoder.entity.User;
import com.becoder.repository.NotesRepository;
import com.becoder.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private NotesRepository notesRepo;

	@ModelAttribute
	public void getUser(Principal p, Model m) {
		String email = p.getName();
		User user = userRepo.findByEmail(email);
		m.addAttribute("user", user);
	}

	  @GetMapping("/addNotes")
    public String addNotes(Model m) {
        m.addAttribute("note", new Notes());
        return "add_notes";
    }
	
	@PostMapping("/addNotes")
public String saveNotes(@ModelAttribute Notes note, Principal principal) {

    // Logged-in user email
    String email = principal.getName();
    System.out.println("Logged user email: " + email);

    // Find user by email
    User user = userRepo.findByEmail(email);

    // Map note to user
    note.setUser(user);

    notesRepo.save(note);

    return "redirect:/user/viewNotes";
}

	@GetMapping("/viewNotes")
public String viewNotes(Model m, Principal principal) {

    String email = principal.getName();
    User user = userRepo.findByEmail(email);

    List<Notes> notes = notesRepo.findByUser(user);

    m.addAttribute("notes", notes);

    return "view_notes";
}

	 @GetMapping("/editNotes/{id}")
    public String editNotes(@PathVariable Long id, Model m, Principal p) {
        Notes note = notesRepo.findById(id).orElse(null);
        if (note != null && note.getUser().getEmail().equals(p.getName())) {
            m.addAttribute("note", note);
            return "edit_notes";
        }
        return "redirect:/user/viewNotes";
    }


	 @PostMapping("/updateNotes")
    public String updateNotes(@ModelAttribute Notes note, Principal p) {
        String email = p.getName();
         userRepo.findByEmail(email);

        Notes existingNote = notesRepo.findById(note.getId()).orElse(null);
        if (existingNote != null && existingNote.getUser().getEmail().equals(email)) {
            existingNote.setTitle(note.getTitle());
            existingNote.setDescription(note.getDescription());
            notesRepo.save(existingNote);
        }
        return "redirect:/user/viewNotes";
    }

	@GetMapping("/deleteNotes/{id}")
    public String deleteNotes(@PathVariable Long id, Principal p) {
        Notes note = notesRepo.findById(id).orElse(null);
        if (note != null && note.getUser().getEmail().equals(p.getName())) {
            notesRepo.delete(note);
        }
        return "redirect:/user/viewNotes";
    }


}
