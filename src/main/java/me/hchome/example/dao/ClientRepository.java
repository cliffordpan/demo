package me.hchome.example.dao;

import me.hchome.example.model.Client;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author Cliff Pan
 * @since
 */
@Repository
public interface ClientRepository extends AccountRepository<Client> {

}
