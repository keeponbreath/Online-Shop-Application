package spring.cloud.storageservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.cloud.storageservice.entity.Address;
import spring.cloud.storageservice.repository.AddressesRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AddressesService {
    private final AddressesRepository repository;

    @Autowired
    public AddressesService(AddressesRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void saveAddress(Address address) {
        repository.save(address);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean editAddress(Long id, Address address) {
        Address addressToUpdate = getAddressesByStorageId(id);
        addressToUpdate.setCity(address.getCity());
        addressToUpdate.setStreet(address.getStreet());
        addressToUpdate.setHouse(address.getHouse());
        repository.save(addressToUpdate);
        return Boolean.TRUE;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Address getAddressesByStorageId(Long id) {
        Optional<Address> addressOptional = Optional.ofNullable(repository.getAddressesByStorageId(id));
        return addressOptional.orElseThrow(() ->
                new NoSuchElementException("No address with Storage ID " + id + " found"));
    }
}
