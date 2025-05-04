package com.poly.admin;

import com.poly.dto.TransactionDTO;
import com.poly.model.Transaction;
import com.poly.repository.TransactionRepository;
import com.poly.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/api/transactions")
@CrossOrigin(origins = "*")
public class AdminTransactionRestController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.findAll();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Integer id) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(id);

        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();

        
            TransactionDTO transactionDTO = new TransactionDTO(
                transaction.getId(),
                transaction.getOrder().getId(),
                transaction.getTotal(),
                transaction.getStatus(),
                transaction.getTransactionCode(),
                transaction.getOrder().getState().getName_status_order(), // tt dh
                transaction.getOrder().getUser().getFullName(),       
                transaction.getOrder().getMethodPayment().getName_method() // phương thức
            );

            return ResponseEntity.ok(transactionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Integer id, @RequestBody Transaction transactionDetails) {
        Optional<Transaction> transactionOpt = transactionService.findById(id);

        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            transaction.setStatus(transactionDetails.getStatus()); //cn
            return ResponseEntity.ok(transactionService.save(transaction));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
