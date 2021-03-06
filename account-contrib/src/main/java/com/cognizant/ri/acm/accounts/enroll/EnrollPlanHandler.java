package com.cognizant.ri.acm.accounts.enroll;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.cognizant.kernel.CommandHandler;
import com.cognizant.ri.acm.accounts.Account;
import com.cognizant.ri.acm.accounts.AccountRepository;
import com.cognizant.ri.acm.accounts.Contribution;
import com.cognizant.ri.acm.accounts.FundContribution;
import com.cognizant.ri.acm.plan.Fund;
import com.cognizant.ri.acm.plan.Plan;
import com.cognizant.ri.acm.plan.PlanRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EnrollPlanHandler extends CommandHandler<EnrollPlanCommand, Account> {

	private final AccountRepository accounts;
	private final PlanRepository plans;

	protected EnrollPlanHandler(AccountRepository accounts, PlanRepository plans) {
		this.accounts = accounts;
		this.plans = plans;
	}

	@Override
	public Account handle(EnrollPlanCommand command) {
		Account acc = accounts.findByParticipantId(command.getParticipantId());
		
		Plan dbPlan = plans.findOne(command.getPlanId());
		
		if (dbPlan == null) {
			throw new NoSuchElementException("plan");
		}
		if (acc == null) {
			acc = new Account(command.getParticipantId());
			log.debug("Existing account not found. A ParticipantAdded may not have been received.");
		}
		Set<Contribution> contribs = acc.getContributions();
		int contribAmount = contribs.isEmpty() ? 100 : 0;
		Plan p = new Plan();
		p.setId(command.getPlanId());

		int componentCount = dbPlan.getFunds().size();
		int perFundComponent = 100 / componentCount;
		int firstFundComponent = 100 - (componentCount - 1) * perFundComponent;

		Set<FundContribution> fundContributions = new HashSet<>();
		for (Fund f : dbPlan.getFunds()) {
			int contribution = fundContributions.isEmpty() ? firstFundComponent : perFundComponent;
			fundContributions.add(new FundContribution(f.getId(), contribution));
		}

		Contribution c = Contribution.builder().plan(p).contribution(contribAmount).fundComponents(fundContributions)
				.build();

		contribs.add(c);
		acc.setContributions(contribs);
		return accounts.save(acc);
	}

}
