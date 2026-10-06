package com.charukesh.portfolio;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api")
public class ContactController {
  private final ContactRepository repo;
  public ContactController(ContactRepository repo){this.repo=repo;}

  @GetMapping("/health") public Map<String,String> health(){ return Map.of("status","online"); }

  @PostMapping("/contact")
  public ResponseEntity<Map<String,String>> contact(@Valid @RequestBody ContactRequest r){
    repo.save(new ContactMessage(clean(r.name()),clean(r.email()),clean(r.subject()),clean(r.message())));
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status","received"));
  }

  /** Strip tags and control characters. Output must still be encoded wherever it is displayed. */
  static String clean(String s){ return s.replaceAll("<[^>]*>","").replaceAll("[\\p{Cntrl}&&[^\\n\\t]]","").trim(); }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Map<String,String>> invalid(){ return ResponseEntity.badRequest().body(Map.of("error","Invalid input. Check all fields and try again.")); }

  @ExceptionHandler(Exception.class)
  ResponseEntity<Map<String,String>> fail(){ return ResponseEntity.internalServerError().body(Map.of("error","Unable to process the request. Please try again later.")); }
}
