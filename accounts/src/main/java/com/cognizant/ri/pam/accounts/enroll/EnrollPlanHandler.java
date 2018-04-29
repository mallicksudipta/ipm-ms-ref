package com.cognizant.ri.pam.accounts.enroll;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;

import com.cognizant.ri.pam.CommandDispatcher;
import com.cognizant.ri.pam.CommandHandler;
import com.cognizant.ri.pam.accounts.Account;
import com.cognizant.ri.pam.accounts.AccountRepository;
import com.cognizant.ri.pam.plan.Plan;
import com.cognizant.ri.pam.plan.PlanRepository;

@Component
public class EnrollPlanHandler extends CommandHandler<EnrollPlanCommand, Account> {

	private final AccountRepository accounts;
	private final PlanRepository plans;
	
	protected EnrollPlanHandler(CommandDispatcher dispatcher, AccountRepository accounts, PlanRepository plans) {
		super(dispatcher, EnrollPlanCommand.class, Account.class);
		this.accounts = accounts;
		this.plans = plans;
	}

	@Override
	public Account handle(EnrollPlanCommand command) {
		Account acc = accounts.findByParticipantId(command.getParticipantId());
		if (acc == null) {
			throw new NoSuchElementException("account");
		}
		if(!plans.existsById(command.getPlanId())){
			throw new NoSuchElementException("plan");
		}
		acc.addPlan(new Plan(command.getPlanId()));
		return accounts.save(acc);
	}

}