package com.cognizant.ri.acm.accounts.add;

import org.springframework.stereotype.Component;

import com.cognizant.kernel.CommandHandler;
import com.cognizant.ri.acm.accounts.Account;
import com.cognizant.ri.acm.accounts.AccountRepository;

@Component
public class CreateNewAccountHandler extends CommandHandler<CreateNewAccountCommand, Account> {

	private AccountRepository repo;
	
	protected CreateNewAccountHandler(AccountRepository repo) {
		this.repo = repo;
	}

	@Override
	public Account handle(CreateNewAccountCommand command) {
		Account dbVal = repo.findByParticipantId(command.getParticipantId());
		if (dbVal == null) {
			Account acct = new Account(command);
			dbVal = repo.save(acct);
		}
		return dbVal;

	}

}
