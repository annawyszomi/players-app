package project.players.entity;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.players.repository.PlayerRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PlayerValidationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testNotValidUsername() {
        Player playerNotValidUserName = new Player(1, "x", "name", "lastName", "user@exmple.com", new BigDecimal("0"));

        assertThrows(ConstraintViolationException.class, () -> {
            playerRepository.save(playerNotValidUserName);
            entityManager.flush();
        });
    }

    @Test
    void testEmptyFirstName() {
        Player playerEmptyFirstName = new Player(1, "user", " ", "lastName", "user@exmple.com", new BigDecimal("0"));

        assertThrows(ConstraintViolationException.class, () -> {
            playerRepository.save(playerEmptyFirstName);
            entityManager.flush();
        });
    }

    @Test
    void testEmptyLastName() {
        Player playerEmptyLastName = new Player(1, "user", "name", " ", "user@exmple.com", new BigDecimal("0"));

        assertThrows(ConstraintViolationException.class, () -> {
            playerRepository.save(playerEmptyLastName);
            entityManager.flush();
        });
    }

    @Test
    void testNotValidEmail() {
        Player playerNotValidEmail = new Player(1, "user", "name", "lastName", "@exmple.com", new BigDecimal("0"));

        assertThrows(ConstraintViolationException.class, () -> {
            playerRepository.save(playerNotValidEmail);
            entityManager.flush();
        });
    }

    @Test
    void testNotValidAccountBalance() {
        Player playerNotValidEmail = new Player(1, "user", "name", "lastName", "user@exmple.com", new BigDecimal("-1"));

        assertThrows(ConstraintViolationException.class, () -> {
            playerRepository.save(playerNotValidEmail);
            entityManager.flush();
        });
    }
}