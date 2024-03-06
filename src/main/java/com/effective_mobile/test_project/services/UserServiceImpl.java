package com.effective_mobile.test_project.services;

import com.effective_mobile.test_project.custom_excepions.InsufficientFundsException;
import com.effective_mobile.test_project.model.BankAccount;
import com.effective_mobile.test_project.model.User;
import com.effective_mobile.test_project.repository.BankAccountRepository;
import com.effective_mobile.test_project.repository.UserRepository;
import com.effective_mobile.test_project.specifications.UserSpecifications;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Data
@ToString
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BankAccountRepository bankAccountRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Бин добавлен в этот класс для удобства, лучше вынести в отдельный конфигурационный класс
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public User createUser(String username, String password, String name, LocalDate dateOfBirth, String email, String phone, BigDecimal initialBalance) {
        // Проверяем, существует ли пользователь с таким именем, email или телефоном
        if (userRepository.findByUsername(username).isPresent() ||
                userRepository.findByEmail(email).isPresent() ||
                userRepository.findByPhone(phone).isPresent()) {
            throw new RuntimeException("Пользователь с такими учетными данными уже существует");
        }

        // Создаем банковский аккаунт с начальным балансом
        BankAccount account = new BankAccount(initialBalance);

        // Создаем пользователя с указанным аккаунтом
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setBirthDate(dateOfBirth);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAccount(account);

        // Сохраняем пользователя в репозитории, при этом аккаунт будет сохранен автоматически
        return userRepository.save(user);
    }

    @Override
    public User updateUserContacts(String username, String newContact, String contactType) {
        // Находим пользователя по имени
        User user = findUserByUsername(username);

        // Обновляем контакт пользователя в зависимости от типа контакта
        if ("email".equalsIgnoreCase(contactType)) {
            user.changeEmail(newContact);
        } else if ("phone".equalsIgnoreCase(contactType)) {
            // Проверяем, является ли новый номер телефона корректным
            if (newContact != null && !newContact.isEmpty()) {
                user.changePhone(newContact);
            } else {
                // Удаляем номер телефона, если новый номер телефона пустой
                user.deletePhone();
            }
        } else {
            throw new IllegalArgumentException("Некорректный тип контакта для обновления");
        }

        // Сохраняем обновленного пользователя
        return userRepository.save(user);
    }
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public User deleteUserContacts(String username, String contactType) {
        // Находим пользователя по имени
        Optional<User> user = findByUsername(username);

        // Удаляем контакт пользователя в зависимости от типа контакта
        if ("email".equalsIgnoreCase(contactType)) {
            user.deleteEmail();
        } else if ("phone".equalsIgnoreCase(contactType)) {
            user.deletePhone();
        } else {
            throw new IllegalArgumentException("Некорректный тип контакта для удаления");
        }

        // Сохраняем обновленного пользователя
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUsers(LocalDate birthDate, String phone, String fullName, String email, Pageable pageable) {
        return userRepository.findAll(UserSpecifications.withFilters(birthDate, phone, fullName, email), pageable).getContent();
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void increaseUserBalances() {
        List<User> users = userRepository.findAll();
        List<BankAccount> accounts = new ArrayList<>();
        for (User user : users) {
            BankAccount account = user.getAccount();
            BigDecimal initialBalance = account.getInitialBalance();
            BigDecimal increasePercentage = BigDecimal.valueOf(0.05); // 5%

            BigDecimal increaseAmount = initialBalance.multiply(increasePercentage);
            BigDecimal newBalance = initialBalance.add(increaseAmount).min(BigDecimal.valueOf(207));

            account.setBalance(newBalance);
            accounts.add(account);
        }

        userRepository.saveAll(users);
        bankAccountRepository.saveAll(accounts); // сохраняет обновленные банковские счета
    }

    // Запускаем метод раз в минуту
    @Scheduled(fixedRate = 60000) // 60 секунд = 1 минута
    public void scheduledIncreaseUserBalances() {
        increaseUserBalances();
    }

    @Override
    public void transferMoney(Long senderUserId, Long receiverUserId, BigDecimal amount) {
        User sender = findById(senderUserId);
        User receiver = findById(receiverUserId);

        BigDecimal senderBalance = sender.getAccount().getBalance();
        if (senderBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in sender's account");
        }

        synchronized (this) {
            boolean successfulTransfer = sender.getAccount().decreaseBalance(amount);
            if (successfulTransfer) {
                receiver.getAccount().increaseBalance(amount);
                bankAccountRepository.saveAll(List.of(sender.getAccount(), receiver.getAccount()));
            } else {
                throw new RuntimeException("Transfer failed. Please try again.");
            }
        }
    }
}
