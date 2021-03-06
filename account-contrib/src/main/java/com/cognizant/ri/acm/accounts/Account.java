package com.cognizant.ri.acm.accounts;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cognizant.ri.acm.accounts.add.CreateNewAccountCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "acm_accounts")
@Builder
@ToString
public class Account {

	private @Getter String participantId;

	@Id
	private @Getter String id;

	@Singular
	private @Getter Set<Contribution> contributions = new HashSet<>();

	public Account(CreateNewAccountCommand cmd) {
		this.participantId = cmd.getParticipantId();
	}

	public Account(String participantId) {
		this.participantId = participantId;
	}
	
	public void setContributions(Set<Contribution> contributions) {
		int total = contributions.stream().mapToInt(c -> c.getContribution()).sum();
		if (total != 100) {
			throw new IllegalArgumentException("incomplete");
		}
		this.contributions = new HashSet<>(contributions);
	}

}
