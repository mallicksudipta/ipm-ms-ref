package com.cognizant.ri.pam.accounts.handlers;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;

import com.cognizant.kernel.CommandHandler;
import com.cognizant.ri.pam.accounts.Account;
import com.cognizant.ri.pam.accounts.EnrollPlanCommand;
import com.cognizant.ri.pam.plan.Plan;
import com.cognizant.ri.pam.plan.PlanRepository;

@Component
public class EnrollPlanHandler extends CommandHandler<EnrollPlanCommand, Account> {

	private final AccountRepository accounts;
	private final PlanRepository plans;

	protected EnrollPlanHandler(AccountRepository accounts, PlanRepository plans) {
		this.accounts = accounts;
		this.plans = plans;
	}

	@Override
	public Account handle(EnrollPlanCommand command) {
		if (!plans.exists(command.getPlanId())) {
			throw new NoSuchElementException("plan");
		}
		Account acc = accounts.findByParticipantId(command.getParticipantId());
		if (acc == null) {
			acc = new Account(command.getParticipantId());
		}
		acc.addPlan(new Plan(command.getPlanId()));
		return accounts.save(acc);
	}

}