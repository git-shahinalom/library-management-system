package com.library.security;

import com.library.model.Person;
import com.library.repository.PersonRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Bridges our Person hierarchy to Spring Security's UserDetails contract.
 * Whether the logged-in username belongs to a Student, a Teacher, or a
 * Librarian, this class doesn't need to know or care - it just asks
 * person.getRole() (polymorphism again) to get the right Spring Security role.
 */
@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));

        return User.builder()
                .username(person.getUsername())
                .password(person.getPassword())
                .roles(person.getRole()) // Spring Security prefixes this with "ROLE_"
                .build();
    }
}
