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
            <h2>Reset your password</h2>
            <p>Enter your email address to receive reset instructions</p>

            <form id="kc-reset-password-form" action="${url.loginAction}" method="post">
                <input type="hidden" name="execution" value="${execution}">
                <div class="input-group">
                    <input type="email" id="username" name="username"
                           placeholder="Email address"
                           value="${(username!'')}"
                           required autofocus autocomplete="email">
                </div>

                <button type="submit" id="kc-reset-password">Send reset link</button>
            </form>

            <hr class="separator" />

            <div class="links">
                <a href="${url.loginUrl}" id="back-to-login">Back to login</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
