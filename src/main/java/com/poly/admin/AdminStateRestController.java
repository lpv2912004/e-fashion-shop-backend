package com.poly.admin;

import com.poly.model.State;
import com.poly.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin/api/states")
@CrossOrigin(origins = "*")  // Cho phép tất cả các nguồn gốc
public class AdminStateRestController {

    @Autowired
    private StateService stateService;

    @GetMapping
    public List<State> getAllStates() {
        return stateService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<State> getStateById(@PathVariable Integer id) {
        Optional<State> state = stateService.findById(id);
        if (state.isPresent()) {
            return ResponseEntity.ok(state.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<State> createState(@RequestBody State state) {
        return ResponseEntity.ok(stateService.save(state));
    }

    @PutMapping("/{id}")
    public ResponseEntity<State> updateState(@PathVariable Integer id, @RequestBody State stateDetails) {
        Optional<State> state = stateService.findById(id);
        if (state.isPresent()) {
            State updatedState = state.get();
            updatedState.setName_status_order(stateDetails.getName_status_order());
            return ResponseEntity.ok(stateService.save(updatedState));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Integer id) {
        stateService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
