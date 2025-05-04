package com.poly.service.implement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.poly.model.TypeVoucher;
import com.poly.repository.TypeVoucherRepository;
import com.poly.service.TypeVoucherService;

@Service
public class TypeVoucherServiceImpl implements TypeVoucherService {

    @Autowired
    private TypeVoucherRepository typeVoucherRepository;

    @Override
    public List<TypeVoucher> findAllTypeVouchers() {
        return typeVoucherRepository.findAll();
    }
}
