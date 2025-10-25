const togglePasswordElements = document.getElementsByClassName('toggle-password');

Array.from(togglePasswordElements).forEach(toggle => {
    const passwordInput = toggle.previousElementSibling;
    const eyeIcon = toggle.querySelector('.eye');
    const eyeSlashIcon = toggle.querySelector('.eye-slash');

    toggle.addEventListener("click", () => {
        const isHidden = passwordInput.type === 'password';
        passwordInput.type = isHidden ? 'text' : 'password';
        if (eyeIcon) eyeIcon.style.display = isHidden ? 'none' : 'inline';
        if (eyeSlashIcon) eyeSlashIcon.style.display = isHidden ? 'inline' : 'none';
    });
});
