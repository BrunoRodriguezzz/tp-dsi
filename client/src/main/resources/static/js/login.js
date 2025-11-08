document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const showRegisterLink = document.getElementById('show-register');
    const showLoginLink = document.getElementById('show-login');

    showRegisterLink.addEventListener('click', function (e) {
        e.preventDefault();

        hideErrorMessage();

        loginForm.style.display = 'none';
        registerForm.style.display = 'flex';
    });

    showLoginLink.addEventListener('click', function (e) {
        e.preventDefault();

        hideErrorMessage();

        registerForm.style.display = 'none';
        loginForm.style.display = 'flex';
    });

    function hideErrorMessage() {
        const errorElement = document.querySelector('.error-message') ||
            document.querySelector('#error-message') ||
            document.querySelector('[th\\:if*="error"]') ||
            document.querySelector('.alert-danger') ||
            document.querySelector('.error');

        if (errorElement) {
            errorElement.style.display = 'none';
        }
    }
});