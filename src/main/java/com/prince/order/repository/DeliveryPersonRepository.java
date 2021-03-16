package com.prince.order.repository;

import com.prince.order.entity.DeliveryPerson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryPersonRepository extends CrudRepository<DeliveryPerson, Long> {

    DeliveryPerson findByMobileNo(String mobileNo);
}
