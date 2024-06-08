package me.hchome.example.controller;

import me.hchome.example.dto.BaseAccount;
import me.hchome.example.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Cliff Pan
 * @since
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    /**
     * Account service.
     */
    private final AccountService accountService;

    /**
     * Default constructor.
     *
     * @param accountServiceDi DI {@link AccountService}
     */
    public AccountController(
            final AccountService accountServiceDi) {
        this.accountService = accountServiceDi;
    }

    /**
     * Async request.
     * @return a {@link Callable} object for base account infos
     */
    @GetMapping
    public Callable<List<BaseAccount>> listAll() {
        return accountService::listAllBase;
    }
}
