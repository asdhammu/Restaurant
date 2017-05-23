package com.taj.validator;

import org.apache.commons.validator.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.taj.model.CustomerInfo;

@Component
public class CustomerInfoValidator implements Validator{

	private EmailValidator emailValidator = EmailValidator.getInstance();
	
	public boolean supports(Class<?> arg0) {
		return arg0 == CustomerInfo.class;
	}

	public void validate(Object target, Errors errors) {
		CustomerInfo custInfo = (CustomerInfo) target;
		 
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fName", "NotEmpty.customerForm.fName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lName", "NotEmpty.customerForm.lName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.customerForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.customerForm.address");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.customerForm.phone");
 
        

        if(!errors.hasErrors() && custInfo.getPhone().length()!=10){
        	errors.rejectValue("phone","Length.greater.phone");
        }
        
        if(!errors.hasErrors() && custInfo.getPhone().contains("[0-9]+")){
        	errors.rejectValue("phone","NoCharacater.cusomerForm.phone");
        }
        
        
        if (!errors.hasErrors() && !emailValidator.isValid(custInfo.getEmail())) {
            errors.rejectValue("email", "NotValid.customer.email");
        }
	}

}
