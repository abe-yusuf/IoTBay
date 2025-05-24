// Email validation
const EMAIL_PATTERN = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/;
const PASSWORD_PATTERN = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d\w\W]{8,}$/;
const PHONE_PATTERN = /^(?:\+61|0)[2-478](?:[ -]?[0-9]){8}$/;
const PRICE_PATTERN = /^\d+(\.\d{1,2})?$/;

function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return true;

    let isValid = true;
    const errorMessages = [];

    // Clear previous error messages
    clearErrors(formId);

    // Validate email if present
    const emailInput = form.querySelector('input[type="email"]');
    if (emailInput && !validateEmail(emailInput.value)) {
        isValid = false;
        showError(emailInput, 'Please enter a valid email address');
        errorMessages.push('Invalid email address');
    }

    // Validate password if present
    const passwordInput = form.querySelector('input[type="password"]');
    if (passwordInput && !validatePassword(passwordInput.value)) {
        isValid = false;
        showError(passwordInput, 'Password must be at least 8 characters long and contain uppercase, lowercase, and numbers');
        errorMessages.push('Invalid password format');
    }

    // Validate name fields if present
    const nameInputs = form.querySelectorAll('input[name*="name"]');
    nameInputs.forEach(input => {
        if (!validateName(input.value)) {
            isValid = false;
            showError(input, 'Name must be between 2 and 50 characters');
            errorMessages.push('Invalid name');
        }
    });

    // Validate phone if present
    const phoneInput = form.querySelector('input[name*="phone"]');
    if (phoneInput && phoneInput.value && !validatePhone(phoneInput.value)) {
        isValid = false;
        showError(phoneInput, 'Please enter a valid Australian phone number');
        errorMessages.push('Invalid phone number');
    }

    // Validate address if present
    const addressInput = form.querySelector('input[name*="address"]');
    if (addressInput && addressInput.value && !validateAddress(addressInput.value)) {
        isValid = false;
        showError(addressInput, 'Address must be between 5 and 200 characters');
        errorMessages.push('Invalid address');
    }

    // Validate price if present
    const priceInput = form.querySelector('input[name*="price"]');
    if (priceInput && !validatePrice(priceInput.value)) {
        isValid = false;
        showError(priceInput, 'Please enter a valid price (e.g., 99.99)');
        errorMessages.push('Invalid price');
    }

    // Validate quantity if present
    const quantityInput = form.querySelector('input[name*="quantity"]');
    if (quantityInput && !validateQuantity(quantityInput.value)) {
        isValid = false;
        showError(quantityInput, 'Quantity must be a positive number');
        errorMessages.push('Invalid quantity');
    }

    if (!isValid) {
        // Show error summary at the top of the form
        showErrorSummary(formId, errorMessages);
    }

    return isValid;
}

function validateEmail(email) {
    return EMAIL_PATTERN.test(email);
}

function validatePassword(password) {
    return PASSWORD_PATTERN.test(password);
}

function validateName(name) {
    return name && name.trim().length >= 2 && name.trim().length <= 50;
}

function validatePhone(phone) {
    return PHONE_PATTERN.test(phone);
}

function validateAddress(address) {
    return address && address.trim().length >= 5 && address.trim().length <= 200;
}

function validatePrice(price) {
    return PRICE_PATTERN.test(price);
}

function validateQuantity(quantity) {
    return !isNaN(quantity) && parseInt(quantity) >= 0;
}

function showError(input, message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.style.color = 'red';
    errorDiv.style.fontSize = '0.8em';
    errorDiv.style.marginTop = '5px';
    errorDiv.textContent = message;
    input.parentNode.appendChild(errorDiv);
    input.style.borderColor = 'red';
}

function showErrorSummary(formId, messages) {
    const form = document.getElementById(formId);
    const summary = document.createElement('div');
    summary.className = 'error-summary';
    summary.style.backgroundColor = '#fff3f3';
    summary.style.border = '1px solid #dc3545';
    summary.style.padding = '10px';
    summary.style.marginBottom = '20px';
    summary.style.borderRadius = '4px';

    const heading = document.createElement('h4');
    heading.style.color = '#dc3545';
    heading.style.margin = '0 0 10px 0';
    heading.textContent = 'Please correct the following errors:';
    summary.appendChild(heading);

    const list = document.createElement('ul');
    list.style.margin = '0';
    list.style.paddingLeft = '20px';
    messages.forEach(message => {
        const item = document.createElement('li');
        item.style.color = '#dc3545';
        item.textContent = message;
        list.appendChild(item);
    });
    summary.appendChild(list);

    form.insertBefore(summary, form.firstChild);
}

function clearErrors(formId) {
    const form = document.getElementById(formId);
    if (!form) return;

    // Remove error summary
    const errorSummary = form.querySelector('.error-summary');
    if (errorSummary) {
        errorSummary.remove();
    }

    // Remove individual error messages
    const errorMessages = form.querySelectorAll('.error-message');
    errorMessages.forEach(error => error.remove());

    // Reset input styles
    const inputs = form.querySelectorAll('input');
    inputs.forEach(input => {
        input.style.borderColor = '';
    });
} 