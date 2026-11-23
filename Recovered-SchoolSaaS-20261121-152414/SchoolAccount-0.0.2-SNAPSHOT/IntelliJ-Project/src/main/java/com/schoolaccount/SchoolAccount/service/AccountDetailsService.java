/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.AccountDetails
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository
 *  com.schoolaccount.SchoolAccount.service.AccountDetailsService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.schoolaccount.SchoolAccount.model.AccountDetails;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsService {
    @Autowired
    private AccountDetailsRepository repository;

    public ResponseMessage createAccount(AccountDetails accountDetails) {
        if (this.repository.count() > 0L) {
            return new ResponseMessage("Account already exists! You can only update it.", null);
        }
        accountDetails.setSchoolId("67ca9167d56ec91fc9539234");
        AccountDetails savedAccount = (AccountDetails)this.repository.save(accountDetails);
        return new ResponseMessage("Account created successfully!", (Object)savedAccount);
    }

    public ResponseMessage getAccountDetails() {
        Optional<AccountDetails> accountOpt = this.repository.findAll().stream().findFirst();
        return accountOpt.map(account -> new ResponseMessage("Account details fetched!", account)).orElse(new ResponseMessage("No account found!", null));
    }

    public ResponseMessage updateAccount(AccountDetails updatedAccount) {
        Optional existingAccountOpt = this.repository.findAll().stream().findFirst();
        if (existingAccountOpt.isPresent()) {
            AccountDetails existingAccount = (AccountDetails)existingAccountOpt.get();
            existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
            existingAccount.setIfscCode(updatedAccount.getIfscCode());
            existingAccount.setBankName(updatedAccount.getBankName());
            existingAccount.setBranch(updatedAccount.getBranch());
            existingAccount.setAccountHolderName(updatedAccount.getAccountHolderName());
            existingAccount.setOpeningAmount(updatedAccount.getOpeningAmount());
            this.repository.save(existingAccount);
            return new ResponseMessage("Account updated successfully!", (Object)existingAccount);
        }
        return new ResponseMessage("No account found to update!", null);
    }

    public ResponseMessage createAccountWithSchool(AccountDetails accountDetails, String schoolId) {
        Optional existingAccount = this.repository.findBySchoolId(schoolId);
        if (existingAccount.isPresent()) {
            return new ResponseMessage("Account already exists for this school! You can only update it.", null);
        }
        accountDetails.setSchoolId(schoolId);
        AccountDetails savedAccount = (AccountDetails)this.repository.save(accountDetails);
        return new ResponseMessage("Account created successfully for school!", (Object)savedAccount);
    }

    public ResponseMessage getAccountDetailsBySchool(String schoolId) {
        Optional<AccountDetails> accountOpt = this.repository.findBySchoolId(schoolId);
        return accountOpt.map(account -> new ResponseMessage("Account details fetched for school!", account)).orElse(new ResponseMessage("No account found for this school!", null));
    }

    public ResponseMessage updateAccountWithSchool(AccountDetails updatedAccount, String schoolId) {
        Optional existingAccountOpt = this.repository.findBySchoolId(schoolId);
        if (existingAccountOpt.isPresent()) {
            AccountDetails existingAccount = (AccountDetails)existingAccountOpt.get();
            existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
            existingAccount.setIfscCode(updatedAccount.getIfscCode());
            existingAccount.setBankName(updatedAccount.getBankName());
            existingAccount.setBranch(updatedAccount.getBranch());
            existingAccount.setAccountHolderName(updatedAccount.getAccountHolderName());
            existingAccount.setOpeningAmount(updatedAccount.getOpeningAmount());
            existingAccount.setSchoolId(schoolId);
            this.repository.save(existingAccount);
            return new ResponseMessage("Account updated successfully for school!", (Object)existingAccount);
        }
        return new ResponseMessage("No account found for this school to update!", null);
    }
}

