document.getElementById('togglePassword').addEventListener('click', function() {
    ['current-password', 'new-password', 'confirm-password'].forEach(function(id) {
        const passwordField = document.getElementById(id);
        const toggleIcon = document.getElementById('toggleIcon');

        const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordField.setAttribute('type', type);
    });

    const toggleIcon = document.getElementById('toggleIcon');
    toggleIcon.classList.toggle('fa-eye');
    toggleIcon.classList.toggle('fa-eye-slash');
});

document.getElementById('password-form').addEventListener('submit', function(event){
	var newPassword = document.getElementById('new-password').value;
	var confirmPassword = document.getElementById('confirm-password').value;
	
	if(newPassword !== confirmPassword){
		event.preventDefault();
		document.getElementById('error-message').innerHTML  = '<i class="fa-solid fa-circle-exclamation"></i> Mật khẩu nhập lại không khớp!';
		
	}elseP
	document.getElementById('error-message').innerHTML  = '';
});