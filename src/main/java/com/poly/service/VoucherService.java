package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.model.Voucher;
import com.poly.repository.VoucherRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    public List<Voucher> findAllVouchers() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> findById(Long id) {
        return voucherRepository.findById(id);
    }

    public Voucher saveVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public boolean deleteById(Long id) {
        if (voucherRepository.existsById(id)) {
            voucherRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public boolean existsByCode(String code) {
        return voucherRepository.existsByCode(code);
    }
}
