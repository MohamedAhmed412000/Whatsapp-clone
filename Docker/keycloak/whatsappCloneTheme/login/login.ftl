<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>WhatsApp</title>
    <link rel="icon" type="image/png" href="${url.resourcesPath}/img/favicon.ico">
    <link rel="stylesheet" href="${url.resourcesPath}/css/style.css">
</head>
<body>
    <div class="background">
        <div class="logo">
            <img src="${url.resourcesPath}/img/logo.png" class="logo-img">
            <div class="logo-text">Whatsapp Clone App</div>
        </div>
        <div class="whatsapp-container">
            <div class="card">
                <h2>Enter your credentials</h2>
                <p>Use your account to continue</p>

                <form id="kc-form-login" onsubmit="return true;" action="${url.loginAction}" method="post">
                    <div class="input-group">
                        <input type="email" id="username" name="username"
                               placeholder="Email address"
                               value="${(username!'')}"
                               required autofocus autocomplete="email">
                    </div>

                    <div class="input-group password-group">
                        <input type="password" id="password" name="password"
                            placeholder="Password" required autocomplete="current-password">
                        <span id="togglePassword" class="toggle-password" aria-label="Show password">
                            <svg class="eye" xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="none"
                                 viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                            </svg>
                            <svg class="eye-slash" xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="none"
                                 viewBox="0 0 24 24" stroke="currentColor" style="display:none;">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M13.875 18.825A10.05 10.05 0 0112 19c-4.477 0-8.268-2.943-9.542-7a9.96 9.96 0 012.293-3.708m3.62-2.565A9.963 9.963 0 0112 5c4.478 0 8.268 2.943 9.542 7a9.958 9.958 0 01-4.493 5.3M3 3l18 18" />
                            </svg>
                        </span>
                    </div>

                    <div class="remember-me">
                        <label>
                            <input tabindex="3" id="rememberMe" name="rememberMe" type="checkbox"
                                autocomplete="off" <#if login.rememberMe?? && login.rememberMe>checked</#if>
                            >
                            Remember me
                        </label>
                    </div>

                    <button type="submit" id="kc-login">Log in</button>
                </form>

                <hr class="separator" />

                <div class="links">
                    <a href="${url.registrationUrl}" id="create-account">Create an account?</a><br>
                    <a href="${url.loginResetCredentialsUrl}" id="forgot-password">Forgot password?</a>
                </div>
            </div>
        </div>
    </div>
</body>
<script src="${url.resourcesPath}/js/password.js"></script>
</html>
